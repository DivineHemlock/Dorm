package com.airbyte.dorm.person;

import com.airbyte.dorm.common.ParentController;
import com.airbyte.dorm.dto.PersonDTO;
import com.airbyte.dorm.dto.RelatedObjectDTO;
import com.airbyte.dorm.model.RelatedObject;
import com.airbyte.dorm.model.people.Person;
import com.airbyte.dorm.relatedobject.RelatedObjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/supervisor/person")
@CrossOrigin("*")
public class PersonController extends ParentController<Person, PersonService, PersonDTO> {

    public PersonController(PersonService service) {
        super(service);
    }

    @PostMapping("/guest/{parentId}")
    public ResponseEntity<RelatedObject> patchRelated(HttpServletResponse response, Authentication authentication, @PathVariable(name = "parentId") String id, @RequestBody RelatedObjectDTO dto) {
        response.setHeader("role", authentication.getAuthorities().toString().replace("[", "").replace("]", ""));
        return new ResponseEntity<>(service.saveRelatedObject(id, dto), HttpStatus.CREATED);
    }
}
