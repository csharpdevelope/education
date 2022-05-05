package uz.example.flower.controller.v1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.example.flower.model.JSend;
import uz.example.flower.service.ProductCategoryService;

import java.util.List;

@RestController
@RequestMapping("api/v1/category")
public class CategoryController {

    private final ProductCategoryService productCategoryService;

    public CategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @GetMapping("list")
    public ResponseEntity<?> getCategories(@RequestParam(value = "ids", required = false) List<Long> ids,
                                           @RequestParam(value = "page", required = false) int page,
                                           @RequestParam(value = "page_size", required = false) int pageSize) {
        JSend response = productCategoryService.getCategory(ids, page, pageSize);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }
}
