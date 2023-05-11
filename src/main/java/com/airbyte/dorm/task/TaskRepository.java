package com.airbyte.dorm.task;

import com.airbyte.dorm.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository <Task , String> {
}
