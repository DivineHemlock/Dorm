package com.airbyte.dorm.floor;

import com.airbyte.dorm.accessory.AccessoryService;
import com.airbyte.dorm.common.ParentService;
import com.airbyte.dorm.dto.FloorDTO;
import com.airbyte.dorm.dto.FloorRequestDTO;
import com.airbyte.dorm.dto.FloorSaveDTO;
import com.airbyte.dorm.model.Accessory;
import com.airbyte.dorm.model.Floor;
import com.airbyte.dorm.model.Unit;
import com.airbyte.dorm.unit.UnitRepository;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class FloorService extends ParentService<Floor, FloorRepository, FloorDTO> {
    private final UnitRepository unitRepository;
    private final AccessoryService accessoryService;

    public FloorService(FloorRepository repository, UnitRepository unitRepository, AccessoryService accessoryService) {
        super(repository);
        this.unitRepository = unitRepository;
        this.accessoryService = accessoryService;
    }

    @Override
    public Floor updateModelFromDto(Floor model, FloorDTO dto) {
        model.setEmpty(dto.getEmpty() != null ? dto.getEmpty() : model.getEmpty());
        model.setName(dto.getName() != null ? dto.getName() : model.getName());
        return model;
    }

    @Override
    protected void postUpdate(Floor model, FloorDTO dto) {
        if (dto.getAccessory() != null) {
            updateAccessories(model, dto);
        }
    }

    private void updateAccessories(Floor model, FloorDTO dto) {
        List<String> ids = accessoryService.getAll()
                .stream()
                .filter(accessory -> model.getId().equals(accessory.getFloorId()))
                .map(Accessory::getId)
                .collect(Collectors.toList());

        accessoryService.deleteAllById(ids);
        model.setAccessories(accessoryService.bulkSave(dto.getAccessory(), model));
    }

    @Override
    public Floor convertDTO(FloorDTO dto) {
        Floor floor = new Floor();
        floor.setName(dto.getName());
        floor.setEmpty(dto.getEmpty());
        return floor;
    }

    @Override
    public List<Floor> getWithSearch(FloorDTO search) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Floor> criteriaBuilderQuery = criteriaBuilder.createQuery(Floor.class);

        Root<Floor> floorRoot = criteriaBuilderQuery.from(Floor.class);
        List<Predicate> predicates = new ArrayList<>();

        if (search.getId() != null) {
            predicates.add(criteriaBuilder.equal(floorRoot.get("id"), search.getId()));
        }
        if (search.getName() != null) {
            predicates.add(criteriaBuilder.equal(floorRoot.get("name"), search.getName()));
        }
        if (search.getEmpty() != null) {
            predicates.add(criteriaBuilder.equal(floorRoot.get("empty"), search.getEmpty()));
        }

        criteriaBuilderQuery.where(predicates.toArray(new Predicate[0]));

        List<Floor> floorList = entityManager.createQuery(criteriaBuilderQuery).getResultList();

        for (String field : search.getSort()) {
            if (field.equals("id")) {
                Collections.sort(floorList, Comparator.comparing(Floor::getId));
            } else if (field.equals("name")) {
                Collections.sort(floorList, Comparator.comparing(Floor::getName));
            } else if (field.equals("empty")) {
                Collections.sort(floorList, Comparator.comparing(Floor::getEmpty));
            }
        }
        return floorList;
    }

    public Long getEmptySize() {
        return repository.findAll()
                .stream()
                .filter(Floor::getEmpty)
                .count();
    }

    public List<Floor> getEmptyFloor() {
        return repository.findAll()
                .stream()
                .filter(Floor::getEmpty)
                .collect(Collectors.toList());
    }

    public List<Floor> saveFloorWithUnits(FloorRequestDTO floor) {
        List<Floor> floorList = new ArrayList<>();

        for (FloorSaveDTO dto : floor.getFloors()) {
            Floor newFloor = repository.save(new Floor()
                    .name(dto.getFloorName())
            );

            dto.getUnits()
                    .forEach(newUnit ->
                            {
                                Unit unit = new Unit();
                                unit.setFloor(newFloor);
                                unit.setNumber(newUnit.getNumber());
                                unit = unitRepository.save(unit);
                                if (newUnit.getAccessory() != null && !newUnit.getAccessory().isEmpty()) {
                                    unit.setAccessories(accessoryService.bulkSave(newUnit.getAccessory(), unit));
                                }
                                newFloor.getUnits().add(unit);
                            }
                    );
            floorList.add(this.getOne(newFloor.getId()));
        }

        return floorList;
    }

    @Override
    protected void postSave(Floor model, FloorDTO dto) {
        if (dto.getAccessory() != null) {
            model.setAccessories(accessoryService.bulkSave(dto.getAccessory(), model));
        }
    }

}
