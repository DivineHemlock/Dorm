package com.airbyte.dorm.personnel;

import com.airbyte.dorm.model.people.Personnel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonnelRepository extends JpaRepository<Personnel, String> {
}
