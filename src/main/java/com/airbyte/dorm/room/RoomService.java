package com.airbyte.dorm.room;

import com.airbyte.dorm.accessory.AccessoryService;
import com.airbyte.dorm.bed.BedRepository;
import com.airbyte.dorm.common.ParentService;
import com.airbyte.dorm.dto.RoomDTO;
import com.airbyte.dorm.dto.RoomDTOShow;
import com.airbyte.dorm.dto.RoomRequestDTO;
import com.airbyte.dorm.dto.RoomSaveDTO;
import com.airbyte.dorm.model.Accessory;
import com.airbyte.dorm.model.Bed;
import com.airbyte.dorm.model.Room;
import com.airbyte.dorm.model.Unit;
import com.airbyte.dorm.unit.UnitService;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoomService extends ParentService<Room, RoomRepository, RoomDTO> {

    private final UnitService unitService;
    private final AccessoryService accessoryService;
    private final BedRepository bedRepository;

    public RoomService(RoomRepository repository, UnitService unitService, AccessoryService accessoryService, BedRepository bedRepository) {
        super(repository);
        this.unitService = unitService;
        this.accessoryService = accessoryService;
        this.bedRepository = bedRepository;
    }

    @Override
    public Room updateModelFromDto(Room model, RoomDTO dto) {
        model.setEmpty(dto.getEmpty() != null ? dto.getEmpty() : model.getEmpty());
        model.setNumber(dto.getNumber() != null ? dto.getNumber() : model.getNumber());
        model.setUnit(dto.getUnitId() != null ? convertUnit(dto.getUnitId()) : model.getUnit());
        model.setDescription(dto.getDescription() != null ? dto.getDescription() : model.getDescription());
        return model;
    }

    @Override
    protected void postUpdate(Room model, RoomDTO dto) {
        if (dto.getAccessory() != null) {
            updateAccessories(model, dto);
        }
    }

    private void updateAccessories(Room model, RoomDTO dto) {
        List<String> ids = accessoryService.getAll()
                .stream()
                .filter(accessory -> model.getId().equals(accessory.getRoomId()))
                .map(Accessory::getId)
                .collect(Collectors.toList());

        accessoryService.deleteAllById(ids);
        model.setAccessories(accessoryService.bulkSave(dto.getAccessory(), model));
    }

    private Unit convertUnit(String unitId) {
        return unitService.getOne(unitId);
    }

    @Override
    public Room convertDTO(RoomDTO dto) {
        Room room = new Room();
        room.setEmpty(dto.getEmpty());
        room.setNumber(dto.getNumber());
        room.setUnit(convertUnit(dto.getUnitId()));
        room.setDescription(dto.getDescription());
        return room;
    }

    @Override
    protected void postSave(Room model, RoomDTO dto) {
        if (dto.getAccessory() != null) {
            model.setAccessories(accessoryService.bulkSave(dto.getAccessory(), model));
        }
    }

    @Override
    public List<Room> getWithSearch(RoomDTO search) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Room> criteriaBuilderQuery = criteriaBuilder.createQuery(Room.class);

        Root<Room> RoomRoot = criteriaBuilderQuery.from(Room.class);
        List<Predicate> predicates = new ArrayList<>();

        if (search.getId() != null) {
            predicates.add(criteriaBuilder.equal(RoomRoot.get("id"), search.getId()));
        }
        if (search.getNumber() != null) {
            predicates.add(criteriaBuilder.equal(RoomRoot.get("number"), search.getNumber()));
        }
        if (search.getEmpty() != null) {
            predicates.add(criteriaBuilder.equal(RoomRoot.get("empty"), search.getEmpty()));
        }
        if (search.getDescription() != null) {
            predicates.add(criteriaBuilder.equal(RoomRoot.get("description"), search.getDescription()));
        }

        criteriaBuilderQuery.where(predicates.toArray(new Predicate[0]));

        List<Room> roomList = entityManager.createQuery(criteriaBuilderQuery).getResultList();

        if (search.getUnitId() != null) {
            roomList = roomList.stream()
                    .filter(unit -> unit.getUnitId().equals(search.getUnitId()))
                    .collect(Collectors.toList());
        }

        for (String field : search.getSort()) {
            if (field.equals("id")) {
                Collections.sort(roomList, Comparator.comparing(Room::getId));
            } else if (field.equals("number")) {
                Collections.sort(roomList, Comparator.comparing(Room::getNumber));
            } else if (field.equals("empty")) {
                Collections.sort(roomList, Comparator.comparing(Room::getEmpty));
            } else if (field.equals("description")) {
                Collections.sort(roomList, Comparator.comparing(Room::getDescription));
            }
        }
        return roomList;
    }

    public List<RoomDTOShow> getConcatName() {
        List<RoomDTOShow> concatList = new ArrayList<>();

        for (Room room : repository.findAll()) {
            RoomDTOShow show = new RoomDTOShow();
            show.setConcatName(room.getConcatName());
            show.setId(room.getId());
            concatList.add(show);
        }
        return concatList;
    }

    public Long getEmptySize() {
        return repository.findAll()
                .stream()
                .filter(Room::getEmpty)
                .count();
    }

    public List<Room> getEmptyRoom() {
        return repository.findAll()
                .stream()
                .filter(Room::getEmpty)
                .collect(Collectors.toList());
    }

    public List<Accessory> getAccessories(String id) {
        Optional<Room> roomOptional = repository.findById(id);
        if (roomOptional.isPresent()) {
            return roomOptional.get().getAccessories();
        }
        return null;
    }

    public List<Room> getSpecificRooms(String unitId) {
        return repository.findAll()
                .stream()
                .filter(room -> room.getUnitId().equals(unitId))
                .collect(Collectors.toList());
    }

    public List<Room> saveWithBeds(RoomRequestDTO rooms) {
        List<Room> roomList = new ArrayList<>();

        Unit unit = unitService.getOne(rooms.getUnitId());

        for (RoomSaveDTO dto : rooms.getRooms()) {
            RoomDTO roomDTO = new RoomDTO();
            roomDTO.setDescription(dto.getDescription());
            roomDTO.setNumber(dto.getRoomNumber());
            roomDTO.setUnitId(unit.getId());
            roomDTO.setAccessory(dto.getAccessory());
            roomDTO.setEmpty(true);
            Room newRoom = this.save(roomDTO);

            dto.getBeds()
                    .forEach(newBed ->
                            {
                                Bed bed = new Bed();
                                bed.setRoom(newRoom);
                                bed.setName(newBed.getName());
                                bed.setEmpty(true);
                                bed = bedRepository.save(bed);
                                newRoom.getBeds().add(bed);
                            }
                    );
            roomList.add(newRoom);
        }
        return roomList;
    }
}
