package com.example.lecturescheduler.service;

import com.example.lecturescheduler.exception.ResourceNotFoundException;
import com.example.lecturescheduler.model.Instructor;
import com.example.lecturescheduler.model.SingleGroup;
import com.example.lecturescheduler.repository.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SingleGroupServiceTest {

    @Mock
    private GroupRepository groupRepository;


    @InjectMocks
    private SingleGroupService singleGroupService;

    private SingleGroup group;
    private Instructor instructor;

    @BeforeEach
    public void setup() {
        group = new SingleGroup();
        group.setId(1L);
        group.setName("Group 1");
        group.setProgramOfStudy("Computer Science");
        group.setNumberOfStudents(30);
        group.setInstructors(new ArrayList<>());

        instructor = new Instructor();
        instructor.setId(1L);
        instructor.setName("John Doe");
    }

    @Test
    public void testFindAllGroups() {
        when(groupRepository.findAll()).thenReturn(Arrays.asList(group));

        List<SingleGroup> groups = singleGroupService.findAllGroups();

        assertNotNull(groups);
        assertEquals(1, groups.size());
        verify(groupRepository, times(1)).findAll();
    }

    @Test
    public void testFindGroupById() {
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));

        Optional<SingleGroup> foundGroup = singleGroupService.findGroupById(1L);

        assertTrue(foundGroup.isPresent());
        assertEquals("Group 1", foundGroup.get().getName());
        verify(groupRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindGroupByIdNotFound() {
        when(groupRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<SingleGroup> foundGroup = singleGroupService.findGroupById(1L);

        assertFalse(foundGroup.isPresent());
        verify(groupRepository, times(1)).findById(1L);
    }

    @Test
    public void testSaveGroup() {
        when(groupRepository.save(group)).thenReturn(group);

        SingleGroup savedGroup = singleGroupService.saveGroup(group);

        assertNotNull(savedGroup);
        assertEquals("Group 1", savedGroup.getName());
        verify(groupRepository, times(1)).save(group);
    }

    @Test
    public void testUpdateGroup() {
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(groupRepository.save(group)).thenReturn(group);

        group.setName("Updated Group 1");
        SingleGroup updatedGroup = singleGroupService.updateGroup(1L, group);

        assertNotNull(updatedGroup);
        assertEquals("Updated Group 1", updatedGroup.getName());
        verify(groupRepository, times(1)).findById(1L);
        verify(groupRepository, times(1)).save(group);
    }

    @Test
    public void testUpdateGroupNotFound() {
        when(groupRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            singleGroupService.updateGroup(1L, group);
        });
        verify(groupRepository, times(1)).findById(1L);
    }

    @Test
    public void testDeleteGroup() {
        doNothing().when(groupRepository).deleteById(1L);

        singleGroupService.deleteGroup(1L);

        verify(groupRepository, times(1)).deleteById(1L);
    }


}