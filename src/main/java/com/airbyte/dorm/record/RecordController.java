package com.airbyte.dorm.record;

import com.airbyte.dorm.common.ParentController;
import com.airbyte.dorm.common.ParentService;
import com.airbyte.dorm.dto.RecordDTO;
import com.airbyte.dorm.model.Record;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/supervisor/record")
@CrossOrigin("*")
public class RecordController extends ParentController<Record, RecordService, RecordDTO> {
    public RecordController(RecordService service) {
        super(service);
    }
}
