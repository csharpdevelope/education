package uz.example.flower.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
