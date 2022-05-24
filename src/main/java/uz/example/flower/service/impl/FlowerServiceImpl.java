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
import uz.example.flower.model.entity.Flower;
import uz.example.flower.model.entity.Images;
import uz.example.flower.model.entity.User;
import uz.example.flower.repository.FlowerRepository;
import uz.example.flower.repository.ImagesRepository;
import uz.example.flower.service.FlowerService;
import uz.example.flower.service.MinioService;
import uz.example.flower.service.tools.FlowerMapping;
import uz.example.flower.service.tools.SecurityUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class FlowerServiceImpl implements FlowerService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final FlowerRepository flowerRepository;
    private final MinioService minioService;
    private final SecurityUtils securityUtils;
    private final ImagesRepository imagesRepository;

    public FlowerServiceImpl(FlowerRepository flowerRepository, MinioService minioService, SecurityUtils securityUtils, ImagesRepository imagesRepository) {
        this.flowerRepository = flowerRepository;
        this.minioService = minioService;
        this.securityUtils = securityUtils;
        this.imagesRepository = imagesRepository;
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
}
