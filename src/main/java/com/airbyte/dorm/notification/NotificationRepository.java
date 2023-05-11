package com.airbyte.dorm.notification;

import com.airbyte.dorm.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification , String> {
}
