package com.example.lecturescheduler.controller;

import com.example.lecturescheduler.exception.ResourceNotFoundException;
import com.example.lecturescheduler.model.Subject;
import com.example.lecturescheduler.service.SubjectService;
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

class SubjectControllerTest {

    @InjectMocks
    private SubjectController subjectController;

    @Mock
    private SubjectService subjectService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllSubjects() {
        Subject subject1 = new Subject();
        subject1.setId(1L);
        subject1.setName("Subject 1");

        Subject subject2 = new Subject();
        subject2.setId(2L);
        subject2.setName("Subject 2");

        List<Subject> subjects = Arrays.asList(subject1, subject2);
        when(subjectService.findAllSubjects()).thenReturn(subjects);

        List<Subject> result = subjectController.getAllSubjects();
        assertEquals(2, result.size());
        verify(subjectService, times(1)).findAllSubjects();
    }

    @Test
    void testGetSubjectById() {
        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Subject 1");

        when(subjectService.findSubjectById(1L)).thenReturn(Optional.of(subject));

        ResponseEntity<Subject> response = subjectController.getSubjectById(1L);
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(subjectService, times(1)).findSubjectById(1L);
    }

    @Test
    void testGetSubjectByIdNotFound() {
        when(subjectService.findSubjectById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            subjectController.getSubjectById(1L);
        });

        verify(subjectService, times(1)).findSubjectById(1L);
    }

    @Test
    void testCreateSubject() {
        Subject subject = new Subject();
        subject.setName("New Subject");

        when(subjectService.saveSubject(any(Subject.class))).thenReturn(subject);

        Subject result = subjectController.createSubject(subject);
        assertNotNull(result);
        assertEquals("New Subject", result.getName());
        verify(subjectService, times(1)).saveSubject(any(Subject.class));
    }

    @Test
    void testUpdateSubject() {
        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Updated Subject");

        when(subjectService.updateSubject(anyLong(), any(Subject.class))).thenReturn(subject);

        ResponseEntity<Subject> response = subjectController.updateSubject(1L, subject);
        assertNotNull(response.getBody());
        assertEquals("Updated Subject", response.getBody().getName());
        verify(subjectService, times(1)).updateSubject(anyLong(), any(Subject.class));
    }

    @Test
    void testDeleteSubject() {
        doNothing().when(subjectService).deleteSubject(1L);

        ResponseEntity<?> response = subjectController.deleteSubject(1L);
        assertEquals(200, response.getStatusCode().value());
        verify(subjectService, times(1)).deleteSubject(1L);
    }
}
