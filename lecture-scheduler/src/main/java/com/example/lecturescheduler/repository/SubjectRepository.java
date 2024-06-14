package com.example.lecturescheduler.repository;

import com.example.lecturescheduler.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Optional<Subject> findByName(String newSubject);
}
