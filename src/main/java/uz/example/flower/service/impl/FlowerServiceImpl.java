package uz.example.flower.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uz.example.flower.model.dto.FlImageResponse;
import uz.example.flower.model.dto.FlowerDto;
import uz.example.flower.model.dto.FlowerResponse;
import uz.example.flower.model.entity.Flower;
import uz.example.flower.model.entity.Images;
import uz.example.flower.repository.FlowerRepository;
import uz.example.flower.repository.ImagesRepository;
import uz.example.flower.service.FileDbStorageService;
import uz.example.flower.service.FlowerService;
import uz.example.flower.service.tools.FlowerMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FlowerServiceImpl implements FlowerService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final FlowerRepository flowerRepository;
    private final ImagesRepository imagesRepository;
    private final FileDbStorageService fileDbStorageService;

    public FlowerServiceImpl(FlowerRepository flowerRepository, ImagesRepository imagesRepository, FileDbStorageService fileDbStorageService) {
        this.flowerRepository = flowerRepository;
        this.imagesRepository = imagesRepository;
        this.fileDbStorageService = fileDbStorageService;
    }

    @Override
    public List<FlowerResponse> getAll() {
        List<Flower> flowers = flowerRepository.findAll();
        return FlowerMapping.toFlowerResponse(flowers);
    }

    @Override
    public FlowerDto postFlower(FlowerDto flower, List<MultipartFile> files) {
        Flower flower1 = new Flower();
        flower1.setDescription(flower.getDescription());
        if (flower.getDiscount() != null)
            flower1.setDiscount(flower.getDiscount());
        flower1.setDiscount(0L);
        flower1.setHeading(flower.getHeading());
        flower1.setPrice(flower.getPrice());
        flower1.setName(flower.getName());
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
        Images images = imagesRepository.findByFilename(filename);
        return images;
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
}
