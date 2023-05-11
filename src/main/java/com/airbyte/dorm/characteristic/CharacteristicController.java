package com.airbyte.dorm.characteristic;

import com.airbyte.dorm.common.GeneratePdf;
import com.airbyte.dorm.common.ParentController;
import com.airbyte.dorm.dto.CharacteristicDTO;
import com.airbyte.dorm.model.Characteristic;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/api/v1/supervisor/characteristic")
@CrossOrigin("*")
public class CharacteristicController extends ParentController<Characteristic, CharacteristicService, CharacteristicDTO> {

    public CharacteristicController(CharacteristicService service) {
        super(service);
    }

    @GetMapping("/report/{id}")
    public ResponseEntity<InputStreamResource> characteristicReport(HttpServletResponse response, Authentication authentication, @PathVariable("id") String id) {
        response.setHeader("role", authentication.getAuthorities().toString().replace("[", "").replace("]", ""));
        Characteristic characteristic = service.getOne(id);
        ByteArrayInputStream bis = GeneratePdf.reportCharacteristic(characteristic);

        var headers = new HttpHeaders();
        headers.add("Content-Disposition",
                "inline; filename=report.pdf");

        assert bis != null;
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
