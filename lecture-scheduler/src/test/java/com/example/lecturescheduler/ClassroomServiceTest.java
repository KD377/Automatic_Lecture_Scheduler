package com.example.lecturescheduler;

import com.example.lecturescheduler.exception.ResourceNotFoundException;
import com.example.lecturescheduler.model.Classroom;
import com.example.lecturescheduler.repository.ClassroomRepository;
import com.example.lecturescheduler.service.ClassroomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ClassroomServiceTest {

    @Mock
    private ClassroomRepository classroomRepository;

    @InjectMocks
    private ClassroomService classroomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveClassroom_shouldReturnSavedClassroom() {
        Classroom classroom = Classroom.builder()
                .name("Fizyka 201")
                .capacity(25)
                .specialFeatures("Laboratorium")
                .build();

        when(classroomRepository.save(any(Classroom.class))).thenReturn(classroom);

        Classroom savedClassroom = classroomService.saveClassroom(classroom);

        assertThat(savedClassroom).isNotNull();
        verify(classroomRepository).save(any(Classroom.class));
    }

    @Test
    void findAllClassrooms_shouldReturnAllClassrooms() {
        when(classroomRepository.findAll()).thenReturn(Arrays.asList(new Classroom(), new Classroom()));

        List<Classroom> classrooms = classroomService.findAllClassrooms();

        assertThat(classrooms).hasSize(2);
        verify(classroomRepository).findAll();
    }

    @Test
    void findClassroomById_shouldReturnClassroom() {
        Long classroomId = 1L;
        Optional<Classroom> classroomOptional = Optional.of(new Classroom());
        when(classroomRepository.findById(classroomId)).thenReturn(classroomOptional);

        Optional<Classroom> foundClassroom = classroomService.findClassroomById(classroomId);

        assertThat(foundClassroom.isPresent()).isTrue();
        verify(classroomRepository).findById(classroomId);
    }

    @Test
    void updateClassroom_shouldReturnUpdatedClassroom() {
        Long classroomId = 1L;
        Classroom existingClassroom = Classroom.builder()
                .name("Fizyka 201")
                .capacity(25)
                .specialFeatures("Laboratorium")
                .build();
        Classroom updatedDetails = Classroom.builder()
                .name("Matematyka 201")
                .capacity(50)
                .specialFeatures("Tablica interaktywna")
                .build();
        when(classroomRepository.findById(classroomId)).thenReturn(Optional.of(existingClassroom));
        when(classroomRepository.save(any(Classroom.class))).thenReturn(updatedDetails);

        Classroom updatedClassroom = classroomService.updateClassroom(classroomId, updatedDetails);

        assertThat(updatedClassroom).isNotNull();
        verify(classroomRepository).save(any(Classroom.class));
    }

    @Test
    void deleteClassroom_shouldDeleteClassroom() {
        Long classroomId = 1L;
        Classroom classroom = Classroom.builder()
                .name("Fizyka 201")
                .capacity(25)
                .specialFeatures("Laboratorium")
                .build();
        when(classroomRepository.findById(classroomId)).thenReturn(Optional.of(classroom));
        doNothing().when(classroomRepository).delete(classroom);

        classroomService.deleteClassroom(classroomId);

        verify(classroomRepository).delete(classroom);
    }

    @Test
    void updateClassroom_whenNotFound_shouldThrowException() {
        Long classroomId = 1L;
        Classroom updatedDetails = Classroom.builder()
                .name("Update 201")
                .capacity(30)
                .specialFeatures("Laboratorium, komputery")
                .build();
        when(classroomRepository.findById(classroomId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> classroomService.updateClassroom(classroomId, updatedDetails))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Classroom not found for this id :: " + classroomId);

        verify(classroomRepository, never()).save(any(Classroom.class));
    }

}
