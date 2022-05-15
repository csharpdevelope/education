package uz.example.flower.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.example.flower.exception.BadRequestException;
import uz.example.flower.exception.NotFoundException;
import uz.example.flower.model.JSend;
import uz.example.flower.model.dto.FlowerDto;
import uz.example.flower.model.dto.ProductDto;
import uz.example.flower.model.entity.Flower;
import uz.example.flower.model.entity.GiftType;
import uz.example.flower.model.entity.User;
import uz.example.flower.model.enums.GiftTypeEnum;
import uz.example.flower.repository.CategoryRepository;
import uz.example.flower.repository.FlowerRepository;
import uz.example.flower.repository.GiftTypeRepository;
import uz.example.flower.service.FlowerService;
import uz.example.flower.service.product.ProductService;
import uz.example.flower.service.tools.SecurityUtils;
import uz.example.flower.utils.Messages;

import java.util.*;

@Service
public class ProductServiceImp implements ProductService {
    private final FlowerService flowerService;
    private final FlowerRepository flowerRepository;
    private final SecurityUtils securityUtils;
    private final ObjectMapper objectMapper;
    private final GiftTypeRepository giftTypeRepository;
    @Autowired
    private Gson gson;

    public ProductServiceImp(FlowerService flowerService, FlowerRepository flowerRepository, SecurityUtils securityUtils, ObjectMapper objectMapper, GiftTypeRepository giftTypeRepository) {
        this.flowerService = flowerService;
        this.flowerRepository = flowerRepository;
        this.securityUtils = securityUtils;
        this.objectMapper = objectMapper;
        this.giftTypeRepository = giftTypeRepository;
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
        responseFlowers.sort(Comparator.comparing(FlowerDto::getId));
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
    public JsonNode getFlowerByCategories(List<String> categoryName) {
        List<GiftType> listGift = giftTypeRepository.findAllByNameIn(categoryName);
        ObjectNode response = objectMapper.createObjectNode();
        for (GiftType gift : listGift) {
            response.putPOJO("data", getFlowerListByGiftType(gift.getName().name()));
        }
        return response;
    }

    @Override
    public JSend getFlowersByCategory(String giftName) {
        return JSend.success(getFlowerListByGiftType(giftName));
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
        return giftTypeRepository.findByName(GiftTypeEnum.valueOf(name))
                .orElseThrow(() -> new NotFoundException("Gift Type not found this name: " + name));
    }

    private List<FlowerDto> getFlowerListByGiftType(String giftName) {
        GiftType gift = giftByName(giftName);
        List<Flower> flowers;
        User user = securityUtils.getCurrentUser();
        if (user != null) {
            flowers = flowerRepository.findAllByUserAndGiftType(user, gift);
        } else {
            flowers = flowerRepository.findAllByGiftType(gift);
        }
        List<FlowerDto> flowerDtos = new ArrayList<>();

        for(Flower flower : flowers) {
            flowerDtos.add(flower.toFlowerDto());
        }
        return flowerDtos;
    }
}
