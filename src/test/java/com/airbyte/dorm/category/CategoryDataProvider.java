package com.airbyte.dorm.category;

import com.airbyte.dorm.dto.CategoryDTO;
import org.springframework.stereotype.Component;

import static com.airbyte.dorm.CommonTestData.*;

@Component
public class CategoryDataProvider {

    public CategoryDTO createDTO() {
        CategoryDTO dto = new CategoryDTO();
        dto.setName(DEFAULT_CATEGORY_NAME);
        dto.setType(DEFAULT_STRING);
        return dto;
    }

    public CategoryDTO updateDTO() {
        CategoryDTO dto = new CategoryDTO();
        dto.setName(UPDATED_CATEGORY_NAME);
        dto.setType(UPDATED_STRING);
        return dto;
    }
}
