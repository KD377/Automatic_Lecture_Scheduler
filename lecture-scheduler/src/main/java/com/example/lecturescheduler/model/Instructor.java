package com.example.lecturescheduler.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class Instructor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String department;
    @ElementCollection
    private List<String> subjectsTaught;
    private String availability;
    private String preferences;

    @Builder
    public Instructor(String name, String department, List<String> subjectsTaught, String availability, String preferences) {
        this.name = name;
        this.department = department;
        this.subjectsTaught = subjectsTaught;
        this.availability = availability;
        this.preferences = preferences;
    }
}
