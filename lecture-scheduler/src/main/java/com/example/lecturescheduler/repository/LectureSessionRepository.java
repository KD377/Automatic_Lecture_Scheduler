package com.example.lecturescheduler.repository;

import com.example.lecturescheduler.model.LectureSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LectureSessionRepository extends JpaRepository<LectureSession, Long> {
    @Query("SELECT ls FROM LectureSession ls WHERE ls.instructor.email = :email")
    List<LectureSession> findLectureSessionsByInstructorEmail(@Param("email") String email);
}
