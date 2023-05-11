package com.airbyte.dorm.characteristic;

import com.airbyte.dorm.DormApplication;
import com.airbyte.dorm.dto.CharacteristicDTO;
import com.airbyte.dorm.model.Characteristic;
import com.airbyte.dorm.model.enums.Gender;
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
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@ContextConfiguration(classes = {DormApplication.class})
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CharacteristicControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CharacteristicService service;
    @Autowired
    private CharacteristicDataProvider dataProvider;
    private CharacteristicDTO dto;

    @BeforeEach
    public void initTest() {
        dto = dataProvider.createDTO();
    }

    @Test
    @Transactional
    @DisplayName("REST Request to save characteristic")
    public void saveCharacteristic() throws Exception {
        int databaseSizeBeforeSave = service.getAll().size();

        mockMvc.perform(post("/api/v1/characteristic")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        Characteristic entity = service.getAll().get(databaseSizeBeforeSave);

        assertThat(service.getAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(entity.getId()).isNotBlank();
        assertThat(entity.getCvv2()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getBankAccountShabaNumber()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getBankAccountOwnerName()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getBankAccountNumber()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getCardNumber()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getBankName()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getAddress()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getAlias()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getBankAccountExpirationDate()).isNotNull();
        assertThat(entity.getBirthDate()).isNotNull();
        assertThat(entity.getBirthPlace()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getCertificateNumber()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getEducation()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(entity.getEmergencyNumber()).isEqualTo(DEFAULT_NUMBER_STRING);
        assertThat(entity.getFatherJob()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getFatherPhoneNumber()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getFatherName()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getFirstName()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getHealth()).isEqualTo(DEFAULT_BOOLEAN);
        assertThat(entity.getHealthyStatus()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getHomeNumber()).isEqualTo(DEFAULT_HOME_NUMBER);
        assertThat(entity.getJob()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getLastName()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getMajor()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getMaritalStatus().toString()).isEqualTo(DEFAULT_MARITAL_STATUS);
        assertThat(entity.getMotherPhoneNumber()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getNationalCode()).isEqualTo(DEFAULT_NATIONAL_CODE);
        assertThat(entity.getNationality()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getParentAddress()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(entity.getPlaceOfIssue()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getPostalCode()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getReligion()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getReservationDate()).isNotNull();
        assertThat(entity.getSpouseFatherName()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getSpouseFullName()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getSpouseJob()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getStudentNumber()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getSubReligion()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getTelephoneNumber()).isEqualTo(DEFAULT_HOME_NUMBER);
        assertThat(entity.getUniversity()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getGender()).isEqualTo(Gender.valueOf(DEFAULT_GENDER));
        assertThat(entity.getTimePeriod().getStartDate()).isNotNull();
        assertThat(entity.getTimePeriod().getEndDate()).isNotNull();
        assertThat(entity.getRegistrationType()).isEqualTo(DEFAULT_REGISTRATION);
        assertThat(entity.getPaymentDate()).isEqualTo(DEFAULT_DATE);
        assertThat(entity.getRentPaymentAmount()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getDepositPaymentAmount()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getDiscountPaymentAmount()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getRelationshipWithResident()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getProfileId()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getPersonType()).isEqualTo(DEFAULT_STRING);
    }
}
