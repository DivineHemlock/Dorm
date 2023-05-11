package com.airbyte.dorm.phonebook;

import com.airbyte.dorm.dto.PhoneBookDTO;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.airbyte.dorm.CommonTestData.*;
import static com.airbyte.dorm.CommonTestData.UPDATED_TELEPHONE_NUMBER;

@Component
public class PhoneBookDataProvider {

    public PhoneBookDTO createDTO() {
        PhoneBookDTO dto = new PhoneBookDTO();
        dto.setName(DEFAULT_NAME);
        dto.setMobileNumbers(List.of(DEFAULT_MOBILE_NUMBER));
        dto.setTelephoneNumbers(List.of(DEFAULT_TELEPHONE_NUMBER));
        return dto;
    }

    public PhoneBookDTO updateDTO() {
        PhoneBookDTO dto = new PhoneBookDTO();
        dto.setName(UPDATED_NAME);
        dto.setMobileNumbers(List.of(DEFAULT_MOBILE_NUMBER, UPDATED_MOBILE_NUMBER));
        dto.setTelephoneNumbers(List.of(DEFAULT_TELEPHONE_NUMBER, UPDATED_TELEPHONE_NUMBER));
        return dto;
    }
}
