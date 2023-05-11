package com.airbyte.dorm.telephonehistory;

import com.airbyte.dorm.dto.TelephoneHistoryDTO;
import org.springframework.stereotype.Component;

import static com.airbyte.dorm.CommonTestData.*;

@Component
public class TelephoneHistoryDataProvider {

    public TelephoneHistoryDTO createDTO() {
        TelephoneHistoryDTO dto = new TelephoneHistoryDTO();
        dto.setPhoneNumber(DEFAULT_STRING);
        dto.setDate(DEFAULT_DATE);
        dto.setDescription(DEFAULT_STRING);
        dto.setTitle(DEFAULT_STRING);
        dto.setCallerName(DEFAULT_NAME);
        return dto;
    }

    public TelephoneHistoryDTO updateDTO() {
        TelephoneHistoryDTO dto = new TelephoneHistoryDTO();
        dto.setPhoneNumber(UPDATED_STRING);
        dto.setDate(UPDATED_DATE);
        dto.setDescription(UPDATED_STRING);
        dto.setTitle(UPDATED_STRING);
        dto.setCallerName(UPDATED_NAME);
        return dto;
    }
}
