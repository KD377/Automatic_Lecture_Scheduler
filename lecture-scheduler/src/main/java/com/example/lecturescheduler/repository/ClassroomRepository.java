package com.example.lecturescheduler.repository;

import com.example.lecturescheduler.model.Classroom;
import com.example.lecturescheduler.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    @Query("SELECT c FROM Classroom c JOIN c.subjects s WHERE s = :subject")
    List<Classroom> findBySubject(@Param("subject") Subject subject);
}