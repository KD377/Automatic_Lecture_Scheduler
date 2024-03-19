package com.example.lecturescheduler.repository;

import com.example.lecturescheduler.model.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
}