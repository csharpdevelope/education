package uz.example.flower.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.example.flower.exception.BadRequestException;
import uz.example.flower.exception.NotFoundException;
import uz.example.flower.model.JSend;
import uz.example.flower.model.dto.FlowerDto;
import uz.example.flower.model.dto.FlowerResponse;
import uz.example.flower.model.entity.*;
import uz.example.flower.model.enums.GiftTypeEnum;
import uz.example.flower.repository.CategoryRepository;
import uz.example.flower.repository.FlowerRepository;
import uz.example.flower.repository.GiftTypeRepository;
import uz.example.flower.repository.ImagesRepository;
import uz.example.flower.service.FlowerService;
import uz.example.flower.service.MinioService;
import uz.example.flower.service.tools.FlowerMapping;
import uz.example.flower.service.tools.SecurityUtils;

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

    public FlowerServiceImpl(FlowerRepository flowerRepository, MinioService minioService, SecurityUtils securityUtils, ImagesRepository imagesRepository, CategoryRepository categoryRepository, GiftTypeRepository giftTypeRepository) {
        this.flowerRepository = flowerRepository;
        this.minioService = minioService;
        this.securityUtils = securityUtils;
        this.imagesRepository = imagesRepository;
        this.categoryRepository = categoryRepository;
        this.giftTypeRepository = giftTypeRepository;
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
        return flowerRepository.getById(id);
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
