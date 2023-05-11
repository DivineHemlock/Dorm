package com.airbyte.dorm.insurance;

import com.airbyte.dorm.model.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsuranceRepository extends JpaRepository <Insurance , String> {
}
