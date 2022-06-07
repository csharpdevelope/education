package uz.example.flower.service.impl;

import org.springframework.stereotype.Service;
import uz.example.flower.exception.BadRequestException;
import uz.example.flower.model.JSend;
import uz.example.flower.model.entity.Flower;
import uz.example.flower.model.entity.Order;
import uz.example.flower.model.entity.User;
import uz.example.flower.model.enums.Status;
import uz.example.flower.payload.request.OrderDto;
import uz.example.flower.payload.request.OrderPrepare;
import uz.example.flower.repository.OrderRepository;
import uz.example.flower.service.FlowerService;
import uz.example.flower.service.OrderService;
import uz.example.flower.component.SecurityUtils;
import uz.example.flower.utils.Messages;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final FlowerService flowerService;
    private final SecurityUtils securityUtils;

    public OrderServiceImpl(OrderRepository orderRepository, FlowerService flowerService, SecurityUtils securityUtils) {
        this.orderRepository = orderRepository;
        this.flowerService = flowerService;
        this.securityUtils = securityUtils;
    }

    @Override
    public JSend create(OrderDto orderDto) {
        List<Long> productIds = orderDto.getFlowerIds();
        List<Flower> flowers = flowerService.getFlowersByIds(productIds);
        if (flowers.isEmpty()) {
            return JSend.badRequest(400, Messages.PRODUCT_NOT_FOUNDS + "(" + productIds + ")");
        }
        User user = securityUtils.getCurrentUser();
        Order order = orderDto.toOrder();
        order.setStatus(Status.accepted);
        order.setPaymentStatus(orderDto.getPaymentStatus());
        order.setUser(user);
        order.setFlowers(flowers);
        orderRepository.save(order);
        return JSend.success(Messages.ORDER_CREATE, order.getId());
    }

    @Override
    public JSend delete(Long id) {
        if (id == null) {
            throw new BadRequestException("Id not null");
        }
        Optional<Order> order = orderRepository.findById(id);

        if (order.isPresent()) {
            orderRepository.delete(order.get());
            return JSend.success(Messages.ORDER_DELETE);
        }
        return JSend.notFound(Messages.ORDER_NOT_FOUND + id);
    }

    @Override
    public JSend prepare(OrderPrepare orderPrepare) {

        Optional<Order> optional = orderRepository.findById(orderPrepare.getOrderId());
        if (optional.isEmpty())
            return JSend.notFound("Order not found");

        Order order = optional.get();
        orderStatusChange(order, Status.prepared);
        return JSend.success();
    }

    @Override
    public JSend way(OrderPrepare orderPrepare) {
        Optional<Order> optional = orderRepository.findById(orderPrepare.getOrderId());
        if (optional.isEmpty())
            return JSend.notFound("Order not found");

        Order order = optional.get();
        orderStatusChange(order, Status.way);
        return JSend.success();
    }

    @Override
    public JSend delivered(OrderPrepare orderPrepare) {
        Optional<Order> optional = orderRepository.findById(orderPrepare.getOrderId());
        if (optional.isEmpty())
            return JSend.notFound("Order not found");

        Order order = optional.get();
        orderStatusChange(order, Status.delivered);
        return JSend.success();
    }

    private void orderStatusChange(Order order, Status status) {
        order.setStatus(status);
        orderRepository.save(order);
    }
}
