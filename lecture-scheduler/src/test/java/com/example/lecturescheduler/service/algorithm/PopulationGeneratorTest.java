package com.example.lecturescheduler.service.algorithm;

import com.example.lecturescheduler.model.*;
import com.example.lecturescheduler.service.ClassroomService;
import com.example.lecturescheduler.service.InstructorService;
import com.example.lecturescheduler.service.SingleGroupService;
import com.example.lecturescheduler.service.SubjectService;
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
public class PopulationGeneratorTest {

    @Mock
    private SubjectService subjectService;

    @Mock
    private InstructorService instructorService;

    @Mock
    private SingleGroupService singleGroupService;

    @Mock
    private ClassroomService classroomService;

    @InjectMocks
    private PopulationGenerator populationGenerator;

    private Subject subject1;
    private Subject subject2;
    private Instructor instructor1;
    private Instructor instructor2;
    private SingleGroup group1;
    private SingleGroup group2;
    private Classroom classroom1;
    private Classroom classroom2;

    @BeforeEach
    public void setup() {
        subject1 = new Subject("Algorithms", "Intermediate", 15, null);
        subject2 = new Subject("Maths", "Intermediate", 15, null);

        instructor1 = new Instructor();
        instructor1.setId(1L);
        instructor1.setName("James Blunt");
        instructor1.setDepartment("IT");
        instructor1.setSubjectsTaught(Collections.singletonList(subject1));

        instructor2 = new Instructor();
        instructor2.setId(2L);
        instructor2.setName("Adam Smith");
        instructor2.setDepartment("IT");
        instructor2.setSubjectsTaught(Collections.singletonList(subject2));

        group1 = new SingleGroup("Group 1", "IT", 20, null);
        group2 = new SingleGroup("Group 2", "IT", 25, null);

        classroom1 = new Classroom("Room 101");
        classroom2 = new Classroom("Room 102");
    }

    @Test
    public void testGenerateChromosome() {
        Map<Subject, Integer> subjects = new HashMap<>();
        subjects.put(subject1, 2);
        subjects.put(subject2, 1);

        List<SingleGroup> groups = Arrays.asList(group1, group2);
        List<Instructor> instructors = Arrays.asList(instructor1, instructor2);
        List<Classroom> classrooms = Arrays.asList(classroom1, classroom2);

        when(subjectService.getSubjectsWithOcurrences()).thenReturn(subjects);
        when(singleGroupService.findAllGroups()).thenReturn(groups);
        when(instructorService.findBySubject(subject1)).thenReturn(instructors);
        when(instructorService.findBySubject(subject2)).thenReturn(instructors);
        when(classroomService.findBySubject(subject1)).thenReturn(classrooms);
        when(classroomService.findBySubject(subject2)).thenReturn(classrooms);

        Chromosome chromosome = populationGenerator.generateChromosome();

        assertNotNull(chromosome);
        assertFalse(chromosome.getLectureSessions().isEmpty());
    }

    @Test
    public void testCheckConflictsNoConflicts() {
        LectureSession session1 = LectureSession.builder()
                .group(group1)
                .subject(subject1)
                .instructor(instructor1)
                .classroom(classroom1)
                .day(DayOfWeek.MONDAY)
                .numberOfTimeSlot(1)
                .build();

        LectureSession session2 = LectureSession.builder()
                .group(group2)
                .subject(subject2)
                .instructor(instructor2)
                .classroom(classroom2)
                .day(DayOfWeek.MONDAY)
                .numberOfTimeSlot(2)
                .build();

        List<LectureSession> existingSessions = Collections.singletonList(session1);
        assertTrue(populationGenerator.checkConflicts(session2, existingSessions));
    }

    @Test
    public void testCheckConflictsWithConflicts() {
        LectureSession session1 = LectureSession.builder()
                .group(group1)
                .subject(subject1)
                .instructor(instructor1)
                .classroom(classroom1)
                .day(DayOfWeek.MONDAY)
                .numberOfTimeSlot(1)
                .build();

        LectureSession session2 = LectureSession.builder()
                .group(group1)
                .subject(subject2)
                .instructor(instructor1)
                .classroom(classroom1)
                .day(DayOfWeek.MONDAY)
                .numberOfTimeSlot(1)
                .build();

        List<LectureSession> existingSessions = Collections.singletonList(session1);
        assertFalse(populationGenerator.checkConflicts(session2, existingSessions));
    }

    @Test
    public void testCheckConflictsSameInstructorConflict() {
        LectureSession session1 = LectureSession.builder()
                .group(group1)
                .subject(subject1)
                .instructor(instructor1)
                .classroom(classroom1)
                .day(DayOfWeek.MONDAY)
                .numberOfTimeSlot(1)
                .build();

        LectureSession session2 = LectureSession.builder()
                .group(group2)
                .subject(subject2)
                .instructor(instructor1)
                .classroom(classroom2)
                .day(DayOfWeek.MONDAY)
                .numberOfTimeSlot(1)
                .build();

        List<LectureSession> existingSessions = Collections.singletonList(session1);
        assertFalse(populationGenerator.checkConflicts(session2, existingSessions));
    }

    @Test
    public void testCheckConflictsSameClassroomConflict() {
        LectureSession session1 = LectureSession.builder()
                .group(group1)
                .subject(subject1)
                .instructor(instructor1)
                .classroom(classroom1)
                .day(DayOfWeek.MONDAY)
                .numberOfTimeSlot(1)
                .build();

        LectureSession session2 = LectureSession.builder()
                .group(group2)
                .subject(subject2)
                .instructor(instructor2)
                .classroom(classroom1)
                .day(DayOfWeek.MONDAY)
                .numberOfTimeSlot(1)
                .build();

        List<LectureSession> existingSessions = Collections.singletonList(session1);
        assertFalse(populationGenerator.checkConflicts(session2, existingSessions));
    }

}
