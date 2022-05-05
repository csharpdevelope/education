package uz.example.flower.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {
    private Long id;
    private String name;
    private String heading;
    private String description;
    private Long discount;
    private Double price;
    private Long quantity;
}
