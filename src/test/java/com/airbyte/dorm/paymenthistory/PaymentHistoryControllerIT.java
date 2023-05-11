package com.airbyte.dorm.paymenthistory;

import com.airbyte.dorm.DormApplication;
import com.airbyte.dorm.dto.PaymentHistoryDTO;
import com.airbyte.dorm.model.PaymentHistory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
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
public class PaymentHistoryControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PaymentHistoryService service;
    @Autowired
    private PaymentHistoryDataProvider dataProvider;
    private PaymentHistoryDTO dto;

    @BeforeEach
    public void initTest() {
        dto = dataProvider.createDTO();
    }

    @Test
    @Transactional
    @DisplayName("REST Request to save paymentHistory")
    public void savePaymentHistory() throws Exception {
        int databaseSizeBeforeSave = service.getAll().size();

        mockMvc.perform(post("/api/v1/paymentHistory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        PaymentHistory entity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(service.getAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(entity.getId()).isNotBlank();
        assertThat(entity.getDate()).isNotNull();
        assertThat(entity.getAmount().getUnit()).isEqualTo(DEFAULT_UNIT);
        assertThat(entity.getAmount().getValue()).isEqualTo(DEFAULT_VALUE_STRING);
        assertThat(entity.getType()).isEqualTo(DEFAULT_CATEGORY_NAME);
        assertThat(entity.getPaymentType()).isEqualTo(DEFAULT_PAYMENT_TYPE);
        assertThat(entity.getDescription()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getFile().get(DEFAULT_STRING)).isEqualTo(DEFAULT_ID);
        assertThat(entity.getParentId()).isNotBlank();
        assertThat(entity.getParentType()).isEqualTo(DEFAULT_PARENT_TYPE);
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get paymentHistory By Id")
    public void getPaymentHistoryById() throws Exception {
        PaymentHistory entity = service.save(dto);

        mockMvc.perform(get("/api/v1/paymentHistory/{id}", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(entity.getId()))
                .andExpect(jsonPath("$.date").value(DEFAULT_DATE))
                .andExpect(jsonPath("$.amount.unit").value(DEFAULT_UNIT))
                .andExpect(jsonPath("$.amount.value").value(DEFAULT_VALUE_STRING))
                .andExpect(jsonPath("$.type").value(DEFAULT_CATEGORY_NAME))
                .andExpect(jsonPath("$.paymentType").value(DEFAULT_PAYMENT_TYPE.name()))
                .andExpect(jsonPath("$.description").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.file", Matchers.hasKey(DEFAULT_STRING)))
                .andExpect(jsonPath("$.file", Matchers.hasValue(DEFAULT_ID)))
                .andExpect(jsonPath("$.parentId").isNotEmpty())
                .andExpect(jsonPath("$.parentType").value(DEFAULT_PARENT_TYPE));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get paymentHistories")
    public void getPaymentHistories() throws Exception {
        PaymentHistory entity = service.save(dto);

        mockMvc.perform(get("/api/v1/paymentHistory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].date").value(DEFAULT_DATE))
                .andExpect(jsonPath("$.[0].amount.unit").value(DEFAULT_UNIT))
                .andExpect(jsonPath("$.[0].amount.value").value(DEFAULT_VALUE_STRING))
                .andExpect(jsonPath("$.[0].type").value(DEFAULT_CATEGORY_NAME))
                .andExpect(jsonPath("$.[0].paymentType").value(DEFAULT_PAYMENT_TYPE.name()))
                .andExpect(jsonPath("$.[0].description").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].file", Matchers.hasKey(DEFAULT_STRING)))
                .andExpect(jsonPath("$.[0].file", Matchers.hasValue(DEFAULT_ID)))
                .andExpect(jsonPath("$.[0].parentId").isNotEmpty())
                .andExpect(jsonPath("$.[0].parentType").value(DEFAULT_PARENT_TYPE));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to update paymentHistory")
    public void updatePaymentHistoryById() throws Exception {
        int databaseSizeBeforeSave = service.getAll().size();
        PaymentHistory entity = service.save(dto);

        dto = dataProvider.updateDTO();

        mockMvc.perform(patch("/api/v1/paymentHistory/{id}", entity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isAccepted());

        PaymentHistory newEntity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(service.getAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(newEntity.getId()).isNotBlank();
        assertThat(entity.getDate()).isNotNull();
        assertThat(entity.getAmount().getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(entity.getAmount().getValue()).isEqualTo(UPDATED_VALUE_STRING);
        assertThat(entity.getType()).isEqualTo(UPDATED_CATEGORY_NAME);
        assertThat(entity.getPaymentType()).isEqualTo(UPDATED_PAYMENT_TYPE);
        assertThat(entity.getDescription()).isEqualTo(UPDATED_STRING);
        assertThat(entity.getFile().get(UPDATED_STRING)).isEqualTo(UPDATED_ID);
        assertThat(entity.getParentId()).isNotBlank();
        assertThat(entity.getParentType()).isEqualTo(UPDATED_PARENT_TYPE);
    }

    @Test
    @Transactional
    @DisplayName("REST Request to delete paymentHistory")
    public void deleteFloor() throws Exception {
        PaymentHistory entity = service.save(dto);
        int databaseSizeAfterSave = service.getAll().size();


        mockMvc.perform(delete("/api/v1/paymentHistory/{id}", entity.getId()))
                .andExpect(status().isNoContent());

        assertThat(service.getAll()).hasSize(databaseSizeAfterSave - 1);
    }
}
