package com.example.lecturescheduler.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class SingleGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String programOfStudy;
    private int numberOfStudents;

    @Builder
    public SingleGroup(String name, String programOfStudy, int numberOfStudents) {
        this.name = name;
        this.programOfStudy = programOfStudy;
        this.numberOfStudents = numberOfStudents;
    }
}
