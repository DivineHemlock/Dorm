package com.airbyte.dorm.personnel;

import com.airbyte.dorm.DormApplication;
import com.airbyte.dorm.dto.PersonnelDTO;
import com.airbyte.dorm.model.enums.Gender;
import com.airbyte.dorm.model.people.Personnel;
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
public class PersonnelControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PersonnelService service;
    @Autowired
    private PersonnelDataProvider dataProvider;
    private PersonnelDTO dto;

    @BeforeEach
    public void initTest() {
        dto = dataProvider.createDTO();
    }

    @Test
    @Transactional
    @DisplayName("REST Request to save personnel")
    public void savePersonnel() throws Exception {
        int databaseSizeBeforeSave = service.getAll().size();

        mockMvc.perform(post("/api/v1/personnel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        Personnel entity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(service.getAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(entity.getId()).isNotBlank();
        assertThat(entity.getGender()).isEqualTo(Gender.valueOf(DEFAULT_GENDER));
        assertThat(entity.getResidenceType()).isEqualTo(DEFAULT_RESIDENT);
        assertThat(entity.getCharacteristicId()).isNotBlank();
        assertThat(entity.getType()).isEqualTo(DEFAULT_CATEGORY_NAME);
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get personnel By Id")
    public void getPersonnelById() throws Exception {
        Personnel entity = service.save(dto);

        mockMvc.perform(get("/api/v1/personnel/{id}", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(entity.getId()))
                .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER))
                .andExpect(jsonPath("$.residenceType").value(DEFAULT_RESIDENT.name()))
                .andExpect(jsonPath("$.characteristicId").isNotEmpty())
                .andExpect(jsonPath("$.type").value(DEFAULT_CATEGORY_NAME));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get personnel")
    public void getPersonnel() throws Exception {
        Personnel entity = service.save(dto);

        mockMvc.perform(get("/api/v1/personnel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].gender").value(DEFAULT_GENDER))
                .andExpect(jsonPath("$.[0].residenceType").value(DEFAULT_RESIDENT.name()))
                .andExpect(jsonPath("$.[0].characteristicId").isNotEmpty())
                .andExpect(jsonPath("$.[0].type").value(DEFAULT_CATEGORY_NAME));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to update personnel")
    public void updatePersonnel() throws Exception {
        int databaseSizeBeforeSave = service.getAll().size();
        Personnel entity = service.save(dto);

        dto = dataProvider.updateDTO();

        mockMvc.perform(patch("/api/v1/personnel/{id}", entity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isAccepted());

        Personnel newEntity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(service.getAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(newEntity.getId()).isNotBlank();
        assertThat(newEntity.getGender()).isEqualTo(Gender.valueOf(UPDATED_GENDER));
        assertThat(newEntity.getResidenceType()).isEqualTo(UPDATED_RESIDENT);
        assertThat(newEntity.getCharacteristicId()).isNotBlank();
        assertThat(newEntity.getType()).isEqualTo(UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    @DisplayName("REST Request to delete personnel")
    public void deletePersonnel() throws Exception {
        Personnel entity = service.save(dto);
        int databaseSizeAfterSave = service.getAll().size();


        mockMvc.perform(delete("/api/v1/personnel/{id}", entity.getId()))
                .andExpect(status().isNoContent());

        assertThat(service.getAll()).hasSize(databaseSizeAfterSave - 1);
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of personnel")
    public void searchResultListByName() throws Exception {
        Personnel entity = service.save(dto);

        mockMvc.perform(get("/api/v1/personnel/search?name=" + DEFAULT_STRING, entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].gender").value(DEFAULT_GENDER))
                .andExpect(jsonPath("$.[0].residenceType").value(DEFAULT_RESIDENT.name()))
                .andExpect(jsonPath("$.[0].characteristicId").isNotEmpty());
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of personnel")
    public void searchResultListById() throws Exception {
        Personnel entity = service.save(dto);

        mockMvc.perform(get("/api/v1/personnel/search?id=" + entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].gender").value(DEFAULT_GENDER))
                .andExpect(jsonPath("$.[0].residenceType").value(DEFAULT_RESIDENT.name()))
                .andExpect(jsonPath("$.[0].characteristicId").isNotEmpty());
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of personnel")
    public void searchResultListByNationalCode() throws Exception {
        Personnel entity = service.save(dto);

        mockMvc.perform(get("/api/v1/personnel/search?nationalCode=" + DEFAULT_NATIONAL_CODE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].gender").value(DEFAULT_GENDER))
                .andExpect(jsonPath("$.[0].residenceType").value(DEFAULT_RESIDENT.name()))
                .andExpect(jsonPath("$.[0].characteristicId").isNotEmpty());
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of personnel")
    public void searchResultListByGender() throws Exception {
        Personnel entity = service.save(dto);

        mockMvc.perform(get("/api/v1/personnel/search?gender=" + DEFAULT_GENDER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].gender").value(DEFAULT_GENDER))
                .andExpect(jsonPath("$.[0].residenceType").value(DEFAULT_RESIDENT.name()))
                .andExpect(jsonPath("$.[0].characteristicId").isNotEmpty());
    }
}
