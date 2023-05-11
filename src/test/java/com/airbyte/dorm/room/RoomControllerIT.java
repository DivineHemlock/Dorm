package com.airbyte.dorm.room;

import com.airbyte.dorm.DormApplication;
import com.airbyte.dorm.dto.RoomDTO;
import com.airbyte.dorm.dto.RoomRequestDTO;
import com.airbyte.dorm.model.Room;
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
import static com.airbyte.dorm.CommonTestData.DEFAULT_BIG_DECIMAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {DormApplication.class})
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RoomControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RoomService service;
    @Autowired
    private RoomDataProvider dataProvider;
    private RoomDTO dto;

    @BeforeEach
    public void initTest() {
        dto = dataProvider.createDTO();
    }

    @Test
    @Transactional
    @DisplayName("REST Request to save room")
    public void saveRoom() throws Exception {
        int databaseSizeBeforeSave = service.getAll().size();

        mockMvc.perform(post("/api/v1/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        Room entity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(service.getAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(entity.getId()).isNotBlank();
        assertThat(entity.getEmpty()).isEqualTo(DEFAULT_BOOLEAN);
        assertThat(entity.getNumber()).isEqualTo(DEFAULT_INTEGER);
        assertThat(entity.getUnitId()).isNotBlank();
        assertThat(entity.getUnit().getEmpty()).isEqualTo(DEFAULT_BOOLEAN);
        assertThat(entity.getUnit().getNumber()).isEqualTo(DEFAULT_INTEGER);
        entity.getAccessories().forEach(
                accessory -> {
                    assertThat(accessory.getId()).isNotBlank();
                    assertThat(accessory.getDescription()).isEqualTo(DEFAULT_STRING);
                    assertThat(accessory.getName()).isEqualTo(DEFAULT_STRING);
                    assertThat(accessory.getCount()).isEqualTo(DEFAULT_BIG_DECIMAL);
                    assertThat(accessory.getRoomId()).isEqualTo(entity.getId());
                }
        );
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get room By Id")
    public void getRoomById() throws Exception {
        Room entity = service.save(dto);

        mockMvc.perform(get("/api/v1/room/{id}", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(entity.getId()))
                .andExpect(jsonPath("$.empty").value(DEFAULT_BOOLEAN))
                .andExpect(jsonPath("$.number").value(DEFAULT_INTEGER))
                .andExpect(jsonPath("$.unit").isNotEmpty())
                .andExpect(jsonPath("$.description").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.accessories.[0].name").value(DEFAULT_STRING));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get rooms")
    public void getRooms() throws Exception {
        Room entity = service.save(dto);

        mockMvc.perform(get("/api/v1/room"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].empty").value(DEFAULT_BOOLEAN))
                .andExpect(jsonPath("$.[0].number").value(DEFAULT_INTEGER))
                .andExpect(jsonPath("$.[0].unit").isNotEmpty())
                .andExpect(jsonPath("$.[0].description").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].accessories.[0].name").value(DEFAULT_STRING));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to update room")
    public void updateRoom() throws Exception {
        int databaseSizeBeforeSave = service.getAll().size();
        Room entity = service.save(dto);

        dto = dataProvider.updateDTO();

        mockMvc.perform(patch("/api/v1/room/{id}", entity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isAccepted());

        Room newEntity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(service.getAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(newEntity.getId()).isNotBlank();
        assertThat(newEntity.getEmpty()).isEqualTo(UPDATED_BOOLEAN);
        assertThat(newEntity.getNumber()).isEqualTo(UPDATED_INTEGER);
        assertThat(newEntity.getUnitId()).isNotBlank();
        assertThat(newEntity.getUnit().getEmpty()).isEqualTo(UPDATED_BOOLEAN);
        assertThat(newEntity.getUnit().getNumber()).isEqualTo(UPDATED_INTEGER);
        entity.getAccessories()
                .stream()
                .filter(accessory -> UPDATED_STRING.equals(accessory.getName()))
                .forEach(
                        accessory -> {
                            assertThat(accessory.getId()).isNotBlank();
                            assertThat(accessory.getDescription()).isEqualTo(UPDATED_STRING);
                            assertThat(accessory.getName()).isEqualTo(UPDATED_STRING);
                            assertThat(accessory.getCount()).isEqualTo(UPDATED_BIG_DECIMAL);
                            assertThat(accessory.getRoomId()).isEqualTo(entity.getId());
                        }
                );
    }

    @Test
    @Transactional
    @DisplayName("REST Request to delete room")
    public void deleteRoom() throws Exception {
        Room entity = service.save(dto);
        int databaseSizeAfterSave = service.getAll().size();

        mockMvc.perform(delete("/api/v1/room/{id}", entity.getId()))
                .andExpect(status().isNoContent());

        assertThat(service.getAll()).hasSize(databaseSizeAfterSave - 1);
    }

    @Test
    @Transactional
    @DisplayName("REST Request to empty size of room")
    public void emptySize() throws Exception {
        Room entity = service.save(dto);

        mockMvc.perform(get("/api/v1/room/empty/size", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to empty list of room")
    public void emptyList() throws Exception {
        Room entity = service.save(dto);

        mockMvc.perform(get("/api/v1/room/empty/list", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of room")
    public void searchResultListByNumber() throws Exception {
        Room entity = service.save(dto);

        mockMvc.perform(get("/api/v1/room/search?number=" + DEFAULT_INTEGER, entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].number").value(entity.getNumber()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of room")
    public void searchResultListById() throws Exception {
        Room entity = service.save(dto);

        mockMvc.perform(get("/api/v1/room/search?id=" + entity.getId(), entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].number").value(entity.getNumber()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of room")
    public void searchResultListByEmpty() throws Exception {
        Room entity = service.save(dto);

        mockMvc.perform(get("/api/v1/room/search?empty=" + true, entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].number").value(entity.getNumber()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of room")
    public void searchResultListByIdAndSortById() throws Exception {
        Room entity = service.save(dto);

        mockMvc.perform(get("/api/v1/room/search?id=" + entity.getId() + "&sort=id", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].number").value(entity.getNumber()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of room")
    public void searchResultListByIdAndSortByRoomId() throws Exception {
        Room entity = service.save(dto);

        mockMvc.perform(get("/api/v1/room/search?unitId=" + entity.getUnitId(), entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].number").value(entity.getNumber()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get rooms with description")
    public void searchResultListByUnit() throws Exception {
        Room entity = service.save(dto);

        mockMvc.perform(get("/api/v1/room/search?description=" + entity.getDescription() + "&sort=description"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].number").value(entity.getNumber()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of room")
    public void searchResultListByIdAndSortByNumber() throws Exception {
        Room entity = service.save(dto);

        mockMvc.perform(get("/api/v1/room/search?id=" + entity.getId() + "&sort=number", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].number").value(entity.getNumber()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of room")
    public void searchResultListByIdAndSortByEmpty() throws Exception {
        Room entity = service.save(dto);

        mockMvc.perform(get("/api/v1/room/search?id=" + entity.getId() + "&sort=empty", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].number").value(entity.getNumber()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of floor")
    public void searchResultListByNameAndSortByEmpty() throws Exception {
        Room entity = service.save(dto);

        mockMvc.perform(get("/api/v1/room/search?number=" + DEFAULT_INTEGER + "&sort=id,number", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].number").value(entity.getNumber()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get specific room for unit")
    public void specificUnitsForFloor() throws Exception {
        Room entity = service.save(dto);

        mockMvc.perform(get("/api/v1/room/specific/{unitId}", entity.getUnitId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].number").value(entity.getNumber()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get concat name")
    public void concatName() throws Exception {
        Room entity = service.save(dto);

        mockMvc.perform(get("/api/v1/room/concat/show"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].concatName").value(entity.getConcatName()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get accessories")
    public void getAccessories() throws Exception {
        Room entity = service.save(dto);

        mockMvc.perform(get("/api/v1/room/{id}/accessory", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getAccessories().get(0).getId()))
                .andExpect(jsonPath("$.[0].name").value(entity.getAccessories().get(0).getName()))
                .andExpect(jsonPath("$.[0].count").value(entity.getAccessories().get(0).getCount().longValue()))
                .andExpect(jsonPath("$.[0].description").value(entity.getAccessories().get(0).getDescription()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get accessories")
    public void getAccessoriesNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/room/{id}/accessory", "A"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @DisplayName("REST Request to save room and their units")
    public void bulkSave() throws Exception {
        RoomRequestDTO dto1 = dataProvider.prepareDTO();
        int databaseSizeBeforeSave = service.getAll().size();

        mockMvc.perform(post("/api/v1/room/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto1)))
                .andExpect(status().isCreated());

        Room entity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(entity.getId()).isNotBlank();
        assertThat(entity.getEmpty()).isEqualTo(DEFAULT_BOOLEAN);
        assertThat(entity.getNumber()).isEqualTo(DEFAULT_INTEGER);
        assertThat(entity.getUnitId()).isNotBlank();
        assertThat(entity.getUnit().getEmpty()).isEqualTo(DEFAULT_BOOLEAN);
        assertThat(entity.getUnit().getNumber()).isEqualTo(DEFAULT_INTEGER);
        entity.getAccessories().forEach(
                accessory -> {
                    assertThat(accessory.getId()).isNotBlank();
                    assertThat(accessory.getDescription()).isEqualTo(DEFAULT_STRING);
                    assertThat(accessory.getName()).isEqualTo(DEFAULT_STRING);
                    assertThat(accessory.getCount()).isEqualTo(DEFAULT_BIG_DECIMAL);
                    assertThat(accessory.getRoomId()).isEqualTo(entity.getId());
                }
        );
    }

}
