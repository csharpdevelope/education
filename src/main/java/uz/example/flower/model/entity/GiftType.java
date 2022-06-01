package uz.example.flower.model.entity;

import lombok.Data;
import uz.example.flower.model.enums.GiftTypeEnum;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class GiftType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "giftTypes")
    private List<Flower> flowers;
}
