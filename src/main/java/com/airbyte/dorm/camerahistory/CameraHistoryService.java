package com.airbyte.dorm.camerahistory;

import com.airbyte.dorm.common.TimeConverter;
import com.airbyte.dorm.common.ParentService;
import com.airbyte.dorm.dto.CameraHistoryDTO;
import com.airbyte.dorm.model.events.CameraHistory;
import com.airbyte.dorm.unit.UnitService;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class CameraHistoryService extends ParentService<CameraHistory, CameraHistoryRepository, CameraHistoryDTO> {
    private final UnitService unitService;

    public CameraHistoryService(CameraHistoryRepository repository, UnitService unitService) {
        super(repository);
        this.unitService = unitService;
    }

    @Override
    public CameraHistory updateModelFromDto(CameraHistory model, CameraHistoryDTO dto) {
        model.setTitle(dto.getTitle() != null ? dto.getTitle() : model.getTitle());
        model.setDescription(dto.getDescription() != null ? dto.getDescription() : model.getDescription());
        model.setDate(dto.getDate() != null ? TimeConverter.convertStringToInstant(dto.getDate(), TimeConverter.DEFAULT_PATTERN_FORMAT) : model.date());
        model.setAssignee(dto.getAssignee() != null ? dto.getAssignee() : model.getAssignee());
        model.setSupervisor(dto.getSupervisor() != null ? dto.getSupervisor() : model.getSupervisor());
        model.setUnit(dto.getUnit() != null ? dto.getUnit() : model.getUnit());
        return model;
    }

    @Override
    public CameraHistory convertDTO(CameraHistoryDTO dto) {
        CameraHistory entity = new CameraHistory();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setDate(TimeConverter.convertStringToInstant(dto.getDate(), TimeConverter.DEFAULT_PATTERN_FORMAT));
        entity.setUnit(dto.getUnit());
//        entity.setSupervisor(dto.getSupervisor());
        entity.setAssignee(dto.getAssignee());
        return entity;
    }

    @Override
    public List<CameraHistory> getWithSearch(CameraHistoryDTO search) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CameraHistory> criteriaBuilderQuery = criteriaBuilder.createQuery(CameraHistory.class);

        Root<CameraHistory> cameraHistoryRoot = criteriaBuilderQuery.from(CameraHistory.class);
        List<Predicate> predicates = new ArrayList<>();

        if (search.getId() != null) {
            predicates.add(criteriaBuilder.equal(cameraHistoryRoot.get("id"), search.getId()));
        }
        if (search.getDate() != null) {
            predicates.add(criteriaBuilder.equal(cameraHistoryRoot.get("date"), TimeConverter.convertStringToInstant(search.getDate(), TimeConverter.UPDATED_PATTERN_FORMAT)));
        }
        if (search.getAssignee() != null && !search.getAssignee().isEmpty()) {
            predicates.add(criteriaBuilder.like(cameraHistoryRoot.get("assignee"), "%" + search.getAssignee() + "%"));
        }
        if (search.getTitle() != null && !search.getTitle().isEmpty()) {
            predicates.add(criteriaBuilder.like(cameraHistoryRoot.get("title"), "%" + search.getTitle() + "%"));
        }


        criteriaBuilderQuery.where(predicates.toArray(new Predicate[0]));

        List<CameraHistory> cameraHistories = entityManager.createQuery(criteriaBuilderQuery).getResultList();

        for (String field : search.getSort()) {
            if (field.equals("id")) {
                Collections.sort(cameraHistories, Comparator.comparing(CameraHistory::getId));
            } else if (field.equals("date")) {
                Collections.sort(cameraHistories, Comparator.comparing(CameraHistory::getDate));
            }

        }
        return cameraHistories;
    }
}
