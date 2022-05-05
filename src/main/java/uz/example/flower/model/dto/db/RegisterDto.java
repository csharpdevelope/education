package uz.example.flower.model.dto.db;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class RegisterDto {
    @NotNull
    @Email(message = "Email is invalid. Please enter email is right.")
    @Column(unique = true)
    private String email;
    @NotNull
    @Column(unique = true)
    private String username;
    @NotNull
    private String password;
    @NotNull
    @JsonProperty("confirm_password")
    private String confirmPassword;
    private String firstname;
    private String lastname;
}
