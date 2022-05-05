package uz.example.flower.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@RequiredArgsConstructor
public class FlImageResponse {
    private Long id;
    private String name;
    private String url;
    private String type;
    private long size;
}
