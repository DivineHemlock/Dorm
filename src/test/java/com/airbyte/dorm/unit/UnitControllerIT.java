package com.airbyte.dorm.unit;

import com.airbyte.dorm.DormApplication;
import com.airbyte.dorm.dto.UnitDTO;
import com.airbyte.dorm.model.Unit;
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
public class UnitControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UnitService service;
    @Autowired
    private UnitDataProvider dataProvider;
    private UnitDTO dto;

    @BeforeEach
    public void initTest() {
        dto = dataProvider.createDTO();
    }

    @Test
    @Transactional
    @DisplayName("REST Request to save unit")
    public void saveUnit() throws Exception {
        int databaseSizeBeforeSave = service.getAll().size();

        mockMvc.perform(post("/api/v1/unit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        Unit entity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(service.getAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(entity.getId()).isNotBlank();
        assertThat(entity.getEmpty()).isEqualTo(DEFAULT_BOOLEAN);
        assertThat(entity.getNumber()).isEqualTo(DEFAULT_INTEGER);
        assertThat(entity.getFloorId()).isNotBlank();
        assertThat(entity.getFloor().getEmpty()).isEqualTo(DEFAULT_BOOLEAN);
        assertThat(entity.getFloor().getName()).isEqualTo(DEFAULT_STRING);
        entity.getAccessories().forEach(
                accessory -> {
                    assertThat(accessory.getId()).isNotBlank();
                    assertThat(accessory.getDescription()).isEqualTo(DEFAULT_STRING);
                    assertThat(accessory.getName()).isEqualTo(DEFAULT_STRING);
                    assertThat(accessory.getCount()).isEqualTo(DEFAULT_BIG_DECIMAL);
                    assertThat(accessory.getUnitId()).isEqualTo(entity.getId());
                }
        );
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get unit By Id")
    public void getUnitById() throws Exception {
        Unit entity = service.save(dto);

        mockMvc.perform(get("/api/v1/unit/{id}", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(entity.getId()))
                .andExpect(jsonPath("$.empty").value(DEFAULT_BOOLEAN))
                .andExpect(jsonPath("$.number").value(DEFAULT_INTEGER))
                .andExpect(jsonPath("$.floor").isNotEmpty())
                .andExpect(jsonPath("$.accessories.[0].name").value(DEFAULT_STRING));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get units")
    public void getUnits() throws Exception {
        Unit entity = service.save(dto);

        mockMvc.perform(get("/api/v1/unit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].empty").value(DEFAULT_BOOLEAN))
                .andExpect(jsonPath("$.[0].number").value(DEFAULT_INTEGER))
                .andExpect(jsonPath("$.[0].floor").isNotEmpty())
                .andExpect(jsonPath("$.[0].accessories.[0].name").value(DEFAULT_STRING));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to update unit")
    public void updateUnit() throws Exception {
        int databaseSizeBeforeSave = service.getAll().size();
        Unit entity = service.save(dto);

        dto = dataProvider.updateDTO();

        mockMvc.perform(patch("/api/v1/unit/{id}", entity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isAccepted());

        Unit newEntity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(service.getAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(newEntity.getId()).isNotBlank();
        assertThat(newEntity.getEmpty()).isEqualTo(UPDATED_BOOLEAN);
        assertThat(newEntity.getNumber()).isEqualTo(UPDATED_INTEGER);
        assertThat(newEntity.getFloorId()).isNotBlank();
        assertThat(newEntity.getFloor().getEmpty()).isEqualTo(UPDATED_BOOLEAN);
        assertThat(newEntity.getFloor().getName()).isEqualTo(UPDATED_STRING);
        entity.getAccessories()
                .stream()
                .filter(accessory -> UPDATED_STRING.equals(accessory.getName()))
                .forEach(
                        accessory -> {
                            assertThat(accessory.getId()).isNotBlank();
                            assertThat(accessory.getDescription()).isEqualTo(UPDATED_STRING);
                            assertThat(accessory.getName()).isEqualTo(UPDATED_STRING);
                            assertThat(accessory.getCount()).isEqualTo(UPDATED_BIG_DECIMAL);
                            assertThat(accessory.getUnitId()).isEqualTo(entity.getId());
                        }
                );
    }

    @Test
    @Transactional
    @DisplayName("REST Request to delete unit")
    public void deleteFloor() throws Exception {
        Unit entity = service.save(dto);
        int databaseSizeAfterSave = service.getAll().size();


        mockMvc.perform(delete("/api/v1/unit/{id}", entity.getId()))
                .andExpect(status().isNoContent());

        assertThat(service.getAll()).hasSize(databaseSizeAfterSave - 1);
    }

    @Test
    @Transactional
    @DisplayName("REST Request to empty size of unit")
    public void emptySize() throws Exception {
        Unit entity = service.save(dto);

        mockMvc.perform(get("/api/v1/unit/empty/size", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to empty list of unit")
    public void emptyList() throws Exception {
        Unit entity = service.save(dto);

        mockMvc.perform(get("/api/v1/unit/empty/list", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of unit")
    public void searchResultListByNumber() throws Exception {
        Unit entity = service.save(dto);

        mockMvc.perform(get("/api/v1/unit/search?number=" + DEFAULT_INTEGER, entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].number").value(entity.getNumber()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of unit")
    public void searchResultListById() throws Exception {
        Unit entity = service.save(dto);

        mockMvc.perform(get("/api/v1/unit/search?id=" + entity.getId(), entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].number").value(entity.getNumber()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of unit")
    public void searchResultListByEmpty() throws Exception {
        Unit entity = service.save(dto);

        mockMvc.perform(get("/api/v1/unit/search?empty=" + true, entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].number").value(entity.getNumber()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of unit")
    public void searchResultListByIdAndSortById() throws Exception {
        Unit entity = service.save(dto);

        mockMvc.perform(get("/api/v1/unit/search?id=" + entity.getId() + "&sort=id", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].number").value(entity.getNumber()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of unit")
    public void searchResultListByIdAndSortByFloorId() throws Exception {
        Unit entity = service.save(dto);

        mockMvc.perform(get("/api/v1/unit/search?floorId=" + entity.getFloorId(), entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].number").value(entity.getNumber()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of unit")
    public void searchResultListByIdAndSortByNumber() throws Exception {
        Unit entity = service.save(dto);

        mockMvc.perform(get("/api/v1/unit/search?id=" + entity.getId() + "&sort=number", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].number").value(entity.getNumber()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of unit")
    public void searchResultListByIdAndSortByEmpty() throws Exception {
        Unit entity = service.save(dto);

        mockMvc.perform(get("/api/v1/unit/search?id=" + entity.getId() + "&sort=empty", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].number").value(entity.getNumber()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of floor")
    public void searchResultListByNameAndSortByEmpty() throws Exception {
        Unit entity = service.save(dto);

        mockMvc.perform(get("/api/v1/unit/search?number=" + DEFAULT_INTEGER + "&sort=id,number", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].number").value(entity.getNumber()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get specific units for floor")
    public void specificUnitsForFloor() throws Exception {
        Unit entity = service.save(dto);

        mockMvc.perform(get("/api/v1/unit/specific/{floorId}", entity.getFloorId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].number").value(entity.getNumber()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get specific units for floor")
    public void getRoomsByUnitId() throws Exception {
        Unit entity = service.save(dto);

        mockMvc.perform(get("/api/v1/unit/room/{unitId}", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

}
