package com.example.lecturescheduler.service;

import com.example.lecturescheduler.exception.ResourceNotFoundException;
import com.example.lecturescheduler.model.Instructor;
import com.example.lecturescheduler.model.SingleGroup;
import com.example.lecturescheduler.repository.GroupRepository;
import com.example.lecturescheduler.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SingleGroupService {

    private final GroupRepository groupRepository;
    private final InstructorRepository instructorRepository;
    private static final String GROUP_NOT_FOUND = "Group not found for this id :: ";

    @Autowired
    public SingleGroupService(GroupRepository groupRepository, InstructorRepository instructorRepository) {
        this.groupRepository = groupRepository;
        this.instructorRepository = instructorRepository;
    }

    public List<SingleGroup> findAllGroups() {
        return groupRepository.findAll();
    }

    public Optional<SingleGroup> findGroupById(Long id) {
        return groupRepository.findById(id);
    }

    public SingleGroup saveGroup(SingleGroup group) {
        return groupRepository.save(group);
    }

    public SingleGroup updateGroup(Long id, SingleGroup groupDetails) {
        SingleGroup group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(GROUP_NOT_FOUND + id));
        group.setName(groupDetails.getName());
        group.setProgramOfStudy(groupDetails.getProgramOfStudy());
        group.setNumberOfStudents(groupDetails.getNumberOfStudents());
        return groupRepository.save(group);
    }

    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }

    public SingleGroup addInstructorToGroup(Long groupId, Long instructorId) {
        SingleGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException(GROUP_NOT_FOUND + groupId));
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found for this id :: " + instructorId));

        if (group.getInstructors() == null) {
            group.setInstructors(new ArrayList<>());
        }
        group.getInstructors().add(instructor);
        return groupRepository.save(group);
    }

    public SingleGroup removeInstructorFromGroup(Long groupId, Long instructorId) {
        SingleGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException(GROUP_NOT_FOUND + groupId));
        group.getInstructors().removeIf(instructor -> instructor.getId().equals(instructorId));
        return groupRepository.save(group);
    }

    public List<Instructor> getInstructorsForGroup(Long groupId) {
        SingleGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException(GROUP_NOT_FOUND + groupId));
        return group.getInstructors();
    }

}
