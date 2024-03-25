package com.example.lecturescheduler.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Entity
public class LectureSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private SingleGroup group;

    @ManyToOne(fetch = FetchType.LAZY)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    private Instructor instructor;

    @ManyToOne(fetch = FetchType.LAZY)
    private Classroom classroom;

    @Enumerated(EnumType.STRING)
    private DayOfWeek day;

    @Embedded
    private TimeSlot timeSlot;
}
