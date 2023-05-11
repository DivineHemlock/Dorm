package com.airbyte.dorm.characteristic;

import com.airbyte.dorm.model.Characteristic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacteristicRepository extends JpaRepository<Characteristic, String> {
}
