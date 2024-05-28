package com.example.lecturescheduler.model;

import jakarta.persistence.*;
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
    private String department;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Subject> subjectsTaught;

    @ElementCollection
    private List<Boolean> preferences;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "instructor_groups",
            joinColumns = @JoinColumn(name = "instructor_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private List<SingleGroup> groups;

    public Instructor(String name, String department, List<Subject> subjectsTaught, List<Boolean> preferences, List<SingleGroup> groups) {
        this.name = name;
        this.department = department;
        this.subjectsTaught = subjectsTaught;
        this.preferences = preferences;
        this.groups = groups;
    }
}
