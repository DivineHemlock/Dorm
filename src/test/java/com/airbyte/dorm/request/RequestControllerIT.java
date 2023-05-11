package com.airbyte.dorm.request;

import com.airbyte.dorm.DormApplication;
import com.airbyte.dorm.dto.RequestDTO;
import com.airbyte.dorm.model.Request;
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
public class RequestControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RequestService service;
    @Autowired
    private RequestDataProvider dataProvider;
    private RequestDTO dto;

    @BeforeEach
    public void initTest() {
        dto = dataProvider.createDTO();
    }

    @Test
    @Transactional
    @DisplayName("REST Request to save request")
    public void saveRequest() throws Exception {
        int databaseSizeBeforeSave = service.getAll().size();

        mockMvc.perform(post("/api/v1/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        Request entity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(service.getAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(entity.getId()).isNotBlank();
        assertThat(entity.getDescription()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(entity.getChecked()).isEqualTo(DEFAULT_BOOLEAN);
        assertThat(entity.getType()).isEqualTo(DEFAULT_CATEGORY_NAME);
        assertThat(entity.getDateOfRegistration()).isEqualTo(DEFAULT_DATE);
        assertThat(entity.getReason()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getAssignee()).isEqualTo(DEFAULT_STRING);
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get request By Id")
    public void getRequestById() throws Exception {
        Request entity = service.save(dto);

        mockMvc.perform(get("/api/v1/request/{id}", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(entity.getId()))
                .andExpect(jsonPath("$.description").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.checked").value(DEFAULT_BOOLEAN))
                .andExpect(jsonPath("$.type").value(DEFAULT_CATEGORY_NAME))
                .andExpect(jsonPath("$.dateOfRegistration").value(DEFAULT_DATE))
                .andExpect(jsonPath("$.reason").value(DEFAULT_STRING));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get requests")
    public void getRequests() throws Exception {
        Request entity = service.save(dto);

        mockMvc.perform(get("/api/v1/request"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].description").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.[0].checked").value(DEFAULT_BOOLEAN))
                .andExpect(jsonPath("$.[0].type").value(DEFAULT_CATEGORY_NAME))
                .andExpect(jsonPath("$.[0].dateOfRegistration").value(DEFAULT_DATE))
                .andExpect(jsonPath("$.[0].reason").value(DEFAULT_STRING));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to update request")
    public void updateRequest() throws Exception {
        int databaseSizeBeforeSave = service.getAll().size();
        Request entity = service.save(dto);

        dto = dataProvider.updateDTO();

        mockMvc.perform(patch("/api/v1/request/{id}", entity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isAccepted());

        Request newEntity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(service.getAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(newEntity.getId()).isNotBlank();
        assertThat(entity.getDescription()).isEqualTo(UPDATED_STRING);
        assertThat(entity.getName()).isEqualTo(UPDATED_NAME);
        assertThat(entity.getChecked()).isEqualTo(UPDATED_BOOLEAN);
        assertThat(entity.getType()).isEqualTo(UPDATED_CATEGORY_NAME);
        assertThat(entity.getDateOfRegistration()).isEqualTo(UPDATED_DATE);
        assertThat(entity.getReason()).isEqualTo(UPDATED_STRING);
    }

    @Test
    @Transactional
    @DisplayName("REST Request to delete request")
    public void deleteRequest() throws Exception {
        Request entity = service.save(dto);
        int databaseSizeAfterSave = service.getAll().size();

        mockMvc.perform(delete("/api/v1/request/{id}", entity.getId()))
                .andExpect(status().isNoContent());

        assertThat(service.getAll()).hasSize(databaseSizeAfterSave - 1);
    }

}
