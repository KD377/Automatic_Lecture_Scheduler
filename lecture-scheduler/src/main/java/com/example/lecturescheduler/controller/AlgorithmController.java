package com.example.lecturescheduler.controller;

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

import java.util.List;

@RestController
@RequestMapping("/api/algorithm")
public class AlgorithmController {

    private final PopulationGenerator populationGenerator;

    private final SingleGroupService singleGroupService;

    private final GeneticAlgorithm geneticAlgorithm;

    @Autowired
    public AlgorithmController(PopulationGenerator populationGenerator,SingleGroupService singleGroupService,GeneticAlgorithm geneticAlgorithm){
        this.populationGenerator = populationGenerator;
        this.singleGroupService = singleGroupService;
        this.geneticAlgorithm = geneticAlgorithm;
    }

    @GetMapping("/trigger-genetic-algorithm")
    public ResponseEntity<Double> triggerGeneticAlgorithm() {
        Chromosome best = geneticAlgorithm.run();
        List<LectureSession> timetable  = best.getLectureSessions();
        List<SingleGroup> singleGroups = singleGroupService.findAllGroups();
        for (SingleGroup group : singleGroups){
            System.out.println(group.getName() + ":");
            for(LectureSession session : timetable) {
                if (group.equals(session.getGroup())){
                    System.out.println(session);
                }
            }

        }
        return ResponseEntity.ok(best.getFitnessScore());
    }

}
