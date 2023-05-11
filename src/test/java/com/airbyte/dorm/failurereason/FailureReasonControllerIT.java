package com.airbyte.dorm.failurereason;

import com.airbyte.dorm.DormApplication;
import com.airbyte.dorm.dto.FailureReasonDTO;
import com.airbyte.dorm.model.FailureReason;
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
import static com.airbyte.dorm.CommonTestData.UPDATED_STRING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {DormApplication.class})
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FailureReasonControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private FailureReasonService service;
    @Autowired
    private FailureReasonDataProvider dataProvider;
    private FailureReasonDTO dto;

    @BeforeEach
    public void initTest() {
        dto = dataProvider.createDTO();
    }

    @Test
    @Transactional
    @DisplayName("REST Request to save failure reason")
    public void saveFailureReason() throws Exception {
        int databaseSizeBeforeSave = service.getAll().size();

        mockMvc.perform(post("/api/v1/failureReason")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        FailureReason entity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(service.getAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(entity.getId()).isNotBlank();
        assertThat(entity.getReason()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getType()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getName()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getRequestId()).isNull();
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get failure reason By Id")
    public void getFailureReasonById() throws Exception {
        FailureReason entity = service.save(dto);

        mockMvc.perform(get("/api/v1/failureReason/{id}", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(entity.getId()))
                .andExpect(jsonPath("$.reason").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.type").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.name").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.requestId").isEmpty());
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get failure reason")
    public void getFailureReasons() throws Exception {
        FailureReason entity = service.save(dto);

        mockMvc.perform(get("/api/v1/failureReason"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].reason").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].type").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].name").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].requestId").isEmpty());
    }

    @Test
    @Transactional
    @DisplayName("REST Request to update failure reason")
    public void updateFloor() throws Exception {
        int databaseSizeBeforeSave = service.getAll().size();
        FailureReason entity = service.save(dto);

        dto = dataProvider.updateDTO();

        mockMvc.perform(patch("/api/v1/failureReason/{id}", entity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isAccepted());

        FailureReason newEntity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(service.getAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(newEntity.getId()).isNotBlank();
        assertThat(newEntity.getReason()).isEqualTo(UPDATED_STRING);
        assertThat(newEntity.getType()).isEqualTo(UPDATED_STRING);
        assertThat(newEntity.getName()).isEqualTo(UPDATED_STRING);
        assertThat(newEntity.getRequestId()).isNull();
    }

    @Test
    @Transactional
    @DisplayName("REST Request to delete failure reason")
    public void deleteFloor() throws Exception {
        FailureReason entity = service.save(dto);
        int databaseSizeAfterSave = service.getAll().size();

        mockMvc.perform(delete("/api/v1/failureReason/{id}", entity.getId()))
                .andExpect(status().isNoContent());

        assertThat(service.getAll()).hasSize(databaseSizeAfterSave - 1);
    }
}
