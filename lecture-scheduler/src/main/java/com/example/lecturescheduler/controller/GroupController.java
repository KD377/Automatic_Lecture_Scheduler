package com.example.lecturescheduler.controller;

import com.example.lecturescheduler.model.SingleGroup;
import com.example.lecturescheduler.service.SingleGroupService;
import com.example.lecturescheduler.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final SingleGroupService groupService;

    @Autowired
    public GroupController(SingleGroupService groupService) {
        this.groupService = groupService;
    }

    // GET All Groups
    @GetMapping
    public List<SingleGroup> getAllGroups() {
        return groupService.findAllGroups();
    }

    // GET Group by ID
    @GetMapping("/{id}")
    public ResponseEntity<SingleGroup> getGroupById(@PathVariable Long id) {
        SingleGroup group = groupService.findGroupById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found for this id :: " + id));
        return ResponseEntity.ok().body(group);
    }

    // POST Create Group
    @PostMapping
    public SingleGroup createGroup(@RequestBody SingleGroup group) {
        return groupService.saveGroup(group);
    }

    // PUT Update Group
    @PutMapping("/{id}")
    public ResponseEntity<SingleGroup> updateGroup(@PathVariable Long id, @RequestBody SingleGroup groupDetails) {
        SingleGroup updatedGroup = groupService.updateGroup(id, groupDetails);
        return ResponseEntity.ok(updatedGroup);
    }

    // DELETE Group
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);
        return ResponseEntity.ok().build();
    }
}
