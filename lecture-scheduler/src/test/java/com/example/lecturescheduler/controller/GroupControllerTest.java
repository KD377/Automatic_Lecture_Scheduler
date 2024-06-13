package com.example.lecturescheduler.controller;

import com.example.lecturescheduler.exception.ResourceNotFoundException;
import com.example.lecturescheduler.model.SingleGroup;
import com.example.lecturescheduler.service.SingleGroupService;
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

public class GroupControllerTest {

    @InjectMocks
    private GroupController groupController;

    @Mock
    private SingleGroupService groupService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllGroups() {
        SingleGroup group1 = new SingleGroup();
        group1.setId(1L);
        group1.setName("Group 1");

        SingleGroup group2 = new SingleGroup();
        group2.setId(2L);
        group2.setName("Group 2");

        List<SingleGroup> groups = Arrays.asList(group1, group2);
        when(groupService.findAllGroups()).thenReturn(groups);

        List<SingleGroup> result = groupController.getAllGroups();
        assertEquals(2, result.size());
        verify(groupService, times(1)).findAllGroups();
    }

    @Test
    public void testGetGroupById() {
        SingleGroup group = new SingleGroup();
        group.setId(1L);
        group.setName("Group 1");

        when(groupService.findGroupById(1L)).thenReturn(Optional.of(group));

        ResponseEntity<SingleGroup> response = groupController.getGroupById(1L);
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(groupService, times(1)).findGroupById(1L);
    }

    @Test
    public void testGetGroupByIdNotFound() {
        when(groupService.findGroupById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            groupController.getGroupById(1L);
        });

        verify(groupService, times(1)).findGroupById(1L);
    }

    @Test
    public void testCreateGroup() {
        SingleGroup group = new SingleGroup();
        group.setName("New Group");

        when(groupService.saveGroup(any(SingleGroup.class))).thenReturn(group);

        SingleGroup result = groupController.createGroup(group);
        assertNotNull(result);
        assertEquals("New Group", result.getName());
        verify(groupService, times(1)).saveGroup(any(SingleGroup.class));
    }

    @Test
    public void testUpdateGroup() {
        SingleGroup group = new SingleGroup();
        group.setId(1L);
        group.setName("Updated Group");

        when(groupService.updateGroup(anyLong(), any(SingleGroup.class))).thenReturn(group);

        ResponseEntity<SingleGroup> response = groupController.updateGroup(1L, group);
        assertNotNull(response.getBody());
        assertEquals("Updated Group", response.getBody().getName());
        verify(groupService, times(1)).updateGroup(anyLong(), any(SingleGroup.class));
    }

    @Test
    public void testDeleteGroup() {
        doNothing().when(groupService).deleteGroup(1L);

        ResponseEntity<?> response = groupController.deleteGroup(1L);
        assertEquals(200, response.getStatusCode().value());
        verify(groupService, times(1)).deleteGroup(1L);
    }
}
