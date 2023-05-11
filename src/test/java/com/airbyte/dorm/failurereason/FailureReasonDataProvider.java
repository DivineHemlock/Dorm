package com.airbyte.dorm.failurereason;

import com.airbyte.dorm.dto.FailureReasonDTO;
import org.springframework.stereotype.Component;

import static com.airbyte.dorm.CommonTestData.*;

@Component
public class FailureReasonDataProvider {

    public FailureReasonDTO createDTO() {
        FailureReasonDTO dto = new FailureReasonDTO();
        dto.setReason(DEFAULT_STRING);
        dto.setName(DEFAULT_STRING);
        dto.setType(DEFAULT_STRING);
        return dto;
    }

    public FailureReasonDTO updateDTO() {
        FailureReasonDTO dto = new FailureReasonDTO();
        dto.setReason(UPDATED_STRING);
        dto.setName(UPDATED_STRING);
        dto.setType(UPDATED_STRING);
        return dto;
    }
}
