package com.airbyte.dorm.record;

import com.airbyte.dorm.DormApplication;
import com.airbyte.dorm.dto.RecordDTO;
import com.airbyte.dorm.model.Record;
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
public class RecordControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RecordService service;
    @Autowired
    private RecordDataProvider dataProvider;
    private RecordDTO dto;

    @BeforeEach
    public void initTest() {
        dto = dataProvider.createDTO();
    }

    @Test
    @Transactional
    @DisplayName("REST Request to save record")
    public void saveRecord() throws Exception {
        int databaseSizeBeforeSave = service.getAll().size();

        mockMvc.perform(post("/api/v1/record")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        Record entity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(service.getAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(entity.getId()).isNotBlank();
        assertThat(entity.getDate()).isNotNull();
        assertThat(entity.getStartDate()).isNotNull();
        assertThat(entity.getEndDate()).isNotNull();
        assertThat(entity.getTitle()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getDestinationAddress()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getDestinationPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(entity.getDescription()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getPersonId()).isEqualTo(dto.getPersonId());
        assertThat(entity.getPerson()).isNotNull();
        assertThat(entity.getCheckCleaning()).isEqualTo(DEFAULT_BOOLEAN);
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get record By Id")
    public void getRecordById() throws Exception {
        Record entity = service.save(dto);

        mockMvc.perform(get("/api/v1/record/{id}", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(entity.getId()))
                .andExpect(jsonPath("$.date").value(DEFAULT_DATE))
                .andExpect(jsonPath("$.startDate").value(DEFAULT_DATE))
                .andExpect(jsonPath("$.endDate").value(DEFAULT_DATE))
                .andExpect(jsonPath("$.title").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.destinationAddress").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.destinationPhoneNumber").value(DEFAULT_PHONE_NUMBER))
                .andExpect(jsonPath("$.description").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.penalty").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.checkCleaning").value(DEFAULT_BOOLEAN))
                .andExpect(jsonPath("$.personId").value(dto.getPersonId()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get records")
    public void getPersons() throws Exception {
        Record entity = service.save(dto);

        mockMvc.perform(get("/api/v1/record"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].date").value(DEFAULT_DATE))
                .andExpect(jsonPath("$.[0].startDate").value(DEFAULT_DATE))
                .andExpect(jsonPath("$.[0].endDate").value(DEFAULT_DATE))
                .andExpect(jsonPath("$.[0].title").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].destinationAddress").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].destinationPhoneNumber").value(DEFAULT_PHONE_NUMBER))
                .andExpect(jsonPath("$.[0].description").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].penalty").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].checkCleaning").value(DEFAULT_BOOLEAN))
                .andExpect(jsonPath("$.[0].personId").value(dto.getPersonId()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to update record")
    public void updatePerson() throws Exception {
        int databaseSizeBeforeSave = service.getAll().size();
        Record entity = service.save(dto);

        dto = dataProvider.updateDTO();

        mockMvc.perform(patch("/api/v1/record/{id}", entity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isAccepted());

        Record newEntity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(service.getAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(newEntity.getId()).isNotBlank();
        assertThat(newEntity.getDate()).isNotNull();
        assertThat(newEntity.getStartDate()).isNotNull();
        assertThat(newEntity.getEndDate()).isNotNull();
        assertThat(newEntity.getTitle()).isEqualTo(UPDATED_STRING);
        assertThat(newEntity.getDestinationAddress()).isEqualTo(UPDATED_STRING);
        assertThat(newEntity.getDestinationPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(newEntity.getDescription()).isEqualTo(UPDATED_STRING);
        assertThat(newEntity.getPersonId()).isNotBlank();
        assertThat(newEntity.getPerson()).isNotNull();
        assertThat(entity.getCheckCleaning()).isEqualTo(UPDATED_BOOLEAN);
    }

    @Test
    @Transactional
    @DisplayName("REST Request to delete record")
    public void deletePerson() throws Exception {
        Record entity = service.save(dto);
        int databaseSizeAfterSave = service.getAll().size();


        mockMvc.perform(delete("/api/v1/record/{id}", entity.getId()))
                .andExpect(status().isNoContent());

        assertThat(service.getAll()).hasSize(databaseSizeAfterSave - 1);
    }
}
