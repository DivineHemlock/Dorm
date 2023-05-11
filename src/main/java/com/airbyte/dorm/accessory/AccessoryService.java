package com.airbyte.dorm.accessory;

import com.airbyte.dorm.common.ParentService;
import com.airbyte.dorm.dto.AccessoryDTO;
import com.airbyte.dorm.model.*;
import com.airbyte.dorm.model.enums.AccessoryType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccessoryService  extends ParentService<Accessory, AccessoryRepository, AccessoryDTO> {

    public AccessoryService(AccessoryRepository repository) {
        super(repository);
    }

    @Override
    public Accessory updateModelFromDto(Accessory model, AccessoryDTO dto) {
        model.setName(dto.getName() != null ? dto.getName() : model.getName());
        model.setCount(dto.getCount() != null ? BigDecimal.valueOf(dto.getCount()) : model.getCount());
        model.setDescription(dto.getDescription() != null ? dto.getDescription() : model.getDescription());
        return model;
    }

    @Override
    public Accessory convertDTO(AccessoryDTO dto) {
        Accessory entity = new Accessory();
        entity.setName(dto.getName());
        entity.setCount(BigDecimal.valueOf(dto.getCount()));
        entity.setDescription(dto.getDescription() != null ? dto.getDescription() : null);
        return entity;
    }

    @Override
    public List<Accessory> getWithSearch(AccessoryDTO search) {
        return null;
    }

    public List<Accessory> bulkSave(List<AccessoryDTO> dtoList, Room room) {
        List<Accessory> responseList = new ArrayList<>();

        for (AccessoryDTO dto : dtoList) {
            Accessory stuff  = new Accessory();
            stuff.setName(dto.getName());
            stuff.setCount(BigDecimal.valueOf(dto.getCount()));
            stuff.setRoom(room);
            stuff.setDescription(dto.getDescription() != null ? dto.getDescription() : null);
            responseList.add(repository.save(stuff));
        }
        return responseList;
    }

    public List<Accessory> bulkSaveForInventory(List<AccessoryDTO> dtoList, Inventory inventory) {
        List<Accessory> responseList = new ArrayList<>();

        if (dtoList != null) {
            for (AccessoryDTO dto : dtoList) {
                Accessory stuff = new Accessory();
                stuff.setName(dto.getName());
                stuff.setCount(BigDecimal.valueOf(dto.getCount()));
                stuff.setInventory(inventory);
                stuff.setDescription(dto.getDescription() != null ? dto.getDescription() : null);
                responseList.add(repository.save(stuff));
            }
        }
        return responseList;
    }

    public List<Accessory> bulkSave(List<AccessoryDTO> dtoList, Unit unit) {
        List<Accessory> responseList = new ArrayList<>();

        for (AccessoryDTO dto : dtoList) {
            Accessory stuff  = new Accessory();
            stuff.setName(dto.getName());
            stuff.setCount(BigDecimal.valueOf(dto.getCount()));
            stuff.setUnit(unit);
            stuff.setDescription(dto.getDescription() != null ? dto.getDescription() : null);
            responseList.add(repository.save(stuff));
        }
        return responseList;
    }

    public List<Accessory> bulkSave(List<AccessoryDTO> dtoList, Floor floor) {
        List<Accessory> responseList = new ArrayList<>();

        for (AccessoryDTO dto : dtoList) {
            Accessory stuff  = new Accessory();
            stuff.setName(dto.getName());
            stuff.setCount(BigDecimal.valueOf(dto.getCount()));
            stuff.setFloor(floor);
            stuff.setDescription(dto.getDescription() != null ? dto.getDescription() : null);
            responseList.add(repository.save(stuff));
        }
        return responseList;
    }
}
