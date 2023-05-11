package com.airbyte.dorm.paymenthistory;

import com.airbyte.dorm.dto.FileDTO;
import com.airbyte.dorm.dto.PaymentHistoryDTO;
import com.airbyte.dorm.person.PersonDataProvider;
import com.airbyte.dorm.person.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.airbyte.dorm.CommonTestData.*;
import static com.airbyte.dorm.CommonTestData.UPDATED_UNIT;

@Component
public class PaymentHistoryDataProvider {
    @Autowired
    private PersonService personService;
    @Autowired
    private PersonDataProvider personDataProvider;

    public PaymentHistoryDTO createDTO() {
        PaymentHistoryDTO dto = new PaymentHistoryDTO();
        dto.setDate(DEFAULT_DATE);
        dto.setUnit(DEFAULT_UNIT);
        dto.setValue(DEFAULT_VALUE_STRING);
        dto.setType(DEFAULT_CATEGORY_NAME);
        dto.setPaymentType(DEFAULT_PAYMENT_TYPE.name());
        dto.setDescription(DEFAULT_STRING);
        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileId(DEFAULT_ID);
        fileDTO.setName(DEFAULT_STRING);
        dto.setFile(fileDTO);
        dto.setParentType(DEFAULT_PARENT_TYPE);
        dto.setParentId(personService.save(personDataProvider.createDTO()).getId());
        return dto;
    }

    public PaymentHistoryDTO updateDTO() {
        PaymentHistoryDTO dto = new PaymentHistoryDTO();
        dto.setDate(UPDATED_DATE);
        dto.setUnit(UPDATED_UNIT);
        dto.setValue(UPDATED_VALUE_STRING);
        dto.setType(UPDATED_CATEGORY_NAME);
        dto.setPaymentType(UPDATED_PAYMENT_TYPE.name());
        dto.setDescription(UPDATED_STRING);
        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileId(UPDATED_ID);
        fileDTO.setName(UPDATED_STRING);
        dto.setFile(fileDTO);
        dto.setParentType(UPDATED_PARENT_TYPE);
        dto.setParentId(personService.save(personDataProvider.createDTO()).getId());
        return dto;
    }
}
