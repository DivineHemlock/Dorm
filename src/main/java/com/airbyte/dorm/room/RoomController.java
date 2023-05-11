package com.airbyte.dorm.room;

import com.airbyte.dorm.common.ParentController;
import com.airbyte.dorm.dto.RoomDTO;
import com.airbyte.dorm.dto.RoomDTOShow;
import com.airbyte.dorm.dto.RoomRequestDTO;
import com.airbyte.dorm.model.Accessory;
import com.airbyte.dorm.model.Bed;
import com.airbyte.dorm.model.Room;
import com.airbyte.dorm.model.Unit;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/v1/supervisor/room")
@CrossOrigin("*")
public class RoomController extends ParentController<Room, RoomService, RoomDTO> {

    public RoomController(RoomService service) {
        super(service);
    }

    @GetMapping("/getBeds/{id}")
    public ResponseEntity<List<Bed>> getBeds (@PathVariable ("id") String id) {
        return new ResponseEntity<>(service.getOne(id).getBeds(), HttpStatus.OK);
    }

    @GetMapping("/concat/show")
    public ResponseEntity<List<RoomDTOShow>> getConcatName(HttpServletResponse response, Authentication authentication) {
        response.setHeader("role", authentication.getAuthorities().toString().replace("[", "").replace("]", ""));
        return new ResponseEntity<>(service.getConcatName(), HttpStatus.OK);
    }

    @GetMapping("/empty/size")
    public ResponseEntity<Long> getEmptySize(HttpServletResponse response, Authentication authentication) {
        response.setHeader("role", authentication.getAuthorities().toString().replace("[", "").replace("]", ""));
        return new ResponseEntity<>(service.getEmptySize(), HttpStatus.OK);
    }

    @GetMapping("/empty/list")
    public ResponseEntity<List<Room>> getEmptyRoom(HttpServletResponse response, Authentication authentication) {
        response.setHeader("role", authentication.getAuthorities().toString().replace("[", "").replace("]", ""));
        return new ResponseEntity<>(service.getEmptyRoom(), HttpStatus.OK);
    }

    @GetMapping("/{id}/accessory")
    public ResponseEntity<List<Accessory>> getAccessories(HttpServletResponse response, Authentication authentication, @PathVariable(name = "id") String id) {
        response.setHeader("role", authentication.getAuthorities().toString().replace("[", "").replace("]", ""));
        List<Accessory> accessories = service.getAccessories(id);
        if (accessories != null) {
            return new ResponseEntity<>(accessories, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/specific/{unitId}")
    public ResponseEntity<List<Room>> getSpecificUnits(HttpServletResponse response, Authentication authentication, @PathVariable(name = "unitId") String unitId) {
        response.setHeader("role", authentication.getAuthorities().toString().replace("[", "").replace("]", ""));
        return new ResponseEntity<>(service.getSpecificRooms(unitId), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<List<Room>> saveRoomWithBeds(HttpServletResponse response, Authentication authentication ,@RequestBody RoomRequestDTO rooms) {
        response.setHeader("role", authentication.getAuthorities().toString().replace("[", "").replace("]", ""));
        return new ResponseEntity<>(service.saveWithBeds(rooms), HttpStatus.CREATED);
    }
}
