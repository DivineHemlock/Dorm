package com.airbyte.dorm.responsefile;

import com.airbyte.dorm.dto.ResponseFileDTO;
import com.airbyte.dorm.model.ResponseFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/v1/supervisor/responseFile")
@CrossOrigin("*")
public class ResponseFileController {
    private final ResponseFileService service;

    public ResponseFileController(ResponseFileService service) {
        this.service = service;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity<List<ResponseFile>> getWithSearch(HttpServletResponse response, Authentication authentication, ResponseFileDTO search) {
        response.setHeader("role", authentication.getAuthorities().toString().replace("[", "").replace("]", ""));
        return new ResponseEntity<>(service.getWithSearch(search), HttpStatus.OK);
    }

}
