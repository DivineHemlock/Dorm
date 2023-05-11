package com.airbyte.dorm.floor;

import com.airbyte.dorm.accessory.AccessoryDataProvider;
import com.airbyte.dorm.dto.FloorDTO;
import com.airbyte.dorm.dto.FloorRequestDTO;
import com.airbyte.dorm.dto.FloorSaveDTO;
import com.airbyte.dorm.dto.UnitSaveDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.airbyte.dorm.CommonTestData.*;

@Component
public class FloorDataProvider {
    @Autowired
    private AccessoryDataProvider accessoryDataProvider;

    public FloorDTO createDTO() {
        FloorDTO dto = new FloorDTO();
        dto.setEmpty(DEFAULT_BOOLEAN);
        dto.setName(DEFAULT_STRING);
        dto.setAccessory(List.of(accessoryDataProvider.createDTO()));
        return dto;
    }

    public FloorDTO updateDTO() {
        FloorDTO dto = new FloorDTO();
        dto.setEmpty(UPDATED_BOOLEAN);
        dto.setName(UPDATED_STRING);
        dto.setAccessory(List.of(accessoryDataProvider.createDTO(), accessoryDataProvider.updateDTO()));
        return dto;
    }

    public FloorRequestDTO prepareDTO() {
        FloorRequestDTO floorRequestDTO = new FloorRequestDTO();
        List<FloorSaveDTO> dtoList = new ArrayList<>();

        FloorSaveDTO dto = new FloorSaveDTO();

        dto.setFloorName(DEFAULT_STRING);

        List<UnitSaveDTO> unitSaveDTOList = new ArrayList<>();

        UnitSaveDTO unitSaveDTO1 = new UnitSaveDTO();
        unitSaveDTO1.setNumber(DEFAULT_INTEGER);
        unitSaveDTO1.setAccessory(List.of(accessoryDataProvider.createDTO()));
        unitSaveDTOList.add(unitSaveDTO1);

        UnitSaveDTO unitSaveDTO2 = new UnitSaveDTO();
        unitSaveDTO2.setNumber(UPDATED_INTEGER);
        unitSaveDTO2.setAccessory(List.of(accessoryDataProvider.createDTO()));
        unitSaveDTOList.add(unitSaveDTO2);

        dto.setUnits(unitSaveDTOList);

        dtoList.add(dto);
        floorRequestDTO.setFloors(dtoList);
        return floorRequestDTO;
    }
}
