package uz.example.flower.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordDto {
    private String username;
    private String password;
    @JsonProperty("confirm_password")
    private String confirmPassword;
}
