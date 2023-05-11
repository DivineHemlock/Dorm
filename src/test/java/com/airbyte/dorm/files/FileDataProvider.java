package com.airbyte.dorm.files;

import com.airbyte.dorm.dto.FileDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static com.airbyte.dorm.CommonTestData.*;

@Component
public class FileDataProvider {

    public List<FileDTO> createFiles() {
        FileDTO dto1 = new FileDTO();
        dto1.setName(DEFAULT_CATEGORY_NAME);
        dto1.setFileId(DEFAULT_ID);

        FileDTO dto2 = new FileDTO();
        dto2.setName(UPDATED_CATEGORY_NAME);
        dto2.setFileId(UPDATED_ID);

        return List.of(dto1, dto2);
    }

    public FileDTO createFile() {
        FileDTO dto = new FileDTO();
        dto.setName(DEFAULT_CATEGORY_NAME);
        dto.setFileId(DEFAULT_ID);
        return dto;
    }

}
