package com.example.lecturescheduler.controller;

import com.example.lecturescheduler.model.LectureSession;
import com.example.lecturescheduler.model.SingleGroup;
import com.example.lecturescheduler.service.SingleGroupService;
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

    @Autowired
    public AlgorithmController(PopulationGenerator populationGenerator,SingleGroupService singleGroupService){
        this.populationGenerator = populationGenerator;
        this.singleGroupService = singleGroupService;
    }

    @GetMapping("/trigger-genetic-algorithm")
    public ResponseEntity<String> triggerGeneticAlgorithm() {
        List<LectureSession> timetable = this.populationGenerator.generateChromosome(10);
        List<SingleGroup> singleGroups = singleGroupService.findAllGroups();
        for (SingleGroup group : singleGroups){
            System.out.println(group.getName() + ":");
            for(LectureSession session : timetable) {
                if (group.equals(session.getGroup())){
                    System.out.println(session.toString());
                }
            }

        }
        return ResponseEntity.ok("Genetic algorithm completed successfully.");
    }

}
