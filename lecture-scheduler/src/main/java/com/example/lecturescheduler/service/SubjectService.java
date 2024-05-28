package com.example.lecturescheduler.service;

import com.example.lecturescheduler.exception.ResourceNotFoundException;
import com.example.lecturescheduler.model.Classroom;
import com.example.lecturescheduler.model.Subject;
import com.example.lecturescheduler.repository.ClassroomRepository;
import com.example.lecturescheduler.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public Subject saveSubject(Subject subject) {
        return subjectRepository.save(subject);
    }

    public List<Subject> findAllSubjects() {
        return subjectRepository.findAll();
    }

    //returns a map of subject with calculated how many times per week it should take place
    //We assume that a semester has 15 weeks
    public Map<Subject,Integer> getSubjectsWithOcurrences() {

        List <Subject> subjects = subjectRepository.findAll();
        Map <Subject,Integer> subjectsWithOcc = new HashMap<>();

        for (Subject subject : subjects) {
            int numberOfHours = subject.getCourseLength();
            double hoursPerWeek = numberOfHours / 15.0;

            int occurences = 0;
            if (hoursPerWeek >= 3 && hoursPerWeek < 4.5) {
                occurences = 2;
            } else if (hoursPerWeek < 3) {
                occurences = 1;
            } else {
                occurences = (int) Math.round(hoursPerWeek / 1.5);
            }

            subjectsWithOcc.put(subject,occurences);
        }
        return subjectsWithOcc;
    }

    public Optional<Subject> findSubjectById(Long id) {
        return subjectRepository.findById(id);
    }

    public Subject updateSubject(Long id, Subject subjectDetails) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found for this id :: " + id));
        subject.setName(subjectDetails.getName());
        subject.setCourseLevel(subjectDetails.getCourseLevel());
        subject.setCourseLength(subjectDetails.getCourseLength());
        subject.setClassrooms(subjectDetails.getClassrooms());
        return subjectRepository.save(subject);
    }

    public Subject addClassroomToSubject(Long subjectId, Long classroomId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found for this id :: " + subjectId));
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found for this id :: " + classroomId));
        subject.getClassrooms().add(classroom);
        return subjectRepository.save(subject);
    }

    public Subject removeClassroomFromSubject(Long subjectId, Long classroomId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found for this id :: " + subjectId));
        subject.getClassrooms().removeIf(classroom -> classroom.getId().equals(classroomId));
        return subjectRepository.save(subject);
    }

    public void deleteSubject(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found for this id :: " + id));
        subjectRepository.delete(subject);
    }
}

