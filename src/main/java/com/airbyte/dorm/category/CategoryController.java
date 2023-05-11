package com.airbyte.dorm.category;

import com.airbyte.dorm.common.ParentController;
import com.airbyte.dorm.dto.CategoryDTO;
import com.airbyte.dorm.model.Category;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/supervisor/category")
@CrossOrigin("*")
public class CategoryController extends ParentController<Category, CategoryService, CategoryDTO> {

    public CategoryController(CategoryService service) {
        super(service);
    }
}
