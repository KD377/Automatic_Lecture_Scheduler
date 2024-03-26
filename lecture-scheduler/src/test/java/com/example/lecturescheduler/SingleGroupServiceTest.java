package com.example.lecturescheduler;

import com.example.lecturescheduler.exception.ResourceNotFoundException;
import com.example.lecturescheduler.model.Instructor;
import com.example.lecturescheduler.model.SingleGroup;
import com.example.lecturescheduler.repository.GroupRepository;
import com.example.lecturescheduler.repository.InstructorRepository;
import com.example.lecturescheduler.service.SingleGroupService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SingleGroupServiceTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private InstructorRepository instructorRepository;

    @InjectMocks
    private SingleGroupService groupService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllGroups_ShouldReturnAllGroups() {
        // given
        when(groupRepository.findAll()).thenReturn(List.of(SingleGroup.builder()
                .name("Group 1")
                .programOfStudy("Program 1")
                .numberOfStudents(30)
                .build()));

        // when
        List<SingleGroup> result = groupService.findAllGroups();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getName()).isEqualTo("Group 1");
        verify(groupRepository).findAll();
    }

    @Test
    void saveGroup_ShouldReturnSavedGroup() {
        // given
        SingleGroup group = SingleGroup.builder()
                .name("Group 1")
                .programOfStudy("Program 1")
                .numberOfStudents(30)
                .build();
        when(groupRepository.save(any(SingleGroup.class))).thenReturn(group);

        // when
        SingleGroup savedGroup = groupService.saveGroup(group);

        // then
        assertThat(savedGroup).isNotNull();
        verify(groupRepository).save(group);
    }

    @Test
    void updateGroup_WhenNotFound_ShouldThrowException() {
        // given
        Long groupId = 1L;
        SingleGroup groupDetails = SingleGroup.builder()
                .name("Updated Name")
                .programOfStudy("Updated Program")
                .numberOfStudents(25)
                .build();
        when(groupRepository.findById(groupId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> groupService.updateGroup(groupId, groupDetails))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Group not found for this id :: " + groupId);

        verify(groupRepository, never()).save(any(SingleGroup.class));
    }

    @Test
    void deleteGroup_ShouldDeleteGroup() {
        // given
        Long groupId = 1L;
        doNothing().when(groupRepository).deleteById(groupId);

        // when
        groupService.deleteGroup(groupId);

        // then
        verify(groupRepository).deleteById(groupId);
    }

    @Test
    void addInstructorToGroup_ShouldCorrectlyAddInstructor() {
        // given
        Long groupId = 1L;
        Long instructorId = 1L;
        SingleGroup group = new SingleGroup();
        group.setInstructors(new ArrayList<>()); // Inicjalizacja pustej listy instruktorów
        Instructor instructor = new Instructor();
        instructor.setId(instructorId);
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
        when(groupRepository.save(any(SingleGroup.class))).thenReturn(group); // Zwróć zmockowaną grupę po zapisie

        // when
        SingleGroup updatedGroup = groupService.addInstructorToGroup(groupId, instructorId);

        // then
        assertThat(updatedGroup.getInstructors()).contains(instructor);
        verify(groupRepository).save(group);
        verify(instructorRepository).findById(instructorId);
    }


    @Test
    void removeInstructorFromGroup_ShouldCorrectlyRemoveInstructor() {
        // given
        Long groupId = 1L;
        Long instructorId = 1L;
        Instructor instructor = new Instructor();
        instructor.setId(instructorId);
        SingleGroup group = new SingleGroup();
        group.setInstructors(new ArrayList<>(List.of(instructor))); // Inicjalizacja listy z instruktorem
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(groupRepository.save(any(SingleGroup.class))).thenReturn(group); // Zwróć zmockowaną grupę po zapisie

        // when
        SingleGroup updatedGroup = groupService.removeInstructorFromGroup(groupId, instructorId);

        // then
        assertThat(updatedGroup.getInstructors()).doesNotContain(instructor);
        verify(groupRepository).save(group);
    }

}
