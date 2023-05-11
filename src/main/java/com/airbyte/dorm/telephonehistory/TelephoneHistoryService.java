package com.airbyte.dorm.telephonehistory;

import com.airbyte.dorm.common.TimeConverter;
import com.airbyte.dorm.common.ParentService;
import com.airbyte.dorm.dto.TelephoneHistoryDTO;
import com.airbyte.dorm.model.events.TelephoneHistory;
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
public class TelephoneHistoryService extends ParentService<TelephoneHistory, TelephoneHistoryRepository, TelephoneHistoryDTO> {

    public TelephoneHistoryService(TelephoneHistoryRepository repository) {
        super(repository);
    }

    @Override
    public TelephoneHistory updateModelFromDto(TelephoneHistory model, TelephoneHistoryDTO dto) {
        model.setCallerName(dto.getCallerName() != null ? dto.getCallerName() : model.getCallerName());
        model.setPhoneNumber(dto.getPhoneNumber() != null ? dto.getPhoneNumber() : model.getPhoneNumber());
        model.setDescription(dto.getDescription() != null ? dto.getDescription() : model.getDescription());
        model.setTitle(dto.getTitle() != null ? dto.getTitle() : model.getTitle());
        model.setDate(dto.getDate() != null ? TimeConverter.convertStringToInstant(dto.getDate(), TimeConverter.DEFAULT_PATTERN_FORMAT) : model.date());
        return model;
    }

    @Override
    public TelephoneHistory convertDTO(TelephoneHistoryDTO dto) {
        TelephoneHistory telephoneHistory = new TelephoneHistory();
        telephoneHistory.setTitle(dto.getTitle());
        telephoneHistory.setDescription(dto.getDescription());
        telephoneHistory.setPhoneNumber(dto.getPhoneNumber());
        telephoneHistory.setCallerName(dto.getCallerName());
        telephoneHistory.setDate(TimeConverter.convertStringToInstant(dto.getDate(), TimeConverter.DEFAULT_PATTERN_FORMAT));
        return telephoneHistory;
    }

    @Override
    public List<TelephoneHistory> getWithSearch(TelephoneHistoryDTO search) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TelephoneHistory> criteriaBuilderQuery = criteriaBuilder.createQuery(TelephoneHistory.class);

        Root<TelephoneHistory> telephoneHistoryRoot = criteriaBuilderQuery.from(TelephoneHistory.class);
        List<Predicate> predicates = new ArrayList<>();

        if (search.getId() != null) {
            predicates.add(criteriaBuilder.equal(telephoneHistoryRoot.get("id"), search.getId()));
        }
        if (search.getPhoneNumber() != null && !search.getPhoneNumber().isEmpty()) {
            predicates.add(criteriaBuilder.like(telephoneHistoryRoot.get("phoneNumber"), "%" + search.getPhoneNumber() + "%"));
        }
        if (search.getCallerName() != null && !search.getCallerName().isEmpty()) {
            predicates.add(criteriaBuilder.like(telephoneHistoryRoot.get("callerName"), "%" + search.getCallerName() + "%"));
        }
        if (search.getTitle() != null && !search.getTitle().isEmpty()) {
            predicates.add(criteriaBuilder.like(telephoneHistoryRoot.get("title"), "%" + search.getTitle() + "%"));
        }
        if (search.getDate() != null && !search.getDate().isEmpty()) {
            predicates.add(criteriaBuilder.equal(telephoneHistoryRoot.get("date"), TimeConverter.convertStringToInstant(search.getDate(), TimeConverter.DEFAULT_PATTERN_FORMAT)));
        }

        criteriaBuilderQuery.where(predicates.toArray(new Predicate[0]));

        List<TelephoneHistory> telephoneHistoryList = entityManager.createQuery(criteriaBuilderQuery).getResultList();

        for (String field : search.getSort()) {
            if (field.equals("id")) {
                Collections.sort(telephoneHistoryList, Comparator.comparing(TelephoneHistory::getId));
            } else if (field.equals("phoneNumber")) {
                Collections.sort(telephoneHistoryList, Comparator.comparing(TelephoneHistory::getPhoneNumber));
            } else if (field.equals("callerName")) {
                Collections.sort(telephoneHistoryList, Comparator.comparing(TelephoneHistory::getCallerName));
            } else if (field.equals("title")) {
                Collections.sort(telephoneHistoryList, Comparator.comparing(TelephoneHistory::getTitle));
            } else if (field.equals("date")) {
                Collections.sort(telephoneHistoryList, Comparator.comparing(TelephoneHistory::getDate));
            }
        }
        return telephoneHistoryList;
    }
}
