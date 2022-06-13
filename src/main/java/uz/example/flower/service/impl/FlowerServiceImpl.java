package uz.example.flower.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.example.flower.exception.BadRequestException;
import uz.example.flower.exception.NotFoundException;
import uz.example.flower.model.JSend;
import uz.example.flower.model.dto.FlowerDto;
import uz.example.flower.model.dto.FlowerResponse;
import uz.example.flower.model.entity.*;
import uz.example.flower.repository.CategoryRepository;
import uz.example.flower.repository.FlowerRepository;
import uz.example.flower.repository.GiftTypeRepository;
import uz.example.flower.repository.ImagesRepository;
import uz.example.flower.service.FlowerService;
import uz.example.flower.service.MinioService;
import uz.example.flower.service.tools.FlowerMapping;
import uz.example.flower.component.SecurityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FlowerServiceImpl implements FlowerService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final FlowerRepository flowerRepository;
    private final MinioService minioService;
    private final SecurityUtils securityUtils;
    private final ImagesRepository imagesRepository;
    private final CategoryRepository categoryRepository;
    private final GiftTypeRepository giftTypeRepository;
    private final ObjectMapper objectMapper;

    public FlowerServiceImpl(FlowerRepository flowerRepository, MinioService minioService, SecurityUtils securityUtils, ImagesRepository imagesRepository, CategoryRepository categoryRepository, GiftTypeRepository giftTypeRepository, ObjectMapper objectMapper) {
        this.flowerRepository = flowerRepository;
        this.minioService = minioService;
        this.securityUtils = securityUtils;
        this.imagesRepository = imagesRepository;
        this.categoryRepository = categoryRepository;
        this.giftTypeRepository = giftTypeRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<FlowerResponse> getAll() {
        List<Flower> flowers = flowerRepository.findAllFlower();
        return FlowerMapping.toFlowerResponse(flowers);
    }

    @Override
    public FlowerDto postFlower(FlowerDto flower, List<MultipartFile> files) {
        if (flower.getDiscount() == null) {
            flower.setDiscount(0L);
        }
        Flower flower1 = flower.toFlower();
        User user = securityUtils.getCurrentUser();
        flower1.setUser(user);
        flowerRepository.save(flower1);
        List<Images> images = new ArrayList<>();
        for(MultipartFile file : files) {
            try {
                JSend response = minioService.uploadFile(file, flower1);
                if (response.getCode() == 200) {
                    images.add((Images) response.getData());
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("File Upload Error: ", e);
            }
        }
        flower1.setImages(images);
        flower.setId(flower1.getId());
        flower.setDiscount(flower1.getDiscount());
        List<String> imgList = new ArrayList<>();
        for (Images image : images) {
            imgList.add(image.getFilename());
        }
        flower.setImagesList(imgList);
        return flower;
    }

    @Override
    public FlowerDto postFlower(FlowerDto flower, String name, List<String> giftTypes, List<MultipartFile> files) {
        if (flower.getDiscount() == null) {
            flower.setDiscount(0L);
        }
        Flower flower1 = flower.toFlower();
        User user = securityUtils.getCurrentUser();
        flower1.setUser(user);
        Category category = getCategory(name);
        flower1.setCategory(category);
        List<GiftType> giftTypeList = getGiftTypeList(giftTypes);
        flower1.setGiftTypes(giftTypeList);
        flowerRepository.save(flower1);
        List<Images> images = new ArrayList<>();
        for(MultipartFile file : files) {
            try {
                JSend response = minioService.uploadFile(file, flower1);
                if (response.getCode() == 200) {
                    images.add((Images) response.getData());
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("File Upload Error: ", e);
            }
        }
        flower1.setImages(images);
        flower.setId(flower1.getId());
        flower.setDiscount(flower1.getDiscount());
        List<String> imgList = new ArrayList<>();
        for (Images image : images) {
            imgList.add(image.getFilename());
        }
        flower.setImagesList(imgList);
        return flower;
    }

    @Override
    public JSend getFlowerImage(String objectName) {
        Images images = imagesRepository.findByFilename(objectName);
        return minioService.getFileByObjectName(images);
    }

    @Override
    public Images getFlowerImageById(Long id) {
        return null;
    }

    @Override
    public List<Flower> getFlowersByIds(List<Long> ids) {
        List<Flower> flowers = flowerRepository.findAllByIdIn(ids);
        if (!flowers.isEmpty()) {
            return null;
        }
        return flowers;
    }

    @Override
    public FlowerDto editFlowers(FlowerDto flowerDto, List<MultipartFile> files) {
        if (flowerDto.getId() == null) {
            throw new BadRequestException("Id not found");
        }
        Flower flower = flowerRepository.findById(flowerDto.getId())
                .orElseThrow(() -> new NotFoundException("Flower not found"));

        flower.setName(flowerDto.getName());
        flower.setHeading(flowerDto.getHeading());
        flower.setDescription(flowerDto.getDescription());
        flower.setDiscount(flowerDto.getDiscount());
        flower.setPrice(flowerDto.getPrice());
        flower.setQuantity(flowerDto.getQuantity());
        flowerRepository.save(flower);
        List<String> imgList = new ArrayList<>();
        flower.getImages().forEach(image -> {
            imgList.add(image.getFilename());
        });
        flowerDto.setImagesList(imgList);

        return flowerDto;
    }

    @Override
    public Flower getById(Long id) {
        Optional<Flower> optional = flowerRepository.findById(id);
        if (optional.isPresent())
            return optional.get();

        return null;
    }

    @Override
    public JsonNode getAllWithPage(Integer page, Integer size) {
        if (page == null)
            page = 0;
        if (size == null)
            size = 10;
        Pageable pageable = PageRequest.of(page, size);
        return getFlowerByPageable(pageable);
    }

    @Override
    public JsonNode getAllWithPage(Integer page, Integer size, String name, Boolean isDesc) {
        Pageable pageable;
        if (isDesc) {
            pageable = PageRequest.of(page, size, Sort.by(name).descending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(name));
        }
        return getFlowerByPageable(pageable);
    }

    @Override
    public JSend getFlowerById(Long id) {
        Flower flower = getById(id);
        if (flower == null)
            return JSend.notFound("Not found product");
        return JSend.success(flower.toFlowerDto());
    }

    public JsonNode getFlowerByPageable(Pageable pageable) {
        Page<Flower> flowers = flowerRepository.findAll(pageable);
        return getFlowerWithPage(flowers);
    }

    public JsonNode getFlowerByPageable(Pageable pageable, String name) {
        String output = name.substring(0, 1).toUpperCase() + name.substring(1);
        GiftType giftType = giftTypeRepository.findByName(output)
                .orElseThrow(() -> new NotFoundException("Not found Gift"));
        Page<Flower> flowers = flowerRepository.findAllByGiftTypesIn(List.of(giftType), pageable);
        return getFlowerWithPage(flowers);
    }

    private JsonNode getFlowerWithPage(Page<Flower> flowers) {
        ObjectNode response = objectMapper.createObjectNode();
        List<FlowerDto> flowerDtos = new ArrayList<>();
        flowers.getContent().forEach(flower -> flowerDtos.add(flower.toFlowerDto()));
        response.putPOJO("list", flowerDtos);
        response.put("page", flowers.getNumber());
        response.put("page_size", flowers.getSize());
        response.put("total_pages", flowers.getTotalPages());
        response.put("total_elements", flowers.getTotalElements());
        return response;
    }

    private Category getCategory(String name) {
        Optional<Category> optional = categoryRepository.findByName(name);
        if (optional.isPresent()) {
            return optional.get();
        }

        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }

    private List<GiftType> getGiftTypeList(List<String> giftTypes) {
        List<GiftType> giftTypeList = new ArrayList<>();
        for (String gift: giftTypes) {
            GiftType giftType = giftTypeRepository.findByName(gift)
                    .orElseThrow(() -> new NotFoundException("Not found navbar"));
            giftTypeList.add(giftType);
        }
        return giftTypeList;
    }
}
