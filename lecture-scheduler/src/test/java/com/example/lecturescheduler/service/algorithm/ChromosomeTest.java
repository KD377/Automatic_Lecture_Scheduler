package com.example.lecturescheduler.service.algorithm;

import com.example.lecturescheduler.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ChromosomeTest {

    private LectureSession session1;
    private LectureSession session2;
    private List<LectureSession> lectureSessions;
    private Chromosome chromosome;

    @BeforeEach
    public void setup() {
        session1 = new LectureSession(1L, new SingleGroup(), new Subject(), new Instructor(), new Classroom(), DayOfWeek.MONDAY, new TimeSlot(), 1);
        session2 = new LectureSession(2L, new SingleGroup(), new Subject(), new Instructor(), new Classroom(), DayOfWeek.TUESDAY, new TimeSlot(), 2);
        lectureSessions = Arrays.asList(session1, session2);
        chromosome = new Chromosome(lectureSessions);
    }

    @Test
    public void testConstructor() {
        Chromosome newChromosome = new Chromosome(lectureSessions);
        assertNotNull(newChromosome);
        assertEquals(lectureSessions, newChromosome.getLectureSessions());
        assertEquals(0, newChromosome.getFitnessScore(), 0.001);
    }

    @Test
    public void testGetLectureSessions() {
        List<LectureSession> sessions = chromosome.getLectureSessions();
        assertEquals(lectureSessions, sessions);
    }

    @Test
    public void testSetLectureSessions() {
        List<LectureSession> newSessions = Arrays.asList(session2);
        chromosome.setLectureSessions(newSessions);
        assertEquals(newSessions, chromosome.getLectureSessions());
    }

    @Test
    public void testGetFitnessScore() {
        double score = chromosome.getFitnessScore();
        assertEquals(0, score, 0.001);
    }

    @Test
    public void testSetFitnessScore() {
        double newScore = 10.5;
        chromosome.setFitnessScore(newScore);
        assertEquals(newScore, chromosome.getFitnessScore(), 0.001);
    }
}
