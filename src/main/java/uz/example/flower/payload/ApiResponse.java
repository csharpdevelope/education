package uz.example.flower.payload;

import lombok.Data;

import java.util.Date;

@Data
public class ApiResponse<T> {
    private int status;
    private String path;
    private String method;
    private Date timestamp;
    private T message;
}
