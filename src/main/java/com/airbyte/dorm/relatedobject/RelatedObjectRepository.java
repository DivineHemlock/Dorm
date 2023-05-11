package com.airbyte.dorm.relatedobject;

import com.airbyte.dorm.model.RelatedObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelatedObjectRepository extends JpaRepository <RelatedObject, String> {
}
