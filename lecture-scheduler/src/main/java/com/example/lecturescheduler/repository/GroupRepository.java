package com.example.lecturescheduler.repository;

import com.example.lecturescheduler.model.SingleGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<SingleGroup, Long> {
}