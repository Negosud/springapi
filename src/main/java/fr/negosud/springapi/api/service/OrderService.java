package fr.negosud.springapi.api.service;

import fr.negosud.springapi.api.model.dto.OrderStatus;
import fr.negosud.springapi.api.model.dto.PlaceOrderRequest;
import fr.negosud.springapi.api.model.dto.SetOrderedProductElement;
import fr.negosud.springapi.api.model.entity.Order;
import fr.negosud.springapi.api.model.entity.OrderProduct;
import fr.negosud.springapi.api.model.entity.Product;
import fr.negosud.springapi.api.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    public Order placeOrderFromRequest(PlaceOrderRequest placeOrderRequest) {
        Order order = new Order();

        List<SetOrderedProductElement> orderedProducts = placeOrderRequest.getOrderedProducts();
        List<OrderProduct> orderProducts = new ArrayList<>();
        for (SetOrderedProductElement orderedProductElement : orderedProducts) {
            Product product = productService.getProductById(orderedProductElement.getProductId()).orElse(null);
            assert product != null : "Product Id " + orderedProductElement.getProductId() + "  doesn't correspond to a proper product";
            assert !product.isActive() : "Product for Id " + orderedProductElement.getProductId() + " isn't active";
            assert productService.getMaxOrderableProductQuantity(product) < orderedProductElement.getQuantity() : "Available product quantity for Product Id " + orderedProductElement.getProductId() + " is insufficient";
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);
            orderProduct.setProduct(product);
            orderProduct.setQuantity(orderedProductElement.getQuantity());
            orderProducts.add(orderProduct);
        }

        order.setStatus(OrderStatus.PENDING);
        order.setProductList(orderProducts);
        saveOrder(order);

        return order;
    }

}
