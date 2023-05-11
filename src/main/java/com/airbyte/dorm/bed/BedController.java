package com.airbyte.dorm.bed;

import com.airbyte.dorm.common.ParentController;
import com.airbyte.dorm.dto.BedDTO;
import com.airbyte.dorm.dto.ReportEmptyBed;
import com.airbyte.dorm.model.Bed;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/v1/supervisor/bed")
@CrossOrigin("*")
public class BedController extends ParentController<Bed, BedService, BedDTO> {

    public BedController(BedService service) {
        super(service);
    }

    @GetMapping("/specific/{roomId}")
    public ResponseEntity<List<Bed>> getSpecificBeds(HttpServletResponse response, Authentication authentication, @PathVariable(name = "roomId") String bedId) {
        response.setHeader("role", authentication.getAuthorities().toString().replace("[", "").replace("]", ""));
        return new ResponseEntity<>(service.getSpecificBeds(bedId), HttpStatus.OK);
    }

    @GetMapping("/concat/empty")
    public ResponseEntity<List<ReportEmptyBed>> getEmptyBeds(HttpServletResponse response, Authentication authentication) {
        response.setHeader("role", authentication.getAuthorities().toString().replace("[", "").replace("]", ""));
        return new ResponseEntity<>(service.getEmptyBeds(), HttpStatus.OK);
    }
}
