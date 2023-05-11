package com.airbyte.dorm.record;

import com.airbyte.dorm.dto.RecordDTO;
import com.airbyte.dorm.person.PersonDataProvider;
import com.airbyte.dorm.person.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.airbyte.dorm.CommonTestData.*;

@Component
public class RecordDataProvider {
    @Autowired
    private PersonDataProvider personDataProvider;
    @Autowired
    private PersonService personService;

    public RecordDTO createDTO() {
        RecordDTO dto = new RecordDTO();
        dto.setDate(DEFAULT_DATE);
        dto.setDescription(DEFAULT_STRING);
        dto.setEndDate(DEFAULT_DATE);
        dto.setStartDate(DEFAULT_DATE);
        dto.setDestinationAddress(DEFAULT_STRING);
        dto.setDestinationPhoneNumber(DEFAULT_PHONE_NUMBER);
        dto.setTitle(DEFAULT_STRING);
        dto.setPenaltyAmount(DEFAULT_STRING);
        dto.setPenaltyAmount(DEFAULT_STRING);
        dto.setPersonId(personService.save(personDataProvider.createDTO()).getId());
        dto.setCheckCleaning(DEFAULT_BOOLEAN);
        return dto;
    }

    public RecordDTO updateDTO() {
        RecordDTO dto = new RecordDTO();
        dto.setDate(UPDATED_DATE);
        dto.setDescription(UPDATED_STRING);
        dto.setEndDate(UPDATED_DATE);
        dto.setStartDate(UPDATED_DATE);
        dto.setDestinationAddress(UPDATED_STRING);
        dto.setDestinationPhoneNumber(UPDATED_PHONE_NUMBER);
        dto.setTitle(UPDATED_STRING);
        dto.setPenaltyAmount(UPDATED_STRING);
        dto.setPenaltyAmount(UPDATED_STRING);
        dto.setCheckCleaning(UPDATED_BOOLEAN);
        return dto;
    }
}
