package com.airbyte.dorm.record;

import com.airbyte.dorm.common.TimeConverter;
import com.airbyte.dorm.common.ParentService;
import com.airbyte.dorm.dto.RecordDTO;
import com.airbyte.dorm.model.Bed;
import com.airbyte.dorm.model.Record;
import com.airbyte.dorm.model.enums.PenaltyType;
import com.airbyte.dorm.model.people.Person;
import com.airbyte.dorm.person.PersonService;
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
public class RecordService extends ParentService<Record, RecordRepository, RecordDTO> {
    private final PersonService personService;

    public RecordService(RecordRepository repository, PersonService personService) {
        super(repository);
        this.personService = personService;
    }

    @Override
    public Record updateModelFromDto(Record model, RecordDTO dto) {
        model.setDate(dto.getDate() != null ? dto.getDate() : model.getDate());
        model.setDescription(dto.getDescription() != null ? dto.getDescription() : model.getDescription());
        model.setDestinationAddress(dto.getDestinationAddress() != null ? dto.getDestinationAddress() : model.getDestinationAddress());
        model.setDestinationPhoneNumber(dto.getDestinationPhoneNumber() != null ? dto.getDestinationPhoneNumber() : model.getDestinationPhoneNumber());
        model.setTitle(dto.getTitle() != null ? dto.getTitle() : model.getTitle());
        model.setPenaltyAmount(dto.getPenaltyAmount() != null ? dto.getPenaltyAmount() : model.getPenaltyAmount());
        model.setStartDate(dto.getStartDate() != null ? TimeConverter.convertStringToInstant(dto.getStartDate(), TimeConverter.DEFAULT_PATTERN_FORMAT).toInstant() : model.startDate());
        model.setEndDate(dto.getEndDate() != null ? TimeConverter.convertStringToInstant(dto.getEndDate(), TimeConverter.DEFAULT_PATTERN_FORMAT).toInstant() : model.endDate());
        model.setCheckCleaning(dto.getCheckCleaning() != null ? dto.getCheckCleaning() : model.getCheckCleaning());
        model.setHour(dto.getHour() != null ? dto.getHour() : model.getHour());
        model.setPenaltyType(dto.getPenaltyType() != null ? PenaltyType.valueOf(dto.getPenaltyType()) : model.getPenaltyType());
        model.setReturnedAmount(dto.getReturnedAmount() != null ? dto.getReturnedAmount() : model.getReturnedAmount());
        model.setRelation(dto.getRelation() != null ? dto.getRelation() : model.getRelation());
        return model;
    }

    @Override
    public Record convertDTO(RecordDTO dto) {
        Record record = new Record();
        record.setTitle(dto.getTitle());
        record.setPenaltyAmount(dto.getPenaltyAmount());
        record.setDestinationAddress(dto.getDestinationAddress());
        record.setDestinationPhoneNumber(dto.getDestinationPhoneNumber());
        record.setStartDate(dto.getStartDate() != null ? TimeConverter.convertStringToInstant(dto.getStartDate(), TimeConverter.DEFAULT_PATTERN_FORMAT).toInstant() : null);
        record.setEndDate(dto.getEndDate() != null ? TimeConverter.convertStringToInstant(dto.getEndDate(), TimeConverter.DEFAULT_PATTERN_FORMAT).toInstant() : null);
        record.setDescription(dto.getDescription());
        record.setDate(dto.getDate());
        record.setPerson(personService.getOne(dto.getPersonId()));
        record.setCheckCleaning(dto.getCheckCleaning());
        record.setReturnedAmount(dto.getReturnedAmount());
        record.setHour(dto.getHour());
        record.setPenaltyType(dto.getPenaltyType() != null ? PenaltyType.valueOf(dto.getPenaltyType()) : null);
        record.setRelation(dto.getRelation());
        return record;
    }

    @Override
    protected void postSave(Record model, RecordDTO dto) {
        if (model.getTitle().equals("cancelContract")) {
            Person person = model.getPerson();
            Bed bed = person.getBed();
            bed.setPerson(null);
            person.setBed(null);
        }
    }

    @Override
    public List<Record> getWithSearch(RecordDTO search) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Record> criteriaBuilderQuery = criteriaBuilder.createQuery(Record.class);

        Root<Record> RecordRoot = criteriaBuilderQuery.from(Record.class);
        List<Predicate> predicates = new ArrayList<>();

        if (search.getId() != null) {
            predicates.add(criteriaBuilder.equal(RecordRoot.get("id"), search.getId()));
        }
        if (search.getPenaltyAmount() != null) {
            predicates.add(criteriaBuilder.equal(RecordRoot.get("penalty"), search.getPenaltyAmount()));
        }
        if (search.getDescription() != null) {
            predicates.add(criteriaBuilder.equal(RecordRoot.get("description"), search.getDescription()));
        }
        if (search.getDate() != null) {
            predicates.add(criteriaBuilder.equal(RecordRoot.get("date"), search.getDate()));
        }
        if (search.getTitle() != null) {
            predicates.add(criteriaBuilder.equal(RecordRoot.get("title"), search.getTitle()));
        }

        criteriaBuilderQuery.where(predicates.toArray(new Predicate[0]));

        List<Record> recordList = entityManager.createQuery(criteriaBuilderQuery).getResultList();

        for (String field : search.getSort()) {
            if (field.equals("id")) {
                Collections.sort(recordList, Comparator.comparing(Record::getId));
            } else if (field.equals("penalty")) {
                Collections.sort(recordList, Comparator.comparing(Record::getPenaltyAmount));
            } else if (field.equals("description")) {
                Collections.sort(recordList, Comparator.comparing(Record::getDescription));
            }
        }
        return recordList;
    }
}
