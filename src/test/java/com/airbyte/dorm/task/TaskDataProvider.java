package com.airbyte.dorm.task;

import com.airbyte.dorm.dto.TaskDTO;
import com.airbyte.dorm.personnel.PersonnelDataProvider;
import com.airbyte.dorm.personnel.PersonnelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.airbyte.dorm.CommonTestData.*;
import static com.airbyte.dorm.CommonTestData.UPDATED_STRING;

@Component
public class TaskDataProvider {
    @Autowired
    private PersonnelDataProvider personnelDataProvider;
    @Autowired
    private PersonnelService personnelService;

    public TaskDTO createDTO() {
        TaskDTO dto = new TaskDTO();
        dto.setTimeLog(DEFAULT_DOUBLE);
        dto.setDescription(DEFAULT_STRING);
        dto.setDueDate(DEFAULT_DATE);
        dto.setStatus(DEFAULT_TASK_STATUS.name());
        dto.setPriority(DEFAULT_TASK_PRIORITY.name());
        dto.setName(DEFAULT_NAME);
        dto.setPersonnelId(personnelService.save(personnelDataProvider.createDTO()).getId());
        return dto;
    }

    public TaskDTO updateDTO() {
        TaskDTO dto = new TaskDTO();
        dto.setTimeLog(UPDATED_DOUBLE);
        dto.setDescription(UPDATED_STRING);
        dto.setDueDate(UPDATED_DATE);
        dto.setStatus(UPDATED_TASK_STATUS.name());
        dto.setPriority(UPDATED_TASK_PRIORITY.name());
        dto.setName(UPDATED_NAME);
        return dto;
    }
}
