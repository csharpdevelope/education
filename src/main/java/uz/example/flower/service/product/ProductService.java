package uz.example.flower.service.product;

import org.springframework.web.multipart.MultipartFile;
import uz.example.flower.model.JSend;

import java.util.List;

public interface ProductService {
    JSend saveProduct(String body, List<MultipartFile> files);

    JSend editProduct(String body, List<MultipartFile> files);
    JSend getAll();
    JSend getById(Long id);
}
