package com.example.lecturescheduler.controller;

import com.example.lecturescheduler.model.*;
import com.example.lecturescheduler.service.LectureSessionService;
import com.example.lecturescheduler.service.algorithm.Chromosome;
import com.example.lecturescheduler.service.algorithm.GeneticAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class AlgorithmControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GeneticAlgorithm geneticAlgorithm;

    @MockBean
    private LectureSessionService lectureSessionService;

    private List<LectureSession> lectureSessions;

    @BeforeEach
    public void setUp() {
        Subject subject = new Subject(1L, "Math", "Intermediate", 45, null);
        SingleGroup group = new SingleGroup(1L, "Group A", "CS", 30, null);
        Instructor instructor = new Instructor(1L, "John Doe", "johndoe@example.com", "CS", Arrays.asList(subject), Arrays.asList(true, false, true), null);
        Classroom classroom = new Classroom(1L, "Classroom 101", null);
        TimeSlot timeSlot = new TimeSlot(LocalTime.of(9, 0), LocalTime.of(10, 30));

        LectureSession lectureSession = new LectureSession(1L, group, subject, instructor, classroom, DayOfWeek.MONDAY, timeSlot, 1);

        lectureSessions = Collections.singletonList(lectureSession);

        Chromosome bestChromosome = new Chromosome(lectureSessions);

        Mockito.when(geneticAlgorithm.run()).thenReturn(bestChromosome);
        Mockito.doNothing().when(lectureSessionService).deleteAllLectureSessions();
        Mockito.when(lectureSessionService.saveLectureSession(Mockito.any())).thenReturn(lectureSession);
    }

    @Test
    public void testTriggerGeneticAlgorithm() throws Exception {
        mockMvc.perform(get("/api/algorithm/trigger-genetic-algorithm"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}

