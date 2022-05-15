package uz.example.flower.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckOutResponse {
    private Long id;
    private String name;
    private Integer count;
    @JsonProperty("img_url")
    private String imgUrl;
    private Double price;
    private Long discount;
    private boolean isFavorite;

    public CheckOutResponse() {
    }

    public CheckOutResponse(Long id, String name, Integer count, String imgUrl, Double price, Long discount, boolean isFavorite) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.imgUrl = imgUrl;
        this.price = price;
        this.discount = discount;
        this.isFavorite = isFavorite;
    }
}
