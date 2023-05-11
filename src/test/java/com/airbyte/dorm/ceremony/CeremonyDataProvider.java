package com.airbyte.dorm.ceremony;

import com.airbyte.dorm.dto.CeremonyDTO;
import org.springframework.stereotype.Component;

import static com.airbyte.dorm.CommonTestData.*;

@Component
public class CeremonyDataProvider {

    public CeremonyDTO createDTO() {
        CeremonyDTO dto = new CeremonyDTO();
        dto.setDate(DEFAULT_DATE);
        dto.setDescription(DEFAULT_STRING);
        dto.setTitle(DEFAULT_STRING);
        return dto;
    }

    public CeremonyDTO updateDTO() {
        CeremonyDTO dto = new CeremonyDTO();
        dto.setDate(UPDATED_DATE);
        dto.setDescription(UPDATED_STRING);
        dto.setTitle(UPDATED_STRING);
        return dto;
    }
}
