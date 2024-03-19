package com.example.lecturescheduler.service;

import com.example.lecturescheduler.exception.ResourceNotFoundException;
import com.example.lecturescheduler.model.Subject;
import com.example.lecturescheduler.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;

    @Autowired
    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
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
        return subjectRepository.save(subject);
    }

    // DELETE
    public void deleteSubject(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found for this id :: " + id));
        subjectRepository.delete(subject);
    }
}
