package com.airbyte.dorm.notification;

import com.airbyte.dorm.dto.NotificationDTO;
import org.springframework.stereotype.Component;

import static com.airbyte.dorm.CommonTestData.*;

@Component
public class NotificationDataProvider {

    public NotificationDTO createDTO() {
        NotificationDTO dto = new NotificationDTO();
        dto.setDate(DEFAULT_DATE);
        dto.setEventDescription(DEFAULT_STRING);
        dto.setEventName(DEFAULT_STRING);
        return dto;
    }

    public NotificationDTO updateDTO() {
        NotificationDTO dto = new NotificationDTO();
        dto.setDate(UPDATED_DATE);
        dto.setEventDescription(UPDATED_STRING);
        dto.setEventName(UPDATED_STRING);
        return dto;
    }
}
