package uz.example.flower.controller.v1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.example.flower.model.JSend;
import uz.example.flower.service.product.ProductService;

import java.util.List;

@RestController
@RequestMapping("api/v1/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Secured("USER")
    @PostMapping("save")
    public ResponseEntity<?> postProduct(@RequestPart(value = "body") String body,
                                         @RequestPart(value = "files") List<MultipartFile> files) {
        JSend response = null;
        if (body == null) {
            response = JSend.badRequest("Data invalid");
            return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
        }
        response = productService.saveProduct(body, files);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }


    @PutMapping("edit")
    public ResponseEntity<?> editProduct(@RequestPart(value = "body") String body,
                                         @RequestPart(value = "files") List<MultipartFile> files) {
        JSend response = productService.editProduct(body, files);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }
}
