package com.example.lecturescheduler;

import com.example.lecturescheduler.exception.ResourceNotFoundException;
import com.example.lecturescheduler.model.Instructor;
import com.example.lecturescheduler.repository.InstructorRepository;
import com.example.lecturescheduler.service.InstructorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
/*
class InstructorServiceTest {

    @Mock
    private InstructorRepository instructorRepository;

    @InjectMocks
    private InstructorService instructorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllInstructors_ShouldReturnAllInstructors() {
        when(instructorRepository.findAll()).thenReturn(Arrays.asList(
                new Instructor("John Doe", "Mathematics", Arrays.asList("Algebra", "Calculus"), "Available", "Mornings"),
                new Instructor("Jane Smith", "Physics", Arrays.asList("Mechanics", "Quantum Physics"), "Available", "Afternoons")
        ));

        List<Instructor> result = instructorService.findAllInstructors();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("John Doe");
        assertThat(result.get(1).getName()).isEqualTo("Jane Smith");
        verify(instructorRepository).findAll();
    }

    @Test
    void saveInstructor_ShouldReturnSavedInstructor() {
        Instructor instructor = new Instructor("Mark Spencer", "Chemistry", List.of("Organic Chemistry"), "Available", "Evenings");
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        Instructor savedInstructor = instructorService.saveInstructor(instructor);

        assertThat(savedInstructor.getName()).isEqualTo("Mark Spencer");
        verify(instructorRepository).save(instructor);
    }


    @Test
    void updateInstructor_WhenNotFound_ShouldThrowException() {
        Long instructorId = 1L;
        Instructor instructorDetails = new Instructor("Updated Name", "Updated Department", List.of("Updated Subject"), "Available", "No preference");
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> instructorService.updateInstructor(instructorId, instructorDetails))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Instructor not found for this id :: " + instructorId);

        verify(instructorRepository, never()).save(any(Instructor.class));
    }

    @Test
    void deleteInstructor_ShouldDeleteInstructor() {
        Long instructorId = 2L;
        Instructor instructor = new Instructor("Emily Watson", "Biology", List.of("Genetics"), "Unavailable", "No preference");
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
        doNothing().when(instructorRepository).delete(instructor);

        instructorService.deleteInstructor(instructorId);

        verify(instructorRepository).delete(instructor);
    }

}
*/