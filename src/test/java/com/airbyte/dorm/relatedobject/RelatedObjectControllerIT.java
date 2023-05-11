package com.airbyte.dorm.relatedobject;

import com.airbyte.dorm.DormApplication;
import com.airbyte.dorm.dto.RelatedObjectDTO;
import com.airbyte.dorm.model.RelatedObject;
import com.airbyte.dorm.model.people.Person;
import com.airbyte.dorm.model.people.Personnel;
import com.airbyte.dorm.person.PersonDataProvider;
import com.airbyte.dorm.person.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static com.airbyte.dorm.CommonTestData.*;
import static com.airbyte.dorm.CommonTestData.DEFAULT_NATIONAL_CODE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {DormApplication.class})
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RelatedObjectControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RelatedObjectService service;
    @Autowired
    private RelatedObjectDataProvider dataProvider;
    private RelatedObjectDTO dto;
    @Autowired
    private RelatedObjectRepository repository;
    @Autowired
    private PersonService personService;
    @Autowired
    private PersonDataProvider personDataProvider;
    private Person parent;

    @BeforeEach
    public void initTest() {
        dto = dataProvider.createDTO();
    }

    @Test
    @Transactional
    @DisplayName("REST Request to save relatedObject")
    public void savePersonnel() throws Exception {
        int databaseSizeBeforeSave = repository.findAll().size();
        Person parent = personService.save(personDataProvider.createDTO());
        mockMvc.perform(post("/api/v1/person/guest/" + parent.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        RelatedObject entity = repository.findAll().get(databaseSizeBeforeSave);

        assertThat(repository.findAll()).hasSize(databaseSizeBeforeSave + 1);
        assertThat(entity.getId()).isNotBlank();
        assertThat(entity.getChildId()).isNotBlank();
        assertThat(entity.getParentId()).isEqualTo(parent.getId());
        assertThat(entity.getName()).isEqualTo(DEFAULT_STRING);
        assertThat(entity.getType()).isEqualTo(Person.class.getSimpleName());
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get related object By Id")
    public void getPersonnelById() throws Exception {
        Person parent = personService.save(personDataProvider.createDTO());
        RelatedObject entity = personService.saveRelatedObject(parent.getId() , dto);

        mockMvc.perform(get("/api/v1/relatedObject/{id}", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(entity.getId()))
                .andExpect(jsonPath("$.name").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.parentId").value(parent.getId()))
                .andExpect(jsonPath("$.childId").isNotEmpty())
                .andExpect(jsonPath("$.type").value(Person.class.getSimpleName()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to get related objects")
    public void getRelatedObjects () throws Exception {
        Person parent = personService.save(personDataProvider.createDTO());
        RelatedObject entity = personService.saveRelatedObject(parent.getId() , dto);

        mockMvc.perform(get("/api/v1/relatedObject"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].name").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].parentId").value(parent.getId()))
                .andExpect(jsonPath("$.[0].childId").isNotEmpty())
                .andExpect(jsonPath("$.[0].type").value(Person.class.getSimpleName()));
    }

    @Test
    @Transactional
    @DisplayName("REST Request to search result list of related objects")
    public void searchResultListByName() throws Exception {
        Person parent = personService.save(personDataProvider.createDTO());
        RelatedObject entity = personService.saveRelatedObject(parent.getId() , dto);

        mockMvc.perform(get("/api/v1/relatedObject/" + entity.getType()+ "/" + entity.getParentId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(entity.getId()))
                .andExpect(jsonPath("$.[0].name").value(DEFAULT_STRING))
                .andExpect(jsonPath("$.[0].parentId").value(parent.getId()))
                .andExpect(jsonPath("$.[0].childId").isNotEmpty())
                .andExpect(jsonPath("$.[0].type").value(Person.class.getSimpleName()));
    }
}
