package com.airbyte.dorm.telephonehistory;

import com.airbyte.dorm.model.events.TelephoneHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelephoneHistoryRepository extends JpaRepository <TelephoneHistory , String> {
}
