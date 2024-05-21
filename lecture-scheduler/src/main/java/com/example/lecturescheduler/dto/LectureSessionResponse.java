package com.example.lecturescheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LectureSessionResponse {
    String groupName;
    String lecturer;
    String classroom;
    int numberOfTimeSlot;
    DayOfWeek dayOfWeek;
}
