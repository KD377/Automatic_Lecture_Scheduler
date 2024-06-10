package com.example.lecturescheduler.service;

import com.example.lecturescheduler.controller.GoogleCalendarController;
import com.example.lecturescheduler.dto.LectureSessionResponse;
import com.example.lecturescheduler.model.LectureSession;
import com.example.lecturescheduler.repository.LectureSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GoogleCalendarService {

    @Autowired
    private LectureSessionRepository lectureSessionRepository;

    @Autowired
    private LectureSessionService lectureSessionService;
    public Map<String, List<LectureSessionResponse>> findByEmail(String email){
        List<LectureSession> lectureSessions = lectureSessionService.findByEmail(email);
        Map<String, List<LectureSessionResponse>> groupedResponse = lectureSessions.stream()
                .map(session -> new LectureSessionResponse(
                        session.getSubject().getName(),
                        session.getGroup().getName(),
                        session.getInstructor().getName(),
                        session.getClassroom().getName(),
                        session.getNumberOfTimeSlot(),
                        session.getDay()
                ))
                .collect(Collectors.groupingBy(LectureSessionResponse::getGroupName));

        return groupedResponse;
    }


}
