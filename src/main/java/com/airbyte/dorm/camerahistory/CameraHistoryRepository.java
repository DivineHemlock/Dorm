package com.airbyte.dorm.camerahistory;

import com.airbyte.dorm.model.events.CameraHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CameraHistoryRepository extends JpaRepository <CameraHistory , String> {
}
