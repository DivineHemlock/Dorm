package com.airbyte.dorm.inventory;

import com.airbyte.dorm.dto.InventoryDTO;
import com.airbyte.dorm.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, String> {
}
