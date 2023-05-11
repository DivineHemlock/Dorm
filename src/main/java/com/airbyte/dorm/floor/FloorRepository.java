package com.airbyte.dorm.floor;

import com.airbyte.dorm.model.Floor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FloorRepository extends JpaRepository<Floor, String> {
}
