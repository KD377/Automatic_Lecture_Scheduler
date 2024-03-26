package com.example.lecturescheduler;

import com.example.lecturescheduler.model.*;
import com.example.lecturescheduler.repository.LectureSessionRepository;
import com.example.lecturescheduler.service.LectureSessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LectureSessionTest {

    @Mock
    private LectureSessionRepository lectureSessionRepository;

    @InjectMocks
    private LectureSessionService lectureSessionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveLectureSession_ShouldReturnSavedSession() {
        LectureSession lectureSession = new LectureSession();
        when(lectureSessionRepository.save(any(LectureSession.class))).thenReturn(lectureSession);

        LectureSession savedSession = lectureSessionService.saveLectureSession(new LectureSession());

        assertThat(savedSession).isNotNull();
        verify(lectureSessionRepository).save(any(LectureSession.class));
    }

    @Test
    void findAllLectureSessions_ShouldReturnAllSessions() {
        when(lectureSessionRepository.findAll()).thenReturn(Arrays.asList(new LectureSession(), new LectureSession()));

        List<LectureSession> sessions = lectureSessionService.findAllLectureSessions();

        assertThat(sessions).hasSize(2);
        verify(lectureSessionRepository).findAll();
    }

    @Test
    void findLectureSessionById_ShouldReturnSession() {
        Long id = 1L;
        LectureSession lectureSession = new LectureSession();
        when(lectureSessionRepository.findById(id)).thenReturn(Optional.of(lectureSession));

        Optional<LectureSession> foundSession = lectureSessionService.findLectureSessionById(id);

        assertThat(foundSession.isPresent()).isTrue();
        assertThat(foundSession.get()).isEqualTo(lectureSession);
        verify(lectureSessionRepository).findById(id);
    }

    @Test
    void updateLectureSession_WhenExists_ShouldUpdateSession() {
        // Given
        Long sessionId = 1L;
        LectureSession existingSession = LectureSession.builder()
                .id(sessionId)
                .build();

        SingleGroup group = SingleGroup.builder()
                .id(1L)
                .name("Group A")
                .programOfStudy("Computer Science")
                .numberOfStudents(25)
                .build();

        Subject subject = Subject.builder()
                .id(1L)
                .name("Introduction to Programming")
                .courseLevel("101")
                .courseLength(45)
                .build();

        Instructor instructor = Instructor.builder()
                .id(1L)
                .name("Dr. John Doe")
                .department("Computer Science")
                .build();

        Classroom classroom = Classroom.builder()
                .id(1L)
                .name("Room 101")
                .build();

        TimeSlot timeSlot = TimeSlot.builder()
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(10, 30))
                .build();


        LectureSession updatedSession = LectureSession.builder()
                .group(group)
                .subject(subject)
                .instructor(instructor)
                .classroom(classroom)
                .day(DayOfWeek.MONDAY)
                .timeSlot(timeSlot)
                .build();

        when(lectureSessionRepository.findById(sessionId)).thenReturn(Optional.of(existingSession));
        when(lectureSessionRepository.save(any(LectureSession.class))).thenReturn(updatedSession);

        // When
        LectureSession result = lectureSessionService.updateLectureSession(sessionId, updatedSession);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getGroup()).isEqualTo(group);
        assertThat(result.getSubject()).isEqualTo(subject);
        assertThat(result.getInstructor()).isEqualTo(instructor);
        assertThat(result.getClassroom()).isEqualTo(classroom);
        assertThat(result.getDay()).isEqualTo(DayOfWeek.MONDAY);
        assertThat(result.getTimeSlot()).usingRecursiveComparison().isEqualTo(timeSlot);

        verify(lectureSessionRepository).save(any(LectureSession.class));
    }

    @Test
    void deleteLectureSession_WhenExists_ShouldDeleteSession() {
        Long id = 1L;
        LectureSession existingSession = new LectureSession();
        when(lectureSessionRepository.findById(id)).thenReturn(Optional.of(existingSession));
        doNothing().when(lectureSessionRepository).delete(any(LectureSession.class));

        lectureSessionService.deleteLectureSession(id);

        verify(lectureSessionRepository).delete(any(LectureSession.class));
    }

}
