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

    @Override
    public String toString() {
        return String.format("Time Slot: %d - %s\nSubject: %s\nInstructor: %s\nClassroom: %s\n",
                this.numberOfTimeSlot,
                this.day.toString(),
                this.subject.getName(),
                this.instructor.getName(),
                this.classroom.getName());
    }

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

    private int numberOfTimeSlot;

    public LectureSession(LectureSession other) {
        this.id = other.id;
        this.subject = other.subject;
        this.timeSlot = other.timeSlot;
        this.group = other.group;
        this.day = other.day;
        this.numberOfTimeSlot = other.numberOfTimeSlot;
        this.instructor = other.instructor;
        this.classroom = other.classroom;
    }
}
