package com.airbyte.dorm.supervisor;

import com.airbyte.dorm.DormApplication;
import com.airbyte.dorm.dto.SupervisorDTO;
import com.airbyte.dorm.model.people.Supervisor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
public class SupervisorControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SupervisorService service;
    @Autowired
    private SupervisorDataProvider dataProvider;
    private SupervisorDTO dto;

    @BeforeEach
    public void initTest() {
        dto = dataProvider.createDTO();
    }

    @Test
    @Transactional
    @DisplayName("REST Request to save supervisor")
    @Disabled
    public void saveSupervisor() throws Exception {
        int databaseSizeBeforeSave = service.getAll().size();

        mockMvc.perform(post("/api/v1/supervisor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        Supervisor entity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(service.getAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(entity.getId()).isNotBlank();
        assertThat(entity.getFullName()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getUsername()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getPassword()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getProfileId()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getEmail()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getRole()).isEqualTo("SUPERVISOR");
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get supervisor By Id")
    public void getSupervisorById() throws Exception {
        Supervisor entity = service.save(dto);

        mockMvc.perform(get("/api/v1/supervisor/{id}", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(entity.getId()))
                .andExpect(jsonPath("$.fullName").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.profileId").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.username").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.password").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.role").value("SUPERVISOR"))
                .andExpect(jsonPath("$.email").value(DEFAULT_STRING));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get supervisors")
    public void getSupervisors() throws Exception {
        Supervisor entity = service.save(dto);

        mockMvc.perform(get("/api/v1/supervisor"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[1].id").value(entity.getId()))
                .andExpect(jsonPath("$.[1].fullName").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[1].profileId").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[1].username").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[1].password").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[1].role").value("SUPERVISOR"))
                .andExpect(jsonPath("$.[1].email").value(DEFAULT_STRING));

    }

    @Test
    @Transactional
    @DisplayName("REST Request to update supervisor")
    public void updateSupervisor() throws Exception {
        int databaseSizeBeforeSave = service.getAll().size();
        Supervisor entity = service.save(dto);

        dto = dataProvider.updateDTO();

        mockMvc.perform(patch("/api/v1/supervisor/{id}", entity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isAccepted());

        Supervisor newEntity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(service.getAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(newEntity.getId()).isNotBlank();
        assertThat(entity.getFullName()).isEqualTo(UPDATED_STRING);
        assertThat(entity.getUsername()).isEqualTo(UPDATED_STRING);
        assertThat(entity.getPassword()).isEqualTo(UPDATED_STRING);
        assertThat(entity.getProfileId()).isEqualTo(UPDATED_STRING);
        assertThat(entity.getEmail()).isEqualTo(UPDATED_STRING);
        assertThat(entity.getRole()).isEqualTo("SUPERVISOR");

    }

    @Test
    @Transactional
    @DisplayName("REST Request to delete supervisor")
    public void deleteSupervisor() throws Exception {
        Supervisor entity = service.save(dto);
        int databaseSizeAfterSave = service.getAll().size();


        mockMvc.perform(delete("/api/v1/supervisor/{id}", entity.getId()))
                .andExpect(status().isNoContent());

        assertThat(service.getAll()).hasSize(databaseSizeAfterSave - 1);
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of supervisor")
    public void searchResultListByName() throws Exception {
        Supervisor entity = service.save(dto);

        mockMvc.perform(get("/api/v1/supervisor/search?name=" + DEFAULT_STRING, entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[1].id").value(entity.getId()))
                .andExpect(jsonPath("$.[1].fullName").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[1].profileId").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[1].username").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[1].password").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[1].role").value("SUPERVISOR"))
                .andExpect(jsonPath("$.[1].email").value(DEFAULT_STRING));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of supervisor by id")
    public void searchResultListById() throws Exception {
        Supervisor entity = service.save(dto);

        mockMvc.perform(get("/api/v1/supervisor/search?id=" + entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].fullName").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].profileId").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].username").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].password").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].role").value("SUPERVISOR"))
                .andExpect(jsonPath("$.[0].email").value(DEFAULT_STRING));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of supervisor by username")
    public void searchResultListByNationalCode() throws Exception {
        Supervisor entity = service.save(dto);

        mockMvc.perform(get("/api/v1/supervisor/search?username=" + DEFAULT_STRING))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].fullName").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].profileId").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].username").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].password").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].role").value("SUPERVISOR"))
                .andExpect(jsonPath("$.[0].email").value(DEFAULT_STRING));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of supervisor by email")
    public void searchResultListByEmail() throws Exception {
        Supervisor entity = service.save(dto);

        mockMvc.perform(get("/api/v1/supervisor/search?email=" + DEFAULT_STRING))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].fullName").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].profileId").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].username").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].password").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].role").value("SUPERVISOR"))
                .andExpect(jsonPath("$.[0].email").value(DEFAULT_STRING));
    }

}
