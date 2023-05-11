package com.airbyte.dorm.supervisor;

import com.airbyte.dorm.characteristic.CharacteristicDataProvider;
import com.airbyte.dorm.characteristic.CharacteristicService;
import com.airbyte.dorm.dto.FileDTO;
import com.airbyte.dorm.dto.SupervisorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.airbyte.dorm.CommonTestData.*;

@Component
public class SupervisorDataProvider {

    @Autowired
    private CharacteristicDataProvider characteristicDataProvider;
    @Autowired
    private CharacteristicService characteristicService;

    public SupervisorDTO createDTO() {
        SupervisorDTO dto = new SupervisorDTO();
        dto.setEmail(DEFAULT_STRING);
        dto.setUsername(DEFAULT_STRING);
        dto.setPassword(DEFAULT_STRING);
        dto.setRole(DEFAULT_STRING);
        dto.setFullName(DEFAULT_STRING);
        dto.setProfileId(DEFAULT_STRING);
        return dto;
    }

    public SupervisorDTO updateDTO () {
        SupervisorDTO dto = new SupervisorDTO();
        dto.setEmail(UPDATED_STRING);
        dto.setUsername(UPDATED_STRING);
        dto.setPassword(UPDATED_STRING);
        dto.setRole(UPDATED_STRING);
        dto.setFullName(UPDATED_STRING);
        dto.setProfileId(UPDATED_STRING);
        return dto;
    }

    private List<FileDTO> prepareFileDTO() {
        FileDTO dto1 = new FileDTO();
        dto1.setName(DEFAULT_CATEGORY_NAME);
        dto1.setFileId(DEFAULT_STRING);

        FileDTO dto2 = new FileDTO();
        dto2.setName(UPDATED_CATEGORY_NAME);
        dto2.setFileId(UPDATED_STRING);

        return List.of(dto1, dto2);
    }

    private List<FileDTO> prepareUpdateFileDTO() {
        FileDTO dto1 = new FileDTO();
        dto1.setName(DEFAULT_CATEGORY_NAME);
        dto1.setFileId(DEFAULT_STRING);

        return List.of(dto1);
    }
}
