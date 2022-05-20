package uz.example.flower.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import uz.example.flower.model.entity.Flower;

import java.util.Date;
import java.util.List;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDiscount() {
        return discount;
    }

    public void setDiscount(Long discount) {
        this.discount = discount;
    }

    public List<String> getImagesList() {
        return imagesList;
    }

    public void setImagesList(List<String> imagesList) {
        this.imagesList = imagesList;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

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
