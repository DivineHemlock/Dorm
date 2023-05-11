package com.airbyte.dorm.notification;

import com.airbyte.dorm.DormApplication;
import com.airbyte.dorm.dto.NotificationDTO;
import com.airbyte.dorm.model.Notification;
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
public class NotificationControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private NotificationService service;
    @Autowired
    private NotificationDataProvider dataProvider;
    private NotificationDTO dto;

    @BeforeEach
    public void initTest() {
        dto = dataProvider.createDTO();
    }

    @Test
    @Transactional
    @DisplayName("REST Request to save notification")
    public void saveNotification() throws Exception {
        int databaseSizeBeforeSave = service.getAll().size();

        mockMvc.perform(post("/api/v1/notification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        Notification entity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(service.getAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(entity.getId()).isNotBlank();
        assertThat(entity.getEventName()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getEventDescription()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    @DisplayName("REST Request to save notification By Id")
    public void getNotificationById() throws Exception {
        Notification entity = service.save(dto);

        mockMvc.perform(get("/api/v1/notification/{id}", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(entity.getId()))
                .andExpect(jsonPath("$.eventName").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.eventDescription").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.date").value(DEFAULT_DATE));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get notifications")
    public void getInventories() throws Exception {
        Notification entity = service.save(dto);

        mockMvc.perform(get("/api/v1/notification"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].eventName").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].eventDescription").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].date").value(DEFAULT_DATE));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to update notification")
    public void updateInventory() throws Exception {
        int databaseSizeBeforeSave = service.getAll().size();
        Notification entity = service.save(dto);

        dto = dataProvider.updateDTO();

        mockMvc.perform(patch("/api/v1/notification/{id}", entity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isAccepted());

        Notification newEntity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(service.getAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(newEntity.getId()).isNotBlank();
        assertThat(entity.getEventName()).isEqualTo(UPDATED_STRING);
        assertThat(entity.getEventDescription()).isEqualTo(UPDATED_STRING);
        assertThat(entity.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    @DisplayName("REST Request to delete notification")
    public void deleteNotification() throws Exception {
        Notification entity = service.save(dto);
        int databaseSizeAfterSave = service.getAll().size();

        mockMvc.perform(delete("/api/v1/notification/{id}", entity.getId()))
                .andExpect(status().isNoContent());

        assertThat(service.getAll()).hasSize(databaseSizeAfterSave - 1);
    }


}
