package com.example.lecturescheduler.service;

import com.example.lecturescheduler.exception.ResourceNotFoundException;
import com.example.lecturescheduler.model.Classroom;
import com.example.lecturescheduler.model.Subject;
import com.example.lecturescheduler.repository.ClassroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassroomService {

    private final ClassroomRepository classroomRepository;

    @Autowired
    public ClassroomService(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    // CREATE
    public Classroom saveClassroom(Classroom classroom) {
        return classroomRepository.save(classroom);
    }

    // READ - Find all classrooms
    public List<Classroom> findAllClassrooms() {
        return classroomRepository.findAll();
    }

    public List<Classroom> findBySubject(Subject subject) {
        return classroomRepository.findBySubject(subject);
    }

    // READ - Find classroom by ID
    public Optional<Classroom> findClassroomById(Long id) {
        return classroomRepository.findById(id);
    }

    // UPDATE
    public Classroom updateClassroom(Long id, Classroom classroomDetails) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found for this id :: " + id));
        classroom.setName(classroomDetails.getName());
        return classroomRepository.save(classroom);
    }

    // DELETE
    public void deleteClassroom(Long id) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found for this id :: " + id));
        classroomRepository.delete(classroom);
    }
}
