package com.airbyte.dorm.camerahistory;

import com.airbyte.dorm.common.ParentController;
import com.airbyte.dorm.dto.CameraHistoryDTO;
import com.airbyte.dorm.model.events.CameraHistory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/supervisor/cameraHistory")
@CrossOrigin("*")
public class CameraHistoryController extends ParentController<CameraHistory, CameraHistoryService, CameraHistoryDTO> {
    public CameraHistoryController(CameraHistoryService service) {
        super(service);
    }
}
