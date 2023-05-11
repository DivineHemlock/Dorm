package com.airbyte.dorm.personnel;

import com.airbyte.dorm.characteristic.CharacteristicDataProvider;
import com.airbyte.dorm.characteristic.CharacteristicService;
import com.airbyte.dorm.dto.FileDTO;
import com.airbyte.dorm.dto.PersonDTO;
import com.airbyte.dorm.dto.PersonnelDTO;
import com.airbyte.dorm.model.people.Personnel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.airbyte.dorm.CommonTestData.*;

@Component
public class PersonnelDataProvider {

    @Autowired
    private CharacteristicDataProvider characteristicDataProvider;
    @Autowired
    private CharacteristicService characteristicService;

    public PersonnelDTO createDTO () {
        PersonnelDTO personnelDTO = new PersonnelDTO();
        personnelDTO.setCharacteristicId(characteristicService.save(characteristicDataProvider.createDTO()).getId());
        personnelDTO.setGender(DEFAULT_GENDER);
        personnelDTO.setResidenceType(String.valueOf(DEFAULT_RESIDENT));
        personnelDTO.setGender(DEFAULT_GENDER);
        personnelDTO.setType(DEFAULT_CATEGORY_NAME);
        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileId(DEFAULT_ID);
        fileDTO.setName(DEFAULT_STRING);
        personnelDTO.setFiles(List.of(fileDTO));
        //files and payments
        return personnelDTO;
    }

    public PersonnelDTO updateDTO () {
        PersonnelDTO dto = new PersonnelDTO();
        dto.setCharacteristicId(characteristicService.save(characteristicDataProvider.createDTO()).getId());
        dto.setResidenceType(String.valueOf(UPDATED_RESIDENT));
        dto.setGender(UPDATED_GENDER);
        dto.setType(UPDATED_CATEGORY_NAME);
        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileId(DEFAULT_ID);
        fileDTO.setName(DEFAULT_STRING);
        dto.setFiles(List.of(fileDTO));
        //files and payments and task and record
        return dto;
    }

}
