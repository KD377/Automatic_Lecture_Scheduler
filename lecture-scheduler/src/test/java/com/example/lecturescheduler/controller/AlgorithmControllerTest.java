package com.example.lecturescheduler.controller;

import com.example.lecturescheduler.dto.LectureSessionResponse;
import com.example.lecturescheduler.model.*;
import com.example.lecturescheduler.service.LectureSessionService;
import com.example.lecturescheduler.service.algorithm.Chromosome;
import com.example.lecturescheduler.service.algorithm.GeneticAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AlgorithmControllerTest {

    @Mock
    private GeneticAlgorithm geneticAlgorithm;

    @Mock
    private LectureSessionService lectureSessionService;

    @InjectMocks
    private AlgorithmController algorithmController;

    private List<LectureSession> lectureSessions;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        Subject subject = new Subject(1L, "Math", "Intermediate", 45, null);
        SingleGroup group = new SingleGroup(1L, "Group A", "CS", 30, null);
        Instructor instructor = new Instructor(1L, "John Doe", "johndoe@example.com", "CS", Arrays.asList(subject), Arrays.asList(true, false, true), null);
        Classroom classroom = new Classroom(1L, "Classroom 101", null);
        TimeSlot timeSlot = new TimeSlot(LocalTime.of(9, 0), LocalTime.of(10, 30));

        LectureSession lectureSession = new LectureSession(1L, group, subject, instructor, classroom, DayOfWeek.MONDAY, timeSlot, 1);

        lectureSessions = Collections.singletonList(lectureSession);

        Chromosome bestChromosome = new Chromosome(lectureSessions);

        when(geneticAlgorithm.run()).thenReturn(bestChromosome);
        doNothing().when(lectureSessionService).deleteAllLectureSessions();
        when(lectureSessionService.saveLectureSession(any(LectureSession.class))).thenReturn(lectureSession);
    }

    @Test
    public void testTriggerGeneticAlgorithm() {
        ResponseEntity<Map<String, List<LectureSessionResponse>>> response = algorithmController.triggerGeneticAlgorithm();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertTrue(response.getBody().containsKey("Group A"));

        List<LectureSessionResponse> sessions = response.getBody().get("Group A");
        assertEquals(1, sessions.size());

        LectureSessionResponse sessionResponse = sessions.get(0);
        assertEquals("Math", sessionResponse.getSubjectName());
        assertEquals("Group A", sessionResponse.getGroupName());
        assertEquals("John Doe", sessionResponse.getLecturer());
        assertEquals("Classroom 101", sessionResponse.getClassroom());
        assertEquals(1, sessionResponse.getNumberOfTimeSlot());
        assertEquals("MONDAY", sessionResponse.getDayOfWeek().toString());
    }
}
