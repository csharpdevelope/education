package uz.example.flower.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import uz.example.flower.model.enums.Status;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class OrderPrepare {
    @NotNull
    @JsonProperty("order_id")
    private Long orderId;
}
