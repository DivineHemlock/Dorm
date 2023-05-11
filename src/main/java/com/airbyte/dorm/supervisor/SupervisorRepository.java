package com.airbyte.dorm.supervisor;

import com.airbyte.dorm.model.people.Supervisor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupervisorRepository extends JpaRepository<Supervisor, String> {
}
