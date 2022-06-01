package uz.example.flower.service.impl;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uz.example.flower.exception.BadRequestException;
import uz.example.flower.exception.NotFoundException;
import uz.example.flower.model.JSend;
import uz.example.flower.model.dto.Attachment;
import uz.example.flower.model.dto.FlowerDto;
import uz.example.flower.model.dto.GiftTypeDto;
import uz.example.flower.model.dto.ProductDto;
import uz.example.flower.model.entity.*;
import uz.example.flower.repository.CategoryRepository;
import uz.example.flower.repository.FlowerRepository;
import uz.example.flower.repository.GiftTypeRepository;
import uz.example.flower.repository.ImagesRepository;
import uz.example.flower.service.FlowerService;
import uz.example.flower.service.MinioService;
import uz.example.flower.service.product.ProductService;
import uz.example.flower.service.tools.SecurityUtils;
import uz.example.flower.utils.Messages;

import java.util.*;

@Service
public class ProductServiceImp implements ProductService {
    private final FlowerService flowerService;
    private final FlowerRepository flowerRepository;
    private final SecurityUtils securityUtils;
    private final GiftTypeRepository giftTypeRepository;
    private final MinioService minioService;
    private final ImagesRepository imagesRepository;
    private final CategoryRepository categoryRepository;
    @Autowired
    private Gson gson;

    public ProductServiceImp(FlowerService flowerService, FlowerRepository flowerRepository, SecurityUtils securityUtils, GiftTypeRepository giftTypeRepository, MinioService minioService, ImagesRepository imagesRepository, CategoryRepository categoryRepository) {
        this.flowerService = flowerService;
        this.flowerRepository = flowerRepository;
        this.securityUtils = securityUtils;
        this.giftTypeRepository = giftTypeRepository;
        this.minioService = minioService;
        this.imagesRepository = imagesRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public JSend saveProduct(String body, String category,  List<String> giftTypes, List<MultipartFile> files) {
        ProductDto productDto = gson.fromJson(body, ProductDto.class);
        FlowerDto flowerDto = toFlowerDto(productDto);
        flowerService.postFlower(flowerDto, category, giftTypes, files);
        return JSend.success(flowerDto);
    }

    @Override
    @Transactional
    public JSend saveProduct(ProductDto productDto) {
        List<GiftType> giftTypes = new ArrayList<>();
        productDto.getGiftTypes().forEach(gift -> {
            GiftType giftType = giftByName(gift);
            giftTypes.add(giftType);
        });
        Category category = categoryRepository.findByName(productDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category not found: " + productDto.getCategory()));
        Flower flower = new Flower();
        flower.setName(productDto.getName());
        flower.setHeading(productDto.getHeading());
        flower.setDescription(productDto.getDescription());
        flower.setQuantity(0L);
        flower.setPrice(productDto.getPrice());
        flower.setQuantity(productDto.getQuantity());
        flower.setCategory(category);
        flower.setUser(securityUtils.getCurrentUser());
        flower.setGiftTypes(giftTypes);
        flowerRepository.save(flower);
        for(Long id: productDto.getImageIds()){
            Images image = imagesRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Image not found: " + id));
            image.setFlower(flower);
            imagesRepository.save(image);
        }
        return JSend.success();
    }

    @Override
    public JSend editProduct(String body, List<MultipartFile> files) {
        ProductDto productDto = gson.fromJson(body, ProductDto.class);
        FlowerDto flowerDto = toFlowerDto(productDto);
        flowerService.editFlowers(flowerDto, files);
        return JSend.success(flowerDto);
    }

    @Override
    public JSend getAll() {
        User user = securityUtils.getCurrentUser();
        List<Flower> flowers = flowerRepository.findAllByUser(user);
        List<FlowerDto> responseFlowers = new ArrayList<>();

        flowers.forEach(flower -> {
            responseFlowers.add(flower.toFlowerDto());
        });
        responseFlowers.sort((o1, o2) -> Long.compare(o2.getId(), o1.getId()));
        return JSend.success(responseFlowers);
    }

    @Override
    public JSend getById(Long id) {
        if (id == null) {
            throw new BadRequestException("Id not null");
        }
        Optional<Flower> optional = flower(id);
        if (optional.isPresent())
            return JSend.success(optional.get().toFlowerDto());

        return JSend.notFound(Messages.PRODUCT_NOT_FOUND + id);
    }

    @Override
    public JSend deleteProduct(Long id) {
        if (id == null) {
            throw new BadRequestException("Id not null");
        }
        Optional<Flower> optional = flower(id);
        if (optional.isPresent()) {
            flowerRepository.delete(optional.get());
            return JSend.success();
        }
        return JSend.notFound(Messages.PRODUCT_NOT_FOUND + id);
    }

    @Override
    public JSend editQuantityProduct(Long id, Long quantity) {
        if (id == null || quantity == null)
            throw new BadRequestException("Id not null");

        Optional<Flower> optional = flower(id);
        if (optional.isPresent()) {
            Flower flower = optional.get();
            flower.setQuantity(quantity);
            flowerRepository.save(flower);
            return JSend.success(Messages.PRODUCT_SUCCESS_CHANGE);
        }
        return JSend.success(Messages.PRODUCT_NOT_FOUND + id);
    }

    @Override
    public JSend editDiscount(Long id, Long discount) {
        if (id == null || discount == null)
            throw new BadRequestException("Id not null");

        Optional<Flower> optional = flower(id);
        if (optional.isPresent()) {
            Flower flower = optional.get();
            flower.setDiscount(discount);
            flowerRepository.save(flower);
            return JSend.success(Messages.PRODUCT_ADD_DISCOUNT);
        }
        return JSend.success(Messages.PRODUCT_NOT_FOUND + id);
    }

    @Override
    public JSend<List<FlowerDto>> getFlowerByCategories(List<String> categoryName) {
        List<GiftType> listGift = giftTypeRepository.findAllByNameIn(categoryName);
        List<Flower> flowers = flowerRepository.findAllByGiftTypesIn(listGift);
        List<FlowerDto> flowerDtos = new ArrayList<>();
        flowers.forEach(flower ->
            flowerDtos.add(flower.toFlowerDto()));
        return JSend.success(flowerDtos);
    }

    @Override
    public JSend getFlowersByCategory(String giftName) {
        return JSend.success(getFlowerListByGiftType(giftName));
    }

    @Override
    public JSend searchProduct(String text) {
        List<Flower> flowers = flowerRepository.searchAllByProducts(text);
        List<FlowerDto> flowerDtos = new ArrayList<>();
        flowers.forEach(flower -> {
            flowerDtos.add(flower.toFlowerDto());
        });
        return JSend.success(flowerDtos);
    }

    @Override
    public JSend getGiftTypes() {
        List<GiftTypeDto> list = new ArrayList<>();
        List<GiftType> types = giftTypeRepository.findAll();
        types.forEach(type -> {
            GiftTypeDto dto = new GiftTypeDto();
            dto.setId(type.getId());
            dto.setName(type.getName());
            list.add(dto);
        });
        return JSend.success(list);
    }

    @Override
    public Attachment uploadImage(MultipartFile file) {
        return minioService.uploadFile(file);
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

    private Optional<Flower> flower(Long id) {
        User user = securityUtils.getCurrentUser();
        return flowerRepository.findByIdAndUser(id, user);
    }

    private GiftType giftByName(String name) {
        return giftTypeRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Gift Type not found this name: " + name));
    }

    private List<FlowerDto> getFlowerListByGiftType(String giftName) {
        GiftType gift = giftByName(giftName);
        List<Flower> flowers;
        User user = securityUtils.getCurrentUser();
        if (user != null) {
            flowers = flowerRepository.findAllByUser(user);
        } else {
            flowers = flowerRepository.findAllFlower();
        }
        List<FlowerDto> flowerDtos = new ArrayList<>();

        for(Flower flower : flowers) {
            flowerDtos.add(flower.toFlowerDto());
        }
        return flowerDtos;
    }
}
