package com.example.lecturescheduler.controller;

import com.example.lecturescheduler.dto.LectureSessionResponse;
import com.example.lecturescheduler.model.LectureSession;
import com.example.lecturescheduler.model.SingleGroup;
import com.example.lecturescheduler.service.SingleGroupService;
import com.example.lecturescheduler.service.algorithm.Chromosome;
import com.example.lecturescheduler.service.algorithm.GeneticAlgorithm;
import com.example.lecturescheduler.service.algorithm.PopulationGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/algorithm")
public class AlgorithmController {


    private final SingleGroupService singleGroupService;

    private final GeneticAlgorithm geneticAlgorithm;

    @Autowired
    public AlgorithmController(SingleGroupService singleGroupService,GeneticAlgorithm geneticAlgorithm){
        this.singleGroupService = singleGroupService;
        this.geneticAlgorithm = geneticAlgorithm;
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

        return ResponseEntity.ok(groupedResponse);
    }

}
