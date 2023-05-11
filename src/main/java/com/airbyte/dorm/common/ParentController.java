package com.airbyte.dorm.common;

import com.airbyte.dorm.dto.LogHistoryDTO;
import com.airbyte.dorm.dto.ParentDTO;
import com.airbyte.dorm.loghistory.LogHistoryService;
import com.airbyte.dorm.manager.ManagerService;
import com.airbyte.dorm.model.people.Manager;
import com.airbyte.dorm.model.people.Supervisor;
import com.airbyte.dorm.supervisor.SupervisorService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public abstract class ParentController<T, S extends ParentService, DTO extends ParentDTO> {

    protected final S service;
    @Autowired
    private ManagerService managerService;
    @Autowired
    private SupervisorService supervisorService;
    @Autowired
    private LogHistoryService logHistoryService;

    public ParentController(S service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.POST, value = "", consumes = "application/json")
    public ResponseEntity<T> save(HttpServletResponse response, HttpServletRequest request, Authentication authentication, @RequestBody @Valid DTO dto) {
        response.setHeader("role", authentication.getAuthorities().toString().replace("[", "").replace("]", ""));
        handleRequest(request, "POST");
        return new ResponseEntity<>((T) service.save(dto), HttpStatus.CREATED);
    }

    private void handleRequest(HttpServletRequest request, String method) {
        LogHistoryDTO logHistoryDTO = new LogHistoryDTO();
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        String path = new UrlPathHelper().getPathWithinApplication(request).replace("/api/v1/", "");

        logHistoryDTO.setAction(method);
        logHistoryDTO.setUrl(path);

        String refresh_token = authorizationHeader;
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(refresh_token);
        String username = decodedJWT.getSubject();
        Manager manager = managerService.findManagerByUsername(username);

        if (manager != null) {
            logHistoryDTO.setDoer(manager.getFullName());
        } else {
            Supervisor supervisor = supervisorService.findSupervisorByUsername(username);
            logHistoryDTO.setDoer(supervisor.getFullName());
        }

        Instant timeNow = Instant.now();
        Date now = Date.from(timeNow);
        if ((now.getMonth() == 3 && now.getDay() >= 26) || (now.getMonth() >= 4 && now.getMonth() <= 9) || (now.getMonth() == 10 && now.getDay() <= 29)) {
            timeNow = timeNow.plus(2, ChronoUnit.HOURS).plus(30, ChronoUnit.MINUTES);
        } else {
            timeNow = timeNow.plus(3, ChronoUnit.HOURS).plus(30, ChronoUnit.MINUTES);
        }
        String[] date = TimeConverter.convert(Date.from(timeNow), TimeConverter.DEFAULT_PATTERN_FORMAT).split(" ");
        logHistoryDTO.setDate(TimeConverter.georgianToJalali(date[0]));
        logHistoryDTO.setHour(date[1]);

        logHistoryService.save(logHistoryDTO);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<T> getById(HttpServletResponse response, Authentication authentication, HttpServletRequest request, @PathVariable(name = "id") String id) {
        response.setHeader("role", authentication.getAuthorities().toString().replace("[", "").replace("]", ""));
        handleRequest(request, "GET");
        return new ResponseEntity<>((T) service.getOne(id), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "")
    public ResponseEntity<List<T>> getAll(HttpServletResponse response, Authentication authentication, HttpServletRequest request) {
        response.setHeader("role", authentication.getAuthorities().toString().replace("[", "").replace("]", ""));
        handleRequest(request, "GET");
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<T> update(HttpServletResponse response, Authentication authentication, HttpServletRequest request, @PathVariable("id") String id, @RequestBody DTO dto) {
        response.setHeader("role", authentication.getAuthorities().toString().replace("[", "").replace("]", ""));
        handleRequest(request, "PATCH");
        return new ResponseEntity<>((T) service.update(id, dto), HttpStatus.ACCEPTED);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity delete(HttpServletResponse response, Authentication authentication, HttpServletRequest request, @PathVariable("id") String id) {
        response.setHeader("role", authentication.getAuthorities().toString().replace("[", "").replace("]", ""));
        handleRequest(request, "DELETE");
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity<List<T>> getWithSearch(HttpServletResponse response, Authentication authentication, HttpServletRequest request, DTO search) {
        response.setHeader("role", authentication.getAuthorities().toString().replace("[", "").replace("]", ""));
        handleRequest(request, "GET");
        return new ResponseEntity<>(service.getWithSearch(search), HttpStatus.OK);
    }
}