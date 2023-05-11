package com.airbyte.dorm.bed;

import com.airbyte.dorm.dto.BedDTO;
import com.airbyte.dorm.room.RoomDataProvider;
import com.airbyte.dorm.room.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.airbyte.dorm.CommonTestData.*;

@Component
public class BedDataProvider {
    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomDataProvider roomDataProvider;

    public BedDTO createDTO() {
        BedDTO dto = new BedDTO();
        dto.setEmpty(DEFAULT_BOOLEAN);
        dto.setName(DEFAULT_STRING);
        dto.setRoomId(roomService.save(roomDataProvider.createDTO()).getId());
        return dto;
    }

    public BedDTO updateDTO() {
        BedDTO dto = new BedDTO();
        dto.setEmpty(UPDATED_BOOLEAN);
        dto.setName(UPDATED_STRING);
        dto.setRoomId(roomService.save(roomDataProvider.updateDTO()).getId());
        return dto;
    }

    public BedDTO assignPerson() {
        BedDTO dto = new BedDTO();
        dto.setEmpty(UPDATED_BOOLEAN);
        dto.setName(UPDATED_STRING);
        dto.setRoomId(roomService.save(roomDataProvider.updateDTO()).getId());
        // dto.setPersonId(); // TODO : FIXME
        return dto;
    }
}
