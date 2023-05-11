package com.airbyte.dorm.accessory;

import com.airbyte.dorm.dto.AccessoryDTO;
import org.springframework.stereotype.Component;

import static com.airbyte.dorm.CommonTestData.*;

@Component
public class AccessoryDataProvider {

    public AccessoryDTO createDTO() {
        AccessoryDTO dto = new AccessoryDTO();
        dto.setCount(DEFAULT_BIG_DECIMAL.longValue());
        dto.setName(DEFAULT_STRING);
        dto.setDescription(DEFAULT_STRING);
        return dto;
    }

    public AccessoryDTO updateDTO() {
        AccessoryDTO dto = new AccessoryDTO();
        dto.setCount(UPDATED_BIG_DECIMAL.longValue());
        dto.setName(UPDATED_STRING);
        dto.setDescription(UPDATED_STRING);
        return dto;
    }
}
