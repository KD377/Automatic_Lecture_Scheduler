package com.example.lecturescheduler;

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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class InstructorServiceTest {

    @Mock
    private InstructorRepository instructorRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private InstructorService instructorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllInstructors_ShouldReturnAllInstructors() {
        when(instructorRepository.findAll()).thenReturn(Arrays.asList(
                new Instructor("John Doe", "Mathematics", null, Arrays.asList(true, false, true), Arrays.asList(false, true, true), null),
                new Instructor("Jane Smith", "Physics", null, Arrays.asList(false, true, false), Arrays.asList(true, false, true), null)
        ));

        List<Instructor> result = instructorService.findAllInstructors();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("John Doe");
        assertThat(result.get(1).getName()).isEqualTo("Jane Smith");
        verify(instructorRepository).findAll();
    }

    @Test
    void findAllInstructorsBySubject_ShouldReturnInstructors() {
        // Mock data
        Long subjectId = 1L;

        // Create a subject
        Subject subject = new Subject();
        subject.setId(subjectId);


        Instructor instructor1 = new Instructor();
        instructor1.setId(1L);
        instructor1.setName("John Doe");
        instructor1.setSubjectsTaught(Arrays.asList(subject));

        Instructor instructor2 = new Instructor();
        instructor2.setId(2L);
        instructor2.setName("Jane Smith");
        instructor2.setSubjectsTaught(Arrays.asList(subject));

        // Mock behavior
        when(subjectRepository.findById(subjectId)).thenReturn(Optional.of(subject));
        when(instructorRepository.findBySubject(subject)).thenReturn(Arrays.asList(instructor1, instructor2));

        // Call the method
        List<Instructor> result = instructorService.findBySubject(subject);

        // Verify the result
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("John Doe");
        assertThat(result.get(1).getName()).isEqualTo("Jane Smith");
    }

    @Test
    void saveInstructor_ShouldReturnSavedInstructor() {
        Instructor instructor = new Instructor("Mark Spencer", "Chemistry", null, Arrays.asList(true), Arrays.asList(false), null);
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        Instructor savedInstructor = instructorService.saveInstructor(instructor);

        assertThat(savedInstructor.getName()).isEqualTo("Mark Spencer");
        verify(instructorRepository).save(instructor);
    }


    @Test
    void updateInstructor_WhenNotFound_ShouldThrowException() {
        Long instructorId = 1L;
        Instructor instructorDetails = new Instructor("Updated Name", "Updated Department", null, Arrays.asList(false, true), Arrays.asList(true, false), null);
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> instructorService.updateInstructor(instructorId, instructorDetails))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Instructor not found for this id :: " + instructorId);

        verify(instructorRepository, never()).save(any(Instructor.class));
    }

    @Test
    void deleteInstructor_ShouldDeleteInstructor() {
        Long instructorId = 2L;
        Instructor instructor = new Instructor("Emily Watson", "Biology", null, Arrays.asList(true, false), Arrays.asList(false, true), null);
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
        doNothing().when(instructorRepository).delete(instructor);

        instructorService.deleteInstructor(instructorId);

        verify(instructorRepository).delete(instructor);
    }

    @Test
    void addSubjectTaught_ShouldAddSubjectToInstructor() {
        Long instructorId = 1L;
        Long subjectId = 1L;
        Instructor instructor = new Instructor();
        instructor.setId(instructorId);
        instructor.setSubjectsTaught(new ArrayList<>());
        Subject subject = new Subject();
        subject.setId(subjectId);

        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
        when(subjectRepository.findById(subjectId)).thenReturn(Optional.of(subject));
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        Instructor updatedInstructor = instructorService.addSubjectTaught(instructorId, subjectId);

        assertThat(updatedInstructor.getSubjectsTaught()).contains(subject);
        verify(instructorRepository).save(instructor);
    }

    @Test
    void removeSubjectTaught_ShouldRemoveSubjectFromInstructor() {
        Long instructorId = 1L;
        Long subjectId = 1L;
        Instructor instructor = new Instructor();
        instructor.setId(instructorId);
        Subject subject = new Subject();
        subject.setId(subjectId);
        instructor.setSubjectsTaught(new ArrayList<>(List.of(subject)));

        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        Instructor updatedInstructor = instructorService.removeSubjectTaught(instructorId, subjectId);

        assertThat(updatedInstructor.getSubjectsTaught()).doesNotContain(subject);
        verify(instructorRepository).save(instructor);
    }

    @Test
    void addGroup_ShouldAddGroupToInstructor() {
        Long instructorId = 1L;
        Long groupId = 1L;
        Instructor instructor = new Instructor();
        instructor.setId(instructorId);
        instructor.setGroups(new ArrayList<>());
        SingleGroup group = new SingleGroup();
        group.setId(groupId);

        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        Instructor updatedInstructor = instructorService.addGroup(instructorId, groupId);

        assertThat(updatedInstructor.getGroups()).contains(group);
        verify(instructorRepository).save(instructor);
    }

    @Test
    void removeGroup_ShouldRemoveGroupFromInstructor() {
        Long instructorId = 1L;
        Long groupId = 1L;
        Instructor instructor = new Instructor();
        instructor.setId(instructorId);
        SingleGroup group = new SingleGroup();
        group.setId(groupId);
        instructor.setGroups(new ArrayList<>(List.of(group)));

        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        Instructor updatedInstructor = instructorService.removeGroup(instructorId, groupId);

        assertThat(updatedInstructor.getGroups()).doesNotContain(group);
        verify(instructorRepository).save(instructor);
    }

}
