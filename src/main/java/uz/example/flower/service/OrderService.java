package uz.example.flower.service;

import uz.example.flower.model.JSend;
import uz.example.flower.payload.request.OrderDto;
import uz.example.flower.payload.request.OrderPrepare;

public interface OrderService {
    JSend create(OrderDto orderDto);

    JSend delete(Long id);

    JSend prepare(OrderPrepare orderPrepare);

    JSend way(OrderPrepare orderPrepare);

    JSend delivered(OrderPrepare orderPrepare);
}
