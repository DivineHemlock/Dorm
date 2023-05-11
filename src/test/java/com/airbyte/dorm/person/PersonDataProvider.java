package com.airbyte.dorm.person;

import com.airbyte.dorm.characteristic.CharacteristicDataProvider;
import com.airbyte.dorm.characteristic.CharacteristicService;
import com.airbyte.dorm.dto.FileDTO;
import com.airbyte.dorm.dto.PersonDTO;
import com.airbyte.dorm.files.FileDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.airbyte.dorm.CommonTestData.*;

@Component
public class PersonDataProvider {

    @Autowired
    private CharacteristicDataProvider characteristicDataProvider;
    @Autowired
    private CharacteristicService characteristicService;
    @Autowired
    private FileDataProvider fileDataProvider;

    public PersonDTO createDTO () {
        PersonDTO dto = new PersonDTO();
        dto.setAccommodationType(String.valueOf(DEFAULT_ACCOMMODATION));
        dto.setCharacteristicId(characteristicService.save(characteristicDataProvider.createDTO()).getId());
        dto.setResidenceType(String.valueOf(DEFAULT_RESIDENT));
        dto.setFiles(fileDataProvider.createFiles());
        return dto;
    }



    public PersonDTO updateDTO () {
        PersonDTO dto = new PersonDTO();
        dto.setAccommodationType(String.valueOf(UPDATED_ACCOMMODATION));
        dto.setCharacteristicId(characteristicService.save(characteristicDataProvider.createDTO()).getId());
        dto.setResidenceType(String.valueOf(UPDATED_RESIDENT));
        dto.setFiles(List.of(fileDataProvider.createFile()));
        return dto;
    }

    private List<FileDTO> prepareUpdateFileDTO() {
        FileDTO dto1 = new FileDTO();
        dto1.setName(DEFAULT_CATEGORY_NAME);
        dto1.setFileId(DEFAULT_STRING);

        return List.of(dto1);
    }
}
