package uz.example.flower.model.entity;

import org.hibernate.annotations.Type;
import uz.example.flower.model.base.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "flower_images")
public class Images extends BaseEntity {
    private String filename;

    @Lob
    @Type(type = "org.hibernate.type.ImageType")
    private byte[] data;

    private Long size;

    @Column(name = "img_type")
    private String imgType;

    @Column(name = "img_url")
    private String imgUrl;

    @ManyToOne
    @JoinColumn(name = "flower_id")
    private Flower flower;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Flower getFlower() {
        return flower;
    }

    public void setFlower(Flower flower) {
        this.flower = flower;
    }
}
