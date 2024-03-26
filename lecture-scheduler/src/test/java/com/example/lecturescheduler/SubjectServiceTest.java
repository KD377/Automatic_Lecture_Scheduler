package com.example.lecturescheduler;

import com.example.lecturescheduler.exception.ResourceNotFoundException;
import com.example.lecturescheduler.model.Classroom;
import com.example.lecturescheduler.model.Subject;
import com.example.lecturescheduler.repository.ClassroomRepository;
import com.example.lecturescheduler.repository.SubjectRepository;
import com.example.lecturescheduler.service.SubjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class SubjectServiceTest {

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private ClassroomRepository classroomRepository;

    @InjectMocks
    private SubjectService subjectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllSubjects_ShouldReturnAllSubjects() {
        when(subjectRepository.findAll()).thenReturn(List.of(
                new Subject("Math", "101", 60, null),
                new Subject("Physics", "201", 45, null)
        ));

        List<Subject> subjects = subjectService.findAllSubjects();

        assertThat(subjects).hasSize(2);
        assertThat(subjects.get(0).getName()).isEqualTo("Math");
        assertThat(subjects.get(1).getName()).isEqualTo("Physics");
        verify(subjectRepository).findAll();
    }

    @Test
    void saveSubject_ShouldReturnSavedSubject() {
        Subject subject = new Subject("Chemistry", "301", 30, null);
        when(subjectRepository.save(any(Subject.class))).thenReturn(subject);

        Subject savedSubject = subjectService.saveSubject(subject);

        assertThat(savedSubject.getName()).isEqualTo("Chemistry");
        verify(subjectRepository).save(subject);
    }

    @Test
    void findSubjectById_WhenSubjectExists_ShouldReturnSubject() {
        Long subjectId = 1L;
        Subject subject = new Subject("Biology", "401", 45, null);
        when(subjectRepository.findById(subjectId)).thenReturn(Optional.of(subject));

        Optional<Subject> foundSubject = subjectService.findSubjectById(subjectId);

        assertThat(foundSubject.isPresent()).isTrue();
        assertThat(foundSubject.get().getName()).isEqualTo("Biology");
        verify(subjectRepository).findById(subjectId);
    }

    @Test
    void updateSubject_WhenNotFound_ShouldThrowException() {
        Long subjectId = 1L;
        Subject subjectDetails = new Subject("Updated Name", "Updated Level", 60, null);
        when(subjectRepository.findById(subjectId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> subjectService.updateSubject(subjectId, subjectDetails))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Subject not found for this id :: " + subjectId);

        verify(subjectRepository, never()).save(any(Subject.class));
    }

    @Test
    void deleteSubject_WhenSubjectExists_ShouldDeleteSubject() {
        Long subjectId = 2L;
        Subject subject = new Subject("Physics", "202", 45, null);
        when(subjectRepository.findById(subjectId)).thenReturn(Optional.of(subject));
        doNothing().when(subjectRepository).delete(subject);

        subjectService.deleteSubject(subjectId);

        verify(subjectRepository).delete(subject);
    }

    @Test
    void addClassroomToSubject_ShouldAddClassroomToSubject() {
        Long subjectId = 1L;
        Long classroomId = 1L;
        Subject subject = new Subject("Math", "101", 60, new ArrayList<>()); // Pusta lista, ale modyfikowalna
        Classroom classroom = new Classroom("101A");

        when(subjectRepository.findById(subjectId)).thenReturn(Optional.of(subject));
        when(classroomRepository.findById(classroomId)).thenReturn(Optional.of(classroom));
        when(subjectRepository.save(any(Subject.class))).thenReturn(subject); // Zwróć podany obiekt

        Subject updatedSubject = subjectService.addClassroomToSubject(subjectId, classroomId);

        assertThat(updatedSubject.getClassrooms()).contains(classroom);
        verify(subjectRepository).save(subject);
        verify(classroomRepository).findById(classroomId);
    }


    @Test
    void removeClassroomFromSubject_ShouldRemoveClassroomFromSubject() {
        Long subjectId = 1L;
        Long classroomId = 1L;
        Classroom classroom = new Classroom("101A");
        classroom.setId(classroomId); // Ustawienie ID dla celów testowych

        Subject subject = new Subject("Math", "101", 60, new ArrayList<>());
        subject.getClassrooms().add(classroom); // Dodanie sali do przedmiotu

        when(subjectRepository.findById(subjectId)).thenReturn(Optional.of(subject));
        when(classroomRepository.findById(classroomId)).thenReturn(Optional.of(classroom));
        when(subjectRepository.save(any(Subject.class))).thenReturn(subject);

        Subject updatedSubject = subjectService.removeClassroomFromSubject(subjectId, classroomId);

        assertThat(updatedSubject.getClassrooms()).doesNotContain(classroom);
        verify(subjectRepository).save(subject);
    }


}
