package com.airbyte.dorm.failurereason;

import com.airbyte.dorm.common.ParentService;
import com.airbyte.dorm.dto.FailureReasonDTO;
import com.airbyte.dorm.model.FailureReason;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class FailureReasonService extends ParentService<FailureReason , FailureReasonRepository , FailureReasonDTO> {

    public FailureReasonService(FailureReasonRepository repository)
    {
        super(repository);
    }

    @Override
    public FailureReason updateModelFromDto(FailureReason model, FailureReasonDTO dto) {
        model.setReason(dto.getReason() != null ? dto.getReason() : model.getReason());
        model.setType(dto.getType()  != null ? dto.getType() : model.getType());
        model.setName(dto.getName() != null ? dto.getName() : model.getName());
        return model;
    }

    @Override
    public FailureReason convertDTO(FailureReasonDTO dto) {
        FailureReason failureReason = new FailureReason();
        failureReason.setReason(dto.getReason());
        failureReason.setType(dto.getType());
        failureReason.setName(dto.getName());
        return failureReason;
    }

    @Override
    public List<FailureReason> getWithSearch(FailureReasonDTO search) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<FailureReason> criteriaBuilderQuery = criteriaBuilder.createQuery(FailureReason.class);

        Root<FailureReason> failureReasonRoot = criteriaBuilderQuery.from(FailureReason.class);
        List<Predicate> predicates = new ArrayList<>();

        if (search.getId() != null) {
            predicates.add(criteriaBuilder.equal(failureReasonRoot.get("id"), search.getId()));
        }
        if (search.getName() != null) {
            predicates.add(criteriaBuilder.equal(failureReasonRoot.get("name"), search.getName()));
        }
        if (search.getReason() != null) {
            predicates.add(criteriaBuilder.equal(failureReasonRoot.get("reason"), search.getReason()));
        }
        if (search.getType() != null)
        {
            predicates.add(criteriaBuilder.equal(failureReasonRoot.get("type") , search.getType()));
        }

        criteriaBuilderQuery.where(predicates.toArray(new Predicate[0]));

        List<FailureReason> failureReasonList = entityManager.createQuery(criteriaBuilderQuery).getResultList();

        for (String field : search.getSort())
        {
            if (field.equals("id")) {
                Collections.sort(failureReasonList, Comparator.comparing(FailureReason::getId));
            } else if (field.equals("name")) {
                Collections.sort(failureReasonList, Comparator.comparing(FailureReason::getName));
            } else if (field.equals("reason")) {
                Collections.sort(failureReasonList, Comparator.comparing(FailureReason::getReason));
            } else if (field.equals("type")) {
                Collections.sort(failureReasonList, Comparator.comparing(FailureReason::getType));
            }
        }
        return failureReasonList;
    }
}
