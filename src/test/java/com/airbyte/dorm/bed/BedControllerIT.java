package com.airbyte.dorm.bed;

import com.airbyte.dorm.DormApplication;
import com.airbyte.dorm.dto.BedDTO;
import com.airbyte.dorm.model.Bed;
import com.airbyte.dorm.model.Floor;
import com.airbyte.dorm.model.Room;
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
import static com.airbyte.dorm.CommonTestData.DEFAULT_BIG_DECIMAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {DormApplication.class})
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BedControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BedService service;
    @Autowired
    private BedDataProvider dataProvider;
    private BedDTO dto;

    @BeforeEach
    public void initTest() {
        dto = dataProvider.createDTO();
    }

    @Test
    @Transactional
    @DisplayName("REST Request to save bed")
    public void saveRoom() throws Exception {
        int databaseSizeBeforeSave = service.getAll().size();

        mockMvc.perform(post("/api/v1/bed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        Bed entity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(service.getAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(entity.getId()).isNotBlank();
        assertThat(entity.getEmpty()).isEqualTo(DEFAULT_BOOLEAN);
        assertThat(entity.getName()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getRoomId()).isNotBlank();
        assertThat(entity.getRoom()).isNotNull();
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get bed By Id")
    public void getBedById() throws Exception {
        Bed entity = service.save(dto);

        mockMvc.perform(get("/api/v1/bed/{id}", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(entity.getId()))
                .andExpect(jsonPath("$.empty").value(DEFAULT_BOOLEAN))
                .andExpect(jsonPath("$.name").value(DEFAULT_STRING));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get beds")
    public void getBeds() throws Exception {
        Bed entity = service.save(dto);

        mockMvc.perform(get("/api/v1/bed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].empty").value(DEFAULT_BOOLEAN))
                .andExpect(jsonPath("$.[0].name").value(DEFAULT_STRING));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to update bed")
    public void updateBed() throws Exception {
        int databaseSizeBeforeSave = service.getAll().size();
        Bed entity = service.save(dto);

        dto = dataProvider.updateDTO();

        mockMvc.perform(patch("/api/v1/bed/{id}", entity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isAccepted());

        Bed newEntity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(service.getAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(newEntity.getId()).isNotBlank();
        assertThat(newEntity.getEmpty()).isEqualTo(UPDATED_BOOLEAN);
        assertThat(newEntity.getName()).isEqualTo(UPDATED_STRING);
    }

    @Test
    @Transactional
    @DisplayName("REST Request to delete bed")
    public void deleteBed() throws Exception {
        Bed entity = service.save(dto);
        int databaseSizeAfterSave = service.getAll().size();

        mockMvc.perform(delete("/api/v1/bed/{id}", entity.getId()))
                .andExpect(status().isNoContent());

        assertThat(service.getAll()).hasSize(databaseSizeAfterSave - 1);
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get beds with specific bedId")
    public void getBedsForSpecificRoom() throws Exception {
        Bed entity = service.save(dto);

        mockMvc.perform(get("/api/v1/bed/specific/{roomId}", entity.getRoomId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].empty").value(DEFAULT_BOOLEAN))
                .andExpect(jsonPath("$.[0].name").value(DEFAULT_STRING));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of bed")
    public void searchResultListByName() throws Exception {
        Bed entity = service.save(dto);

        mockMvc.perform(get("/api/v1/bed/search?name=" + DEFAULT_STRING, entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].empty").value(DEFAULT_BOOLEAN))
                .andExpect(jsonPath("$.[0].name").value(entity.getName()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of bed")
    public void searchResultListById() throws Exception {
        Bed entity = service.save(dto);

        mockMvc.perform(get("/api/v1/bed/search?id=" + entity.getId(), entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].empty").value(DEFAULT_BOOLEAN))
                .andExpect(jsonPath("$.[0].name").value(entity.getName()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of bed")
    public void searchResultListByEmpty() throws Exception {
        Bed entity = service.save(dto);

        mockMvc.perform(get("/api/v1/bed/search?empty=" + true, entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].empty").value(DEFAULT_BOOLEAN))
                .andExpect(jsonPath("$.[0].name").value(entity.getName()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of bed")
    public void searchResultListByIdAndSortById() throws Exception {
        Bed entity = service.save(dto);

        mockMvc.perform(get("/api/v1/bed/search?id=" + entity.getId() + "&sort=id", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].empty").value(DEFAULT_BOOLEAN))
                .andExpect(jsonPath("$.[0].name").value(entity.getName()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of bed")
    public void searchResultListByIdAndSortByName() throws Exception {
        Bed entity = service.save(dto);

        mockMvc.perform(get("/api/v1/bed/search?id=" + entity.getId() + "&sort=name", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].empty").value(DEFAULT_BOOLEAN))
                .andExpect(jsonPath("$.[0].name").value(entity.getName()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of bed")
    public void searchResultListByIdAndSortByEmpty() throws Exception {
        Bed entity = service.save(dto);

        mockMvc.perform(get("/api/v1/bed/search?id=" + entity.getId() + "&sort=empty", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].empty").value(DEFAULT_BOOLEAN))
                .andExpect(jsonPath("$.[0].name").value(entity.getName()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of bed")
    public void searchResultListByRoomId() throws Exception {
        Bed entity = service.save(dto);

        mockMvc.perform(get("/api/v1/bed/search?roomId=" + entity.getRoomId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].empty").value(DEFAULT_BOOLEAN))
                .andExpect(jsonPath("$.[0].name").value(entity.getName()));
    }

}
