package com.example.lecturescheduler.service.algorithm;

import com.example.lecturescheduler.model.*;
import com.example.lecturescheduler.service.ClassroomService;
import com.example.lecturescheduler.service.InstructorService;
import com.example.lecturescheduler.service.SingleGroupService;
import com.example.lecturescheduler.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.time.DayOfWeek;

@Component
public class PopulationGenerator {
    private final SubjectService subjectService;

    private final InstructorService instructorService;

    private final SingleGroupService singleGroupService;

    private final ClassroomService classroomService;

    @Autowired
    public PopulationGenerator(SubjectService subjectService,InstructorService instructorService,SingleGroupService singleGroupService, ClassroomService classroomService) {
        this.subjectService = subjectService;
        this.instructorService = instructorService;
        this.singleGroupService = singleGroupService;
        this.classroomService = classroomService;
    }

    public List<LectureSession> generateChromosome() {
        List<LectureSession> timeTable = new ArrayList<>();
        Random random = new Random();

        Map<Subject, Integer> subjects = subjectService.getSubjectsWithOcurrences();
        List<SingleGroup> groups = singleGroupService.findAllGroups();

        Deque<Subject> subjectsToArrange = this.createStack(subjects);

        while (!subjectsToArrange.isEmpty()) {
            Subject subjectToArrange = subjectsToArrange.pollFirst();
            List<Instructor> possibleInstructors = instructorService.findBySubject(subjectToArrange);
            List<Classroom> possibleClassrooms = classroomService.findBySubject(subjectToArrange);
            Collections.shuffle(possibleClassrooms);
            Collections.shuffle(possibleInstructors);

            for (SingleGroup group : groups) {
                boolean added = false;
                while (!added) {
                    LectureSession lectureSession = new LectureSession();
                    lectureSession.setSubject(subjectToArrange);
                    lectureSession.setGroup(group);

                    DayOfWeek randomDay = drawRandomDay();
                    int randomTimeSlot = random.nextInt(2) + 1;
                    int randomClassIndex = random.nextInt(possibleClassrooms.size());
                    int randomInstructorIndex = random.nextInt(possibleInstructors.size());

                    lectureSession.setDay(randomDay);
                    lectureSession.setInstructor(possibleInstructors.get(randomInstructorIndex));
                    lectureSession.setClassroom(possibleClassrooms.get(randomClassIndex));
                    lectureSession.setNumberOfTimeSlot(randomTimeSlot);

                    if (checkConflicts(lectureSession, timeTable)) {
                        timeTable.add(lectureSession);
                        added = true;
                    }
                }
            }
        }
        return timeTable;
    }


    public boolean checkConflicts(LectureSession newSession, List<LectureSession> existingSessions) {
        if (existingSessions.isEmpty()) {
            return true;
        }
        for (LectureSession existingSession : existingSessions) {
            boolean sameTime = existingSession.getDay() == newSession.getDay() &&
                    existingSession.getNumberOfTimeSlot() == newSession.getNumberOfTimeSlot();
            if (sameTime) {
                if (existingSession.getInstructor().equals(newSession.getInstructor())) {
                    return false;
                }
                if (existingSession.getClassroom().equals(newSession.getClassroom())) {
                    return false;
                }
            }
        }
        return true;
    }



    private DayOfWeek drawRandomDay() {
        Random random = new Random();
        int randomNumber = random.nextInt(5) + 1;
        switch (randomNumber) {
            case 1:
                return DayOfWeek.MONDAY;
            case 2:
                return DayOfWeek.TUESDAY;
            case 3:
                return DayOfWeek.WEDNESDAY;
            case 4:
                return DayOfWeek.THURSDAY;
            case 5:
                return DayOfWeek.FRIDAY;
            default:
                throw new IllegalArgumentException("Invalid random number: " + randomNumber);
        }
    }

    private Deque<Subject> createStack(Map<Subject,Integer> subjects) {
        Deque<Subject> stack = new ArrayDeque<>();


        for (Map.Entry<Subject, Integer> entry : subjects.entrySet()) {
            Subject subject = entry.getKey();
            int occurrences = entry.getValue();


            for (int i = 0; i < occurrences; i++) {
                stack.push(subject);
            }
        }
        // Shuffle the subjects
        List<Subject> list = new ArrayList<>(stack);
        Collections.shuffle(list);

        stack.clear();
        stack.addAll(list);

        return stack;
    }
}
