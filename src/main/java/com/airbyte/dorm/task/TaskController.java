package com.airbyte.dorm.task;

import com.airbyte.dorm.common.ParentController;
import com.airbyte.dorm.dto.TaskDTO;
import com.airbyte.dorm.model.Task;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/supervisor/task")
@CrossOrigin("*")
public class TaskController extends ParentController<Task, TaskService, TaskDTO> {
    public TaskController(TaskService service) {
        super(service);
    }
}
