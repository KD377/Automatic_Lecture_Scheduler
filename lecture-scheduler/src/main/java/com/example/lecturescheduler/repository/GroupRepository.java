package com.example.lecturescheduler.repository;

import com.example.lecturescheduler.model.SingleGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<SingleGroup, Long> {
    Optional<SingleGroup> findByName(String name);
}