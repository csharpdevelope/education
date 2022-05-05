package uz.example.flower.model.entity;

import lombok.Data;
import uz.example.flower.model.enums.GiftTypeEnum;

import javax.persistence.*;

@Entity
@Data
public class GiftType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private GiftTypeEnum name;
}
