package com.airbyte.dorm.phonebook;

import com.airbyte.dorm.DormApplication;
import com.airbyte.dorm.dto.PhoneBookDTO;
import com.airbyte.dorm.model.PhoneBook;
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
public class PhoneBookControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PhoneBookService service;
    @Autowired
    private PhoneBookDataProvider dataProvider;
    private PhoneBookDTO dto;

    @BeforeEach
    public void initTest() {
        dto = dataProvider.createDTO();
    }

    @Test
    @Transactional
    @DisplayName("REST Request to save phoneBook")
    public void savePhoneBook() throws Exception {
        int databaseSizeBeforeSave = service.getAll().size();

        mockMvc.perform(post("/api/v1/phoneBook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        PhoneBook entity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(service.getAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(entity.getId()).isNotBlank();
        assertThat(entity.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(entity.getTelephoneNumbers()).hasSize(1);
        assertThat(entity.getTelephoneNumbers().get(0)).isEqualTo(DEFAULT_TELEPHONE_NUMBER);
        assertThat(entity.getMobileNumbers()).hasSize(1);
        assertThat(entity.getMobileNumbers().get(0)).isEqualTo(DEFAULT_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get phoneBook By Id")
    public void getPhoneBookById() throws Exception {
        PhoneBook entity = service.save(dto);

        mockMvc.perform(get("/api/v1/phoneBook/{id}", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(entity.getId()))
                .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.mobileNumbers.[0]").value(DEFAULT_MOBILE_NUMBER))
                .andExpect(jsonPath("$.telephoneNumbers.[0]").value(DEFAULT_TELEPHONE_NUMBER));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get phoneBook")
    public void getFloors() throws Exception {
        PhoneBook entity = service.save(dto);

        mockMvc.perform(get("/api/v1/phoneBook"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.[0].mobileNumbers.[0]").value(DEFAULT_MOBILE_NUMBER))
                .andExpect(jsonPath("$.[0].telephoneNumbers.[0]").value(DEFAULT_TELEPHONE_NUMBER));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to update phoneBook")
    public void updatePhoneBook() throws Exception {
        int databaseSizeBeforeSave = service.getAll().size();
        PhoneBook entity = service.save(dto);

        dto = dataProvider.updateDTO();

        mockMvc.perform(patch("/api/v1/phoneBook/{id}", entity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isAccepted());

        PhoneBook newEntity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(service.getAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(newEntity.getId()).isNotBlank();
        assertThat(newEntity.getName()).isEqualTo(UPDATED_NAME);
        assertThat(newEntity.getTelephoneNumbers()).hasSize(2);
        assertThat(newEntity.getTelephoneNumbers().get(0)).isEqualTo(DEFAULT_TELEPHONE_NUMBER);
        assertThat(newEntity.getTelephoneNumbers().get(1)).isEqualTo(UPDATED_TELEPHONE_NUMBER);
        assertThat(newEntity.getMobileNumbers()).hasSize(2);
        assertThat(newEntity.getMobileNumbers().get(0)).isEqualTo(DEFAULT_MOBILE_NUMBER);
        assertThat(newEntity.getMobileNumbers().get(1)).isEqualTo(UPDATED_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    @DisplayName("REST Request to delete phoneBook")
    public void deleteFloor() throws Exception {
        PhoneBook entity = service.save(dto);
        int databaseSizeAfterSave = service.getAll().size();

        mockMvc.perform(delete("/api/v1/phoneBook/{id}", entity.getId()))
                .andExpect(status().isNoContent());

        assertThat(service.getAll()).hasSize(databaseSizeAfterSave - 1);
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get phoneBook")
    public void getPhoneBooksByName() throws Exception {
        PhoneBook entity = service.save(dto);

        mockMvc.perform(get("/api/v1/phoneBook/search?name=" + DEFAULT_NAME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.[0].mobileNumbers.[0]").value(DEFAULT_MOBILE_NUMBER))
                .andExpect(jsonPath("$.[0].telephoneNumbers.[0]").value(DEFAULT_TELEPHONE_NUMBER));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get phoneBook")
    public void getPhoneBooksById() throws Exception {
        PhoneBook entity = service.save(dto);

        mockMvc.perform(get("/api/v1/phoneBook/search?id=" + entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.[0].mobileNumbers.[0]").value(DEFAULT_MOBILE_NUMBER))
                .andExpect(jsonPath("$.[0].telephoneNumbers.[0]").value(DEFAULT_TELEPHONE_NUMBER));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get phoneBook")
    public void getPhoneBooksByIdAndSort() throws Exception {
        PhoneBook entity = service.save(dto);

        mockMvc.perform(get("/api/v1/phoneBook/search?id=" + entity.getId() + "&sort=id,name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.[0].mobileNumbers.[0]").value(DEFAULT_MOBILE_NUMBER))
                .andExpect(jsonPath("$.[0].telephoneNumbers.[0]").value(DEFAULT_TELEPHONE_NUMBER));
    }
}
