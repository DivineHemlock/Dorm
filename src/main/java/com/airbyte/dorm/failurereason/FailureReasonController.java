package com.airbyte.dorm.failurereason;

import com.airbyte.dorm.common.ParentController;
import com.airbyte.dorm.dto.FailureReasonDTO;
import com.airbyte.dorm.model.FailureReason;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/supervisor/failureReason")
@CrossOrigin("*")
public class FailureReasonController extends ParentController<FailureReason, FailureReasonService, FailureReasonDTO> {

    public FailureReasonController(FailureReasonService service) {
        super(service);
    }
}
