package com.example.lecturescheduler.repository;

import com.example.lecturescheduler.model.LectureSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureSessionRepository extends JpaRepository<LectureSession, Long> {

}
