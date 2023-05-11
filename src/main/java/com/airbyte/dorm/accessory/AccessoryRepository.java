package com.airbyte.dorm.accessory;

import com.airbyte.dorm.model.Accessory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessoryRepository extends JpaRepository<Accessory, String> {
}
