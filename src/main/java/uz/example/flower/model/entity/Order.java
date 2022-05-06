package uz.example.flower.model.entity;

import lombok.Getter;
import lombok.Setter;
import uz.example.flower.model.base.BaseEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order extends BaseEntity {
    private Long number;
    private String region;
    private String city;
    private String address;
    private String phoneNumber;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "order_flowers",
            joinColumns = @JoinColumn(name = "flower_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id")
    )
    private List<Flower> flowers;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
