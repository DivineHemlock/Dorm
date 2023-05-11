package com.airbyte.dorm.person;

import com.airbyte.dorm.DormApplication;
import com.airbyte.dorm.dto.PersonDTO;
import com.airbyte.dorm.model.enums.Gender;
import com.airbyte.dorm.model.people.Person;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {DormApplication.class})
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PersonService service;
    @Autowired
    private PersonDataProvider dataProvider;
    private PersonDTO dto;

    @BeforeEach
    public void initTest() {
        dto = dataProvider.createDTO();
    }

    @Test
    @Transactional
    @DisplayName("REST Request to save person")
    public void savePerson() throws Exception {
        int databaseSizeBeforeSave = service.getAll().size();

        mockMvc.perform(post("/api/v1/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        Person entity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(service.getAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(entity.getId()).isNotBlank();
        assertThat(entity.getAccommodationType()).isEqualTo(DEFAULT_ACCOMMODATION);
        assertThat(entity.getResidenceType()).isEqualTo(DEFAULT_RESIDENT);
        assertThat(entity.getCharacteristicId()).isNotBlank();
        assertThat(entity.getFiles().get(DEFAULT_CATEGORY_NAME)).isEqualTo(DEFAULT_ID);
        assertThat(entity.getFiles().get(UPDATED_CATEGORY_NAME)).isEqualTo(UPDATED_ID);
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get person By Id")
    public void getPersonById() throws Exception {
        Person entity = service.save(dto);

        mockMvc.perform(get("/api/v1/person/{id}", entity.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(entity.getId()))
//                .andExpect(jsonPath("$.name").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.accommodationType").value(DEFAULT_ACCOMMODATION.name()))
//                .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER))
//                .andExpect(jsonPath("$.timePeriod.startDate").value(DEFAULT_DATE))
//                .andExpect(jsonPath("$.timePeriod.endDate").value(DEFAULT_DATE))
                .andExpect(jsonPath("$.residenceType").value(DEFAULT_RESIDENT.name()))
//                .andExpect(jsonPath("$.nationalCode").value(DEFAULT_NATIONAL_CODE))
//                .andExpect(jsonPath("$.registrationType").value(DEFAULT_REGISTRATION.name()))
                .andExpect(jsonPath("$.characteristicId").isNotEmpty());
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get persons")
    public void getPersons() throws Exception {
        Person entity = service.save(dto);

        mockMvc.perform(get("/api/v1/person"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
//                .andExpect(jsonPath("$.[0].name").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].accommodationType").value(DEFAULT_ACCOMMODATION.name()))
//                .andExpect(jsonPath("$.[0].gender").value(DEFAULT_GENDER))
//                .andExpect(jsonPath("$.[0].timePeriod.startDate").value(DEFAULT_DATE))
//                .andExpect(jsonPath("$.[0].timePeriod.endDate").value(DEFAULT_DATE))
                .andExpect(jsonPath("$.[0].residenceType").value(DEFAULT_RESIDENT.name()))
//                .andExpect(jsonPath("$.[0].nationalCode").value(DEFAULT_NATIONAL_CODE))
//                .andExpect(jsonPath("$.[0].registrationType").value(DEFAULT_REGISTRATION.name()))
                .andExpect(jsonPath("$.[0].characteristicId").isNotEmpty());
    }

    @Test
    @Transactional
    @DisplayName("REST Request to update person")
    public void updatePerson() throws Exception {
        int databaseSizeBeforeSave = service.getAll().size();
        Person entity = service.save(dto);

        dto = dataProvider.updateDTO();

        mockMvc.perform(patch("/api/v1/person/{id}", entity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isAccepted());

        Person newEntity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(service.getAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(newEntity.getId()).isNotBlank();
//        assertThat(newEntity.getName()).isEqualTo(UPDATED_STRING);
        assertThat(newEntity.getAccommodationType()).isEqualTo(UPDATED_ACCOMMODATION);
//        assertThat(newEntity.getGender()).isEqualTo(Gender.valueOf(UPDATED_GENDER));
//        assertThat(newEntity.getTimePeriod().getStartDate()).isNotNull();
//        assertThat(newEntity.getTimePeriod().getEndDate()).isNotNull();
        assertThat(newEntity.getResidenceType()).isEqualTo(UPDATED_RESIDENT);
//        assertThat(newEntity.getNationalCode()).isEqualTo(UPDATED_NATIONAL_CODE);
//        assertThat(newEntity.getRegistrationType()).isEqualTo(UPDATED_REGISTRATION);
        assertThat(newEntity.getCharacteristicId()).isNotBlank();

    }

    @Test
    @Transactional
    @DisplayName("REST Request to delete person")
    public void deletePerson() throws Exception {
        Person entity = service.save(dto);
        int databaseSizeAfterSave = service.getAll().size();

        mockMvc.perform(delete("/api/v1/person/{id}", entity.getId()))
                .andExpect(status().isNoContent());

        assertThat(service.getAll()).hasSize(databaseSizeAfterSave - 1);
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of person")
    public void searchResultListByName() throws Exception {
        Person entity = service.save(dto);

        mockMvc.perform(get("/api/v1/person/search?name=" + DEFAULT_STRING, entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
//                .andExpect(jsonPath("$.[0].name").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].accommodationType").value(DEFAULT_ACCOMMODATION.name()))
//                .andExpect(jsonPath("$.[0].gender").value(DEFAULT_GENDER))
//                .andExpect(jsonPath("$.[0].timePeriod.startDate").value(DEFAULT_DATE))
//                .andExpect(jsonPath("$.[0].timePeriod.endDate").value(DEFAULT_DATE))
                .andExpect(jsonPath("$.[0].residenceType").value(DEFAULT_RESIDENT.name()))
//                .andExpect(jsonPath("$.[0].nationalCode").value(DEFAULT_NATIONAL_CODE))
//                .andExpect(jsonPath("$.[0].registrationType").value(DEFAULT_REGISTRATION.name()))
                .andExpect(jsonPath("$.[0].characteristicId").isNotEmpty());
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of person")
    public void searchResultListById() throws Exception {
        Person entity = service.save(dto);

        mockMvc.perform(get("/api/v1/person/search?id=" + entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
//                .andExpect(jsonPath("$.[0].name").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].accommodationType").value(DEFAULT_ACCOMMODATION.name()))
//                .andExpect(jsonPath("$.[0].gender").value(DEFAULT_GENDER))
//                .andExpect(jsonPath("$.[0].timePeriod.startDate").value(DEFAULT_DATE))
//                .andExpect(jsonPath("$.[0].timePeriod.endDate").value(DEFAULT_DATE))
                .andExpect(jsonPath("$.[0].residenceType").value(DEFAULT_RESIDENT.name()))
//                .andExpect(jsonPath("$.[0].nationalCode").value(DEFAULT_NATIONAL_CODE))
//                .andExpect(jsonPath("$.[0].registrationType").value(DEFAULT_REGISTRATION.name()))
                .andExpect(jsonPath("$.[0].characteristicId").isNotEmpty());
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of person")
    public void searchResultListByNationalCode() throws Exception {
        Person entity = service.save(dto);

        mockMvc.perform(get("/api/v1/person/search?nationalCode=" + DEFAULT_NATIONAL_CODE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
//                .andExpect(jsonPath("$.[0].name").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].accommodationType").value(DEFAULT_ACCOMMODATION.name()))
//                .andExpect(jsonPath("$.[0].gender").value(DEFAULT_GENDER))
//                .andExpect(jsonPath("$.[0].timePeriod.startDate").value(DEFAULT_DATE))
//                .andExpect(jsonPath("$.[0].timePeriod.endDate").value(DEFAULT_DATE))
                .andExpect(jsonPath("$.[0].residenceType").value(DEFAULT_RESIDENT.name()))
//                .andExpect(jsonPath("$.[0].nationalCode").value(DEFAULT_NATIONAL_CODE))
//                .andExpect(jsonPath("$.[0].registrationType").value(DEFAULT_REGISTRATION.name()))
                .andExpect(jsonPath("$.[0].characteristicId").isNotEmpty());
    }
}
