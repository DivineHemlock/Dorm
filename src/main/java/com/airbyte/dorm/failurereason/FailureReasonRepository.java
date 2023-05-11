package com.airbyte.dorm.failurereason;

import com.airbyte.dorm.model.FailureReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FailureReasonRepository extends JpaRepository<FailureReason , String> {
}
