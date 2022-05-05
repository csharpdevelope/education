package uz.example.flower.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import uz.example.flower.model.base.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    @NotNull
    private User user;
}
