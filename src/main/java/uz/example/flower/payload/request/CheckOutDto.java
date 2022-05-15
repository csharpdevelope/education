package uz.example.flower.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckOutDto {
    private Long flowerId;
    private Integer count;
}
