package com.example.lecturescheduler.controller;

import com.example.lecturescheduler.exception.ResourceNotFoundException;
import com.example.lecturescheduler.model.Classroom;
import com.example.lecturescheduler.service.ClassroomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ClassroomControllerTest {

    @InjectMocks
    private ClassroomController classroomController;

    @Mock
    private ClassroomService classroomService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllClassrooms() {
        Classroom classroom1 = new Classroom();
        classroom1.setId(1L);
        classroom1.setName("Classroom 1");

        Classroom classroom2 = new Classroom();
        classroom2.setId(2L);
        classroom2.setName("Classroom 2");

        List<Classroom> classrooms = Arrays.asList(classroom1, classroom2);
        when(classroomService.findAllClassrooms()).thenReturn(classrooms);

        List<Classroom> result = classroomController.getAllClassrooms();
        assertEquals(2, result.size());
        verify(classroomService, times(1)).findAllClassrooms();
    }

    @Test
    public void testGetClassroomById() {
        Classroom classroom = new Classroom();
        classroom.setId(1L);
        classroom.setName("Classroom 1");

        when(classroomService.findClassroomById(1L)).thenReturn(Optional.of(classroom));

        ResponseEntity<Classroom> response = classroomController.getClassroomById(1L);
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(classroomService, times(1)).findClassroomById(1L);
    }

    @Test
    public void testGetClassroomByIdNotFound() {
        when(classroomService.findClassroomById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            classroomController.getClassroomById(1L);
        });

        verify(classroomService, times(1)).findClassroomById(1L);
    }

    @Test
    public void testCreateClassroom() {
        Classroom classroom = new Classroom();
        classroom.setName("New Classroom");

        when(classroomService.saveClassroom(any(Classroom.class))).thenReturn(classroom);

        Classroom result = classroomController.createClassroom(classroom);
        assertNotNull(result);
        assertEquals("New Classroom", result.getName());
        verify(classroomService, times(1)).saveClassroom(any(Classroom.class));
    }

    @Test
    public void testUpdateClassroom() {
        Classroom classroom = new Classroom();
        classroom.setId(1L);
        classroom.setName("Updated Classroom");

        when(classroomService.updateClassroom(anyLong(), any(Classroom.class))).thenReturn(classroom);

        ResponseEntity<Classroom> response = classroomController.updateClassroom(1L, classroom);
        assertNotNull(response.getBody());
        assertEquals("Updated Classroom", response.getBody().getName());
        verify(classroomService, times(1)).updateClassroom(anyLong(), any(Classroom.class));
    }

    @Test
    public void testDeleteClassroom() {
        doNothing().when(classroomService).deleteClassroom(1L);

        ResponseEntity<?> response = classroomController.deleteClassroom(1L);
        assertEquals(200, response.getStatusCode().value());
        verify(classroomService, times(1)).deleteClassroom(1L);
    }
}
