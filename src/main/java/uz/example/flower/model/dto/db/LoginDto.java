package uz.example.flower.model.dto.db;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
