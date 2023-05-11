package com.airbyte.dorm.inventory;

import com.airbyte.dorm.category.CategoryService;
import com.airbyte.dorm.common.ParentController;
import com.airbyte.dorm.dto.InventoryDTO;
import com.airbyte.dorm.model.Category;
import com.airbyte.dorm.model.Inventory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/v1/supervisor/inventory")
@CrossOrigin("*")
public class InventoryController extends ParentController<Inventory, InventoryService, InventoryDTO> {
    private final CategoryService categoryService;

    public InventoryController(InventoryService service, CategoryService categoryService) {
        super(service);
        this.categoryService = categoryService;
    }

    @GetMapping("/category")
    public ResponseEntity<List<String>> getCategories(HttpServletResponse response, Authentication authentication) {
        response.setHeader("role", authentication.getAuthorities().toString().replace("[", "").replace("]", ""));
        return ResponseEntity.ok(categoryService.getByType(Inventory.class.getSimpleName()));
    }
}
