package uz.example.flower.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.example.flower.exception.NotFoundException;
import uz.example.flower.model.JSend;
import uz.example.flower.model.dto.FlowerDto;
import uz.example.flower.model.dto.ProductDto;
import uz.example.flower.model.entity.Flower;
import uz.example.flower.repository.FlowerRepository;
import uz.example.flower.repository.UserRepository;
import uz.example.flower.service.FlowerService;
import uz.example.flower.service.product.ProductService;

import java.util.List;

@Service
public class ProductServiceImp implements ProductService {
    private final FlowerService flowerService;
    private final FlowerRepository flowerRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    @Autowired
    private Gson gson;

    public ProductServiceImp(FlowerService flowerService, FlowerRepository flowerRepository, UserRepository userRepository, ObjectMapper objectMapper) {
        this.flowerService = flowerService;
        this.flowerRepository = flowerRepository;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public JSend saveProduct(String body, List<MultipartFile> files) {
        ProductDto productDto = gson.fromJson(body, ProductDto.class);
        FlowerDto flowerDto = toFlowerDto(productDto);
        flowerService.postFlower(flowerDto, files);
        return JSend.success(flowerDto);
    }

    @Override
    public JSend editProduct(String body, List<MultipartFile> files) {
        ProductDto productDto = gson.fromJson(body, ProductDto.class);
        Long flowerId = productDto.getId();
        if (flowerId == null) {
            return JSend.badRequest("Id not found");
        }
        FlowerDto flowerDto = toFlowerDto(productDto);
        flowerService.editFlowers(flowerDto, files);
        return JSend.success(flowerDto);
    }

    @Override
    public JSend getAll() {
        return null;
    }

    @Override
    public JSend getById(Long id) {
        return null;
    }

    private FlowerDto toFlowerDto(ProductDto productDto) {
        FlowerDto flowerDto = new FlowerDto();
        if (productDto.getId() != null) {
            flowerDto.setId(productDto.getId());
        }
        flowerDto.setName(productDto.getName());
        flowerDto.setHeading(productDto.getHeading());
        flowerDto.setDescription(productDto.getDescription());
        flowerDto.setDiscount(productDto.getDiscount());
        flowerDto.setPrice(productDto.getPrice());
        flowerDto.setQuantity(productDto.getQuantity());
        return flowerDto;
    }

}
