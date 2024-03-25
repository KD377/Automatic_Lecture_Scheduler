package com.example.lecturescheduler.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Subject> subjectsTaught;

    @ElementCollection
    private List<Boolean> availability; // Lista przedziałów czasowych dostępności

    @ElementCollection
    private List<Boolean> preferences; // Lista przedziałów czasowych preferencji

    @ManyToMany(fetch = FetchType.LAZY)
    private List<SingleGroup> groups; // Lista grup, w których nauczyciel może uczyć

    @Builder
    public Instructor(String name, String department, List<Subject> subjectsTaught, List<Boolean> availability, List<Boolean> preferences, List<SingleGroup> groups) {
        this.name = name;
        this.department = department;
        this.subjectsTaught = subjectsTaught;
        this.availability = availability;
        this.preferences = preferences;
        this.groups = groups;
    }
}
