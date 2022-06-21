package uz.example.flower.model.entity;

import lombok.Getter;
import lombok.Setter;
import uz.example.flower.model.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "educations")
@Getter
@Setter
public class Education extends BaseEntity {
    private String purpose;
    private Integer hours;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "subject_id", referencedColumnName = "id")
    private Subject subject;
}
