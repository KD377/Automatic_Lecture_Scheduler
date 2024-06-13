package com.example.lecturescheduler.service;

import com.example.lecturescheduler.exception.ResourceNotFoundException;
import com.example.lecturescheduler.model.Instructor;
import com.example.lecturescheduler.model.SingleGroup;
import com.example.lecturescheduler.model.Subject;
import com.example.lecturescheduler.repository.GroupRepository;
import com.example.lecturescheduler.repository.InstructorRepository;
import com.example.lecturescheduler.repository.SubjectRepository;
import com.example.lecturescheduler.service.InstructorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InstructorServiceTest {

    @Mock
    private InstructorRepository instructorRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private InstructorService instructorService;

    private Instructor instructor;
    private Subject subject;
    private SingleGroup group;

    @BeforeEach
    public void setup() {
        instructor = new Instructor();
        instructor.setId(1L);
        instructor.setName("John Doe");
        instructor.setDepartment("Computer Science");
        instructor.setSubjectsTaught(new ArrayList<>());
        instructor.setGroups(new ArrayList<>());

        subject = new Subject();
        subject.setId(1L);
        subject.setName("Math");

        group = new SingleGroup();
        group.setId(1L);
        group.setName("Group 1");
    }

    @Test
    public void testSaveInstructor() {
        when(instructorRepository.save(instructor)).thenReturn(instructor);

        Instructor savedInstructor = instructorService.saveInstructor(instructor);

        assertNotNull(savedInstructor);
        assertEquals("John Doe", savedInstructor.getName());
        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    public void testFindBySubject() {
        when(instructorRepository.findBySubject(subject)).thenReturn(Arrays.asList(instructor));

        List<Instructor> instructors = instructorService.findBySubject(subject);

        assertNotNull(instructors);
        assertEquals(1, instructors.size());
        verify(instructorRepository, times(1)).findBySubject(subject);
    }

    @Test
    public void testFindAllInstructors() {
        when(instructorRepository.findAll()).thenReturn(Arrays.asList(instructor));

        List<Instructor> instructors = instructorService.findAllInstructors();

        assertNotNull(instructors);
        assertEquals(1, instructors.size());
        verify(instructorRepository, times(1)).findAll();
    }

    @Test
    public void testFindInstructorById() {
        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));

        Optional<Instructor> foundInstructor = instructorService.findInstructorById(1L);

        assertTrue(foundInstructor.isPresent());
        assertEquals("John Doe", foundInstructor.get().getName());
        verify(instructorRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindInstructorById_NotFound() {
        when(instructorRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Instructor> foundInstructor = instructorService.findInstructorById(1L);

        assertFalse(foundInstructor.isPresent());
        verify(instructorRepository, times(1)).findById(1L);
    }

    @Test
    public void testUpdateInstructor() {
        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));
        when(instructorRepository.save(instructor)).thenReturn(instructor);

        instructor.setName("Updated John Doe");
        Instructor updatedInstructor = instructorService.updateInstructor(1L, instructor);

        assertNotNull(updatedInstructor);
        assertEquals("Updated John Doe", updatedInstructor.getName());
        verify(instructorRepository, times(1)).findById(1L);
        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    public void testUpdateInstructor_NotFound() {
        when(instructorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            instructorService.updateInstructor(1L, instructor);
        });
        verify(instructorRepository, times(1)).findById(1L);
    }

    @Test
    public void testDeleteInstructor() {
        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));
        doNothing().when(instructorRepository).delete(instructor);

        instructorService.deleteInstructor(1L);

        verify(instructorRepository, times(1)).findById(1L);
        verify(instructorRepository, times(1)).delete(instructor);
    }

    @Test
    public void testDeleteInstructor_NotFound() {
        when(instructorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            instructorService.deleteInstructor(1L);
        });
        verify(instructorRepository, times(1)).findById(1L);
    }
}
