package com.airbyte.dorm.task;

import com.airbyte.dorm.common.TimeConverter;
import com.airbyte.dorm.common.ParentService;
import com.airbyte.dorm.dto.TaskDTO;
import com.airbyte.dorm.model.Task;
import com.airbyte.dorm.model.enums.Priority;
import com.airbyte.dorm.model.enums.Status;
import com.airbyte.dorm.personnel.PersonnelService;
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
public class TaskService extends ParentService<Task, TaskRepository, TaskDTO> {
    private final PersonnelService personnelService;

    public TaskService(TaskRepository repository, PersonnelService personnelService) {
        super(repository);
        this.personnelService = personnelService;
    }

    @Override
    public Task updateModelFromDto(Task model, TaskDTO dto) {
        model.setName(dto.getName() != null ? dto.getName() : model.getName());
        model.setDescription(dto.getDescription() != null ? dto.getDescription() : model.getDescription());
        model.setDueDate(dto.getDueDate() != null ? TimeConverter.convertStringToInstant(dto.getDueDate(), TimeConverter.DEFAULT_PATTERN_FORMAT) : model.dueDate());
        model.setTimeLog(dto.getTimeLog() != null ? dto.getTimeLog() : model.getTimeLog());
        model.setPriority(dto.getPriority() != null ? Priority.valueOf(dto.getPriority()) : model.getPriority());
        model.setStatus(dto.getStatus() != null ? Status.valueOf(dto.getStatus()) : model.getStatus());
        model.setFullName(dto.getFullName() != null ? dto.getFullName() : model.getFullName());
        return model;
    }

    @Override
    public Task convertDTO(TaskDTO dto) {
        Task task = new Task();
        task.setName(dto.getName());
        task.setDescription(dto.getDescription());
        task.setPriority(dto.getPriority() != null ? Priority.valueOf(dto.getPriority()) : null);
        task.setStatus(dto.getStatus() != null ? Status.valueOf(dto.getStatus()) : null);
        task.setDueDate(TimeConverter.convertStringToInstant(dto.getDueDate(), TimeConverter.DEFAULT_PATTERN_FORMAT));
        task.setTimeLog(dto.getTimeLog());
        task.setPersonnel(dto.getPersonnelId() != null ? personnelService.getOne(dto.getPersonnelId()) : null);
        task.setFullName(dto.getFullName());
        return task;
    }

    @Override
    public List<Task> getWithSearch(TaskDTO search) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> criteriaBuilderQuery = criteriaBuilder.createQuery(Task.class);

        Root<Task> TaskRoot = criteriaBuilderQuery.from(Task.class);
        List<Predicate> predicates = new ArrayList<>();

        if (search.getId() != null) {
            predicates.add(criteriaBuilder.equal(TaskRoot.get("id"), search.getId()));
        }
        if (search.getDueDate() != null) {
            predicates.add(criteriaBuilder.equal(TaskRoot.get("dueDate"), TimeConverter.convertStringToInstant(search.getDueDate(), TimeConverter.DEFAULT_PATTERN_FORMAT)));
        }
        if (search.getStatus() != null) {
            predicates.add(criteriaBuilder.equal(TaskRoot.get("status"), Status.valueOf(search.getStatus())));
        }
        if (search.getPriority() != null) {
            predicates.add(criteriaBuilder.equal(TaskRoot.get("priority"), Priority.valueOf(search.getPriority())));
        }
        if (search.getFullName() != null && !search.getFullName().isEmpty()) {
            predicates.add(criteriaBuilder.like(TaskRoot.get("fullName"), "%" + search.getFullName() + "%"));
        }
        if (search.getName() != null && !search.getName().isEmpty()) {
            predicates.add(criteriaBuilder.like(TaskRoot.get("name"), "%" + search.getName() + "%"));
        }


        criteriaBuilderQuery.where(predicates.toArray(new Predicate[0]));

        List<Task> taskList = entityManager.createQuery(criteriaBuilderQuery).getResultList();

        for (String field : search.getSort()) {
            if (field.equals("id")) {
                Collections.sort(taskList, Comparator.comparing(Task::getId));
            } else if (field.equals("dueDate")) {
                Collections.sort(taskList, Comparator.comparing(Task::getDueDate));
            } else if (field.equals("priority")) {
                Collections.sort(taskList, Comparator.comparing(Task::getPriority));
            } else if (field.equals("status")) {
                Collections.sort(taskList, Comparator.comparing(Task::getStatus));
            }

        }
        return taskList;
    }

}
