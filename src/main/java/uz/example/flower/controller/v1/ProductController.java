package uz.example.flower.controller.v1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.example.flower.model.JSend;
import uz.example.flower.model.dto.Attachment;
import uz.example.flower.model.dto.ProductDto;
import uz.example.flower.service.product.ProductService;

import java.util.List;

@RestController
@RequestMapping("api/v1/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(value = "save")
    public ResponseEntity<?> postProduct(@RequestBody ProductDto productDto) {
        JSend response;
        if (productDto.getImageIds().isEmpty()) {
            return ResponseEntity.ok(JSend.fail("Images id not null"));
        }
        if (productDto.getGiftTypes().isEmpty()) {
            return ResponseEntity.ok(JSend.fail("Gift Types not null"));
        }
        response = productService.saveProduct(productDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("upload/image")
    public ResponseEntity<?> uploadImage(@RequestPart(value = "file") MultipartFile file) {
        Attachment attachment = productService.uploadImage(file);
        return ResponseEntity.ok(attachment);
    }

    @GetMapping("all")
    public ResponseEntity<?> getAllProductsForCustomer(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                       @RequestParam(value = "size", required = false, defaultValue = "10") Integer pageSize) {
        JSend response = productService.getAllWithPage(page, pageSize);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @GetMapping("get/{id}")
    public ResponseEntity<?> getById(@PathVariable(value = "id") Long id) {
        JSend response = productService.getById(id);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @PutMapping("edit")
    public ResponseEntity<?> editProduct(@RequestPart(value = "body") String body,
                                         @RequestPart(value = "files") List<MultipartFile> files) {
        JSend response = productService.editProduct(body, files);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable(value = "id") Long id) {
        JSend response = productService.deleteProduct(id);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @PatchMapping("change/quantity")
    public ResponseEntity<?> changeQuantity(@RequestParam(value = "id") Long id,
                                            @RequestParam(value = "quantity") Long quantity){
        JSend response = productService.editQuantityProduct(id, quantity);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @PatchMapping("discount")
    public ResponseEntity<?> discount(@RequestParam(value = "id") Long id,
                                      @RequestParam(value = "discount") Long discount){
        JSend response = productService.editDiscount(id, discount);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @DeleteMapping("delete/image")
    public ResponseEntity<?> deleteImage(@PathVariable(value = "id") Long id) {
        JSend response = productService.deleteImage(id);
        return ResponseEntity.ok(response);
    }
}
