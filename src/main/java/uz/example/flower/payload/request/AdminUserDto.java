package uz.example.flower.payload.request;

import lombok.Data;

import java.util.List;

@Data
public class AdminUserDto {
    private Long id;
    private String email;
    private String username;
    private String firstname;
    private String lastname;
    private List<String> roles;
}