package com.airbyte.dorm.notification;

import com.airbyte.dorm.common.ParentController;
import com.airbyte.dorm.dto.NotificationDTO;
import com.airbyte.dorm.model.Notification;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/supervisor/notification")
@CrossOrigin("*")
public class NotificationController extends ParentController<Notification, NotificationService , NotificationDTO> {
    public NotificationController(NotificationService service) {
        super(service);
    }
}
