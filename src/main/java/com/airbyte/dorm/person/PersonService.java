package com.airbyte.dorm.person;

import com.airbyte.dorm.characteristic.CharacteristicRepository;
import com.airbyte.dorm.common.ParentService;
import com.airbyte.dorm.dto.PersonDTO;
import com.airbyte.dorm.dto.RelatedObjectDTO;
import com.airbyte.dorm.dto.ResponseFileDTO;
import com.airbyte.dorm.model.Characteristic;
import com.airbyte.dorm.model.RelatedObject;
import com.airbyte.dorm.model.ResponseFile;
import com.airbyte.dorm.model.enums.AccommodationType;
import com.airbyte.dorm.model.enums.ResidenceType;
import com.airbyte.dorm.model.people.Person;
import com.airbyte.dorm.relatedobject.RelatedObjectRepository;
import com.airbyte.dorm.responsefile.ResponseFileService;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PersonService extends ParentService<Person, PersonRepository, PersonDTO> {

    private final RelatedObjectRepository relatedObjectRepository;
    private final CharacteristicRepository characteristicRepository;
    private final ResponseFileService responseFileService;

    public PersonService(PersonRepository repository, RelatedObjectRepository relatedObjectRepository, CharacteristicRepository characteristicRepository, ResponseFileService responseFileService) {
        super(repository);
        this.relatedObjectRepository = relatedObjectRepository;
        this.characteristicRepository = characteristicRepository;
        this.responseFileService = responseFileService;
    }

    @Override
    public Person updateModelFromDto(Person model, PersonDTO dto) {
        model.setCharacteristicId(dto.getCharacteristicId() != null ? dto.getCharacteristicId() : model.getCharacteristicId());
        model.setAccommodationType(dto.getAccommodationType() != null ? AccommodationType.valueOf(dto.getAccommodationType()) : model.getAccommodationType());
        model.setResidenceType(dto.getResidenceType() != null ? ResidenceType.valueOf(dto.getResidenceType()) : model.getResidenceType());
        if (dto.getFiles() != null && !dto.getFiles().isEmpty()) {
            Map<String, String> fileMap = new TreeMap<>();
            dto.getFiles().forEach(fileDTO -> {
                fileMap.put(fileDTO.getName(), fileDTO.getFileId());

                ResponseFileDTO responseFileDTO = new ResponseFileDTO();
                responseFileDTO.setFileId(fileDTO.getFileId());
                responseFileDTO.setParentId(model.getId());
                responseFileDTO.setParentType("Person");
                responseFileDTO.setName(fileDTO.getName());

                List<ResponseFile> responseFileList = responseFileService.getWithSearch(responseFileDTO);
                if (responseFileList == null || responseFileList.isEmpty()) {
                    responseFileService.save(responseFileDTO);
                }
            });
            model.setFiles(fileMap);
        }
        return model;
    }

    @Override
    public Person convertDTO(PersonDTO dto) {
        Person person = new Person();
        person.setCharacteristicId(dto.getCharacteristicId());
        person.setAccommodationType(dto.getAccommodationType() != null ? AccommodationType.valueOf(dto.getAccommodationType()) : null);
        person.setResidenceType(dto.getResidenceType() != null ? ResidenceType.valueOf(dto.getResidenceType()) : null);
        if (dto.getFiles() != null && !dto.getFiles().isEmpty()) {
            Map<String, String> fileMap = new TreeMap<>();
            dto.getFiles().forEach(fileDTO -> fileMap.put(fileDTO.getName(), fileDTO.getFileId()));
            person.setFiles(fileMap);
        }
        return person;
    }

    @Override
    public List<Person> getWithSearch(PersonDTO search) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> criteriaBuilderQuery = criteriaBuilder.createQuery(Person.class);

        Root<Person> personRoot = criteriaBuilderQuery.from(Person.class);
        List<Predicate> predicates = new ArrayList<>();

        if (search.getId() != null) {
            predicates.add(criteriaBuilder.equal(personRoot.get("id"), search.getId()));
        }
        criteriaBuilderQuery.where(predicates.toArray(new Predicate[0]));

        List<Person> personList = entityManager.createQuery(criteriaBuilderQuery).getResultList();

        personList = personList.stream().filter(person -> person.getBed() == null).collect(Collectors.toList());
        return personList;
    }

    public RelatedObject saveRelatedObject(String personId, RelatedObjectDTO dto) {
        RelatedObject relatedObject = new RelatedObject();
        relatedObject.setName(dto.getName());
        relatedObject.setParentId(personId);
        relatedObject.setType(Person.class.getSimpleName());
        relatedObject.setChildId(dto.getChildId());
        return relatedObjectRepository.save(relatedObject);
    }

    @Override
    protected void postSave(Person model, PersonDTO dto) {
        if (dto.getCharacteristicId() != null) {
            Characteristic characteristic = characteristicRepository.findById(dto.getCharacteristicId()).orElse(null);
            assert characteristic != null;
            characteristic.setParentId(model.getId());
            characteristic.setParentType("Person");
            characteristicRepository.save(characteristic);
        }

        if (model.getFiles() != null && !model.getFiles().isEmpty() && model.getFiles().size() > 0) {
            model.getFiles().forEach((name, fileId) -> {
                ResponseFileDTO responseFileDTO = new ResponseFileDTO();
                responseFileDTO.setFileId(fileId);
                responseFileDTO.setParentId(model.getId());
                responseFileDTO.setParentType("Person");
                responseFileDTO.setName(name);
                responseFileService.save(responseFileDTO);
            });
        }
    }
}
