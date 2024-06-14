package com.example.lecturescheduler.controller;

import com.example.lecturescheduler.exception.ResourceNotFoundException;
import com.example.lecturescheduler.model.Classroom;
import com.example.lecturescheduler.repository.ClassroomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class ClassroomControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClassroomRepository classroomRepository;

    private Classroom classroom;

    @BeforeEach
    public void setUp() {
        classroomRepository.deleteAll();
        classroom = new Classroom();
        classroom.setName("Test Classroom");
        classroomRepository.saveAndFlush(classroom);
    }

    @Test
    void testGetAllClassrooms() throws Exception {
        mockMvc.perform(get("/api/classrooms"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Test Classroom"));
    }

    @Test
    void testGetClassroomById() throws Exception {
        mockMvc.perform(get("/api/classrooms/{id}", classroom.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Test Classroom"));
    }

    @Test
    void testGetClassroomByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/classrooms/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ResourceNotFoundException.class))
                .andExpect(result -> assertThat(Objects.requireNonNull(result.getResolvedException()).getMessage()).isEqualTo("Classroom not found for this id :: 999"));
    }

    @Test
    void testCreateClassroom() throws Exception {
        mockMvc.perform(post("/api/classrooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"New Classroom\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Classroom"));

        Optional<Classroom> createdClassroom = classroomRepository.findByName("New Classroom");
        assertThat(createdClassroom).isPresent();
    }

    @Test
    void testUpdateClassroom() throws Exception {
        mockMvc.perform(put("/api/classrooms/{id}", classroom.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated Classroom\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Classroom"));

        Optional<Classroom> updatedClassroom = classroomRepository.findById(classroom.getId());
        assertThat(updatedClassroom).isPresent();
        assertThat(updatedClassroom.get().getName()).isEqualTo("Updated Classroom");
    }

    @Test
    void testDeleteClassroom() throws Exception {
        mockMvc.perform(delete("/api/classrooms/{id}", classroom.getId()))
                .andExpect(status().isOk());

        Optional<Classroom> deletedClassroom = classroomRepository.findById(classroom.getId());
        assertThat(deletedClassroom).isNotPresent();
    }
}
