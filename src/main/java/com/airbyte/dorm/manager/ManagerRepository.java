package com.airbyte.dorm.manager;

import com.airbyte.dorm.model.people.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, String> {
}
