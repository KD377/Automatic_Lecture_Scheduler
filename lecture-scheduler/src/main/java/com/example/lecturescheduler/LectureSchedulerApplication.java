package com.example.lecturescheduler;

import com.example.lecturescheduler.model.Classroom;
import com.example.lecturescheduler.model.Instructor;
import com.example.lecturescheduler.model.SingleGroup;
import com.example.lecturescheduler.model.Subject;
import com.example.lecturescheduler.service.ClassroomService;
import com.example.lecturescheduler.service.InstructorService;
import com.example.lecturescheduler.service.SingleGroupService;
import com.example.lecturescheduler.service.SubjectService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class LectureSchedulerApplication {

	private static final String INTERMEDIETE_LEVEL = "Intermediate";

	public static void main(String[] args) {
		SpringApplication.run(LectureSchedulerApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(SubjectService subjectService, InstructorService instructorService, SingleGroupService singleGroupService, ClassroomService classroomService) {
		return args -> {
			// Create classrooms
			Classroom classroom1 = new Classroom("101");
			Classroom classroom2 = new Classroom("102");
			Classroom classroom3 = new Classroom("103");
			Classroom classroom4 = new Classroom("104");

			// Create subjects
			Subject subject1 = new Subject("Algorithms", INTERMEDIETE_LEVEL,15,null);
			Subject subject2 = new Subject("Maths", INTERMEDIETE_LEVEL,15,null);
			Subject subject3 = new Subject("Object programming", INTERMEDIETE_LEVEL,15,null);
			Subject subject4 = new Subject("Ang", INTERMEDIETE_LEVEL,15,null);
			Subject subject5 = new Subject("WF", INTERMEDIETE_LEVEL,15,null);

			// Set relationships
			classroom1.setSubjects(List.of(subject1, subject2, subject3,subject4,subject5));
			classroom2.setSubjects(List.of(subject1, subject2, subject3,subject4,subject5));
			classroom3.setSubjects(List.of(subject1, subject2, subject3,subject4,subject5));
			classroom4.setSubjects(List.of(subject4));
			subjectService.saveSubject(subject1);
			subjectService.saveSubject(subject2);
			subjectService.saveSubject(subject3);
			subjectService.saveSubject(subject4);
			subjectService.saveSubject(subject5);

			// Save classrooms
			classroomService.saveClassroom(classroom1);
			classroomService.saveClassroom(classroom2);
			classroomService.saveClassroom(classroom3);

			// Save subjects


			// Create instructors and single groups
			Instructor instructor1 = new Instructor("James Blunt", "kolejniczak2002@gmail.com", "IT",List.of(subject1,subject2),List.of(true,true,false,false,false),List.of());
			Instructor instructor2 = new Instructor("Adam", "adam@gmail.com","IT",List.of(subject2,subject3),List.of(true,false,true,false,true),List.of());
			Instructor instructor3 = new Instructor("Jakob","jakub242484@gmail.com", "IT",List.of(subject1,subject3),List.of(true,true,true,true,true),List.of());
			Instructor instructor4 = new Instructor("Julian","julian@gmail.com", "IT",List.of(subject4,subject5),List.of(false,true,true,true,false),List.of());

			SingleGroup singleGroup1 = new SingleGroup("Grupa 1","IT",15,null);
			SingleGroup singleGroup2 = new SingleGroup("Grupa 2","IT",15,null);
			SingleGroup singleGroup3 = new SingleGroup("Grupa 3","IT",15,null);

			// Set relationships
			singleGroup1.setInstructors(List.of(instructor1,instructor2,instructor3,instructor4));
			singleGroup2.setInstructors(List.of(instructor1,instructor2,instructor3,instructor4));
			singleGroup3.setInstructors(List.of(instructor1,instructor2,instructor3,instructor4));

			// Save instructors and single groups
			instructorService.saveInstructor(instructor1);
			instructorService.saveInstructor(instructor2);
			instructorService.saveInstructor(instructor3);
			instructorService.saveInstructor(instructor4);

			singleGroupService.saveGroup(singleGroup1);
			singleGroupService.saveGroup(singleGroup2);
			singleGroupService.saveGroup(singleGroup3);
		};
	}
}
