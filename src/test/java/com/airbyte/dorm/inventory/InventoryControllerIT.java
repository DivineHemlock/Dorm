package com.airbyte.dorm.inventory;

import com.airbyte.dorm.DormApplication;
import com.airbyte.dorm.dto.InventoryDTO;
import com.airbyte.dorm.model.Inventory;
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
public class InventoryControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private InventoryService service;
    @Autowired
    private InventoryDataProvider dataProvider;
    private InventoryDTO dto;

    @BeforeEach
    public void initTest() {
        dto = dataProvider.createDTO();
    }

    @Test
    @Transactional
    @DisplayName("REST Request to save inventory")
    public void saveInventory() throws Exception {
        int databaseSizeBeforeSave = service.getAll().size();

        mockMvc.perform(post("/api/v1/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        Inventory entity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(service.getAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(entity.getId()).isNotBlank();
        assertThat(entity.getAccessoryType()).isEqualTo(DEFAULT_ACCESSORY_TYPE);
        assertThat(entity.getCategory()).isEqualTo(DEFAULT_CATEGORY_NAME);
        entity.getAccessories().forEach(
                accessory -> {
                    assertThat(accessory.getId()).isNotBlank();
                    assertThat(accessory.getDescription()).isEqualTo(DEFAULT_STRING);
                    assertThat(accessory.getName()).isEqualTo(DEFAULT_STRING);
                    assertThat(accessory.getCount()).isEqualTo(DEFAULT_BIG_DECIMAL);
                    assertThat(accessory.getInventoryId()).isEqualTo(entity.getId());
                }
        );
    }

    @Test
    @Transactional
    @DisplayName("REST Request to save inventory By Id")
    public void getInventoryById() throws Exception {
        Inventory entity = service.save(dto);

        mockMvc.perform(get("/api/v1/inventory/{id}", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(entity.getId()))
                .andExpect(jsonPath("$.accessoryType").value(DEFAULT_ACCESSORY_TYPE.name()))
                .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY_NAME))
                .andExpect(jsonPath("$.accessories.[0].name").value(DEFAULT_STRING));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get inventories")
    public void getInventories() throws Exception {
        Inventory entity = service.save(dto);

        mockMvc.perform(get("/api/v1/inventory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].accessoryType").value(DEFAULT_ACCESSORY_TYPE.name()))
                .andExpect(jsonPath("$.[0].category").value(DEFAULT_CATEGORY_NAME))
                .andExpect(jsonPath("$.[0].accessories.[0].name").value(DEFAULT_STRING));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to update inventory")
    public void updateInventory() throws Exception {
        int databaseSizeBeforeSave = service.getAll().size();
        Inventory entity = service.save(dto);

        dto = dataProvider.updateDTO();

        mockMvc.perform(patch("/api/v1/inventory/{id}", entity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isAccepted());

        Inventory newEntity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(service.getAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(newEntity.getId()).isNotBlank();
        assertThat(entity.getAccessoryType()).isEqualTo(UPDATED_ACCESSORY_TYPE);
        assertThat(entity.getCategory()).isEqualTo(UPDATED_CATEGORY_NAME);
        entity.getAccessories()
                .stream()
                .filter(accessory -> UPDATED_STRING.equals(accessory.getName()))
                .forEach(
                        accessory -> {
                            assertThat(accessory.getId()).isNotBlank();
                            assertThat(accessory.getDescription()).isEqualTo(UPDATED_STRING);
                            assertThat(accessory.getName()).isEqualTo(UPDATED_STRING);
                            assertThat(accessory.getCount()).isEqualTo(UPDATED_BIG_DECIMAL);
                            assertThat(accessory.getInventoryId()).isEqualTo(entity.getId());
                        }
                );
    }

    @Test
    @Transactional
    @DisplayName("REST Request to delete inventory")
    public void deleteInventory() throws Exception {
        Inventory entity = service.save(dto);
        int databaseSizeAfterSave = service.getAll().size();

        mockMvc.perform(delete("/api/v1/inventory/{id}", entity.getId()))
                .andExpect(status().isNoContent());

        assertThat(service.getAll()).hasSize(databaseSizeAfterSave - 1);
    }
}
