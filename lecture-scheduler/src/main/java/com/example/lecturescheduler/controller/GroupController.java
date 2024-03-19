package com.example.lecturescheduler.controller;

import com.example.lecturescheduler.model.Group;
import com.example.lecturescheduler.service.GroupService;
import com.example.lecturescheduler.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    // GET All Groups
    @GetMapping
    public List<Group> getAllGroups() {
        return groupService.findAllGroups();
    }

    // GET Group by ID
    @GetMapping("/{id}")
    public ResponseEntity<Group> getGroupById(@PathVariable Long id) {
        Group group = groupService.findGroupById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found for this id :: " + id));
        return ResponseEntity.ok().body(group);
    }

    // POST Create Group
    @PostMapping
    public Group createGroup(@RequestBody Group group) {
        return groupService.saveGroup(group);
    }

    // PUT Update Group
    @PutMapping("/{id}")
    public ResponseEntity<Group> updateGroup(@PathVariable Long id, @RequestBody Group groupDetails) {
        Group updatedGroup = groupService.updateGroup(id, groupDetails);
        return ResponseEntity.ok(updatedGroup);
    }

    // DELETE Group
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);
        return ResponseEntity.ok().build();
    }
}
