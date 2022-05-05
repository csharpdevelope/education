package uz.example.flower.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.example.flower.exception.BadRequestException;
import uz.example.flower.exception.NotFoundException;
import uz.example.flower.model.dto.FlowerDto;
import uz.example.flower.model.dto.FlowerResponse;
import uz.example.flower.model.entity.Flower;
import uz.example.flower.model.entity.Images;
import uz.example.flower.model.entity.User;
import uz.example.flower.repository.FlowerRepository;
import uz.example.flower.repository.ImagesRepository;
import uz.example.flower.service.FileDbStorageService;
import uz.example.flower.service.FlowerService;
import uz.example.flower.service.tools.FlowerMapping;
import uz.example.flower.service.tools.SecurityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FlowerServiceImpl implements FlowerService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final FlowerRepository flowerRepository;
    private final ImagesRepository imagesRepository;
    private final FileDbStorageService fileDbStorageService;
    private final SecurityUtils securityUtils;

    public FlowerServiceImpl(FlowerRepository flowerRepository, ImagesRepository imagesRepository, FileDbStorageService fileDbStorageService, SecurityUtils securityUtils) {
        this.flowerRepository = flowerRepository;
        this.imagesRepository = imagesRepository;
        this.fileDbStorageService = fileDbStorageService;
        this.securityUtils = securityUtils;
    }

    @Override
    public List<FlowerResponse> getAll() {
        List<Flower> flowers = flowerRepository.findAll();
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
                images.add(fileDbStorageService.store(file, flower1));
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("File Upload Error: ", e.getMessage());
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
    public Images getFlowerImage(String filename) {
        return imagesRepository.findByFilename(filename);
    }

    @Override
    public Images getFlowerImageById(Long id) {
        Optional<Images> optional = imagesRepository.findById(id);
        Images images = null;
        if (optional.isPresent()) {
            images = optional.get();
            images.setData(images.getData());
        }

        return images;
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
}
