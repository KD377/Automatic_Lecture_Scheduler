package com.example.lecturescheduler.service;

import com.example.lecturescheduler.exception.ResourceNotFoundException;
import com.example.lecturescheduler.model.Instructor;
import com.example.lecturescheduler.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InstructorService {

    private final InstructorRepository instructorRepository;

    @Autowired
    public InstructorService(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    // CREATE
    public Instructor saveInstructor(Instructor instructor) {
        return instructorRepository.save(instructor);
    }

    // READ - Find all instructors
    public List<Instructor> findAllInstructors() {
        return instructorRepository.findAll();
    }

    // READ - Find instructor by ID
    public Optional<Instructor> findInstructorById(Long id) {
        return instructorRepository.findById(id);
    }

    // UPDATE
    public Instructor updateInstructor(Long id, Instructor instructorDetails) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found for this id :: " + id));
        instructor.setName(instructorDetails.getName());
        instructor.setDepartment(instructorDetails.getDepartment());
        instructor.setSubjectsTaught(instructorDetails.getSubjectsTaught());
        instructor.setAvailability(instructorDetails.getAvailability());
        instructor.setPreferences(instructorDetails.getPreferences());
        return instructorRepository.save(instructor);
    }

    // DELETE
    public void deleteInstructor(Long id) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found for this id :: " + id));
        instructorRepository.delete(instructor);
    }
}
