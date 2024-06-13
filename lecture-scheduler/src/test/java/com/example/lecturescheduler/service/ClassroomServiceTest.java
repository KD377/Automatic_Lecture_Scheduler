package com.example.lecturescheduler.service;

import com.example.lecturescheduler.exception.ResourceNotFoundException;
import com.example.lecturescheduler.model.Classroom;
import com.example.lecturescheduler.model.Subject;
import com.example.lecturescheduler.repository.ClassroomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClassroomServiceTest {

    @Mock
    private ClassroomRepository classroomRepository;

    @InjectMocks
    private ClassroomService classroomService;

    private Classroom classroom1;
    private Classroom classroom2;
    private Subject subject;

    @BeforeEach
    public void setup() {
        classroom1 = new Classroom();
        classroom1.setId(1L);
        classroom1.setName("Classroom  101");

        classroom2 = new Classroom();
        classroom2.setId(2L);
        classroom2.setName("Classroom  102");

        subject = new Subject();
        subject.setId(1L);
        subject.setName("Math");
    }

    @Test
    void testSaveClassroom() {
        when(classroomRepository.save(classroom1)).thenReturn(classroom1);

        Classroom savedClassroom = classroomService.saveClassroom(classroom1);

        assertNotNull(savedClassroom);
        assertEquals("Classroom  101", savedClassroom.getName());
        verify(classroomRepository, times(1)).save(classroom1);
    }

    @Test
    void testFindAllClassrooms() {
        when(classroomRepository.findAll()).thenReturn(Arrays.asList(classroom1, classroom2));

        List<Classroom> classrooms = classroomService.findAllClassrooms();

        assertNotNull(classrooms);
        assertEquals(2, classrooms.size());
        verify(classroomRepository, times(1)).findAll();
    }

    @Test
    void testFindBySubject() {
        when(classroomRepository.findBySubject(subject)).thenReturn(Arrays.asList(classroom1));

        List<Classroom> classrooms = classroomService.findBySubject(subject);

        assertNotNull(classrooms);
        assertEquals(1, classrooms.size());
        verify(classroomRepository, times(1)).findBySubject(subject);
    }

    @Test
    void testFindClassroomById() {
        when(classroomRepository.findById(1L)).thenReturn(Optional.of(classroom1));

        Optional<Classroom> foundClassroom = classroomService.findClassroomById(1L);

        assertTrue(foundClassroom.isPresent());
        assertEquals("Classroom  101", foundClassroom.get().getName());
        verify(classroomRepository, times(1)).findById(1L);
    }

    @Test
    void testFindClassroomById_NotFound() {
        when(classroomRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Classroom> foundClassroom = classroomService.findClassroomById(1L);

        assertFalse(foundClassroom.isPresent());
        verify(classroomRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateClassroom() {
        when(classroomRepository.findById(1L)).thenReturn(Optional.of(classroom1));
        when(classroomRepository.save(classroom1)).thenReturn(classroom1);

        classroom1.setName("Updated Classroom 101");
        Classroom updatedClassroom = classroomService.updateClassroom(1L, classroom1);

        assertNotNull(updatedClassroom);
        assertEquals("Updated Classroom 101", updatedClassroom.getName());
        verify(classroomRepository, times(1)).findById(1L);
        verify(classroomRepository, times(1)).save(classroom1);
    }

    @Test
    void testUpdateClassroom_NotFound() {
        when(classroomRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            classroomService.updateClassroom(1L, classroom1);
        });
        verify(classroomRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteClassroom() {
        when(classroomRepository.findById(1L)).thenReturn(Optional.of(classroom1));
        doNothing().when(classroomRepository).delete(classroom1);

        classroomService.deleteClassroom(1L);

        verify(classroomRepository, times(1)).findById(1L);
        verify(classroomRepository, times(1)).delete(classroom1);
    }

    @Test
    void testDeleteClassroom_NotFound() {
        when(classroomRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            classroomService.deleteClassroom(1L);
        });
        verify(classroomRepository, times(1)).findById(1L);
    }
}
