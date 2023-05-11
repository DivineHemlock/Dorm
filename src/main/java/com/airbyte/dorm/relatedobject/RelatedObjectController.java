package com.airbyte.dorm.relatedobject;

import com.airbyte.dorm.model.RelatedObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/v1/supervisor/relatedObject")
@CrossOrigin("*")
public class RelatedObjectController {

    private final RelatedObjectService service;

    public RelatedObjectController(RelatedObjectService service) {
        this.service = service;
    }

    @GetMapping("/{type}/{parentId}")
    public ResponseEntity<List<RelatedObject>> getWithSearch(HttpServletResponse response, Authentication authentication, @PathVariable(name = "type") String type, @PathVariable(name = "parentId") String parentId) {
        response.setHeader("role", authentication.getAuthorities().toString().replace("[", "").replace("]", ""));
        return new ResponseEntity<>(service.getWithSearch(type, parentId), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RelatedObject> getOne(HttpServletResponse response, Authentication authentication, @PathVariable(name = "id") String id) {
        response.setHeader("role", authentication.getAuthorities().toString().replace("[", "").replace("]", ""));
        return new ResponseEntity<>(service.getRepository().findById(id).orElse(null), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<RelatedObject>> getAll(HttpServletResponse response, Authentication authentication) {
        response.setHeader("role", authentication.getAuthorities().toString().replace("[", "").replace("]", ""));
        return new ResponseEntity<>(service.getRepository().findAll(), HttpStatus.OK);
    }
}
