package com.airbyte.dorm.request;

import com.airbyte.dorm.dto.RequestDTO;
import com.airbyte.dorm.failurereason.FailureReasonDataProvider;
import com.airbyte.dorm.failurereason.FailureReasonService;
import com.airbyte.dorm.person.PersonDataProvider;
import com.airbyte.dorm.person.PersonService;
import com.airbyte.dorm.supervisor.SupervisorDataProvider;
import com.airbyte.dorm.supervisor.SupervisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.airbyte.dorm.CommonTestData.*;

@Component
public class RequestDataProvider {
    @Autowired
    private FailureReasonDataProvider failureReasonDataProvider;
    @Autowired
    private FailureReasonService failureReasonService;
    @Autowired
    private PersonDataProvider personDataProvider;
    @Autowired
    private PersonService personService;
    @Autowired
    private SupervisorDataProvider supervisorDataProvider;
    @Autowired
    private SupervisorService supervisorService;

    public RequestDTO createDTO() {
        RequestDTO dto = new RequestDTO();
        dto.setDescription(DEFAULT_STRING);
        dto.setName(DEFAULT_NAME);
        dto.setReason(DEFAULT_STRING);
        dto.setChecked(DEFAULT_BOOLEAN);
        dto.setType(DEFAULT_CATEGORY_NAME);
        dto.setDateOfRegistration(DEFAULT_DATE);
        dto.setFailureReasonId(failureReasonService.save(failureReasonDataProvider.createDTO()).getId());
        dto.setSupervisorId(supervisorService.save(supervisorDataProvider.createDTO()).getId());
        dto.setAssignee(DEFAULT_STRING);
        return dto;
    }

    public RequestDTO updateDTO() {
        RequestDTO dto = new RequestDTO();
        dto.setDescription(UPDATED_STRING);
        dto.setName(UPDATED_NAME);
        dto.setReason(UPDATED_STRING);
        dto.setChecked(UPDATED_BOOLEAN);
        dto.setType(UPDATED_CATEGORY_NAME);
        dto.setDateOfRegistration(UPDATED_DATE);
        dto.setAssignee(UPDATED_STRING);
        return dto;
    }
}
