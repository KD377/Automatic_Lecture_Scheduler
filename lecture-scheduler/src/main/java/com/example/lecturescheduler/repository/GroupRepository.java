package com.example.lecturescheduler.repository;

import com.example.lecturescheduler.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}