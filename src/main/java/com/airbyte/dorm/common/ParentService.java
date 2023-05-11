package com.airbyte.dorm.common;


import com.airbyte.dorm.dto.ParentDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

public abstract class ParentService<T, R extends JpaRepository<T, String>, DTO extends ParentDTO> {
    protected final R repository;
    @PersistenceContext
    protected EntityManager entityManager;


    public ParentService(R repository) {
        this.repository = repository;
    }

    public T save(DTO dto) {
        T model = convertDTO(dto);
        preSave(dto);
        model = repository.save(model);
        postSave(model, dto);
        return model;
    }

    protected void preSave(DTO dto) {
    }

    protected void postSave(T model, DTO dto) {
    }

    public T getOne(String id) {
        Optional<T> model = repository.findById(id);
        if (model.isPresent()) {
            return model.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "model with this id not found");
        }
    }

    public List<T> getAll() {
        return repository.findAll();
    }

    public T update(String id, DTO dto) {
        T model = getOne(id);
        model = updateModelFromDto(model, dto);
        model = repository.save(model);
        postUpdate(model, dto);
        return model;
    }

    protected void postUpdate(T model, DTO dto) {
    }

    public void delete(String id) {
        if (repository.findById(id).isPresent()) {
            preDelete(repository.findById(id).get());
            repository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "model with this id not found");
        }
    }

    protected void preDelete(T model) {
    }

    public abstract T updateModelFromDto(T model, DTO dto);

    public abstract T convertDTO(DTO dto);

    public abstract List<T> getWithSearch(DTO search);

    public void deleteAllById(List<String> ids) {
        if (ids.size() > 0) {
            repository.deleteAllById(ids);
        }
    }
}
