package com.example.lecturescheduler.service;

import com.example.lecturescheduler.model.LectureSession;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class GoogleCalendarService {

    @Autowired
    private LectureSessionService lectureSessionService;

    @Autowired
    public GoogleCalendarService(LectureSessionService lectureSessionService){
        this.lectureSessionService = lectureSessionService;
    }

    public List<LectureSession> findByEmail(String email){
        return lectureSessionService.findByEmail(email);
    }

    public String getUserEmail(OAuth2User oauth2User){
        return oauth2User.getAttribute("email");
    }

    private LocalDate getNextOccurrence(DayOfWeek dayOfWeek){
        LocalDate today = LocalDate.now();
        LocalDate nextOccurrence = today.with(dayOfWeek);
        if(nextOccurrence.isBefore(today) || nextOccurrence.isEqual(today)){
            nextOccurrence = nextOccurrence.plusWeeks(1);

        }
        return nextOccurrence;
    }

    private Calendar getCalendarService(OAuth2AccessToken accessToken) throws GeneralSecurityException, IOException {
        return new Calendar.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), null)
                .setApplicationName("Lecture Scheduler")
                .setHttpRequestInitializer(request -> request.getHeaders().setAuthorization("Bearer " + accessToken.getTokenValue()))
                .build();
    }

    private Event createCyclicalEvent(LectureSession session){
        LocalDate nextOccurrence = getNextOccurrence(session.getDay());
        LocalDateTime startDateTime = LocalDateTime.of(nextOccurrence, session.getTimeSlot().getStartTime().plusHours(1));
        LocalDateTime endDateTime = LocalDateTime.of(nextOccurrence, session.getTimeSlot().getEndTime().plusHours(1));

        ZonedDateTime startZonedDateTime = startDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime endZonedDateTime = endDateTime.atZone(ZoneId.systemDefault());

        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

        Event event = new Event();
        event.setSummary(session.getSubject().getName());
        event.setLocation(session.getClassroom().getName());
        event.setDescription(session.toString());


        EventDateTime start = new EventDateTime()
                .setDateTime(new DateTime(startZonedDateTime.format(formatter)))
                .setTimeZone(ZoneId.systemDefault().toString());
        event.setStart(start);

        EventDateTime end = new EventDateTime()
                .setDateTime(new DateTime(endZonedDateTime.format(formatter)))
                .setTimeZone(ZoneId.systemDefault().toString());
        event.setEnd(end);

        event.setRecurrence(List.of("RRULE:FREQ=WEEKLY;UNTIL=" + nextOccurrence.plusWeeks(10).toString().replace("-", "") + "T000000Z"));

        return event;
    }


    public void addEventsToCalendar(String email, OAuth2AccessToken accessToken) throws GeneralSecurityException, IOException {
        List<LectureSession> sessions = findByEmail(email);
        Calendar service = getCalendarService(accessToken);

        for (LectureSession session : sessions) {
            Event event = createCyclicalEvent(session);
            service.events().insert("primary", event).execute();
        }
    }




}
