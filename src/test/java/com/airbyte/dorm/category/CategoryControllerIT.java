package com.airbyte.dorm.category;

import com.airbyte.dorm.DormApplication;
import com.airbyte.dorm.dto.CategoryDTO;
import com.airbyte.dorm.model.Category;
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
public class CategoryControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CategoryService service;
    @Autowired
    private CategoryDataProvider dataProvider;
    private CategoryDTO dto;

    @BeforeEach
    public void initTest() {
        dto = dataProvider.createDTO();
    }

    @Test
    @Transactional
    @DisplayName("REST Request to save category")
    public void saveCategory() throws Exception {
        int databaseSizeBeforeSave = service.getAll().size();

        mockMvc.perform(post("/api/v1/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        Category entity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(service.getAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(entity.getId()).isNotBlank();
        assertThat(entity.getName()).isEqualTo(DEFAULT_CATEGORY_NAME);
        assertThat(entity.getType()).isEqualTo(DEFAULT_STRING);
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get category By Id")
    public void getCategoryById() throws Exception {
        Category entity = service.save(dto);

        mockMvc.perform(get("/api/v1/category/{id}", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(entity.getId()))
                .andExpect(jsonPath("$.name").value(DEFAULT_CATEGORY_NAME))
                .andExpect(jsonPath("$.type").value(DEFAULT_STRING));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get categories")
    public void getCategories() throws Exception {
        Category entity = service.save(dto);

        mockMvc.perform(get("/api/v1/category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].name").value(DEFAULT_CATEGORY_NAME))
                .andExpect(jsonPath("$.[0].type").value(DEFAULT_STRING));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to update category")
    public void updateCategory() throws Exception {
        int databaseSizeBeforeSave = service.getAll().size();
        Category entity = service.save(dto);

        dto = dataProvider.updateDTO();

        mockMvc.perform(patch("/api/v1/category/{id}", entity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isAccepted());

        Category newEntity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(service.getAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(newEntity.getId()).isNotBlank();
        assertThat(newEntity.getName()).isEqualTo(UPDATED_CATEGORY_NAME);
        assertThat(newEntity.getType()).isEqualTo(UPDATED_STRING);
    }

    @Test
    @Transactional
    @DisplayName("REST Request to delete category")
    public void deleteCategory() throws Exception {
        Category entity = service.save(dto);
        int databaseSizeAfterSave = service.getAll().size();

        mockMvc.perform(delete("/api/v1/category/{id}", entity.getId()))
                .andExpect(status().isNoContent());

        assertThat(service.getAll()).hasSize(databaseSizeAfterSave - 1);
    }
}
