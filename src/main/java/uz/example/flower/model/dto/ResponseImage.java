package uz.example.flower.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ResponseImage {
    private Long id;
    private String name;
    private String url;
    private String type;
    private Long size;
}
