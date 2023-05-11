package com.airbyte.dorm.paymenthistory;

import com.airbyte.dorm.account.AccountRepository;
import com.airbyte.dorm.category.CategoryService;
import com.airbyte.dorm.common.TimeConverter;
import com.airbyte.dorm.common.ParentService;
import com.airbyte.dorm.dto.CategoryDTO;
import com.airbyte.dorm.dto.PaymentHistoryDTO;
import com.airbyte.dorm.dto.PaymentHistoryQueryDTO;
import com.airbyte.dorm.model.Account;
import com.airbyte.dorm.model.Category;
import com.airbyte.dorm.model.Money;
import com.airbyte.dorm.model.PaymentHistory;
import com.airbyte.dorm.model.enums.PaymentType;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.*;

@Service
public class PaymentHistoryService extends ParentService<PaymentHistory, PaymentHistoryRepository, PaymentHistoryDTO> {
    private final CategoryService categoryService;
    private final AccountRepository accountRepository;

    public PaymentHistoryService(PaymentHistoryRepository repository, CategoryService categoryService, AccountRepository accountRepository) {
        super(repository);
        this.categoryService = categoryService;
        this.accountRepository = accountRepository;
    }

    @Override
    public PaymentHistory updateModelFromDto(PaymentHistory model, PaymentHistoryDTO dto) {
        model.setDate(dto.getDate() != null ? TimeConverter.convertStringToInstant(dto.getDate(), TimeConverter.DEFAULT_PATTERN_FORMAT) : TimeConverter.convertStringToInstant(model.getDate(), TimeConverter.DEFAULT_PATTERN_FORMAT));
        model.setAmount(dto.getValue() != null ? (new Money(dto.getUnit(), new BigDecimal(dto.getValue()))) : model.getAmount());
        model.setPaymentType(dto.getPaymentType() != null ? PaymentType.valueOf(dto.getPaymentType()) : model.getPaymentType());
        model.setType(dto.getType() != null ? dto.getType() : model.getType());
        model.setDescription(dto.getDescription() != null ? dto.getDescription() : model.getDescription());
        model.setParentType(dto.getParentType() != null ? dto.getParentType() : model.getParentType());
        model.setParentId(dto.getParentId() != null ? dto.getParentId() : model.getParentId());

        if (dto.getFile() != null) {
            Map<String, String> file = new TreeMap<>();
            file.put(dto.getFile().getName(), dto.getFile().getFileId());
            model.setFile(file);
        }
        return model;
    }

    @Override
    public PaymentHistory convertDTO(PaymentHistoryDTO dto) {
        PaymentHistory paymentHistory = new PaymentHistory();
        paymentHistory.setAmount(dto.getValue() != null ? (new Money(dto.getUnit(), new BigDecimal(dto.getValue()))) : null);
        paymentHistory.setDate(TimeConverter.convertStringToInstant(dto.getDate(), TimeConverter.DEFAULT_PATTERN_FORMAT));
        paymentHistory.setPaymentType(dto.getPaymentType() != null ? PaymentType.valueOf(dto.getPaymentType()) : null);
        paymentHistory.setType(dto.getType());
        paymentHistory.setDescription(dto.getDescription());
        paymentHistory.setParentType(dto.getParentType());
        paymentHistory.setParentId(dto.getParentId());

        if (dto.getFile() != null) {
            Map<String, String> file = new TreeMap<>();
            file.put(dto.getFile().getName(), dto.getFile().getFileId());
            paymentHistory.setFile(file);
        }
        return paymentHistory;
    }

    @Override
    protected void preSave(PaymentHistoryDTO dto) {
        checkType(dto);
    }

    @Override
    protected void postSave(PaymentHistory model, PaymentHistoryDTO dto) {
        handleAccount(model);
    }

    private void handleAccount(PaymentHistory model) {
        List<Account> accountList = accountRepository.findAll();

        if (accountList == null || accountList.isEmpty()) {

            Account account = new Account();
            if (model.getPaymentType() == PaymentType.receive) {
                account.setTotalReceived(model.getAmount().getValue());
            } else {
                account.setTotalPayment(model.getAmount().getValue());
            }
            account.setName("حساب خوابگاه");
            accountRepository.save(account);

        } else {

            Account account = accountList.get(0);
            if (model.getPaymentType() == PaymentType.receive) {
                account.setTotalReceived(account.getTotalReceived().add(model.getAmount().getValue()));
            } else {
                account.setTotalPayment(account.getTotalPayment().add(model.getAmount().getValue()));
            }
            accountRepository.save(account);

        }
    }

    @Override
    protected void preDelete(PaymentHistory model) {
        handleAccountDelete(model);
    }

    private void handleAccountDelete(PaymentHistory model) {
        List<Account> accountList = accountRepository.findAll();
        Account account = accountList.get(0);

        if (model.getPaymentType() == PaymentType.receive) {
            account.setTotalReceived(account.getTotalReceived().add(model.getAmount().getValue().negate()));
        } else {
            account.setTotalPayment(account.getTotalPayment().add(model.getAmount().getValue().negate()));
        }
        accountRepository.save(account);
    }

    private void checkType(PaymentHistoryDTO dto) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setType("Payment");
        categoryDTO.setName(dto.getType());
        List<Category> categories = categoryService.getWithSearch(categoryDTO);
        if (categories == null || categories.isEmpty()) {
            categoryService.save(categoryDTO);
        }
    }

    @Override
    public List<PaymentHistory> getWithSearch(PaymentHistoryDTO search) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PaymentHistory> criteriaBuilderQuery = criteriaBuilder.createQuery(PaymentHistory.class);

        Root<PaymentHistory> paymentHistoryRoot = criteriaBuilderQuery.from(PaymentHistory.class);
        List<Predicate> predicates = new ArrayList<>();

        if (search.getId() != null) {
            predicates.add(criteriaBuilder.equal(paymentHistoryRoot.get("id"), search.getId()));
        }
        if (search.getType() != null) {
            predicates.add(criteriaBuilder.equal(paymentHistoryRoot.get("name"), search.getType()));
        }
        if (search.getPaymentType() != null) {
            predicates.add(criteriaBuilder.equal(paymentHistoryRoot.get("paymentType"), search.getPaymentType()));
        }

        criteriaBuilderQuery.where(predicates.toArray(new Predicate[0]));

        List<PaymentHistory> paymentHistoryList = entityManager.createQuery(criteriaBuilderQuery).getResultList();

        for (String field : search.getSort()) {
            if (field.equals("id")) {
                Collections.sort(paymentHistoryList, Comparator.comparing(PaymentHistory::getId));
            } else if (field.equals("type")) {
                Collections.sort(paymentHistoryList, Comparator.comparing(PaymentHistory::getType));
            } else if (field.equals("paymentType")) {
                Collections.sort(paymentHistoryList, Comparator.comparing(l -> l.getPaymentType().name()));
            }
        }
        return paymentHistoryList;
    }

    public List<PaymentHistory> getAllBySpecificQueryByTimePeriod(PaymentHistoryQueryDTO dto) {
        Date startDate = TimeConverter.convertStringToInstant(dto.getStartDate(), TimeConverter.DEFAULT_PATTERN_FORMAT);
        Date endDate = TimeConverter.convertStringToInstant(dto.getEndDate(), TimeConverter.DEFAULT_PATTERN_FORMAT);

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PaymentHistory> criteriaBuilderQuery = criteriaBuilder.createQuery(PaymentHistory.class);

        Root<PaymentHistory> paymentHistoryRoot = criteriaBuilderQuery.from(PaymentHistory.class);
        List<Predicate> predicates = new ArrayList<>();

        if (dto.getPaymentType() != null) {
            CriteriaBuilder.In<PaymentType> inClause = criteriaBuilder.in(paymentHistoryRoot.get("paymentType"));
            for (String type : dto.getPaymentType()) {
                inClause.value(PaymentType.valueOf(type));
            }
            predicates.add(inClause);
        }
        if (startDate != null && endDate != null) {
            predicates.add(criteriaBuilder.between(paymentHistoryRoot.get("date"), startDate, endDate));
        }

        criteriaBuilderQuery.where(predicates.toArray(new Predicate[0]));

        List<PaymentHistory> resultList = entityManager.createQuery(criteriaBuilderQuery).getResultList();

        if (dto.getCount() != null) {
            resultList.sort(Comparator.comparing(PaymentHistory::getDate));
            resultList.sort(Collections.reverseOrder());
            resultList = resultList.subList(0, dto.getCount().intValue());
        }

        return resultList;
    }

}
