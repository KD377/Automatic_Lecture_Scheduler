package com.example.lecturescheduler.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    // Relacja wiele-do-wielu z Instructor
    @ManyToMany(mappedBy = "groups", fetch = FetchType.LAZY)
    private List<Instructor> instructors;

    @Builder
    public SingleGroup(String name, String programOfStudy, int numberOfStudents, List<Instructor> instructors) {
        this.name = name;
        this.programOfStudy = programOfStudy;
        this.numberOfStudents = numberOfStudents;
        this.instructors = (instructors == null ? new ArrayList<>() : instructors);
    }
}
