package com.airbyte.dorm.characteristic;

import com.airbyte.dorm.dto.CharacteristicDTO;
import org.springframework.stereotype.Component;

import static com.airbyte.dorm.CommonTestData.*;

@Component
public class CharacteristicDataProvider {

    public CharacteristicDTO createDTO () {
        CharacteristicDTO characteristic = new CharacteristicDTO();
        characteristic.setCvv2(DEFAULT_STRING);
        characteristic.setBankAccountShabaNumber(DEFAULT_STRING);
        characteristic.setBankAccountOwnerName(DEFAULT_STRING);
        characteristic.setBankAccountNumber(DEFAULT_STRING);
        characteristic.setCardNumber(DEFAULT_STRING);
        characteristic.setBankName(DEFAULT_STRING);
        characteristic.setAddress(DEFAULT_STRING);
        characteristic.setAlias(DEFAULT_STRING);
        characteristic.setBankAccountExpirationDate(DEFAULT_DATE);
        characteristic.setBirthDate(DEFAULT_DATE);
        characteristic.setBirthPlace(DEFAULT_STRING);
        characteristic.setCertificateNumber(DEFAULT_STRING);
        characteristic.setEducation(DEFAULT_STRING);
        characteristic.setEmail(DEFAULT_EMAIL);
        characteristic.setEmergencyNumber(DEFAULT_NUMBER_STRING);
        characteristic.setFatherPhoneNumber(DEFAULT_STRING);
        characteristic.setFatherName(DEFAULT_STRING);
        characteristic.setFirstName(DEFAULT_STRING);
        characteristic.setHealth(DEFAULT_BOOLEAN);
        characteristic.setHealthyStatus(DEFAULT_STRING);
        characteristic.setHomeNumber(DEFAULT_HOME_NUMBER);
        characteristic.setJob(DEFAULT_STRING);
        characteristic.setLastName(DEFAULT_STRING);
        characteristic.setMajor(DEFAULT_STRING);
        characteristic.setMaritalStatus(DEFAULT_MARITAL_STATUS);
        characteristic.setMotherPhoneNumber(DEFAULT_STRING);
        characteristic.setNationalCode(DEFAULT_NATIONAL_CODE);
        characteristic.setNationality(DEFAULT_STRING);
        characteristic.setParentAddress(DEFAULT_STRING);
        characteristic.setPhoneNumber(DEFAULT_PHONE_NUMBER);
        characteristic.setPlaceOfIssue(DEFAULT_STRING);
        characteristic.setPostalCode(DEFAULT_STRING);
        characteristic.setReligion(DEFAULT_STRING);
        characteristic.setReservationDate(DEFAULT_DATE);
        characteristic.setSpouseFatherName(DEFAULT_STRING);
        characteristic.setSpouseFullName(DEFAULT_STRING);
        characteristic.setSpouseJob(DEFAULT_STRING);
        characteristic.setStudentNumber(DEFAULT_STRING);
        characteristic.setSubReligion(DEFAULT_STRING);
        characteristic.setTelephoneNumber(DEFAULT_HOME_NUMBER);
        characteristic.setUniversity(DEFAULT_STRING);
        characteristic.setFatherJob(DEFAULT_STRING);
        characteristic.setEndDate(DEFAULT_DATE);
        characteristic.setGender(DEFAULT_GENDER);
        characteristic.setRegistrationType(String.valueOf(DEFAULT_REGISTRATION));
        characteristic.setStartDate(DEFAULT_DATE);
        characteristic.setPaymentDate(DEFAULT_DATE);
        characteristic.setRentPaymentAmount(DEFAULT_STRING);
        characteristic.setDepositPaymentAmount(DEFAULT_STRING);
        characteristic.setDiscountPaymentAmount(DEFAULT_STRING);
        characteristic.setRelationshipWithResident(DEFAULT_STRING);
        characteristic.setPersonType(DEFAULT_STRING);
        characteristic.setProfileId(DEFAULT_STRING);
        return characteristic;
    }

    public CharacteristicDTO updateDTO() {
        CharacteristicDTO dto = new CharacteristicDTO();
        dto.setEndDate(UPDATED_DATE);
        dto.setRegistrationType(String.valueOf(UPDATED_REGISTRATION));
        dto.setStartDate(UPDATED_DATE);
        dto.setGender(UPDATED_GENDER);
        return dto;
    }
}
