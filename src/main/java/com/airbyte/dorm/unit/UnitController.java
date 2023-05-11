package com.airbyte.dorm.unit;

import com.airbyte.dorm.common.ParentController;
import com.airbyte.dorm.dto.PresentAbsentDTO;
import com.airbyte.dorm.dto.UnitDTO;
import com.airbyte.dorm.model.Room;
import com.airbyte.dorm.model.Unit;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/v1/supervisor/unit")
@CrossOrigin("*")
public class UnitController extends ParentController<Unit, UnitService, UnitDTO> {

    public UnitController(UnitService service) {
        super(service);
    }

    @GetMapping("/empty/size")
    public ResponseEntity<Long> getEmptySize(HttpServletResponse response, Authentication authentication) {
        response.setHeader("role", authentication.getAuthorities().toString().replace("[", "").replace("]", ""));
        return new ResponseEntity<>(service.getEmptySize(), HttpStatus.OK);
    }

    @GetMapping("/empty/list")
    public ResponseEntity<List<Unit>> getEmptyRoom(HttpServletResponse response, Authentication authentication) {
        response.setHeader("role", authentication.getAuthorities().toString().replace("[", "").replace("]", ""));
        return new ResponseEntity<>(service.getEmptyUnit(), HttpStatus.OK);
    }

    @GetMapping("/specific/{floorId}")
    public ResponseEntity<List<Unit>> getSpecificUnits(HttpServletResponse response, Authentication authentication, @PathVariable(name = "floorId") String floorId) {
        response.setHeader("role", authentication.getAuthorities().toString().replace("[", "").replace("]", ""));
        return new ResponseEntity<>(service.getSpecificUnits(floorId), HttpStatus.OK);
    }

    @GetMapping("/room/{unitId}")
    public ResponseEntity<List<Room>> getRoomByUnitId(HttpServletResponse response, Authentication authentication,@PathVariable(name = "unitId") String unitId) {
        response.setHeader("role", authentication.getAuthorities().toString().replace("[", "").replace("]", ""));
        return new ResponseEntity<>(service.getRoomByUnitId(unitId), HttpStatus.OK);
    }

    @GetMapping("/person/{unitId}")
    public ResponseEntity<List<PresentAbsentDTO>> getUnitWithPerson(HttpServletResponse response, Authentication authentication,@PathVariable(name = "unitId") String unitId) {
        response.setHeader("role", authentication.getAuthorities().toString().replace("[", "").replace("]", ""));
        return new ResponseEntity<>(service.getUnitWithPerson(unitId), HttpStatus.OK);
    }
}
