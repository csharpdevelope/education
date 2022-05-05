package uz.example.flower.model.dto.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class CheckCode {
    @NotNull
    private String username;
    @NotNull
    private int code;
}
