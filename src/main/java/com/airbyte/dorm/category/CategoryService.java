package com.airbyte.dorm.category;

import com.airbyte.dorm.common.ParentService;
import com.airbyte.dorm.dto.CategoryDTO;
import com.airbyte.dorm.model.Category;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class CategoryService extends ParentService<Category, CategoryRepository, CategoryDTO> {

    public CategoryService(CategoryRepository repository) {
        super(repository);
    }

    @Override
    public Category updateModelFromDto(Category model, CategoryDTO dto) {
        model.setName(dto.getName() != null ? dto.getName() : model.getName());
        model.setType(dto.getType() != null ? dto.getType() : model.getType());
        return model;
    }

    @Override
    public Category convertDTO(CategoryDTO dto) {
        Category category = new Category();
        category.setName(dto.getName());
        category.setType(dto.getType());
        return category;
    }

    @Override
    public List<Category> getWithSearch(CategoryDTO search) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Category> criteriaBuilderQuery = criteriaBuilder.createQuery(Category.class);

        Root<Category> bedRoot = criteriaBuilderQuery.from(Category.class);
        List<Predicate> predicates = new ArrayList<>();

        if (search.getId() != null) {
            predicates.add(criteriaBuilder.equal(bedRoot.get("id"), search.getId()));
        }
        if (search.getName() != null) {
            predicates.add(criteriaBuilder.equal(bedRoot.get("name"), search.getName()));
        }
        if (search.getType() != null) {
            predicates.add(criteriaBuilder.equal(bedRoot.get("type"), search.getType()));
        }

        criteriaBuilderQuery.where(predicates.toArray(new Predicate[0]));

        List<Category> categoryList = entityManager.createQuery(criteriaBuilderQuery).getResultList();


        for (String field : search.getSort()) {
            if (field.equals("id")) {
                Collections.sort(categoryList, Comparator.comparing(Category::getId));
            } else if (field.equals("name")) {
                Collections.sort(categoryList, Comparator.comparing(Category::getName));
            } else if (field.equals("type")) {
                Collections.sort(categoryList, Comparator.comparing(Category::getType));
            }
        }

        return categoryList;
    }

    public List<String> getByType(String type) {
        List<String> categories = new ArrayList<>();
        CategoryDTO dto = new CategoryDTO();
        dto.setType(type);

        this.getWithSearch(dto)
                .forEach(category -> categories.add(category.getName()));

        return categories;
    }
}
