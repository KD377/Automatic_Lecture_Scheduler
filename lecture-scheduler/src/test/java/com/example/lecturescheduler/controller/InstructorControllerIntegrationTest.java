package com.example.lecturescheduler.controller;

import com.example.lecturescheduler.exception.ResourceNotFoundException;
import com.example.lecturescheduler.model.Instructor;
import com.example.lecturescheduler.repository.InstructorRepository;
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
class InstructorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InstructorRepository instructorRepository;

    @BeforeEach
    public void setUp() {
        instructorRepository.deleteAll();

        Instructor instructor = new Instructor();
        instructor.setName("Test Instructor");
        instructor.setEmail("test@example.com");
        instructorRepository.saveAndFlush(instructor);
    }

    @Test
    void testGetAllInstructors() throws Exception {
        mockMvc.perform(get("/api/instructors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Test Instructor"))
                .andExpect(jsonPath("$[0].email").value("test@example.com"));
    }

    @Test
    void testGetInstructorById() throws Exception {
        Instructor instructor = instructorRepository.findAll().getFirst();
        mockMvc.perform(get("/api/instructors/{id}", instructor.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Test Instructor"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testGetInstructorByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/instructors/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ResourceNotFoundException.class))
                .andExpect(result -> assertThat(Objects.requireNonNull(result.getResolvedException()).getMessage()).isEqualTo("Instructor not found for this id :: 999"));
    }

    @Test
    void testCreateInstructor() throws Exception {
        mockMvc.perform(post("/api/instructors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"New Instructor\", \"email\": \"new@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Instructor"))
                .andExpect(jsonPath("$.email").value("new@example.com"));

        Optional<Instructor> createdInstructor = instructorRepository.findByEmail("new@example.com");
        assertThat(createdInstructor).isPresent();
    }

    @Test
    void testUpdateInstructor() throws Exception {
        Instructor instructor = instructorRepository.findAll().getFirst();
        mockMvc.perform(put("/api/instructors/{id}", instructor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated Instructor\", \"email\": \"updated@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Instructor"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));

        Optional<Instructor> updatedInstructor = instructorRepository.findById(instructor.getId());
        assertThat(updatedInstructor).isPresent();
        assertThat(updatedInstructor.get().getName()).isEqualTo("Updated Instructor");
        assertThat(updatedInstructor.get().getEmail()).isEqualTo("updated@example.com");
    }

    @Test
    void testDeleteInstructor() throws Exception {
        Instructor instructor = instructorRepository.findAll().getFirst();
        mockMvc.perform(delete("/api/instructors/{id}", instructor.getId()))
                .andExpect(status().isOk());

        Optional<Instructor> deletedInstructor = instructorRepository.findById(instructor.getId());
        assertThat(deletedInstructor).isNotPresent();
    }
}
