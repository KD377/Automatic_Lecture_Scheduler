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
@Builder
public class Instructor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String department;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Subject> subjectsTaught;

    @ElementCollection
    private List<Boolean> preferences;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<SingleGroup> groups;

    public Instructor(String name, String email, String department, List<Subject> subjectsTaught, List<Boolean> preferences, List<SingleGroup> groups) {
        this.name = name;
        this.email = email;
        this.department = department;
        this.subjectsTaught = subjectsTaught;
        this.preferences = preferences;
        this.groups = groups;
    }
}
