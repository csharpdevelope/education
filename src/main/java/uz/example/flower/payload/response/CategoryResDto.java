package uz.example.flower.payload.response;

import lombok.*;
import uz.example.flower.model.dto.FlowerResponse;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResDto {
    private Long id;
    private String name;
    private List<FlowerResponse> flowers;
}
