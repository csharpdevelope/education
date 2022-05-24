package uz.example.flower.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import uz.example.flower.model.base.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "flower_images")
@Getter
@Setter
public class Images extends BaseEntity {
    private String filename;

    private Long size;

    @Column(name = "img_type")
    private String imgType;

    @Column(name = "img_url")
    private String imgUrl;

    @ManyToOne
    @JoinColumn(name = "flower_id")
    private Flower flower;
}
