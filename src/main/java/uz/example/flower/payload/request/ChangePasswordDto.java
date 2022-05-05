package uz.example.flower.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ChangePasswordDto {
    @NotNull
    private String username;
    @JsonProperty("new_password")
    @NotNull
    private String newPassword;
    @JsonProperty("confirm_password")
    @NotNull
    private String confirmPassword;
}
