package com.airbyte.dorm.personnel;

import com.airbyte.dorm.common.ParentController;
import com.airbyte.dorm.dto.PersonnelDTO;
import com.airbyte.dorm.model.people.Personnel;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/supervisor/personnel")
@CrossOrigin("*")
public class PersonnelController extends ParentController<Personnel, PersonnelService, PersonnelDTO> {
    public PersonnelController(PersonnelService service) {
        super(service);
    }
}
