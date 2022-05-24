package uz.example.flower.controller.v1;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.example.flower.model.JSend;
import uz.example.flower.service.product.ProductService;

import java.util.List;

@RestController
@PreAuthorize(value = "hasAuthority('USER')")
@RequestMapping("api/v1/product")
@SecurityRequirement(name = "FLower Shopping")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("save")
    public ResponseEntity<?> postProduct(@RequestPart(value = "body") String body,
                                         @RequestPart(value = "files") List<MultipartFile> files) {
        JSend response;
        if (body == null) {
            response = JSend.badRequest("Data invalid");
            return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
        }
        response = productService.saveProduct(body, files);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @GetMapping("all")
    public ResponseEntity<?> getAllProductsForCustomer() {
        JSend response = productService.getAll();
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

    @DeleteMapping("delete{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable(value = "id") Long id) {
        JSend response = productService.deleteProduct(id);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @PatchMapping("change/quantity")
    public ResponseEntity<?> changeQuantity(@RequestPart(value = "id") Long id,
                                            @RequestPart(value = "quantity") Long quantity){
        JSend response = productService.editQuantityProduct(id, quantity);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @PatchMapping("discount")
    public ResponseEntity<?> discount(@RequestPart(value = "id") Long id,
                                            @RequestPart(value = "discount") Long discount){
        JSend response = productService.editDiscount(id, discount);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }
}
