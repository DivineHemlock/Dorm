package com.airbyte.dorm.request;

import com.airbyte.dorm.category.CategoryService;
import com.airbyte.dorm.common.TimeConverter;
import com.airbyte.dorm.common.ParentService;
import com.airbyte.dorm.dto.CategoryDTO;
import com.airbyte.dorm.dto.RequestDTO;
import com.airbyte.dorm.failurereason.FailureReasonService;
import com.airbyte.dorm.model.Category;
import com.airbyte.dorm.model.Request;
import com.airbyte.dorm.model.enums.Status;
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
public class RequestService extends ParentService<Request, RequestRepository, RequestDTO> {
    private final FailureReasonService failureReasonService;
    private final CategoryService categoryService;

    public RequestService(RequestRepository repository, FailureReasonService failureReasonService, CategoryService categoryService) {
        super(repository);
        this.failureReasonService = failureReasonService;
        this.categoryService = categoryService;
    }

    @Override
    public Request updateModelFromDto(Request model, RequestDTO dto) {
        model.setChecked(dto.getChecked() != null ? dto.getChecked() : model.getChecked());
        model.setDateOfRegistration(dto.getDateOfRegistration() != null ? TimeConverter.convertStringToInstant(dto.getDateOfRegistration(), TimeConverter.DEFAULT_PATTERN_FORMAT) : model.dateOfRegistration());
        model.setDescription(dto.getDescription() != null ? dto.getDescription() : model.getDescription());
        model.setType(dto.getType() != null ? dto.getType() : model.getType());
        model.setReason(dto.getReason() != null ? dto.getReason() : model.getReason());
        model.setName(dto.getName() != null ? dto.getName() : model.getName());
        model.setAssignee(dto.getAssignee() != null ? dto.getAssignee() : model.getAssignee());
//        model.setSupervisor(dto.getSupervisorId() != null ? (Supervisor) humanService.getOne(dto.getSupervisorId()) : (Supervisor) model.getSupervisor());
        model.setFailureReason(dto.getFailureReasonId() != null ? failureReasonService.getOne(dto.getFailureReasonId()) : model.getFailureReason());
        model.setStatus(dto.getStatus() != null ? Status.valueOf(dto.getStatus()) : model.getStatus());
        model.setStatusDescription(dto.getStatusDescription() != null ? dto.getStatusDescription() : model.getStatusDescription());
        return model;
    }

    @Override
    public Request convertDTO(RequestDTO dto) {
        Request request = new Request();
        request.setName(dto.getName());
        request.setReason(dto.getReason());
        request.setType(dto.getType());
        request.setDescription(dto.getDescription());
        request.setChecked(dto.getChecked());
        request.setDateOfRegistration(TimeConverter.convertStringToInstant(dto.getDateOfRegistration(), TimeConverter.DEFAULT_PATTERN_FORMAT));
        request.setAssignee(dto.getAssignee());
//        request.setSupervisor((Supervisor) humanService.getOne(dto.getSupervisorId())); TODO : FIXME
        request.setFailureReason(dto.getFailureReasonId() != null ? failureReasonService.getOne(dto.getFailureReasonId()) : null);
        request.setStatus(dto.getStatus() != null ? Status.valueOf(dto.getStatus()) : null);
        request.setStatusDescription(dto.getStatusDescription());
        return request;
    }

    @Override
    protected void preSave(RequestDTO dto) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName(dto.getType());
        categoryDTO.setType(Request.class.getSimpleName());

        List<Category> categoryList = categoryService.getWithSearch(categoryDTO);
        if (categoryList.size() == 0) {
            categoryService.save(categoryDTO);
        }
    }

    @Override
    public List<Request> getWithSearch(RequestDTO search) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Request> criteriaBuilderQuery = criteriaBuilder.createQuery(Request.class);

        Root<Request> requestRoot = criteriaBuilderQuery.from(Request.class);
        List<Predicate> predicates = new ArrayList<>();

        if (search.getId() != null) {
            predicates.add(criteriaBuilder.equal(requestRoot.get("id"), search.getId()));
        }
        if (search.getDateOfRegistration() != null) {
            predicates.add(criteriaBuilder.equal(requestRoot.get("dateOfRegistration"), search.getDateOfRegistration()));
        }
        if (search.getChecked() != null) {
            predicates.add(criteriaBuilder.equal(requestRoot.get("checked"), search.getChecked()));
        }
        if (search.getAssignee() != null && !search.getAssignee().isEmpty()) {
            predicates.add(criteriaBuilder.like(requestRoot.get("assignee"), "%" + search.getAssignee() + "%"));
        }
        if (search.getType() != null && !search.getType().isEmpty()) {
            predicates.add(criteriaBuilder.like(requestRoot.get("type"), "%" + search.getType() + "%"));
        }
        if (search.getName() != null && !search.getName().isEmpty()) {
            predicates.add(criteriaBuilder.like(requestRoot.get("name"), "%" + search.getName() + "%"));
        }

        criteriaBuilderQuery.where(predicates.toArray(new Predicate[0]));

        List<Request> requestList = entityManager.createQuery(criteriaBuilderQuery).getResultList();

        for (String field : search.getSort()) {
            if (field.equals("id")) {
                Collections.sort(requestList, Comparator.comparing(Request::getId));
            } else if (field.equals("dateOfRegistration")) {
                Collections.sort(requestList, Comparator.comparing(Request::getDateOfRegistration));
            } else if (field.equals("checked")) {
                Collections.sort(requestList, Comparator.comparing(Request::getChecked));
            } else if (field.equals("person")) {
//                Collections.sort(requestList, Comparator.comparing(l->l.getPerson().getCharacteristic().getFirstName()));
            }
        }
        return requestList;
    }
}
