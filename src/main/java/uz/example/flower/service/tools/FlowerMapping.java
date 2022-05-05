package uz.example.flower.service.tools;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uz.example.flower.model.dto.FlImageResponse;
import uz.example.flower.model.dto.FlowerResponse;
import uz.example.flower.model.entity.Flower;
import uz.example.flower.model.entity.Images;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FlowerMapping {

    public static List<FlowerResponse> toFlowerResponse(List<Flower> flowers) {

        List<FlowerResponse> responseFlower = new ArrayList<>();
        for (Flower flower: flowers) {
            FlowerResponse dto = new FlowerResponse();
            List<Images> images = flower.getImages();
            dto.setDiscount(flower.getDiscount());
            dto.setId(flower.getId());
            dto.setHeading(flower.getHeading());
            dto.setName(flower.getName());
            dto.setDescription(flower.getDescription());
            dto.setPrice(flower.getPrice());
            dto.setId(flower.getId());
            dto.setDiscount(flower.getDiscount());
            List<FlImageResponse> responses = images.stream().map(file -> {
                String fileDownloadUrl = ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("api/download/")
                        .path(file.getFilename())
                        .toUriString();
                FlImageResponse responseImage = new FlImageResponse();
                responseImage.setId(file.getId());
                responseImage.setName(file.getFilename());
                responseImage.setType(file.getImgType());
                responseImage.setSize(file.getSize());
                responseImage.setUrl(fileDownloadUrl);
                return responseImage;
            }).collect(Collectors.toList());
            dto.setImageList(responses);

            responseFlower.add(dto);
        }
        return responseFlower;
    }
}
