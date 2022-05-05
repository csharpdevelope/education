package uz.example.flower.service;

import uz.example.flower.model.JSend;
import uz.example.flower.payload.request.OrderDto;

public interface OrderService {
    JSend create(OrderDto orderDto);
}
