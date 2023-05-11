package com.airbyte.dorm.ceremony;

import com.airbyte.dorm.DormApplication;
import com.airbyte.dorm.dto.CeremonyDTO;
import com.airbyte.dorm.model.events.Ceremony;
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
public class CeremonyControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CeremonyService service;
    @Autowired
    private CeremonyDataProvider dataProvider;
    private CeremonyDTO dto;

    @BeforeEach
    public void initTest() {
        dto = dataProvider.createDTO();
    }

    @Test
    @Transactional
    @DisplayName("REST Request to save ceremony")
    public void saveCeremony() throws Exception {
        int databaseSizeBeforeSave = service.getAll().size();

        mockMvc.perform(post("/api/v1/ceremony")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        Ceremony entity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(service.getAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(entity.getId()).isNotBlank();
        assertThat(entity.getTitle()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getDescription()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get ceremony By Id")
    public void getFloorById() throws Exception {
        Ceremony entity = service.save(dto);

        mockMvc.perform(get("/api/v1/ceremony/{id}", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(entity.getId()))
                .andExpect(jsonPath("$.title").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.description").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.date").value(DEFAULT_DATE));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get ceremonies")
    public void getFloors() throws Exception {
        Ceremony entity = service.save(dto);

        mockMvc.perform(get("/api/v1/ceremony"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].title").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].description").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].date").value(DEFAULT_DATE));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to update ceremony")
    public void updateCeremony() throws Exception {
        int databaseSizeBeforeSave = service.getAll().size();
        Ceremony entity = service.save(dto);

        dto = dataProvider.updateDTO();

        mockMvc.perform(patch("/api/v1/ceremony/{id}", entity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isAccepted());

        Ceremony newEntity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(service.getAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(newEntity.getId()).isNotBlank();
        assertThat(newEntity.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(newEntity.getDescription()).isEqualTo(UPDATED_STRING);
        assertThat(newEntity.getTitle()).isEqualTo(UPDATED_STRING);
    }

    @Test
    @Transactional
    @DisplayName("REST Request to delete ceremony")
    public void deleteCeremony() throws Exception {
        Ceremony entity = service.save(dto);
        int databaseSizeAfterSave = service.getAll().size();

        mockMvc.perform(delete("/api/v1/ceremony/{id}", entity.getId()))
                .andExpect(status().isNoContent());

        assertThat(service.getAll()).hasSize(databaseSizeAfterSave - 1);
    }

}
