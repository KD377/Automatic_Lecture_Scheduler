package com.example.lecturescheduler;

import com.example.lecturescheduler.exception.ResourceNotFoundException;
import com.example.lecturescheduler.model.Group;
import com.example.lecturescheduler.repository.GroupRepository;
import com.example.lecturescheduler.service.GroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GroupServiceTest {

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private GroupService groupService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllGroups_ShouldReturnAllGroups() {
        // given
        when(groupRepository.findAll()).thenReturn(List.of(Group.builder()
                .name("Group 1")
                .programOfStudy("Program 1")
                .numberOfStudents(30)
                .build()));

        // when
        List<Group> result = groupService.findAllGroups();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getName()).isEqualTo("Group 1");
        verify(groupRepository).findAll();
    }

    @Test
    void saveGroup_ShouldReturnSavedGroup() {
        // given
        Group group = Group.builder()
                .name("Group 1")
                .programOfStudy("Program 1")
                .numberOfStudents(30)
                .build();
        when(groupRepository.save(any(Group.class))).thenReturn(group);

        // when
        Group savedGroup = groupService.saveGroup(group);

        // then
        assertThat(savedGroup).isNotNull();
        verify(groupRepository).save(group);
    }

    @Test
    void updateGroup_WhenNotFound_ShouldThrowException() {
        // given
        Long groupId = 1L;
        Group groupDetails = Group.builder()
                .name("Updated Name")
                .programOfStudy("Updated Program")
                .numberOfStudents(25)
                .build();
        when(groupRepository.findById(groupId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> groupService.updateGroup(groupId, groupDetails))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Group not found for this id :: " + groupId);

        verify(groupRepository, never()).save(any(Group.class));
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

}
