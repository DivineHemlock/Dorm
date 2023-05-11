package com.airbyte.dorm.relatedobject;

import com.airbyte.dorm.dto.RelatedObjectDTO;
import com.airbyte.dorm.model.RelatedObject;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RelatedObjectService {

    private final RelatedObjectRepository repository;

    public RelatedObjectService(RelatedObjectRepository repository) {
        this.repository = repository;
    }

    public List<RelatedObject> getWithSearch(String type, String parentId) {
        return repository.findAll().stream()
                .filter(relatedObject -> type.equals(relatedObject.getType()))
                .filter(relatedObject -> parentId.equals(relatedObject.getParentId()))
                .collect(Collectors.toList());
    }

    public RelatedObjectRepository getRepository() {
        return repository;
    }
}
