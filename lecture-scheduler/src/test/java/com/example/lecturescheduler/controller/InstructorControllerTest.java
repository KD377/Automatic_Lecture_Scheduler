package com.example.lecturescheduler.controller;

import com.example.lecturescheduler.exception.ResourceNotFoundException;
import com.example.lecturescheduler.model.Instructor;
import com.example.lecturescheduler.service.InstructorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class InstructorControllerTest {

    @InjectMocks
    private InstructorController instructorController;

    @Mock
    private InstructorService instructorService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllInstructors() {
        Instructor instructor1 = new Instructor();
        instructor1.setId(1L);
        instructor1.setName("Instructor 1");

        Instructor instructor2 = new Instructor();
        instructor2.setId(2L);
        instructor2.setName("Instructor 2");

        List<Instructor> instructors = Arrays.asList(instructor1, instructor2);
        when(instructorService.findAllInstructors()).thenReturn(instructors);

        List<Instructor> result = instructorController.getAllInstructors();
        assertEquals(2, result.size());
        verify(instructorService, times(1)).findAllInstructors();
    }

    @Test
    public void testGetInstructorById() {
        Instructor instructor = new Instructor();
        instructor.setId(1L);
        instructor.setName("Instructor 1");

        when(instructorService.findInstructorById(1L)).thenReturn(Optional.of(instructor));

        ResponseEntity<Instructor> response = instructorController.getInstructorById(1L);
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(instructorService, times(1)).findInstructorById(1L);
    }

    @Test
    public void testGetInstructorByIdNotFound() {
        when(instructorService.findInstructorById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            instructorController.getInstructorById(1L);
        });

        verify(instructorService, times(1)).findInstructorById(1L);
    }

    @Test
    public void testCreateInstructor() {
        Instructor instructor = new Instructor();
        instructor.setName("New Instructor");

        when(instructorService.saveInstructor(any(Instructor.class))).thenReturn(instructor);

        Instructor result = instructorController.createInstructor(instructor);
        assertNotNull(result);
        assertEquals("New Instructor", result.getName());
        verify(instructorService, times(1)).saveInstructor(any(Instructor.class));
    }

    @Test
    public void testUpdateInstructor() {
        Instructor instructor = new Instructor();
        instructor.setId(1L);
        instructor.setName("Updated Instructor");

        when(instructorService.updateInstructor(anyLong(), any(Instructor.class))).thenReturn(instructor);

        ResponseEntity<Instructor> response = instructorController.updateInstructor(1L, instructor);
        assertNotNull(response.getBody());
        assertEquals("Updated Instructor", response.getBody().getName());
        verify(instructorService, times(1)).updateInstructor(anyLong(), any(Instructor.class));
    }

    @Test
    public void testDeleteInstructor() {
        doNothing().when(instructorService).deleteInstructor(1L);

        ResponseEntity<?> response = instructorController.deleteInstructor(1L);
        assertEquals(200, response.getStatusCode().value());
        verify(instructorService, times(1)).deleteInstructor(1L);
    }
}
