package com.airbyte.dorm.personnel;

import com.airbyte.dorm.category.CategoryService;
import com.airbyte.dorm.characteristic.CharacteristicRepository;
import com.airbyte.dorm.characteristic.CharacteristicService;
import com.airbyte.dorm.common.ParentService;
import com.airbyte.dorm.dto.CategoryDTO;
import com.airbyte.dorm.dto.PersonnelDTO;
import com.airbyte.dorm.dto.ResponseFileDTO;
import com.airbyte.dorm.dto.SupervisorDTO;
import com.airbyte.dorm.email.EmailService;
import com.airbyte.dorm.model.Category;
import com.airbyte.dorm.model.Characteristic;
import com.airbyte.dorm.model.ResponseFile;
import com.airbyte.dorm.model.enums.Gender;
import com.airbyte.dorm.model.enums.ResidenceType;
import com.airbyte.dorm.model.people.Personnel;
import com.airbyte.dorm.model.people.Supervisor;
import com.airbyte.dorm.responsefile.ResponseFileService;
import com.airbyte.dorm.supervisor.SupervisorService;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Service
public class PersonnelService extends ParentService<Personnel, PersonnelRepository, PersonnelDTO> {
    private final CategoryService categoryService;
    private final SupervisorService supervisorService;
    private final CharacteristicService characteristicService;
    private final EmailService emailService;
    private final ResponseFileService responseFileService;
    private final CharacteristicRepository characteristicRepository;

    public PersonnelService(PersonnelRepository repository, CategoryService categoryService, SupervisorService supervisorService, CharacteristicService characteristicService, EmailService emailService, ResponseFileService responseFileService, CharacteristicRepository characteristicRepository) {
        super(repository);
        this.categoryService = categoryService;
        this.supervisorService = supervisorService;
        this.characteristicService = characteristicService;
        this.emailService = emailService;
        this.responseFileService = responseFileService;
        this.characteristicRepository = characteristicRepository;
    }

    @Override
    public Personnel updateModelFromDto(Personnel model, PersonnelDTO dto) {
        model.setCharacteristicId(dto.getCharacteristicId() != null ? dto.getCharacteristicId() : model.getCharacteristicId());
        model.setGender(dto.getGender() != null ? Gender.valueOf(dto.getGender()) : model.getGender());
        model.setResidenceType(dto.getResidenceType() != null ? ResidenceType.valueOf(dto.getResidenceType()) : model.getResidenceType());
        model.setType(dto.getType() != null ? dto.getType() : model.getType());

        if (dto.getFiles() != null) {
            Map<String, String> fileMap = new TreeMap<>();
            dto.getFiles().forEach(fileDTO -> {
                fileMap.put(fileDTO.getName(), fileDTO.getFileId());

                ResponseFileDTO responseFileDTO = new ResponseFileDTO();
                responseFileDTO.setFileId(fileDTO.getFileId());
                responseFileDTO.setParentId(model.getId());
                responseFileDTO.setParentType("Personnel");
                responseFileDTO.setName(fileDTO.getName());

                List<ResponseFile> responseFileList = responseFileService.getWithSearch(responseFileDTO);
                if (responseFileList == null || responseFileList.isEmpty()) {
                    responseFileService.save(responseFileDTO);
                }
            });
            model.setFiles(fileMap);
        } else {
            model.setFiles(model.getFiles());
        }
        return model;
    }

    @Override
    public Personnel convertDTO(PersonnelDTO dto) {
        Personnel personnel = new Personnel();
        personnel.setCharacteristicId(dto.getCharacteristicId());
        personnel.setGender(dto.getGender() != null ? Gender.valueOf(dto.getGender()) : null);
        personnel.setResidenceType(dto.getResidenceType() != null ? ResidenceType.valueOf(dto.getResidenceType()) : null);
        personnel.setType(dto.getType());

        if (dto.getFiles() != null) {
            Map<String, String> fileMap = new TreeMap<>();
            dto.getFiles().forEach(fileDTO -> fileMap.put(fileDTO.getName(), fileDTO.getFileId()));
            personnel.setFiles(fileMap);
        }

        return personnel;
    }

    @Override
    protected void preSave(PersonnelDTO dto) {
        checkType(dto);
    }

    private void checkType(PersonnelDTO dto) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setType("Personnel");
        categoryDTO.setName(dto.getType());
        List<Category> categories = categoryService.getWithSearch(categoryDTO);
        if (categories == null || categories.isEmpty()) {
            categoryService.save(categoryDTO);
        }
    }

    @Override
    protected void postSave(Personnel model, PersonnelDTO dto) {
        if (dto.getType().equals("سرپرست")) {
            SupervisorDTO supervisorDTO = new SupervisorDTO();
            Characteristic characteristic = characteristicService.getOne(model.getCharacteristicId());
            supervisorDTO.setProfileId(characteristic.getProfileId());
            supervisorDTO.setFullName(characteristic.getFirstName() + " " + characteristic.getLastName());
            supervisorDTO.setEmail(characteristic.getEmail());
            supervisorDTO.setUsername(characteristic.getFirstName() + " " + characteristic.getLastName());
            supervisorDTO.setPassword(generatePassword(10));
            Supervisor supervisor = supervisorService.save(supervisorDTO);
            emailService.sendInformation(supervisor);
        }

        if (model.getFiles() != null && !model.getFiles().isEmpty() && model.getFiles().size() > 0) {
            model.getFiles().forEach((name, fileId) -> {
                ResponseFileDTO responseFileDTO = new ResponseFileDTO();
                responseFileDTO.setFileId(fileId);
                responseFileDTO.setParentId(model.getId());
                responseFileDTO.setParentType("Personnel");
                responseFileDTO.setName(name);
                responseFileService.save(responseFileDTO);
            });
        }

        if (dto.getCharacteristicId() != null) {
            Characteristic characteristic = characteristicRepository.findById(dto.getCharacteristicId()).orElse(null);
            assert characteristic != null;
            characteristic.setParentId(model.getId());
            characteristic.setParentType("Personnel");
            characteristicRepository.save(characteristic);
        }
    }

    private String generatePassword(Integer size) {
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        for (int index = 0; index <= size; index++) {
            if (index % 3 == 0) {
                result.append((char) (random.nextInt(26) + 'a'));
                continue;
            }
            if (index % 4 == 0) {
                result.append((char) (random.nextInt(26) + 'A'));
                continue;
            }
            result.append(random.nextInt(10));
        }

        return result.toString();
    }

    @Override
    public List<Personnel> getWithSearch(PersonnelDTO search) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Personnel> criteriaBuilderQuery = criteriaBuilder.createQuery(Personnel.class);

        Root<Personnel> personnelRoot = criteriaBuilderQuery.from(Personnel.class);
        List<Predicate> predicates = new ArrayList<>();

        if (search.getId() != null) {
            predicates.add(criteriaBuilder.equal(personnelRoot.get("id"), search.getId()));
        }
        if (search.getGender() != null) {
            predicates.add(criteriaBuilder.equal(personnelRoot.get("gender"), Gender.valueOf(search.getGender())));
        }

        criteriaBuilderQuery.where(predicates.toArray(new Predicate[0]));

        List<Personnel> personnelList = entityManager.createQuery(criteriaBuilderQuery).getResultList();

        for (String field : search.getSort()) {
            if (field.equals("id")) {
                Collections.sort(personnelList, Comparator.comparing(Personnel::getId));
            } else if (field.equals("gender")) {
                Collections.sort(personnelList, Comparator.comparing(l -> l.getGender().name()));
            }
        }
        return personnelList;
    }
}
