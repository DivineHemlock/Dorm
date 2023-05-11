package com.airbyte.dorm.room;

import com.airbyte.dorm.accessory.AccessoryDataProvider;
import com.airbyte.dorm.dto.*;
import com.airbyte.dorm.unit.UnitDataProvider;
import com.airbyte.dorm.unit.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.airbyte.dorm.CommonTestData.*;
import static com.airbyte.dorm.CommonTestData.UPDATED_INTEGER;

@Component
public class RoomDataProvider {
    @Autowired
    private AccessoryDataProvider accessoryDataProvider;
    @Autowired
    private UnitDataProvider unitDataProvider;
    @Autowired
    private UnitService unitService;

    public RoomDTO createDTO() {
        RoomDTO dto = new RoomDTO();
        dto.setEmpty(DEFAULT_BOOLEAN);
        dto.setDescription(DEFAULT_STRING);
        dto.setNumber(DEFAULT_INTEGER);
        dto.setAccessory(List.of(accessoryDataProvider.createDTO()));
        dto.setUnitId(unitService.save(unitDataProvider.createDTO()).getId());
        return dto;
    }


    public RoomDTO updateDTO() {
        RoomDTO dto = new RoomDTO();
        dto.setEmpty(UPDATED_BOOLEAN);
        dto.setDescription(UPDATED_STRING);
        dto.setNumber(UPDATED_INTEGER);
        dto.setAccessory(List.of(accessoryDataProvider.updateDTO(), accessoryDataProvider.createDTO()));
        dto.setUnitId(unitService.save(unitDataProvider.updateDTO()).getId());
        return dto;
    }

    public RoomRequestDTO prepareDTO() {
        RoomRequestDTO dto = new RoomRequestDTO();
        dto.setUnitId(unitService.save(unitDataProvider.createDTO()).getId());
        RoomSaveDTO dto1 = new RoomSaveDTO();
        dto1.setDescription(DEFAULT_STRING);
        dto1.setRoomNumber(DEFAULT_INTEGER);

        AccessoryDTO accessoryDTO = new AccessoryDTO();
        accessoryDTO.setCount(DEFAULT_LONG);
        accessoryDTO.setName(DEFAULT_STRING);
        accessoryDTO.setDescription(DEFAULT_STRING);
        dto1.setAccessory(List.of(accessoryDTO));

        BedSaveDTO bedSaveDTO = new BedSaveDTO();
        bedSaveDTO.setName(DEFAULT_STRING);
        dto1.setBeds(List.of(bedSaveDTO));

        dto.setRooms(List.of(dto1));
        return dto;
    }
}
