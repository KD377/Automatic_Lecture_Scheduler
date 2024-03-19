package com.example.lecturescheduler.repository;

import com.example.lecturescheduler.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
}
