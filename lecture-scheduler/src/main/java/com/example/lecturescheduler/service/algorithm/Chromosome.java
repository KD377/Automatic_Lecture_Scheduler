package com.example.lecturescheduler.service.algorithm;


import com.example.lecturescheduler.model.LectureSession;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class Chromosome {

    private List<LectureSession> lectureSessions;

    private double fitnessScore;

    public Chromosome(List<LectureSession> lectureSessions) {
        this.lectureSessions = lectureSessions;
        this.fitnessScore = 0;
    }

}
