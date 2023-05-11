package com.airbyte.dorm.bed;

import com.airbyte.dorm.common.ParentService;
import com.airbyte.dorm.dto.BedDTO;
import com.airbyte.dorm.dto.ReportEmptyBed;
import com.airbyte.dorm.model.Bed;
import com.airbyte.dorm.model.Room;
import com.airbyte.dorm.person.PersonService;
import com.airbyte.dorm.room.RoomService;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BedService extends ParentService<Bed, BedRepository, BedDTO> {

    private final RoomService roomService;
    private final PersonService personService;

    public BedService(BedRepository repository, RoomService roomService, PersonService personService) {
        super(repository);
        this.roomService = roomService;
        this.personService = personService;
    }

    @Override
    public Bed updateModelFromDto(Bed model, BedDTO dto) {
        model.setEmpty(dto.getEmpty() != null ? dto.getEmpty() : model.getEmpty());
        model.setName(dto.getName() != null ? dto.getName() : model.getName());
        model.setRoom(dto.getRoomId() != null ? convertRoom(dto.getRoomId()) : model.getRoom());
        model.setPerson(dto.getPersonId() != null ? personService.getOne(dto.getPersonId()) : model.getPerson());
        return model;
    }

    private Room convertRoom(String roomId) {
        return roomService.getOne(roomId);
    }

    @Override
    public Bed convertDTO(BedDTO dto) {
        Bed bed = new Bed();
        bed.setName(dto.getName());
        bed.setEmpty(dto.getEmpty());
        bed.setRoom(convertRoom(dto.getRoomId()));
        bed.setPerson(dto.getPersonId() != null ? personService.getOne(dto.getPersonId()) : null);
        return bed;
    }


    @Override
    public List<Bed> getWithSearch(BedDTO search) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Bed> criteriaBuilderQuery = criteriaBuilder.createQuery(Bed.class);

        Root<Bed> bedRoot = criteriaBuilderQuery.from(Bed.class);
        List<Predicate> predicates = new ArrayList<>();

        if (search.getId() != null) {
            predicates.add(criteriaBuilder.equal(bedRoot.get("id"), search.getId()));
        }
        if (search.getName() != null) {
            predicates.add(criteriaBuilder.equal(bedRoot.get("name"), search.getName()));
        }
        if (search.getEmpty() != null) {
            predicates.add(criteriaBuilder.equal(bedRoot.get("empty"), search.getEmpty()));
        }


        criteriaBuilderQuery.where(predicates.toArray(new Predicate[0]));

        List<Bed> bedList = entityManager.createQuery(criteriaBuilderQuery).getResultList();

        if (search.getRoomId() != null) {
            bedList = bedList.stream()
                    .filter(unit -> unit.getRoomId().equals(search.getRoomId()))
                    .collect(Collectors.toList());
        }


        for (String field : search.getSort()) {
            if (field.equals("id")) {
                Collections.sort(bedList, Comparator.comparing(Bed::getId));
            } else if (field.equals("name")) {
                Collections.sort(bedList, Comparator.comparing(Bed::getName));
            } else if (field.equals("empty")) {
                Collections.sort(bedList, Comparator.comparing(Bed::getEmpty));
            }
        }
        return bedList;
    }

    public List<Bed> getSpecificBeds(String roomId) {
        return repository.findAll()
                .stream()
                .filter(bed -> bed.getRoomId().equals(roomId))
                .collect(Collectors.toList());
    }

    public List<ReportEmptyBed> getEmptyBeds() {
        List<ReportEmptyBed> resultList = new ArrayList<>();
        repository.findAll().stream()
                .filter(Bed::getEmpty)
                .forEach(bed -> {
                    ReportEmptyBed result = new ReportEmptyBed();
                    result.setFloor(bed.getRoom().getUnit().getFloor().getName());
                    result.setUnit(String.valueOf(bed.getRoom().getUnit().getNumber()));
                    result.setRoom(String.valueOf(bed.getRoom().getNumber()));
                    result.setBed(bed.getName());
                    resultList.add(result);
                });
        return resultList;
    }
}
