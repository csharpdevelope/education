package uz.example.flower.service.product;

import org.springframework.web.multipart.MultipartFile;
import uz.example.flower.model.JSend;

import java.util.List;

public interface ProductService {
    JSend saveProduct(String body, String category, List<String> giftTypes, List<MultipartFile> files);

    JSend editProduct(String body, List<MultipartFile> files);
    JSend getAll();
    JSend getById(Long id);

    JSend deleteProduct(Long id);

    JSend editQuantityProduct(Long id, Long quantity);

    JSend editDiscount(Long id, Long discount);

    JSend getFlowerByCategories(List<String> categoryName);

    JSend getFlowersByCategory(String categoryName);

    JSend searchProduct(String text);

    JSend getGiftTypes();
}
