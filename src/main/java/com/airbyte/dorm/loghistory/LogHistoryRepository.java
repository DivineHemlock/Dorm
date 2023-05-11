package com.airbyte.dorm.loghistory;

import com.airbyte.dorm.model.LogHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogHistoryRepository  extends JpaRepository<LogHistory, String> {
}
