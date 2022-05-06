package uz.example.flower.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.example.flower.model.JSend;
import uz.example.flower.payload.request.OrderDto;
import uz.example.flower.service.OrderService;

@RestController
@RequestMapping("api/order")
@SecurityRequirement(name = "FLower Shopping")
@PreAuthorize(value = "hasAuthority('USER')")
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
            return new ResponseEntity<>(JSend.badRequest("The data entered is insufficient"), HttpStatus.BAD_REQUEST);
        }
        JSend response = orderService.create(orderDto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @DeleteMapping("delete{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable(value = "id") Long id) {
        JSend response = orderService.delete(id);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

}
