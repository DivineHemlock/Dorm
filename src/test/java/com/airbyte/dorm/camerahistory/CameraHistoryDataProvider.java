package com.airbyte.dorm.camerahistory;

import com.airbyte.dorm.dto.CameraHistoryDTO;
import com.airbyte.dorm.person.PersonDataProvider;
import com.airbyte.dorm.person.PersonService;
import com.airbyte.dorm.supervisor.SupervisorDataProvider;
import com.airbyte.dorm.supervisor.SupervisorService;
import com.airbyte.dorm.unit.UnitDataProvider;
import com.airbyte.dorm.unit.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.airbyte.dorm.CommonTestData.*;
import static com.airbyte.dorm.CommonTestData.UPDATED_NAME;

@Component
public class CameraHistoryDataProvider {
    public CameraHistoryDTO createDTO() {
        CameraHistoryDTO dto = new CameraHistoryDTO();
        dto.setTitle(DEFAULT_STRING);
        dto.setDescription(DEFAULT_STRING);
        dto.setDate(DEFAULT_DATE);
        dto.setSupervisor(DEFAULT_NAME);
        dto.setUnit(DEFAULT_STRING);
        dto.setAssignee(DEFAULT_NAME);
        return dto;
    }

    public CameraHistoryDTO updateDTO() {
        CameraHistoryDTO dto = new CameraHistoryDTO();
        dto.setTitle(UPDATED_STRING);
        dto.setDescription(UPDATED_STRING);
        dto.setDate(UPDATED_DATE);
        dto.setAssignee(UPDATED_NAME);
        dto.setSupervisor(UPDATED_NAME);
        dto.setUnit(UPDATED_UNIT);
        return dto;
    }
}
