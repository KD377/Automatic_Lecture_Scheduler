package com.example.lecturescheduler.service;

import com.example.lecturescheduler.exception.ResourceNotFoundException;
import com.example.lecturescheduler.model.Classroom;
import com.example.lecturescheduler.model.Subject;
import com.example.lecturescheduler.repository.ClassroomRepository;
import com.example.lecturescheduler.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final ClassroomRepository classroomRepository;

    @Autowired
    public SubjectService(SubjectRepository subjectRepository, ClassroomRepository classroomRepository) {
        this.subjectRepository = subjectRepository;
        this.classroomRepository = classroomRepository;
    }

    // CREATE
    public Subject saveSubject(Subject subject) {
        return subjectRepository.save(subject);
    }

    // READ
    public List<Subject> findAllSubjects() {
        return subjectRepository.findAll();
    }

    public Optional<Subject> findSubjectById(Long id) {
        return subjectRepository.findById(id);
    }

    // UPDATE
    public Subject updateSubject(Long id, Subject subjectDetails) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found for this id :: " + id));
        subject.setName(subjectDetails.getName());
        subject.setCourseLevel(subjectDetails.getCourseLevel());
        subject.setCourseLength(subjectDetails.getCourseLength());
        // Możliwe aktualizacje dla listy classrooms pominięte dla uproszczenia
        return subjectRepository.save(subject);
    }

    // Method to add a classroom to a subject
    public Subject addClassroomToSubject(Long subjectId, Long classroomId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found for this id :: " + subjectId));
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found for this id :: " + classroomId));
        subject.getClassrooms().add(classroom);
        return subjectRepository.save(subject);
    }

    // Method to remove a classroom from a subject
    public Subject removeClassroomFromSubject(Long subjectId, Long classroomId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found for this id :: " + subjectId));
        subject.getClassrooms().removeIf(classroom -> classroom.getId().equals(classroomId));
        return subjectRepository.save(subject);
    }

    // DELETE
    public void deleteSubject(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found for this id :: " + id));
        subjectRepository.delete(subject);
    }
}

