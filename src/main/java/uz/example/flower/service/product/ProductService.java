package uz.example.flower.service.product;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.multipart.MultipartFile;
import uz.example.flower.model.JSend;
import uz.example.flower.model.dto.Attachment;
import uz.example.flower.model.dto.ProductDto;

import java.util.List;

public interface ProductService {
    JSend saveProduct(String body, String category, List<String> giftTypes, List<MultipartFile> files);

    JSend saveProduct(ProductDto productDto);

    JSend editProduct(String body, List<MultipartFile> files);

    JSend getAll();

    JSend getAllWithPage(Integer page, Integer pageSize);

    JSend getById(Long id);

    JSend deleteProduct(Long id);

    JSend editQuantityProduct(Long id, Long quantity);

    JSend editDiscount(Long id, Long discount);

    JSend getFlowerByCategories(List<String> categoryName);

    JSend getFlowersByCategory(String categoryName);

    JSend searchProduct(String text);

    JSend getGiftTypes();

    Attachment uploadImage(MultipartFile file);

    JSend getProductWithGift(String name);

    JsonNode getProductWithGift(String name, Integer page, Integer size);

    JSend deleteImage(Long id);
}
