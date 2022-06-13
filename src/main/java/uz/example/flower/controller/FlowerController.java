package uz.example.flower.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.example.flower.model.JSend;
import uz.example.flower.model.dto.FlowerDto;
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

    @GetMapping("gets")
    public ResponseEntity<?> getAllFlowersWithSort(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                           @RequestParam(value = "page_size", required = false, defaultValue = "12") Integer size) {
        JsonNode flowers = flowerService.getAllWithPage(page, size);
        return ResponseEntity.ok(flowers);
    }

    @GetMapping("gets/sort")
    public ResponseEntity<?> getAllFlowers(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                           @RequestParam(value = "page_size", required = false, defaultValue = "10") Integer size,
                                           @RequestParam(value = "name", required = false, defaultValue = "id") String name,
                                           @RequestParam(value = "desc", required = false, defaultValue = "true") Boolean isDesc) {
        JsonNode flowers = flowerService.getAllWithPage(page, size, name, isDesc);
        return ResponseEntity.ok(flowers);
    }

    @GetMapping("get/flower/{id}")
    public ResponseEntity<?> getById(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(flowerService.getFlowerById(id));
    }

//    @PostMapping("add")
//    public ResponseEntity<?> postFlower(@RequestParam(value = "name") String name,
//                                        @RequestParam(value = "heading") String heading,
//                                        @RequestParam(value = "description") String description,
//                                        @RequestParam(value = "discount", required = false) Long discount,
//                                        @RequestParam(value = "price") Double price,
//                                        @RequestParam(value = "files") List<MultipartFile> files) {
//        FlowerDto flowerDto = new FlowerDto();
//        flowerDto.setName(name);
//        flowerDto.setHeading(heading);
//        flowerDto.setPrice(price);
//        flowerDto.setDiscount(discount);
//        flowerDto.setDescription(description);
//        flowerService.postFlower(flowerDto, files);
//        return ResponseEntity.ok(flowerDto);
//    }

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

    @GetMapping("giftname")
    public ResponseEntity<?> getProductWithGiftName(@RequestParam(value = "gift") String name,
                                                    @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                    @RequestParam(value = "page_size", required = false, defaultValue = "10") Integer size) {
        JsonNode response = productService.getProductWithGift(name, page, size);
        return ResponseEntity.ok(response);
    }

//    @PostMapping("get/gift")
//    public ResponseEntity<?> getFlowerByCategory(@RequestBody List<String> categoryName) {
//        JSend response = productService.getFlowerByCategories(categoryName);
//        return ResponseEntity.ok(response);
//    }

    @PostMapping("search")
    public ResponseEntity<?> getSearch(@RequestParam(value = "search") String text,
                                       @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                       @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        JSend response = productService.searchProduct(text);
        return ResponseEntity.ok(response);
    }

    @GetMapping("gifts")
    public ResponseEntity<?> getGiftTypes() {
        JSend response = productService.getGiftTypes();
        return ResponseEntity.ok(response);
    }
}
