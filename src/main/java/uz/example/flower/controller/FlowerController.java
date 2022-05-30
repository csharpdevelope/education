package uz.example.flower.controller;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.example.flower.model.JSend;
import uz.example.flower.model.dto.FlowerDto;
import uz.example.flower.model.dto.FlowerResponse;
import uz.example.flower.service.CategoriesService;
import uz.example.flower.service.FlowerService;
import uz.example.flower.service.product.ProductService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("api/v1/flowers")
public class FlowerController {
    private final FlowerService flowerService;
    private final CategoriesService categoriesService;
    private final ProductService productService;

    public FlowerController(FlowerService flowerService, CategoriesService categoriesService, ProductService productService) {
        this.flowerService = flowerService;
        this.categoriesService = categoriesService;
        this.productService = productService;
    }

    @GetMapping("get")
    public ResponseEntity<?> getAllFlowers() {
        List<FlowerResponse> flowers = flowerService.getAll();
        return ResponseEntity.ok(flowers);
    }

    @PostMapping("add")
    @SecurityRequirement(name = "FLower Shopping")
    public ResponseEntity<?> postFlower(@RequestParam(value = "name") String name,
                                        @RequestParam(value = "heading") String heading,
                                        @RequestParam(value = "description") String description,
                                        @RequestParam(value = "discount", required = false) Long discount,
                                        @RequestParam(value = "price") Double price,
                                        @RequestParam(value = "files") List<MultipartFile> files) {
        FlowerDto flowerDto = new FlowerDto();
        flowerDto.setName(name);
        flowerDto.setHeading(heading);
        flowerDto.setPrice(price);
        flowerDto.setDiscount(discount);
        flowerDto.setDescription(description);
        flowerService.postFlower(flowerDto, files);
        return ResponseEntity.ok(flowerDto);
    }

    @GetMapping("get/{key:.+}")
    public ResponseEntity<?> getFlowerImage(@PathVariable(value = "key") String objectName) throws IOException {
        JSend images = flowerService.getFlowerImage(objectName);
        InputStream inputStream = (InputStream) images.getData();
        byte[] response = inputStream.readAllBytes();
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(response);
    }

    @GetMapping("category")
    public ResponseEntity<?> getCategories() {
        JSend response = categoriesService.getCategory();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @PostMapping("get/gift")
    public ResponseEntity<?> getFlowerByCategory(@RequestBody List<String> categoryName) {
        JSend response = productService.getFlowerByCategories(categoryName);
        return ResponseEntity.ok(response);
    }

    @GetMapping("search")
    public ResponseEntity<?> getSearch(@RequestParam(value = "text") String text) {
        JSend response = productService.searchProduct(text);
        return ResponseEntity.ok(response);
    }

    @GetMapping("gifts")
    public ResponseEntity<?> getGiftTypes() {
        JSend response = productService.getGiftTypes();
        return ResponseEntity.ok(response);
    }
}
