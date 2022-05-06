package uz.example.flower.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.example.flower.model.JSend;
import uz.example.flower.payload.request.OrderDto;
import uz.example.flower.service.OrderService;

@RestController
@RequestMapping("api/order")
@SecurityRequirement(name = "FLower Shopping")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("create")
    public ResponseEntity<?> orderCreate(@RequestBody OrderDto orderDto) {
        if (orderDto.getPhoneNumber() == null ||
                orderDto.getNumber() == null ||
                orderDto.getRegion() == null ||
                orderDto.getFlowerIds().isEmpty() ||
                orderDto.getAddress() == null ||
                orderDto.getCity() == null ) {
            return new ResponseEntity<>(JSend.badRequest("Insufficient data entered"), HttpStatus.BAD_REQUEST);
        }
        JSend response = orderService.create(orderDto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }
}
