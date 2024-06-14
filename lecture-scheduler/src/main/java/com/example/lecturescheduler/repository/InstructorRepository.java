package com.example.lecturescheduler.repository;

import com.example.lecturescheduler.model.Instructor;
import com.example.lecturescheduler.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    @Query("SELECT i FROM Instructor i JOIN i.subjectsTaught s WHERE s = :subject")
    List<Instructor> findBySubject(@Param("subject") Subject subject);
    Optional<Instructor> findByEmail(String mail);
}