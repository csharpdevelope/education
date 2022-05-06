package uz.example.flower.payload.request;

import lombok.Data;
import uz.example.flower.model.enums.RoleEnum;

import java.util.List;

@Data
public class AdminUserDto {
    private String username;
    private List<String> roles;
}