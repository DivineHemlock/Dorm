package com.airbyte.dorm.paymenthistory;

import com.airbyte.dorm.model.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, String> {
    @Query(nativeQuery = true, value = "SELECT * FROM payment_history WHERE ((payment_type IN (:paymentTypes)) AND (date >= :startDate AND date <= :endDate))")
    List<PaymentHistory> getByTimePeriod(List<String> paymentTypes, Date startDate, Date endDate);
}
