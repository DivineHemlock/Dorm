package com.airbyte.dorm.task;

import com.airbyte.dorm.DormApplication;
import com.airbyte.dorm.dto.TaskDTO;
import com.airbyte.dorm.model.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static com.airbyte.dorm.CommonTestData.*;
import static com.airbyte.dorm.CommonTestData.UPDATED_DOUBLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {DormApplication.class})
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TaskService service;
    @Autowired
    private TaskDataProvider dataProvider;
    private TaskDTO dto;

    @BeforeEach
    public void initTest() {
        dto = dataProvider.createDTO();
    }

    @Test
    @Transactional
    @DisplayName("REST Request to save task")
    public void saveRoom() throws Exception {
        int databaseSizeBeforeSave = service.getAll().size();

        mockMvc.perform(post("/api/v1/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        Task entity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(service.getAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(entity.getId()).isNotBlank();
        assertThat(entity.getDescription()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(entity.getDueDate()).isEqualTo(DEFAULT_DATE);
        assertThat(entity.getTimeLog()).isEqualTo(DEFAULT_DOUBLE);
        assertThat(entity.getStatus()).isEqualTo(DEFAULT_TASK_STATUS);
        assertThat(entity.getPriority()).isEqualTo(DEFAULT_TASK_PRIORITY);
        assertThat(entity.getPersonnelId()).isNotNull();
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get task By Id")
    public void getRoomById() throws Exception {
        Task entity = service.save(dto);

        mockMvc.perform(get("/api/v1/task/{id}", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(entity.getId()))
                .andExpect(jsonPath("$.description").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.dueDate").value(DEFAULT_DATE))
                .andExpect(jsonPath("$.timeLog").value(DEFAULT_DOUBLE))
                .andExpect(jsonPath("$.status").value(DEFAULT_TASK_STATUS.name()))
                .andExpect(jsonPath("$.priority").value(DEFAULT_TASK_PRIORITY.name()))
                .andExpect(jsonPath("$.personnelId").isNotEmpty());
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get tasks")
    public void getTasks() throws Exception {
        Task entity = service.save(dto);

        mockMvc.perform(get("/api/v1/task"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].description").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.[0].dueDate").value(DEFAULT_DATE))
                .andExpect(jsonPath("$.[0].timeLog").value(DEFAULT_DOUBLE))
                .andExpect(jsonPath("$.[0].status").value(DEFAULT_TASK_STATUS.name()))
                .andExpect(jsonPath("$.[0].priority").value(DEFAULT_TASK_PRIORITY.name()))
                .andExpect(jsonPath("$.[0].personnelId").isNotEmpty());
    }

    @Test
    @Transactional
    @DisplayName("REST Request to update task")
    public void updateTask() throws Exception {
        int databaseSizeBeforeSave = service.getAll().size();
        Task entity = service.save(dto);

        dto = dataProvider.updateDTO();

        mockMvc.perform(patch("/api/v1/task/{id}", entity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isAccepted());

        Task newEntity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(service.getAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(newEntity.getId()).isNotBlank();
        assertThat(entity.getDescription()).isEqualTo(UPDATED_STRING);
        assertThat(entity.getName()).isEqualTo(UPDATED_NAME);
        assertThat(entity.getDueDate()).isEqualTo(UPDATED_DATE);
        assertThat(entity.getTimeLog()).isEqualTo(UPDATED_DOUBLE);
        assertThat(entity.getStatus()).isEqualTo(UPDATED_TASK_STATUS);
        assertThat(entity.getPriority()).isEqualTo(UPDATED_TASK_PRIORITY);
        assertThat(entity.getPersonnelId()).isNotNull();
    }

    @Test
    @Transactional
    @DisplayName("REST Request to delete task")
    public void deleteRoom() throws Exception {
        Task entity = service.save(dto);
        int databaseSizeAfterSave = service.getAll().size();

        mockMvc.perform(delete("/api/v1/task/{id}", entity.getId()))
                .andExpect(status().isNoContent());

        assertThat(service.getAll()).hasSize(databaseSizeAfterSave - 1);
    }
}
