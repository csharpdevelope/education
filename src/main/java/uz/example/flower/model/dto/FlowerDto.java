package uz.example.flower.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import uz.example.flower.model.entity.Flower;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class FlowerDto {
    private Long id;
    private String name;
    @JsonProperty("created_date")
    private Date createdDate;
    private String heading;
    private String description;
    private Long discount;
    private Double price;
    private Long quantity;
    @JsonProperty("images_list")
    private List<String> imagesList;

    public Flower toFlower() {
        Flower flower = new Flower();
        flower.setQuantity(getQuantity());
        flower.setName(getName());
        flower.setHeading(getHeading());
        flower.setDescription(getDescription());
        flower.setDiscount(getDiscount());
        flower.setPrice(getPrice());
        return flower;
    }
}
