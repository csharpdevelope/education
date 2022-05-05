package uz.example.flower.service.impl;

import org.springframework.stereotype.Service;
import uz.example.flower.model.JSend;
import uz.example.flower.model.entity.Flower;
import uz.example.flower.model.entity.Order;
import uz.example.flower.payload.request.OrderDto;
import uz.example.flower.repository.OrderRepository;
import uz.example.flower.service.FlowerService;
import uz.example.flower.service.OrderService;
import uz.example.flower.utils.Messages;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final FlowerService flowerService;

    public OrderServiceImpl(OrderRepository orderRepository, FlowerService flowerService) {
        this.orderRepository = orderRepository;
        this.flowerService = flowerService;
    }

    @Override
    public JSend create(OrderDto orderDto) {
        List<Flower> flowers = flowerService.getFlowersByIds(orderDto.getFlowerIds());
        if (flowers.isEmpty()) {
            return JSend.badRequest(400, "Flowers not Found");
        }
        Order order = new Order();
        order.setAddress(orderDto.getAddress());
        order.setCity(orderDto.getCity());
        order.setPhoneNumber(orderDto.getPhoneNumber());
        order.setNumber(orderDto.getNumber());
        order.setFullName(orderDto.getFio());
        order.setRegion(orderDto.getRegion());
        order.setFlowers(flowers);
        orderRepository.save(order);
        return JSend.success(Messages.ORDER_CREATE);
    }
}
