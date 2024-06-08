package com.example.lecturescheduler.service;

import com.example.lecturescheduler.controller.GoogleCalendarController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoogleCalendarService {

    @Autowired
    private GoogleCalendarController googleCalendarController;

    GoogleCalendarService (GoogleCalendarController googleCalendarController){
        this.googleCalendarController = googleCalendarController;
    }
}
