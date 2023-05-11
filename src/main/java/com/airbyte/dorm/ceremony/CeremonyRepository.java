package com.airbyte.dorm.ceremony;

import com.airbyte.dorm.model.events.Ceremony;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CeremonyRepository extends JpaRepository <Ceremony,String> {
}
