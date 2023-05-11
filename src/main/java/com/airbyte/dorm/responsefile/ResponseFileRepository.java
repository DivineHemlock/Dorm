package com.airbyte.dorm.responsefile;

import com.airbyte.dorm.model.ResponseFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponseFileRepository extends JpaRepository<ResponseFile, String> {
}
