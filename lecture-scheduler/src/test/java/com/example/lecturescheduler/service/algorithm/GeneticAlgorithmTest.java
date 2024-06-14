package com.example.lecturescheduler.service.algorithm;

import com.example.lecturescheduler.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GeneticAlgorithmTest {

    @Mock
    private PopulationGenerator populationGenerator;

    @InjectMocks
    private GeneticAlgorithm geneticAlgorithm;

    private Chromosome chromosome;
    private List<LectureSession> lectureSessions;
    private SingleGroup group1;
    private SingleGroup group2;
    private Instructor instructor1;
    private Instructor instructor2;
    private Subject subject1;
    private Subject subject2;
    private Classroom classroom1;
    private Classroom classroom2;

    @BeforeEach
    public void setup() {
        subject1 = new Subject();
        subject2 = new Subject();

        instructor1 = new Instructor();
        instructor1.setPreferences(Arrays.asList(true, false, true, false, true));

        instructor2 = new Instructor();
        instructor2.setPreferences(Arrays.asList(true, true, true, true, true));

        group1 = new SingleGroup();
        group2 = new SingleGroup();

        classroom1 = new Classroom();
        classroom2 = new Classroom();

        lectureSessions = new ArrayList<>();
        lectureSessions.add(new LectureSession(1L, group1, subject1, instructor1, classroom1, DayOfWeek.MONDAY, new TimeSlot(), 1));
        lectureSessions.add(new LectureSession(2L, group2, subject2, instructor2, classroom2, DayOfWeek.TUESDAY, new TimeSlot(), 2));

        chromosome = new Chromosome(lectureSessions);
    }

    @Test
    public void testRun() {
        when(populationGenerator.generateChromosome()).thenReturn(chromosome);

        Chromosome result = geneticAlgorithm.run();

        assertNotNull(result);
    }

    @Test
    public void testGetBestChromosome() {
        Chromosome bestChromosome = new Chromosome(lectureSessions);
        bestChromosome.setFitnessScore(10.0);

        Chromosome otherChromosome = new Chromosome(lectureSessions);
        otherChromosome.setFitnessScore(5.0);

        List<Chromosome> population = Arrays.asList(bestChromosome, otherChromosome);

        Chromosome result = geneticAlgorithm.getBestChromosome(population);

        assertEquals(bestChromosome, result);
    }


    @Test
    public void testMutate() {
        when(populationGenerator.generateChromosome()).thenReturn(chromosome);

        Chromosome originalChromosome = new Chromosome(new ArrayList<>(lectureSessions));
        Chromosome newChromosome = geneticAlgorithm.run();

        assertNotEquals(originalChromosome, newChromosome);
    }

}
