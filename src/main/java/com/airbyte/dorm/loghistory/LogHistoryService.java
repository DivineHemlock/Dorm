package com.airbyte.dorm.loghistory;

import com.airbyte.dorm.common.ParentService;
import com.airbyte.dorm.dto.LogHistoryDTO;
import com.airbyte.dorm.model.LogHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogHistoryService extends ParentService<LogHistory, LogHistoryRepository, LogHistoryDTO> {

    public LogHistoryService(LogHistoryRepository repository) {
        super(repository);
    }

    @Override
    public LogHistory updateModelFromDto(LogHistory model, LogHistoryDTO dto) {
        model.setDoer(dto.getDoer() != null ? dto.getDoer() : model.getDoer());
        model.setAction(dto.getAction() != null ? dto.getAction() : model.getAction());
        model.setUrl(dto.getUrl() != null ? dto.getUrl() : model.getUrl());
        model.setDate(dto.getDate() != null ? dto.getDate() : model.getDate());
        model.setHour(dto.getHour() != null ? dto.getHour() : model.getHour());
        model.setCategory(dto.getCategory() != null ? dto.getCategory() : model.getCategory());
        return model;
    }

    private void manageUrl(LogHistoryDTO dto) {
        String url = dto.getUrl();

        if (url.contains("floor") ||
                url.contains("bed") ||
                url.contains("room") ||
                url.contains("unit")) {
            dto.setCategory("موجودی و ظرفیت");

        } else if (url.contains("characteristic")) {
            dto.setCategory("مشخصات شخص (اشخاص، پذیرش، پرسنل)");

        } else if (url.contains("logHistory")) {
            dto.setCategory("تاریخچه کارها");

        } else if (url.contains("notification")) {
            dto.setCategory("رویداد");

        } else if (url.contains("phoneBook")) {
            dto.setCategory("مخاطبین");

        } else if (url.contains("telephoneHistory")) {
            dto.setCategory("تاریخچه تماس");

        } else if (url.contains("task")) {
            dto.setCategory("مدیریت وظایف");

        } else if (url.contains("paymentHistory")) {
            dto.setCategory("فاکتور(ثبت، صورتحساب)");

        } else if (url.contains("account")) {
            dto.setCategory("صورتحساب");

        } else if (url.contains("request")) {
            dto.setCategory("درخواست");

        } else if (url.contains("inventory")) {
            dto.setCategory("انبار");

        } else if (url.contains("record")) {
            dto.setCategory("سوابق");

        } else if (url.contains("person")) {
            dto.setCategory("اشخاص");

        } else if (url.contains("personnel")) {
            dto.setCategory("پرسنل");

        } else if (url.contains("relatedObject")) {
            dto.setCategory("مهمان");

        } else if (url.contains("manager")) {
            dto.setCategory("مدیریت");

        } else if (url.contains("failureReason")) {
            dto.setCategory("علت شکست");

        } else if (url.contains("email")) {
            dto.setCategory("ایمیل");

        } else if (url.contains("changePassword")) {
            dto.setCategory("تغییر پسورد");

        } else if (url.contains("ceremony")) {
            dto.setCategory("مراسمات");

        } else if (url.contains("category")) {
            dto.setCategory("دسته بندی");

        } else if (url.contains("cameraHistory")) {
            dto.setCategory("دوربین");

        }

    }

    @Override
    public LogHistory convertDTO(LogHistoryDTO dto) {
        LogHistory logHistory = new LogHistory();
        logHistory.setDoer(dto.getDoer());
        logHistory.setAction(dto.getAction());
        logHistory.setUrl(dto.getUrl());
        logHistory.setDate(dto.getDate());
        logHistory.setHour(dto.getHour());
        manageUrl(dto);
        logHistory.setCategory(dto.getCategory());
        return logHistory;
    }

    @Override
    public List<LogHistory> getWithSearch(LogHistoryDTO search) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<LogHistory> criteriaBuilderQuery = criteriaBuilder.createQuery(LogHistory.class);

        Root<LogHistory> logHistoryRoot = criteriaBuilderQuery.from(LogHistory.class);
        List<Predicate> predicates = new ArrayList<>();

        if (search.getDate() != null) {
            predicates.add(criteriaBuilder.equal(logHistoryRoot.get("date"), search.getDate()));
        }
        if (search.getAction() != null) {
            predicates.add(criteriaBuilder.equal(logHistoryRoot.get("action"), search.getAction()));
        }
        if (search.getDoer() != null && !search.getDoer().isEmpty()) {
            predicates.add(criteriaBuilder.like(logHistoryRoot.get("doer"), "%" + search.getDoer() + "%"));
        }

        criteriaBuilderQuery.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(criteriaBuilderQuery).getResultList();
    }

    public Page<LogHistory> getAll(Pageable pageable) {
        List<LogHistory> modelList = repository.findAll();

        modelList = modelList.stream()
                .sorted(Comparator.comparing(LogHistory::getRegisterDate).reversed())
                .collect(Collectors.toList());

        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), modelList.size());
        final Page<LogHistory> page = new PageImpl<>(modelList.subList(start, end), pageable, modelList.size());

        return page;
    }

    public Page<LogHistory> search(Pageable pageable, LogHistoryDTO search) {
        List<LogHistory> modelList = this.getWithSearch(search);

        modelList = modelList.stream()
                .sorted(Comparator.comparing(LogHistory::getRegisterDate).reversed())
                .collect(Collectors.toList());

        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), modelList.size());
        final Page<LogHistory> page = new PageImpl<>(modelList.subList(start, end), pageable, modelList.size());

        return page;
    }
}
