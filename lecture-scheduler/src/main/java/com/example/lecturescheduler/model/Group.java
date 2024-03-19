package com.example.lecturescheduler.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String programOfStudy;
    private int numberOfStudents;

    @Builder
    public Group(String name, String programOfStudy, int numberOfStudents) {
        this.name = name;
        this.programOfStudy = programOfStudy;
        this.numberOfStudents = numberOfStudents;
    }
}
