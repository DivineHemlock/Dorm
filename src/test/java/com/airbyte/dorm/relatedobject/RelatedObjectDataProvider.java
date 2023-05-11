package com.airbyte.dorm.relatedobject;

import com.airbyte.dorm.dto.RelatedObjectDTO;
import com.airbyte.dorm.model.people.Person;
import com.airbyte.dorm.person.PersonDataProvider;
import com.airbyte.dorm.person.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static com.airbyte.dorm.CommonTestData.*;

@Component
public class RelatedObjectDataProvider {

    @Autowired
    private final PersonDataProvider personDataProvider;
    @Autowired
    private final PersonService personService;

    public RelatedObjectDataProvider(PersonDataProvider personDataProvider, PersonService personService) {
        this.personDataProvider = personDataProvider;
        this.personService = personService;
    }

    public RelatedObjectDTO createDTO () {
        RelatedObjectDTO relatedObjectDTO = new RelatedObjectDTO();
        relatedObjectDTO.setName(DEFAULT_STRING);
        Person child = personService.save(personDataProvider.createDTO());
        relatedObjectDTO.setChildId(child.getId());
        return relatedObjectDTO;
    }

    public RelatedObjectDTO updateDTO () {
        RelatedObjectDTO relatedObjectDTO = new RelatedObjectDTO();
        relatedObjectDTO.setName(UPDATED_STRING);
        Person child = personService.save(personDataProvider.createDTO());
        relatedObjectDTO.setChildId(child.getId());
        return relatedObjectDTO;
    }
}
