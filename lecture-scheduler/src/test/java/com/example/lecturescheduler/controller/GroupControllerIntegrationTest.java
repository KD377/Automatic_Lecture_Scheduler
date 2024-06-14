package com.example.lecturescheduler.controller;

import com.example.lecturescheduler.exception.ResourceNotFoundException;
import com.example.lecturescheduler.model.SingleGroup;
import com.example.lecturescheduler.repository.GroupRepository;
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
class GroupControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GroupRepository groupRepository;

    @BeforeEach
    public void setUp() {
        groupRepository.deleteAll();

        SingleGroup group = new SingleGroup();
        group.setName("Test Group");
        groupRepository.saveAndFlush(group);
    }

    @Test
    void testGetAllGroups() throws Exception {
        mockMvc.perform(get("/api/groups"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Test Group"));
    }

    @Test
    void testGetGroupById() throws Exception {
        SingleGroup group = groupRepository.findAll().getFirst();
        mockMvc.perform(get("/api/groups/{id}", group.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Test Group"));
    }

    @Test
    void testGetGroupByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/groups/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ResourceNotFoundException.class))
                .andExpect(result -> assertThat(Objects.requireNonNull(result.getResolvedException()).getMessage()).isEqualTo("Group not found for this id :: 999"));
    }

    @Test
    void testCreateGroup() throws Exception {
        mockMvc.perform(post("/api/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"New Group\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Group"));

        Optional<SingleGroup> createdGroup = groupRepository.findByName("New Group");
        assertThat(createdGroup).isPresent();
    }

    @Test
    void testUpdateGroup() throws Exception {
        SingleGroup group = groupRepository.findAll().getFirst();
        mockMvc.perform(put("/api/groups/{id}", group.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated Group\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Group"));

        Optional<SingleGroup> updatedGroup = groupRepository.findById(group.getId());
        assertThat(updatedGroup).isPresent();
        assertThat(updatedGroup.get().getName()).isEqualTo("Updated Group");
    }

    @Test
    void testDeleteGroup() throws Exception {
        SingleGroup group = groupRepository.findAll().getFirst();
        mockMvc.perform(delete("/api/groups/{id}", group.getId()))
                .andExpect(status().isOk());

        Optional<SingleGroup> deletedGroup = groupRepository.findById(group.getId());
        assertThat(deletedGroup).isNotPresent();
    }
}
