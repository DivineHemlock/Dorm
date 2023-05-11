package com.airbyte.dorm.unit;

import com.airbyte.dorm.accessory.AccessoryDataProvider;
import com.airbyte.dorm.dto.UnitDTO;
import com.airbyte.dorm.floor.FloorDataProvider;
import com.airbyte.dorm.floor.FloorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.airbyte.dorm.CommonTestData.*;

@Component
public class UnitDataProvider {
    @Autowired
    private FloorDataProvider floorDataProvider;
    @Autowired
    private FloorService floorService;
    @Autowired
    private AccessoryDataProvider accessoryDataProvider;

    public UnitDTO createDTO() {
        UnitDTO dto = new UnitDTO();
        dto.setEmpty(DEFAULT_BOOLEAN);
        dto.setNumber(DEFAULT_INTEGER);
        dto.setFloorId(floorService.save(floorDataProvider.createDTO()).getId());
        dto.setAccessory(List.of(accessoryDataProvider.createDTO()));
        return dto;
    }

    public UnitDTO updateDTO() {
        UnitDTO dto = new UnitDTO();
        dto.setEmpty(UPDATED_BOOLEAN);
        dto.setNumber(UPDATED_INTEGER);
        dto.setFloorId(floorService.save(floorDataProvider.updateDTO()).getId());
        dto.setAccessory(List.of(accessoryDataProvider.createDTO(), accessoryDataProvider.updateDTO()));
        return dto;
    }
}
