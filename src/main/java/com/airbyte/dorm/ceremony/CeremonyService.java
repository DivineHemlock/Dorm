package com.airbyte.dorm.ceremony;

import com.airbyte.dorm.common.TimeConverter;
import com.airbyte.dorm.common.ParentService;
import com.airbyte.dorm.dto.CeremonyDTO;
import com.airbyte.dorm.model.events.Ceremony;
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
public class CeremonyService extends ParentService <Ceremony , CeremonyRepository , CeremonyDTO> {
    public CeremonyService(CeremonyRepository repository) {
        super(repository);
    }

    @Override
    public Ceremony updateModelFromDto(Ceremony model, CeremonyDTO dto) {
        model.setTitle(dto.getTitle() != null ? dto.getTitle() : model.getTitle());
        model.setDescription(dto.getDescription() != null ? dto.getDescription() : model.getDescription());
        model.setDate(dto.getDate() != null ? TimeConverter.convertStringToInstant(dto.getDate(), TimeConverter.DEFAULT_PATTERN_FORMAT) : model.date());
        return model;
    }

    @Override
    public Ceremony convertDTO(CeremonyDTO dto) {
        Ceremony ceremony = new Ceremony();
        ceremony.setDate(TimeConverter.convertStringToInstant(dto.getDate(), TimeConverter.DEFAULT_PATTERN_FORMAT));
        ceremony.setDescription(dto.getDescription());
        ceremony.setTitle(dto.getTitle());
        return ceremony;
    }

    @Override
    public List<Ceremony> getWithSearch(CeremonyDTO search) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Ceremony> criteriaBuilderQuery = criteriaBuilder.createQuery(Ceremony.class);

        Root<Ceremony> ceremonyRoot = criteriaBuilderQuery.from(Ceremony.class);
        List<Predicate> predicates = new ArrayList<>();

        if (search.getId() != null) {
            predicates.add(criteriaBuilder.equal(ceremonyRoot.get("id"), search.getId()));
        }
        if (search.getDate() != null) {
            predicates.add(criteriaBuilder.equal(ceremonyRoot.get("date") , Instant.parse(search.getDate())));
        }
        if (search.getTitle() != null) {
            predicates.add(criteriaBuilder.equal(ceremonyRoot.get("title"), search.getTitle()));
        }


        criteriaBuilderQuery.where(predicates.toArray(new Predicate[0]));

        List<Ceremony> ceremonyList = entityManager.createQuery(criteriaBuilderQuery).getResultList();

        for (String field : search.getSort()) {
            if (field.equals("id")) {
                Collections.sort(ceremonyList, Comparator.comparing(Ceremony::getId));
            } else if (field.equals("date")) {
                Collections.sort(ceremonyList, Comparator.comparing(Ceremony::getDate));
            } else if (field.equals("title")) {
                Collections.sort(ceremonyList,Comparator.comparing(Ceremony::getTitle));
            }

        }
        return ceremonyList;
    }
}
