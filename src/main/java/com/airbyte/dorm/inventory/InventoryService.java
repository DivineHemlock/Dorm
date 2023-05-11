package com.airbyte.dorm.inventory;

import com.airbyte.dorm.accessory.AccessoryService;
import com.airbyte.dorm.category.CategoryService;
import com.airbyte.dorm.common.ParentService;
import com.airbyte.dorm.dto.CategoryDTO;
import com.airbyte.dorm.dto.InventoryDTO;
import com.airbyte.dorm.model.Category;
import com.airbyte.dorm.model.Inventory;
import com.airbyte.dorm.model.enums.AccessoryType;
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
public class InventoryService extends ParentService<Inventory, InventoryRepository, InventoryDTO> {

    private final AccessoryService accessoryService;
    private final CategoryService categoryService;

    public InventoryService(InventoryRepository repository, AccessoryService accessoryService, CategoryService categoryService) {
        super(repository);
        this.accessoryService = accessoryService;
        this.categoryService = categoryService;
    }


    @Override
    public Inventory updateModelFromDto(Inventory model, InventoryDTO dto) {
        model.setAccessories(dto.getAccessories() != null ? accessoryService.bulkSaveForInventory(dto.getAccessories(), model) : model.getAccessories());
        model.setCategory(dto.getCategory() != null ? dto.getCategory() : model.getCategory());
        model.setAccessoryType(dto.getAccessoryType() != null ? dto.getAccessoryType() : model.getAccessoryType());
        return model;
    }

    @Override
    protected void preSave(InventoryDTO dto) {
        CategoryDTO category = new CategoryDTO();
        category.setName(dto.getCategory());
        category.setType(Inventory.class.getSimpleName());

        List<Category> categoryList = categoryService.getWithSearch(category);
        if (categoryList.size() == 0) {
            categoryService.save(category);
        }

        CategoryDTO accessoryType = new CategoryDTO();
        accessoryType.setName(dto.getAccessoryType());
        accessoryType.setType("InventoryAccessoryType");

        List<Category> accessoryList = categoryService.getWithSearch(accessoryType);
        if (accessoryList.size() == 0) {
            categoryService.save(accessoryType);
        }
    }

    @Override
    public Inventory convertDTO(InventoryDTO dto) {
        Inventory inventory = new Inventory();
        inventory.setCategory(dto.getCategory());
        inventory.setAccessoryType(dto.getAccessoryType());
        inventory.setName(dto.getAccessories() != null && !dto.getAccessories().isEmpty() ? dto.getAccessories().get(0).getName() : null);
        return inventory;
    }

    @Override
    protected void postSave(Inventory model, InventoryDTO dto) {
        model.setAccessories(accessoryService.bulkSaveForInventory(dto.getAccessories(), model));
    }

    @Override
    public List<Inventory> getWithSearch(InventoryDTO search) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Inventory> criteriaBuilderQuery = criteriaBuilder.createQuery(Inventory.class);

        Root<Inventory> inventoryRoot = criteriaBuilderQuery.from(Inventory.class);
        List<Predicate> predicates = new ArrayList<>();

        if (search.getId() != null) {
            predicates.add(criteriaBuilder.equal(inventoryRoot.get("id"), search.getId()));
        }
        if (search.getCategory() != null && !search.getCategory().isEmpty()) {
            predicates.add(criteriaBuilder.like(inventoryRoot.get("category"), "%" + search.getCategory() + "%"));
        }
        if (search.getAccessoryType() != null && !search.getAccessoryType().isEmpty()) {
            predicates.add(criteriaBuilder.equal(inventoryRoot.get("accessoryType"), search.getAccessoryType()));
        }
        if (search.getName() != null && !search.getName().isEmpty()) {
            predicates.add(criteriaBuilder.like(inventoryRoot.get("name"), "%" + search.getName() + "%"));
        }

        criteriaBuilderQuery.where(predicates.toArray(new Predicate[0]));

        List<Inventory> inventoryList = entityManager.createQuery(criteriaBuilderQuery).getResultList();

        for (String field : search.getSort()) {
            if (field.equals("id")) {
                Collections.sort(inventoryList, Comparator.comparing(Inventory::getId));
            }
        }
        return inventoryList;
    }
}
