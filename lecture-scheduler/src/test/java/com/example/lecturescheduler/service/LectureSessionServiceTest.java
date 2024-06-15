package com.example.lecturescheduler.service;

import com.example.lecturescheduler.model.LectureSession;
import com.example.lecturescheduler.model.TimeSlot;
import com.example.lecturescheduler.repository.LectureSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LectureSessionServiceTest {

    @Mock
    private LectureSessionRepository lectureSessionRepository;

    @InjectMocks
    private LectureSessionService lectureSessionService;

    private LectureSession lectureSession;

    @BeforeEach
    public void setUp() {
        TimeSlot timeSlot = new TimeSlot(LocalTime.of(9, 0), LocalTime.of(10, 30));
        lectureSession = new LectureSession(1L, null, null, null, null, DayOfWeek.MONDAY, timeSlot, 1);
    }

    @Test
    public void testSaveLectureSession() {
        when(lectureSessionRepository.save(any(LectureSession.class))).thenReturn(lectureSession);

        LectureSession savedLectureSession = lectureSessionService.saveLectureSession(lectureSession);

        assertNotNull(savedLectureSession);
        assertEquals(lectureSession.getNumberOfTimeSlot(), savedLectureSession.getNumberOfTimeSlot());
        assertEquals(LocalTime.of(8, 0), savedLectureSession.getTimeSlot().getStartTime());
        assertEquals(LocalTime.of(9, 30), savedLectureSession.getTimeSlot().getEndTime());
        verify(lectureSessionRepository, times(1)).save(any(LectureSession.class));
    }

    @Test
    public void testDeleteAllLectureSessions() {
        doNothing().when(lectureSessionRepository).deleteAll();

        lectureSessionService.deleteAllLectureSessions();

        verify(lectureSessionRepository, times(1)).deleteAll();
    }

}
