package fr.negosud.springapi.api.service;

import fr.negosud.springapi.api.model.dto.OrderStatus;
import fr.negosud.springapi.api.model.dto.PlaceOrderRequest;
import fr.negosud.springapi.api.model.entity.Order;
import fr.negosud.springapi.api.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;

    @Autowired
    public OrderService(OrderRepository orderRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
    }

    public List<Order> getAllOrders(OrderStatus orderStatus) {
        return orderStatus == null ? orderRepository.findAll() : orderRepository.findAllByStatus(orderStatus);
    }

    public Optional<Order> getOrderByReference(String reference) {
        return orderRepository.findByReference(reference);
    }

    public Optional<Order> getOrderById(long orderId) {
        return orderRepository.findById(orderId);
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order placeOrderFromRequest(PlaceOrderRequest placeOrderRequest) {
        Order order = new Order();

        return order;
    }

}
