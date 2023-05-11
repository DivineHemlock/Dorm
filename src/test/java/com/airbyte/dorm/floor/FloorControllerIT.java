package com.airbyte.dorm.floor;

import com.airbyte.dorm.DormApplication;
import com.airbyte.dorm.dto.FloorDTO;
import com.airbyte.dorm.dto.FloorRequestDTO;
import com.airbyte.dorm.model.Floor;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = {DormApplication.class})
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FloorControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private FloorService service;
    @Autowired
    private FloorDataProvider dataProvider;
    private FloorDTO dto;

    @BeforeEach
    public void initTest() {
        dto = dataProvider.createDTO();
    }

    @Test
    @Transactional
    @DisplayName("REST Request to save floor")
    public void saveFloor() throws Exception {
        int databaseSizeBeforeSave = service.getAll().size();

        mockMvc.perform(post("/api/v1/floor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        Floor entity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(service.getAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(entity.getId()).isNotBlank();
        assertThat(entity.getEmpty()).isEqualTo(DEFAULT_BOOLEAN);
        assertThat(entity.getName()).isEqualTo(DEFAULT_STRING);
        entity.getAccessories().forEach(
                accessory -> {
                    assertThat(accessory.getId()).isNotBlank();
                    assertThat(accessory.getDescription()).isEqualTo(DEFAULT_STRING);
                    assertThat(accessory.getName()).isEqualTo(DEFAULT_STRING);
                    assertThat(accessory.getCount()).isEqualTo(DEFAULT_BIG_DECIMAL);
                    assertThat(accessory.getFloorId()).isEqualTo(entity.getId());
                }
        );
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get floor By Id")
    public void getFloorById() throws Exception {
        Floor entity = service.save(dto);

        mockMvc.perform(get("/api/v1/floor/{id}", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(entity.getId()))
                .andExpect(jsonPath("$.empty").value(DEFAULT_BOOLEAN))
                .andExpect(jsonPath("$.name").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.accessories.[0].name").value(DEFAULT_STRING));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get floors")
    public void getFloors() throws Exception {
        Floor entity = service.save(dto);

        mockMvc.perform(get("/api/v1/floor"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].empty").value(DEFAULT_BOOLEAN))
                .andExpect(jsonPath("$.[0].name").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].accessories.[0].name").value(DEFAULT_STRING));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to update floor")
    public void updateFloor() throws Exception {
        int databaseSizeBeforeSave = service.getAll().size();
        Floor entity = service.save(dto);

        dto = dataProvider.updateDTO();

        mockMvc.perform(patch("/api/v1/floor/{id}", entity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isAccepted());

        Floor newEntity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(service.getAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(newEntity.getId()).isNotBlank();
        assertThat(newEntity.getEmpty()).isEqualTo(UPDATED_BOOLEAN);
        assertThat(newEntity.getName()).isEqualTo(UPDATED_STRING);
        entity.getAccessories()
                .stream()
                .filter(accessory -> UPDATED_STRING.equals(accessory.getName()))
                .forEach(
                accessory -> {
                    assertThat(accessory.getId()).isNotBlank();
                    assertThat(accessory.getDescription()).isEqualTo(UPDATED_STRING);
                    assertThat(accessory.getName()).isEqualTo(UPDATED_STRING);
                    assertThat(accessory.getCount()).isEqualTo(UPDATED_BIG_DECIMAL);
                    assertThat(accessory.getFloorId()).isEqualTo(entity.getId());
                }
        );
    }

    @Test
    @Transactional
    @DisplayName("REST Request to delete floor")
    public void deleteFloor() throws Exception {
        Floor entity = service.save(dto);
        int databaseSizeAfterSave = service.getAll().size();

        mockMvc.perform(delete("/api/v1/floor/{id}", entity.getId()))
                .andExpect(status().isNoContent());

        assertThat(service.getAll()).hasSize(databaseSizeAfterSave - 1);
    }

    @Test
    @Transactional
    @DisplayName("REST Request to empty size of floor")
    public void emptySize() throws Exception {
        Floor entity = service.save(dto);

        mockMvc.perform(get("/api/v1/floor/empty/size", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to empty list of floor")
    public void emptyList() throws Exception {
        Floor entity = service.save(dto);

        mockMvc.perform(get("/api/v1/floor/empty/list", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of floor")
    public void searchResultListByName() throws Exception {
        Floor entity = service.save(dto);

        mockMvc.perform(get("/api/v1/floor/search?name=" + DEFAULT_STRING, entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].name").value(entity.getName()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of floor")
    public void searchResultListById() throws Exception {
        Floor entity = service.save(dto);

        mockMvc.perform(get("/api/v1/floor/search?id=" + entity.getId(), entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].name").value(entity.getName()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of floor")
    public void searchResultListByEmpty() throws Exception {
        Floor entity = service.save(dto);

        mockMvc.perform(get("/api/v1/floor/search?empty=" + true, entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].name").value(entity.getName()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of floor")
    public void searchResultListByIdAndSortById() throws Exception {
        Floor entity = service.save(dto);

        mockMvc.perform(get("/api/v1/floor/search?id=" + entity.getId() + "&sort=id", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].name").value(entity.getName()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of floor")
    public void searchResultListByIdAndSortByName() throws Exception {
        Floor entity = service.save(dto);

        mockMvc.perform(get("/api/v1/floor/search?id=" + entity.getId() + "&sort=name", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].name").value(entity.getName()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of floor")
    public void searchResultListByIdAndSortByEmpty() throws Exception {
        Floor entity = service.save(dto);

        mockMvc.perform(get("/api/v1/floor/search?id=" + entity.getId() + "&sort=empty", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].name").value(entity.getName()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of floor")
    public void searchResultListByNameAndSortByEmpty() throws Exception {
        Floor entity = service.save(dto);

        mockMvc.perform(get("/api/v1/floor/search?name=" + DEFAULT_STRING + "&sort=id,name", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].name").value(entity.getName()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to save floors and their units")
    public void bulkSave() throws Exception {
        FloorRequestDTO floorRequestDTO = dataProvider.prepareDTO();
        int databaseSizeBeforeSave = service.getAll().size();

        mockMvc.perform(post("/api/v1/floor/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(floorRequestDTO)))
                .andExpect(status().isCreated());

        Floor entity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(entity.getName()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getEmpty()).isEqualTo(DEFAULT_BOOLEAN);
        entity.getUnits()
                .forEach(unit -> {
                    assertThat(unit.getEmpty()).isEqualTo(DEFAULT_BOOLEAN);
                    assertThat(unit.getFloorId()).isEqualTo(entity.getId());
                    assertThat(unit.getRooms()).hasSize(0);
                    assertThat(unit.getNumber()).isNotNull();
                    assertThat(unit.getAccessories().get(0).getCount()).isEqualTo(DEFAULT_BIG_DECIMAL);
                    assertThat(unit.getAccessories().get(0).getName()).isEqualTo(DEFAULT_STRING);
                    assertThat(unit.getAccessories().get(0).getDescription()).isEqualTo(DEFAULT_STRING);
                });
    }

}
