package com.airbyte.dorm.request;

import com.airbyte.dorm.common.ParentController;
import com.airbyte.dorm.dto.RequestDTO;
import com.airbyte.dorm.model.Request;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/supervisor/request")
@CrossOrigin("*")
public class RequestController extends ParentController<Request, RequestService, RequestDTO> {

    public RequestController(RequestService service) {
        super(service);
    }
}
