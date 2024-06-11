package com.example.lecturescheduler.service;

import com.example.lecturescheduler.exception.ResourceNotFoundException;
import com.example.lecturescheduler.model.LectureSession;
import com.example.lecturescheduler.model.TimeSlot;
import com.example.lecturescheduler.repository.LectureSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class LectureSessionService {

    private final LectureSessionRepository lectureSessionRepository;

    @Autowired
    public LectureSessionService(LectureSessionRepository lectureSessionRepository) {
        this.lectureSessionRepository = lectureSessionRepository;
    }

    public LectureSession saveLectureSession(LectureSession lectureSession) {
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setStartTime(LocalTime.of(8, 0).plusMinutes((lectureSession.getNumberOfTimeSlot() - 1) * 90L));
        timeSlot.setEndTime(timeSlot.getStartTime().plusMinutes(90));
        lectureSession.setTimeSlot(timeSlot);
        return lectureSessionRepository.save(lectureSession);
    }

    public List<LectureSession> findAllLectureSessions() {
        return lectureSessionRepository.findAll();
    }

    public Optional<LectureSession> findLectureSessionById(Long id) {
        return lectureSessionRepository.findById(id);
    }

    public LectureSession updateLectureSession(Long id, LectureSession lectureSessionDetails) {
        LectureSession lectureSession = lectureSessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LectureSession not found for this id :: " + id));

        lectureSession.setGroup(lectureSessionDetails.getGroup());
        lectureSession.setSubject(lectureSessionDetails.getSubject());
        lectureSession.setInstructor(lectureSessionDetails.getInstructor());
        lectureSession.setClassroom(lectureSessionDetails.getClassroom());
        lectureSession.setDay(lectureSessionDetails.getDay());
        lectureSession.setTimeSlot(lectureSessionDetails.getTimeSlot());

        return lectureSessionRepository.save(lectureSession);
    }

    public void deleteLectureSession(Long id) {
        LectureSession lectureSession = lectureSessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LectureSession not found for this id :: " + id));
        lectureSessionRepository.delete(lectureSession);
    }

    public void deleteAllLectureSessions() {
        lectureSessionRepository.deleteAll();
    }
    public List<LectureSession> findByEmail(String email){
        return lectureSessionRepository.findLectureSessionsByInstructorEmail(email);
    }
}
