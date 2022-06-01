package uz.example.flower.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class ProductDto {
    private Long id;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    private String heading;
    @NotNull
    @NotBlank
    private String description;
    private Long discount;
    @NotNull
    @NotBlank
    private Double price;
    @NotNull
    @NotBlank
    private Long quantity;
    @NotNull
    @NotBlank
    private String category;
    @JsonProperty("gift_types")
    private List<String> giftTypes;
    @JsonProperty("ids")
    private List<Long> imageIds;
}
