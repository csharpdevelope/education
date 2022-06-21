package uz.example.flower.model.dto.db;

import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RegisterDto {
    @NotNull
    @Column(unique = true)
    private String username;
    @NotNull
    private String password;
    private String firstname;
    private String lastname;
}
