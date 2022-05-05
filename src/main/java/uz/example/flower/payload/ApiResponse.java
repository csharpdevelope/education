package uz.example.flower.payload;

import lombok.Data;

@Data

public class ApiResponse<T> {
    private int status;
    private String path;
    private T message;
}
