package com.airbyte.dorm.inventory;

import com.airbyte.dorm.accessory.AccessoryDataProvider;
import com.airbyte.dorm.dto.InventoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.airbyte.dorm.CommonTestData.*;

@Component
public class InventoryDataProvider {

    @Autowired
    private AccessoryDataProvider accessoryDataProvider;

    public InventoryDTO createDTO() {
        InventoryDTO dto = new InventoryDTO();
        dto.setCategory(DEFAULT_CATEGORY_NAME);
        dto.setAccessoryType(DEFAULT_ACCESSORY_TYPE.name());
        dto.setAccessories(List.of(accessoryDataProvider.createDTO()));
        return dto;
    }

    public InventoryDTO updateDTO() {
        InventoryDTO dto = new InventoryDTO();
        dto.setCategory(UPDATED_CATEGORY_NAME);
        dto.setAccessoryType(UPDATED_ACCESSORY_TYPE.name());
        dto.setAccessories(List.of(accessoryDataProvider.updateDTO(), accessoryDataProvider.createDTO()));
        return dto;
    }
}
