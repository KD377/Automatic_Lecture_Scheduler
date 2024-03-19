package com.example.lecturescheduler.repository;

import com.example.lecturescheduler.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {
}