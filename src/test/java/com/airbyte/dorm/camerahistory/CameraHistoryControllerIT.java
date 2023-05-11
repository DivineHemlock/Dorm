package com.airbyte.dorm.camerahistory;

import com.airbyte.dorm.DormApplication;
import com.airbyte.dorm.dto.CameraHistoryDTO;
import com.airbyte.dorm.model.events.CameraHistory;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {DormApplication.class})
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CameraHistoryControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CameraHistoryService service;
    @Autowired
    private CameraHistoryDataProvider dataProvider;
    private CameraHistoryDTO dto;

    @BeforeEach
    public void initTest() {
        dto = dataProvider.createDTO();
    }

    @Test
    @Transactional
    @DisplayName("REST Request to save cameraHistory")
    public void saveCameraHistory() throws Exception {
        int databaseSizeBeforeSave = service.getAll().size();

        mockMvc.perform(post("/api/v1/cameraHistory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        CameraHistory entity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(service.getAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(entity.getId()).isNotBlank();
        assertThat(entity.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(entity.getDescription()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getTitle()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getUnit()).isNotNull();
        assertThat(entity.getUnit()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getSupervisor()).isEqualTo(DEFAULT_NAME);
        assertThat(entity.getAssignee()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get cameraHistory By Id")
    public void getCameraHistoryById() throws Exception {
        CameraHistory entity = service.save(dto);

        mockMvc.perform(get("/api/v1/cameraHistory/{id}", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(entity.getId()))
                .andExpect(jsonPath("$.date").value(DEFAULT_DATE))
                .andExpect(jsonPath("$.description").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.title").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.unit").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.supervisor").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.assignee").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get cameraHistories")
    public void getCameraHistories() throws Exception {
        CameraHistory entity = service.save(dto);

        mockMvc.perform(get("/api/v1/cameraHistory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].date").value(DEFAULT_DATE))
                .andExpect(jsonPath("$.[0].description").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].title").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].unit").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].supervisor").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.[0].assignee").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to update cameraHistory")
    public void updateCameraHistory() throws Exception {
        int databaseSizeBeforeSave = service.getAll().size();
        CameraHistory entity = service.save(dto);

        dto = dataProvider.updateDTO();

        mockMvc.perform(patch("/api/v1/cameraHistory/{id}", entity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isAccepted());

        CameraHistory newEntity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(service.getAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(newEntity.getId()).isNotBlank();
        assertThat(newEntity.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(newEntity.getDescription()).isEqualTo(UPDATED_STRING);
        assertThat(newEntity.getTitle()).isEqualTo(UPDATED_STRING);
        assertThat(newEntity.getUnit()).isNotNull();
        assertThat(newEntity.getUnit()).isEqualTo(UPDATED_STRING);
        assertThat(newEntity.getSupervisor()).isEqualTo(UPDATED_NAME);
        assertThat(newEntity.getAssignee()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    @DisplayName("REST Request to delete cameraHistory")
    public void deleteCameraHistory() throws Exception {
        CameraHistory entity = service.save(dto);
        int databaseSizeAfterSave = service.getAll().size();

        mockMvc.perform(delete("/api/v1/cameraHistory/{id}", entity.getId()))
                .andExpect(status().isNoContent());

        assertThat(service.getAll()).hasSize(databaseSizeAfterSave - 1);
    }
}
