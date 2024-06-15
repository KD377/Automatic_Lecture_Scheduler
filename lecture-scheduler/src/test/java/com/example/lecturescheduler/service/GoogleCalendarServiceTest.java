package com.example.lecturescheduler.service;


import com.example.lecturescheduler.model.*;
import com.example.lecturescheduler.repository.LectureSessionRepository;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GoogleCalendarServiceTest {

    @Mock
    private LectureSessionRepository lectureSessionRepository;
    @Mock
    private OAuth2User oauth2User;
    @Mock
    private OAuth2AccessToken accessToken;
    @InjectMocks
    private GoogleCalendarService googleCalendarService;

    private LectureSession lectureSession;


    @BeforeEach
    public void setUp(){
        TimeSlot timeSlot = new TimeSlot(LocalTime.of(8, 0), LocalTime.of(9, 30));
        Instructor instructor = new Instructor("Jakub", "test@example.com", null, null, null, null);
        Subject subject = new Subject("Algorithms", "Intermediate", 15, null);
        Classroom classroom = new Classroom("Room 101");
        lectureSession = new LectureSession(1L, null, subject, instructor, classroom, DayOfWeek.MONDAY, timeSlot, 1);
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testFindByEmail() {
        when(lectureSessionRepository.findLectureSessionsByInstructorEmail("test@example.com")).thenReturn(Arrays.asList(lectureSession));
        List<LectureSession> lectureSessions = googleCalendarService.findByEmail("test@example.com");
        assertNotNull(lectureSessions);
        assertEquals(1, lectureSessions.size());
        assertEquals(Arrays.asList(lectureSession), lectureSessions);
        for (LectureSession session : lectureSessions) {
            assertEquals("test@example.com", session.getInstructor().getEmail());
        }
        verify(lectureSessionRepository, times(1)).findLectureSessionsByInstructorEmail("test@example.com");
    }

    @Test
    public void testGetUserEmail() {
        when(oauth2User.getAttribute("email")).thenReturn("test@example.com");
        String email = googleCalendarService.getUserEmail(oauth2User);
        assertEquals("test@example.com", email);
        verify(oauth2User, times(1)).getAttribute("email");
    }

    @Test
    public void testGetNextOccurrenceToday() {
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        LocalDate nextOccurrence = googleCalendarService.getNextOccurrence(dayOfWeek);

        if (today.equals(nextOccurrence)) {
            assertEquals(today.plusWeeks(1), nextOccurrence);
        } else {
            assertTrue(nextOccurrence.isAfter(today));
        }
    }

    @Test
    public void testGetNextOccurrenceDifferentDay() {
        LocalDate today = LocalDate.now();
        DayOfWeek todayDayOfWeek = today.getDayOfWeek();
        DayOfWeek differentDayOfWeek = todayDayOfWeek.plus(1);
        LocalDate nextOccurrence = googleCalendarService.getNextOccurrence(differentDayOfWeek);

        assertTrue(nextOccurrence.isAfter(today));
        assertEquals(differentDayOfWeek, nextOccurrence.getDayOfWeek());
    }

    @Test
    public void testGetNextOccurrenceAllDays() {
        LocalDate today = LocalDate.now();
        DayOfWeek todayDayOfWeek = today.getDayOfWeek();

        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            LocalDate nextOccurrence = googleCalendarService.getNextOccurrence(dayOfWeek);

            if (todayDayOfWeek.equals(dayOfWeek)) {
                if (today.equals(nextOccurrence)) {
                    assertEquals(today.plusWeeks(1), nextOccurrence);
                } else {
                    assertTrue(nextOccurrence.isAfter(today));
                }
            } else {
                assertTrue(nextOccurrence.isAfter(today));
                assertEquals(dayOfWeek, nextOccurrence.getDayOfWeek());
            }
        }
    }

    @Test
    public void testGetCalendarService() throws GeneralSecurityException, IOException {
        Calendar calendar = googleCalendarService.getCalendarService(accessToken);
        assertNotNull(calendar);
    }

    @Test
    public void testCreateCyclicalEvent() {
        Event event = googleCalendarService.createCyclicalEvent(lectureSession);
        assertNotNull(event);
        assertEquals(lectureSession.getSubject().getName(), event.getSummary());
        assertEquals(lectureSession.getClassroom().getName(), event.getLocation());
        assertNotNull(event.getStart());
        assertNotNull(event.getEnd());
        assertTrue(event.getRecurrence().getFirst().contains("RRULE:FREQ=WEEKLY"));
    }

    @Test
    public void testAddEventsToCalendar() throws GeneralSecurityException, IOException {
        when(lectureSessionRepository.findLectureSessionsByInstructorEmail("test@example.com")).thenReturn(Arrays.asList(lectureSession));
        Calendar service = mock(Calendar.class);
        Calendar.Events events = mock(Calendar.Events.class);
        Calendar.Events.Insert insert = mock(Calendar.Events.Insert.class);

        when(service.events()).thenReturn(events);
        when(events.insert(anyString(), any(Event.class))).thenReturn(insert);
        when(insert.execute()).thenReturn(new Event());

        GoogleCalendarService googleCalendarServiceSpy = spy(googleCalendarService);
        doReturn(service).when(googleCalendarServiceSpy).getCalendarService(accessToken);

        googleCalendarServiceSpy.addEventsToCalendar("test@example.com", accessToken);

        verify(service.events(), times(1)).insert(eq("primary"), any(Event.class));
    }

    @Test
    public void testAddEventsToCalendarThrowsGeneralSecurityException() throws GeneralSecurityException, IOException {
        when(lectureSessionRepository.findLectureSessionsByInstructorEmail("test@example.com")).thenReturn(Arrays.asList(lectureSession));
        GoogleCalendarService googleCalendarServiceSpy = spy(googleCalendarService);
        doThrow(new GeneralSecurityException()).when(googleCalendarServiceSpy).getCalendarService(accessToken);

        assertThrows(GeneralSecurityException.class, () -> {
            googleCalendarServiceSpy.addEventsToCalendar("test@example.com", accessToken);
        });
    }

    @Test
    public void testAddEventsToCalendarThrowsIOException() throws GeneralSecurityException, IOException {
        when(lectureSessionRepository.findLectureSessionsByInstructorEmail("test@example.com")).thenReturn(Arrays.asList(lectureSession));
        Calendar service = mock(Calendar.class);
        Calendar.Events events = mock(Calendar.Events.class);
        Calendar.Events.Insert insert = mock(Calendar.Events.Insert.class);

        when(service.events()).thenReturn(events);
        when(events.insert(anyString(), any(Event.class))).thenReturn(insert);
        when(insert.execute()).thenThrow(new IOException());

        GoogleCalendarService googleCalendarServiceSpy = spy(googleCalendarService);
        doReturn(service).when(googleCalendarServiceSpy).getCalendarService(accessToken);

        assertThrows(IOException.class, () -> {
            googleCalendarServiceSpy.addEventsToCalendar("test@example.com", accessToken);
        });
    }


}
