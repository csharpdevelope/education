package uz.example.flower.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class EducationDto {
    @NotNull
    private String name;
    @NotNull
    private Integer hours;
}
