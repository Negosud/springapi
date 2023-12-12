package fr.negosud.springapi.api.service;

import fr.negosud.springapi.api.model.entity.Order;
import fr.negosud.springapi.api.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {

        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrders() {

        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long orderId) {

        return orderRepository.findById(orderId);
    }

    public Order saveOrder(Order order) {

        return orderRepository.save(order);
    }
}
