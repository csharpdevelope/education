package uz.example.flower.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import uz.example.flower.model.entity.Order;
import uz.example.flower.model.enums.PaymentStatus;

import java.util.List;

@Getter
@Setter
public class OrderDto {
    private Long number;
    private String region;
    private String city;
    private String address;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("payment_status")
    private PaymentStatus paymentStatus;

    @JsonProperty(value = "flower_ids")
    private List<Long> flowerIds;

    public Order toOrder() {
        Order order = new Order();
        order.setAddress(getAddress());
        order.setCity(getCity());
        order.setPhoneNumber(getPhoneNumber());
        order.setNumber(getNumber());
        order.setRegion(getRegion());
        order.setPaymentStatus(getPaymentStatus());
        return order;
    }
}
