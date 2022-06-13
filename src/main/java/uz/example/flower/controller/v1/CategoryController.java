package uz.example.flower.controller.v1;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.example.flower.model.JSend;
import uz.example.flower.service.ProductCategoryService;

import java.util.List;

@RestController
@RequestMapping("api/v1/category")
@SecurityRequirement(name = "FLower Shopping")
public class CategoryController {

    private final ProductCategoryService productCategoryService;

    public CategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

//    @GetMapping("list")
//    public ResponseEntity<?> getCategories(@RequestParam(value = "ids", required = false) List<Long> ids,
//                                           @RequestParam(value = "page", required = false) int page,
//                                           @RequestParam(value = "page_size", required = false) int pageSize) {
//        JSend response = productCategoryService.getCategory(ids, page, pageSize);
//        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
//    }

    @GetMapping
    public ResponseEntity<?> getCategory() {
        JSend response = productCategoryService.getCategories();
        return ResponseEntity.ok(response);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<?> getCategory(@PathVariable(value = "id") Long id) {
//        JSend response = productCategoryService.getCategory(id);
//        return ResponseEntity.ok(response);
//    }
}
