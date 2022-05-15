package uz.example.flower.model.entity;

import lombok.Getter;
import lombok.Setter;
import uz.example.flower.model.base.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "basket_cart")
@Getter
@Setter
public class Checkout extends BaseEntity {

    private boolean favorite;
    private Integer count;

    @OneToOne
    @JoinColumn(name = "flower_id", referencedColumnName = "id")
    private Flower flower;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
