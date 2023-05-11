package com.airbyte.dorm.phonebook;

import com.airbyte.dorm.common.ParentService;
import com.airbyte.dorm.dto.PhoneBookDTO;
import com.airbyte.dorm.model.PhoneBook;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PhoneBookService extends ParentService<PhoneBook, PhoneBookRepository, PhoneBookDTO> {
    public PhoneBookService(PhoneBookRepository repository) {
        super(repository);
    }

    @Override
    public PhoneBook updateModelFromDto(PhoneBook model, PhoneBookDTO dto) {
        model.setTelephoneNumbers(dto.getTelephoneNumbers() != null ? dto.getTelephoneNumbers() : model.getTelephoneNumbers());
        model.setMobileNumbers(dto.getMobileNumbers() != null ? dto.getMobileNumbers() : model.getMobileNumbers());
        model.setName(dto.getName() != null ? dto.getName() : model.getName());
        return model;
    }

    @Override
    public PhoneBook convertDTO(PhoneBookDTO dto) {
        PhoneBook phoneBook = new PhoneBook();
        phoneBook.setName(dto.getName());
        phoneBook.setTelephoneNumbers(dto.getTelephoneNumbers());
        phoneBook.setMobileNumbers(dto.getMobileNumbers());
        return phoneBook;
    }

    @Override
    public List<PhoneBook> getWithSearch(PhoneBookDTO search) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PhoneBook> criteriaBuilderQuery = criteriaBuilder.createQuery(PhoneBook.class);

        Root<PhoneBook> phoneBookRoot = criteriaBuilderQuery.from(PhoneBook.class);
        List<Predicate> predicates = new ArrayList<>();

        if (search.getId() != null) {
            predicates.add(criteriaBuilder.equal(phoneBookRoot.get("id"), search.getId()));
        }
        if (search.getName() != null) {
            predicates.add(criteriaBuilder.like(phoneBookRoot.get("name"), "%" + search.getName() + "%"));
        }
        if (search.getTelephoneNumbers() != null && !search.getTelephoneNumbers().isEmpty()) {
            return repository.findAll().stream()
                    .filter(phoneBook -> phoneBook.getTelephoneNumbers().stream()
                            .anyMatch(telephone -> telephone.contains(search.getTelephoneNumbers().get(0)))
                    ).collect(Collectors.toList());
        }
        if (search.getMobileNumbers() != null && !search.getMobileNumbers().isEmpty()) {
            return repository.findAll().stream()
                    .filter(phoneBook -> phoneBook.getMobileNumbers().stream()
                            .anyMatch(mobile -> mobile.contains(search.getMobileNumbers().get(0)))
                    ).collect(Collectors.toList());
        }

        criteriaBuilderQuery.where(predicates.toArray(new Predicate[0]));

        List<PhoneBook> phoneBookList = entityManager.createQuery(criteriaBuilderQuery).getResultList();

        for (String field : search.getSort()) {
            if (field.equals("id")) {
                Collections.sort(phoneBookList, Comparator.comparing(PhoneBook::getId));
            } else if (field.equals("name")) {
                Collections.sort(phoneBookList, Comparator.comparing(PhoneBook::getName));
            }
        }
        return phoneBookList;
    }
}
