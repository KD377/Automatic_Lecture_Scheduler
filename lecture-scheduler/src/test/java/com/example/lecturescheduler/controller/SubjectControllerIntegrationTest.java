package com.example.lecturescheduler.controller;

import com.example.lecturescheduler.exception.ResourceNotFoundException;
import com.example.lecturescheduler.model.Subject;
import com.example.lecturescheduler.repository.ClassroomRepository;
import com.example.lecturescheduler.repository.InstructorRepository;
import com.example.lecturescheduler.repository.SubjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SubjectControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    @BeforeEach
    public void setUp() {
        classroomRepository.deleteAll();
        instructorRepository.deleteAll();
        subjectRepository.deleteAll();

        Subject subject = new Subject();
        subject.setName("Test Subject");
        subjectRepository.saveAndFlush(subject);
    }

    @Test
    void testGetAllSubjects() throws Exception {
        mockMvc.perform(get("/api/subjects"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Test Subject"));
    }

    @Test
    void testGetSubjectById() throws Exception {
        Subject subject = subjectRepository.findAll().getFirst();
        mockMvc.perform(get("/api/subjects/{id}", subject.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Test Subject"));
    }

    @Test
    void testGetSubjectByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/subjects/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ResourceNotFoundException.class))
                .andExpect(result -> assertThat(Objects.requireNonNull(result.getResolvedException()).getMessage()).isEqualTo("Subject not found for this id :: 999"));
    }

    @Test
    void testCreateSubject() throws Exception {
        mockMvc.perform(post("/api/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"New Subject\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Subject"));

        Optional<Subject> createdSubject = subjectRepository.findByName("New Subject");
        assertThat(createdSubject).isPresent();
    }

    @Test
    void testUpdateSubject() throws Exception {
        Subject subject = subjectRepository.findAll().getFirst();
        mockMvc.perform(put("/api/subjects/{id}", subject.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated Subject\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Subject"));

        Optional<Subject> updatedSubject = subjectRepository.findById(subject.getId());
        assertThat(updatedSubject).isPresent();
        assertThat(updatedSubject.get().getName()).isEqualTo("Updated Subject");
    }

    @Test
    void testDeleteSubject() throws Exception {
        Subject subject = subjectRepository.findAll().getFirst();
        mockMvc.perform(delete("/api/subjects/{id}", subject.getId()))
                .andExpect(status().isOk());

        Optional<Subject> deletedSubject = subjectRepository.findById(subject.getId());
        assertThat(deletedSubject).isNotPresent();
    }
}
