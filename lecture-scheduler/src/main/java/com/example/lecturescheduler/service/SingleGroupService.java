package com.example.lecturescheduler.service;

import com.example.lecturescheduler.exception.ResourceNotFoundException;
import com.example.lecturescheduler.model.SingleGroup;
import com.example.lecturescheduler.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SingleGroupService {

    private final GroupRepository groupRepository;

    @Autowired
    public SingleGroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
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
                .orElseThrow(() -> new ResourceNotFoundException("Group not found for this id :: " + id));
        group.setName(groupDetails.getName());
        group.setProgramOfStudy(groupDetails.getProgramOfStudy());
        group.setNumberOfStudents(groupDetails.getNumberOfStudents());
        return groupRepository.save(group);
    }

    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }
}
