package com.airbyte.dorm.ceremony;

import com.airbyte.dorm.common.ParentController;
import com.airbyte.dorm.dto.CeremonyDTO;
import com.airbyte.dorm.model.events.Ceremony;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/supervisor/ceremony")
@CrossOrigin("*")
public class CeremonyController extends ParentController<Ceremony, CeremonyService, CeremonyDTO> {
    public CeremonyController(CeremonyService service) {
        super(service);
    }
}
