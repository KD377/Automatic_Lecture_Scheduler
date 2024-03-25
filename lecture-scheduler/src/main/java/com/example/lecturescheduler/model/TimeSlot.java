package com.example.lecturescheduler.model;

import java.io.Serializable;
import java.time.LocalTime;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Embeddable
public class TimeSlot implements Serializable {
    private LocalTime startTime;
    private LocalTime endTime;

}
