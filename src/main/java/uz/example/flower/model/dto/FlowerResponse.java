package uz.example.flower.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
@RequiredArgsConstructor
public class FlowerResponse {
    private Long id;
    private String name;
    private String heading;
    private String description;
    private Long discount;
    private Double price;
    @JsonProperty("image_list")
    private List<FlImageResponse> imageList;
}
