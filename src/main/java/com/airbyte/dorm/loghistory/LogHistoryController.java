package com.airbyte.dorm.loghistory;

import com.airbyte.dorm.dto.LogHistoryDTO;
import com.airbyte.dorm.model.LogHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/v1/logHistory")
@CrossOrigin("*")
public class LogHistoryController {
    private final LogHistoryService service;

    public LogHistoryController(LogHistoryService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET, value = "")
    public ResponseEntity<Page<LogHistory>> getAll(HttpServletResponse response, Authentication authentication, Pageable pageable) {
        response.setHeader("role", authentication.getAuthorities().toString().replace("[", "").replace("]", ""));
        return new ResponseEntity<>(service.getAll(pageable), HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity<Page<LogHistory>> getWithSearch(HttpServletResponse response, Authentication authentication, Pageable pageable, LogHistoryDTO search) {
        response.setHeader("role", authentication.getAuthorities().toString().replace("[", "").replace("]", ""));
        return new ResponseEntity<>(service.search(pageable, search), HttpStatus.OK);
    }
}
