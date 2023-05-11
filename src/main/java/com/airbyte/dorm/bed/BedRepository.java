package com.airbyte.dorm.bed;

import com.airbyte.dorm.model.Bed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BedRepository extends JpaRepository <Bed, String> {
}
