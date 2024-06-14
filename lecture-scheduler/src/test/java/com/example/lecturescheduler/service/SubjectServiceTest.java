package com.example.lecturescheduler.service;

import com.example.lecturescheduler.exception.ResourceNotFoundException;
import com.example.lecturescheduler.model.Subject;
import com.example.lecturescheduler.repository.SubjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubjectServiceTest {

    @Mock
    private SubjectRepository subjectRepository;


    @InjectMocks
    private SubjectService subjectService;

    private Subject subject1;
    private Subject subject2;
    private Subject subject3;

    @BeforeEach
    public void setup() {
        subject1 = new Subject();
        subject1.setId(1L);
        subject1.setName("Mathematics");
        subject1.setCourseLevel("Bachelor");
        subject1.setCourseLength(45);

        subject2 = new Subject();
        subject2.setId(2L);
        subject2.setName("Physics");
        subject2.setCourseLevel("Master");
        subject2.setCourseLength(30);

        subject3 = new Subject();
        subject3.setId(3L);
        subject3.setName("Chemistry");
        subject3.setCourseLevel("Bachelor");
        subject3.setCourseLength(75);
    }

    @Test
    public void testSaveSubject() {
        when(subjectRepository.save(subject1)).thenReturn(subject1);

        Subject savedSubject = subjectService.saveSubject(subject1);

        assertNotNull(savedSubject);
        assertEquals("Mathematics", savedSubject.getName());
        verify(subjectRepository, times(1)).save(subject1);
    }

    @Test
    public void testFindAllSubjects() {
        when(subjectRepository.findAll()).thenReturn(Arrays.asList(subject1, subject2));

        List<Subject> subjects = subjectService.findAllSubjects();

        assertNotNull(subjects);
        assertEquals(2, subjects.size());
        verify(subjectRepository, times(1)).findAll();
    }

    @Test
    public void testGetSubjectsWithOccurrences() {
        when(subjectRepository.findAll()).thenReturn(Arrays.asList(subject1, subject2, subject3));

        Map<Subject, Integer> subjectsWithOccurrences = subjectService.getSubjectsWithOcurrences();

        assertNotNull(subjectsWithOccurrences);
        assertEquals(3, subjectsWithOccurrences.size());
        assertTrue(subjectsWithOccurrences.containsKey(subject1));
        assertTrue(subjectsWithOccurrences.containsKey(subject2));
        assertTrue(subjectsWithOccurrences.containsKey(subject3));

        assertEquals(2, subjectsWithOccurrences.get(subject1));
        assertEquals(1, subjectsWithOccurrences.get(subject2));
        assertEquals(3, subjectsWithOccurrences.get(subject3));
        verify(subjectRepository, times(1)).findAll();
    }

    @Test
    public void testFindSubjectById() {
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject1));

        Optional<Subject> foundSubject = subjectService.findSubjectById(1L);

        assertTrue(foundSubject.isPresent());
        assertEquals("Mathematics", foundSubject.get().getName());
        verify(subjectRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindSubjectByIdNotFound() {
        when(subjectRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Subject> foundSubject = subjectService.findSubjectById(1L);

        assertFalse(foundSubject.isPresent());
        verify(subjectRepository, times(1)).findById(1L);
    }

    @Test
    public void testUpdateSubject() {
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject1));
        when(subjectRepository.save(subject1)).thenReturn(subject1);

        subject1.setName("Updated Mathematics");
        Subject updatedSubject = subjectService.updateSubject(1L, subject1);

        assertNotNull(updatedSubject);
        assertEquals("Updated Mathematics", updatedSubject.getName());
        verify(subjectRepository, times(1)).findById(1L);
        verify(subjectRepository, times(1)).save(subject1);
    }

    @Test
    public void testUpdateSubjectNotFound() {
        when(subjectRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            subjectService.updateSubject(1L, subject1);
        });
        verify(subjectRepository, times(1)).findById(1L);
    }

    @Test
    public void testDeleteSubject() {
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject1));
        doNothing().when(subjectRepository).delete(subject1);

        subjectService.deleteSubject(1L);

        verify(subjectRepository, times(1)).findById(1L);
        verify(subjectRepository, times(1)).delete(subject1);
    }

    @Test
    public void testDeleteSubjectNotFound() {
        when(subjectRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            subjectService.deleteSubject(1L);
        });
        verify(subjectRepository, times(1)).findById(1L);
    }
}
