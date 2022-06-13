package uz.example.flower.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.multipart.MultipartFile;
import uz.example.flower.model.JSend;
import uz.example.flower.model.dto.FlowerDto;
import uz.example.flower.model.dto.FlowerResponse;
import uz.example.flower.model.entity.Flower;
import uz.example.flower.model.entity.Images;

import java.util.List;

public interface FlowerService {
    List<FlowerResponse> getAll();

    FlowerDto postFlower(FlowerDto flower, List<MultipartFile> files);

    FlowerDto postFlower(FlowerDto flower, String category, List<String> giftTypes, List<MultipartFile> files);

    JSend getFlowerImage(String objectName);

    Images getFlowerImageById(Long id);

    List<Flower> getFlowersByIds(List<Long> ids);

    FlowerDto editFlowers(FlowerDto flowerDto, List<MultipartFile> files);

    Flower getById(Long id);

    JsonNode getAllWithPage(Integer page, Integer size);

    JsonNode getAllWithPage(Integer page, Integer size, String name, Boolean isDesc);

    JSend getFlowerById(Long id);
}
