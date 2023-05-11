package com.airbyte.dorm.unit;

import com.airbyte.dorm.accessory.AccessoryService;
import com.airbyte.dorm.characteristic.CharacteristicService;
import com.airbyte.dorm.common.ParentService;
import com.airbyte.dorm.common.TimeConverter;
import com.airbyte.dorm.dto.BedInformationDTO;
import com.airbyte.dorm.dto.PresentAbsentDTO;
import com.airbyte.dorm.dto.RecordDTO;
import com.airbyte.dorm.dto.UnitDTO;
import com.airbyte.dorm.floor.FloorService;
import com.airbyte.dorm.model.*;
import com.airbyte.dorm.model.people.Person;
import com.airbyte.dorm.person.PersonService;
import com.airbyte.dorm.record.RecordService;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UnitService extends ParentService<Unit, UnitRepository, UnitDTO> {

    private final FloorService floorService;
    private final AccessoryService accessoryService;
    private final PersonService personService;
    private final CharacteristicService characteristicService;
    private final RecordService recordService;

    public UnitService(UnitRepository repository, FloorService floorService, AccessoryService accessoryService, PersonService personService, CharacteristicService characteristicService, RecordService recordService) {
        super(repository);
        this.floorService = floorService;
        this.accessoryService = accessoryService;
        this.personService = personService;
        this.characteristicService = characteristicService;
        this.recordService = recordService;
    }

    @Override
    public Unit updateModelFromDto(Unit model, UnitDTO dto) {
        model.setEmpty(dto.getEmpty() != null ? dto.getEmpty() : model.getEmpty());
        model.setNumber(dto.getNumber() != null ? dto.getNumber() : model.getNumber());
        model.setFloor(dto.getFloorId() != null ? convertFloor(dto.getFloorId()) : model.getFloor());
        return model;
    }

    @Override
    protected void postUpdate(Unit model, UnitDTO dto) {
        if (dto.getAccessory() != null) {
            updateAccessories(model, dto);
        }
    }

    private void updateAccessories(Unit model, UnitDTO dto) {
        List<String> ids = accessoryService.getAll()
                .stream()
                .filter(accessory -> model.getId().equals(accessory.getUnitId()))
                .map(Accessory::getId)
                .collect(Collectors.toList());

        accessoryService.deleteAllById(ids);
        model.setAccessories(accessoryService.bulkSave(dto.getAccessory(), model));
    }

    private Floor convertFloor(String floorId) {
        return floorService.getOne(floorId);
    }

    @Override
    public Unit convertDTO(UnitDTO dto) {
        Unit unit = new Unit();
        unit.setNumber(dto.getNumber());
        unit.setEmpty(dto.getEmpty());
        unit.setFloor(convertFloor(dto.getFloorId()));
        return unit;
    }

    @Override
    protected void postSave(Unit model, UnitDTO dto) {
        if (dto.getAccessory() != null) {
            model.setAccessories(accessoryService.bulkSave(dto.getAccessory(), model));
        }
    }

    @Override
    public List<Unit> getWithSearch(UnitDTO search) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Unit> criteriaBuilderQuery = criteriaBuilder.createQuery(Unit.class);

        Root<Unit> unitRoot = criteriaBuilderQuery.from(Unit.class);
        List<Predicate> predicates = new ArrayList<>();

        if (search.getId() != null) {
            predicates.add(criteriaBuilder.equal(unitRoot.get("id"), search.getId()));
        }
        if (search.getEmpty() != null) {
            predicates.add(criteriaBuilder.equal(unitRoot.get("empty"), search.getEmpty()));
        }
        if (search.getNumber() != null) {
            predicates.add(criteriaBuilder.equal(unitRoot.get("number"), search.getNumber()));
        }

        criteriaBuilderQuery.where(predicates.toArray(new Predicate[0]));

        List<Unit> unitList = entityManager.createQuery(criteriaBuilderQuery).getResultList();

        if (search.getFloorId() != null) {
            unitList = unitList.stream()
                    .filter(unit -> unit.getFloorId().equals(search.getFloorId()))
                    .collect(Collectors.toList());
        }

        for (String field : search.getSort()) {
            if (field.equals("id")) {
                Collections.sort(unitList, Comparator.comparing(Unit::getId));
            } else if (field.equals("number")) {
                Collections.sort(unitList, Comparator.comparing(Unit::getNumber));
            } else if (field.equals("empty")) {
                Collections.sort(unitList, Comparator.comparing(Unit::getEmpty));
            }
        }
        return unitList;
    }

    public Long getEmptySize() {
        return repository.findAll()
                .stream()
                .filter(Unit::getEmpty)
                .count();
    }

    public List<Unit> getEmptyUnit() {
        return repository.findAll()
                .stream()
                .filter(Unit::getEmpty)
                .collect(Collectors.toList());
    }

    public List<Unit> getSpecificUnits(String floorId) {
        return repository.findAll()
                .stream()
                .filter(unit -> unit.getFloorId().equals(floorId))
                .collect(Collectors.toList());
    }

    public List<Room> getRoomByUnitId(String unitId) {
        Optional<Unit> optionalUnit = this.getAll().stream()
                .filter(unit -> unitId.equals(unit.getId()))
                .findFirst();
        return optionalUnit.map(Unit::getRooms).orElse(null);
    }

    public List<PresentAbsentDTO> getUnitWithPerson(String unitId) {
        Unit unit = this.getOne(unitId);
        List<PresentAbsentDTO> resultList = new ArrayList<>();

        unit.getRooms()
                .forEach(room -> {
                    PresentAbsentDTO dto = new PresentAbsentDTO();
                    dto.setRoomNumber(room.getNumber());
                    List<BedInformationDTO> informationDTOS = new ArrayList<>();

                    room.getBeds()
                            .forEach(bed -> {
                                BedInformationDTO information = new BedInformationDTO();
                                if (bed.getPersonId() != null) {
                                    Person person = personService.getOne(bed.getPersonId());
                                    Characteristic characteristic = characteristicService.getOne(person.getCharacteristicId());

                                    RecordDTO recordDTO = new RecordDTO();
                                    String today = TimeConverter.convert(Date.from(Instant.now()), TimeConverter.UPDATED_PATTERN_FORMAT);
                                    today = TimeConverter.georgianToJalali(today);
                                    recordDTO.setDate(today);
                                    recordDTO.setTitle("cleaning");
                                    List<Record> recordList = recordService.getWithSearch(recordDTO);

                                    Boolean check = recordList != null && !recordList.isEmpty();
                                    if (check) {
                                        for (Record record : recordList) {
                                            if (bed.getPersonId().equals(record.getPerson().getId())) {
                                                check = true;
                                                break;
                                            }
                                            check = false;
                                        }
                                    }
                                    information.setChecked(check);
                                    information.setPersonId(bed.getPersonId());
                                    information.setBedName(bed.getName());
                                    information.setPersonNationalCode(characteristic.getNationalCode());
                                    information.setPersonName(characteristic.getFullName());
                                } else {
                                    information.setPersonId(null);
                                    information.setBedName(bed.getName());
                                    information.setPersonNationalCode(null);
                                    information.setPersonName(null);
                                    information.setChecked(null);
                                }
                                informationDTOS.add(information);
                            });
                    dto.setInformation(informationDTOS);
                    resultList.add(dto);
                });
        return resultList;
    }
}
