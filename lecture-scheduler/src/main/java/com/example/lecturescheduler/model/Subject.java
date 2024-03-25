package com.example.lecturescheduler.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String courseLevel;
    private int courseLength;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Classroom> classrooms;

    public Subject(String name, String courseLevel, int courseLength) {
        this.name = name;
        this.courseLevel = courseLevel;
        this.courseLength = courseLength;
    }
}
