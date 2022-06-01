package uz.example.flower.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import uz.example.flower.model.base.BaseEntity;
import uz.example.flower.model.dto.FlowerDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "flowers")
@JsonIgnoreProperties({"createdDate", "updatedDate", "active"})
@Getter
@Setter
public class Flower extends BaseEntity {

    private String name;
    private String heading;
    @Column(columnDefinition = "TEXT")
    private String description;
    private Long discount;
    private Double price;
    private Long quantity;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "flower")
    private List<Images> images;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "gift_flower_type",
            joinColumns = @JoinColumn(name = "gift_type_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "flower_id", referencedColumnName = "id")
    )
    private List<GiftType> giftTypes = new ArrayList<>();

    public FlowerDto toFlowerDto() {
        FlowerDto flower = new FlowerDto();
        flower.setId(getId());
        flower.setName(getName());
        flower.setCreatedDate(getCreatedDate());
        flower.setHeading(getHeading());
        flower.setDescription(getDescription());
        flower.setDiscount(getDiscount());
        flower.setPrice(getPrice());
        flower.setQuantity(getQuantity());
        List<String> images = new ArrayList<>();
        getImages().forEach(img -> {
            images.add(img.getImgUrl());
        });
        flower.setImagesList(images);
        return flower;
    }
}
