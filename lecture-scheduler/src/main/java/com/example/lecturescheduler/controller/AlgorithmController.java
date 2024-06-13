package com.example.lecturescheduler.controller;

import com.example.lecturescheduler.dto.LectureSessionResponse;
import com.example.lecturescheduler.model.LectureSession;
import com.example.lecturescheduler.service.LectureSessionService;
import com.example.lecturescheduler.service.algorithm.Chromosome;
import com.example.lecturescheduler.service.algorithm.GeneticAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/algorithm")
public class AlgorithmController {

    private final GeneticAlgorithm geneticAlgorithm;

    @Autowired
    private LectureSessionService lectureSessionService;


    @Autowired
    public AlgorithmController(GeneticAlgorithm geneticAlgorithm, LectureSessionService lectureSessionService){
        this.geneticAlgorithm = geneticAlgorithm;
        this.lectureSessionService = lectureSessionService;
    }

    @GetMapping("/trigger-genetic-algorithm")
    public ResponseEntity<Map<String, List<LectureSessionResponse>>> triggerGeneticAlgorithm() {
        Chromosome best = geneticAlgorithm.run();
        List<LectureSession> timetable = best.getLectureSessions();

        Map<String, List<LectureSessionResponse>> groupedResponse = timetable.stream()
                .map(session -> new LectureSessionResponse(
                        session.getSubject().getName(),
                        session.getGroup().getName(),
                        session.getInstructor().getName(),
                        session.getClassroom().getName(),
                        session.getNumberOfTimeSlot(),
                        session.getDay()
                ))
                .collect(Collectors.groupingBy(LectureSessionResponse::getGroupName));

        lectureSessionService.deleteAllLectureSessions();

        timetable.forEach(lectureSession -> lectureSessionService.saveLectureSession(lectureSession));


        return ResponseEntity.ok(groupedResponse);
    }
}
