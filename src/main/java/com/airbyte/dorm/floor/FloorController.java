package com.airbyte.dorm.floor;

import com.airbyte.dorm.common.ParentController;
import com.airbyte.dorm.dto.FloorDTO;
import com.airbyte.dorm.dto.FloorRequestDTO;
import com.airbyte.dorm.model.Floor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/v1/supervisor/floor")
@CrossOrigin("*")
public class FloorController extends ParentController<Floor, FloorService, FloorDTO> {

    public FloorController(FloorService service) {
        super(service);
    }

    @GetMapping("/empty/size")
    public ResponseEntity<Long> getEmptySize(HttpServletResponse response, Authentication authentication) {
        response.setHeader("role", authentication.getAuthorities().toString().replace("[", "").replace("]", ""));
        return new ResponseEntity<>(service.getEmptySize(), HttpStatus.OK);
    }

    @GetMapping("/empty/list")
    public ResponseEntity<List<Floor>> getEmptyFloor(HttpServletResponse response, Authentication authentication) {
        response.setHeader("role", authentication.getAuthorities().toString().replace("[", "").replace("]", ""));
        return new ResponseEntity<>(service.getEmptyFloor(), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<List<Floor>> saveFloorWithUnits(HttpServletResponse response, Authentication authentication,@RequestBody FloorRequestDTO floor) {
        response.setHeader("role", authentication.getAuthorities().toString().replace("[", "").replace("]", ""));
        return new ResponseEntity<>(service.saveFloorWithUnits(floor), HttpStatus.CREATED);
    }

}

