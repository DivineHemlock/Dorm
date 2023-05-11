package com.airbyte.dorm.account;

import com.airbyte.dorm.DormApplication;
import com.airbyte.dorm.dto.PaymentHistoryDTO;
import com.airbyte.dorm.model.Account;
import com.airbyte.dorm.paymenthistory.PaymentHistoryDataProvider;
import com.airbyte.dorm.paymenthistory.PaymentHistoryService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {DormApplication.class})
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AccountService service;
    private PaymentHistoryDTO dto;
    @Autowired
    private PaymentHistoryDataProvider paymentHistoryDataProvider;
    @Autowired
    private PaymentHistoryService paymentHistoryService;

    @BeforeEach
    public void initTest() {
        dto = paymentHistoryDataProvider.createDTO();
    }

    @Test
    @Transactional
    @DisplayName("REST Request to save account")
    public void saveRoom() throws Exception {
        int databaseSizeBeforeSave = service.getAll().size();

        mockMvc.perform(post("/api/v1/paymentHistory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/paymentHistory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        dto = paymentHistoryDataProvider.updateDTO();

        mockMvc.perform(post("/api/v1/paymentHistory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        dto = paymentHistoryDataProvider.updateDTO();

        mockMvc.perform(post("/api/v1/paymentHistory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        Account entity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(service.getAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(entity.getId()).isNotBlank();
        assertThat(entity.getName()).isNotNull();
        assertThat(entity.getTotalPayment()).isNotNull();
        assertThat(entity.getTotalReceived()).isNotNull();
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get account")
    public void getAccount() throws Exception {
        mockMvc.perform(get("/api/v1/account"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
    }


    @Test
    @Transactional
    @DisplayName("REST Request to get account")
    public void getAccountWithData() throws Exception {
        mockMvc.perform(post("/api/v1/paymentHistory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        dto = paymentHistoryDataProvider.updateDTO();

        mockMvc.perform(post("/api/v1/paymentHistory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/account"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").isNotEmpty())
                .andExpect(jsonPath("$.totalPayment").value(DEFAULT_VALUE_STRING))
                .andExpect(jsonPath("$.totalReceived").value(UPDATED_VALUE_STRING));
    }

}
