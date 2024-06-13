package com.example.lecturescheduler.model;

import jakarta.persistence.Embeddable;

import lombok.*;

import java.io.Serializable;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Embeddable
public class TimeSlot implements Serializable {
    private LocalTime startTime;
    private LocalTime endTime;
}
