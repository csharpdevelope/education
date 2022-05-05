package uz.example.flower.service;

import org.springframework.web.multipart.MultipartFile;
import uz.example.flower.model.dto.FlowerDto;
import uz.example.flower.model.dto.FlowerResponse;
import uz.example.flower.model.entity.Flower;
import uz.example.flower.model.entity.Images;

import java.util.List;

public interface FlowerService {
    List<FlowerResponse> getAll();

    FlowerDto postFlower(FlowerDto flower, List<MultipartFile> files);

    Images getFlowerImage(String filename);

    Images getFlowerImageById(Long id);

    List<Flower> getFlowersByIds(List<Long> ids);
}
