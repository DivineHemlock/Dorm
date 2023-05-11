package com.airbyte.dorm.notification;

import com.airbyte.dorm.common.TimeConverter;
import com.airbyte.dorm.common.ParentService;
import com.airbyte.dorm.dto.NotificationDTO;
import com.airbyte.dorm.model.Notification;
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
public class NotificationService extends ParentService <Notification , NotificationRepository , NotificationDTO> {
    public NotificationService(NotificationRepository repository) {
        super(repository);
    }

    @Override
    public Notification updateModelFromDto(Notification model, NotificationDTO dto) {
        model.setEventName(dto.getEventName() != null ? dto.getEventName() : model.getEventName());
        model.setEventDescription(dto.getEventDescription() != null ? dto.getEventDescription() : model.getEventDescription());
        model.setDate(dto.getDate() != null ? TimeConverter.convertStringToInstant(dto.getDate(), TimeConverter.DEFAULT_PATTERN_FORMAT) : model.date());
        return model;
    }

    @Override
    public Notification convertDTO(NotificationDTO dto) {
        Notification notification = new Notification();
        notification.setEventName(dto.getEventName());
        notification.setEventDescription(dto.getEventDescription());
        notification.setDate(TimeConverter.convertStringToInstant(dto.getDate(), TimeConverter.DEFAULT_PATTERN_FORMAT));
        return notification;
    }

    @Override
    public List<Notification> getWithSearch(NotificationDTO search) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Notification> criteriaBuilderQuery = criteriaBuilder.createQuery(Notification.class);

        Root<Notification> NotificationRoot = criteriaBuilderQuery.from(Notification.class);
        List<Predicate> predicates = new ArrayList<>();

        if (search.getId() != null) {
            predicates.add(criteriaBuilder.equal(NotificationRoot.get("id"), search.getId()));
        }
        if (search.getDate() != null) {
            predicates.add(criteriaBuilder.equal(NotificationRoot.get("date") , TimeConverter.convertStringToInstant(search.getDate(), TimeConverter.DEFAULT_PATTERN_FORMAT)));
        }
        if (search.getEventName() != null) {
            predicates.add(criteriaBuilder.equal(NotificationRoot.get("eventName"), search.getEventName()));
        }


        criteriaBuilderQuery.where(predicates.toArray(new Predicate[0]));

        List<Notification> notificationList = entityManager.createQuery(criteriaBuilderQuery).getResultList();

        for (String field : search.getSort()) {
            if (field.equals("id")) {
                Collections.sort(notificationList, Comparator.comparing(Notification::getId));
            } else if (field.equals("date")) {
                Collections.sort(notificationList, Comparator.comparing(Notification::getDate));
            } else if (field.equals("eventName")) {
                Collections.sort(notificationList,Comparator.comparing(Notification::getEventName));
            }

        }
        return notificationList;
    }

}
