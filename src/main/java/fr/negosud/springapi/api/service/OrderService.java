package fr.negosud.springapi.api.service;

import fr.negosud.springapi.api.model.OrderStatus;
import fr.negosud.springapi.api.model.dto.request.PlaceOrderRequest;
import fr.negosud.springapi.api.model.dto.request.element.SetOrderProductElement;
import fr.negosud.springapi.api.model.dto.response.OrderResponse;
import fr.negosud.springapi.api.model.dto.response.element.OrderProductInOrderElement;
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
    private final OrderProductService orderProductService;

    @Autowired
    public OrderService(OrderRepository orderRepository, ProductService productService, OrderProductService orderProductService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.orderProductService = orderProductService;
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

        List<OrderProduct> orderProducts = new ArrayList<>();
        for (SetOrderProductElement orderProductElement : placeOrderRequest.getOrderProducts()) {
            Product product = productService.getProductById(orderProductElement.getProductId()).orElse(null);
            assert product != null : "Product Id " + orderProductElement.getProductId() + "  doesn't correspond to a proper product";
            assert product.isActive() : "Product for Id " + orderProductElement.getProductId() + " isn't active";
            assert productService.getMaxOrderableProductQuantity(product) < orderProductElement.getQuantity() : "Available product quantity for Product Id " + orderProductElement.getProductId() + " is insufficient";
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);
            orderProduct.setProduct(product);
            orderProduct.setQuantity(orderProductElement.getQuantity());
            orderProducts.add(orderProduct);
        }

        order.setStatus(OrderStatus.PENDING);
        order.setProductList(orderProducts);
        saveOrder(order);

        return order;
    }

    public void markAsReady(Order order) {
        for (OrderProduct orderProduct : order.getProductList()) {
            try {
                orderProductService.markAsReady(orderProduct);
            } catch (AssertionError ignored) { }
        }
        order.setStatus(OrderStatus.READY);
        saveOrder(order);
    }

    public void cancelOrder(Order order) {
        assert order.getStatus() != OrderStatus.CANCELLED : "Order is already canceled";
        assert order.getStatus() != OrderStatus.COMPLETED : "Order is completed";
        order.setStatus(OrderStatus.CANCELLED);
        saveOrder(order);
    }

    public void completeOrder(Order order) {
        assert order.getStatus() != OrderStatus.COMPLETED : "Order is already completed";
        assert order.getStatus() != OrderStatus.CANCELLED : "Order is cancelled";
        order.setStatus(OrderStatus.COMPLETED);
        saveOrder(order);
    }

    public OrderResponse getResponseFromOrder(Order order) {
        return new OrderResponse()
                .setId(order.getId())
                .setReference(order.getReference())
                .setStatus(order.getStatus())
                .setPreparedBy(order.getPreparedBy())
                .setPreparedAt(order.getPreparedAt())
                .setInvoice(order.getInvoice())
                .setProductList(getOrderProductElements(order.getProductList()));
    }

    private List<OrderProductInOrderElement> getOrderProductElements(List<OrderProduct> orderProducts) {
        List<OrderProductInOrderElement> orderProductInOrderElements = new ArrayList<>();
        for (OrderProduct orderProduct : orderProducts) {
            OrderProductInOrderElement orderProductInOrderElement = new OrderProductInOrderElement();
            orderProductInOrderElement.setId(orderProduct.getId())
                    .setQuantity(orderProduct.getQuantity())
                    .setPreparedAt(orderProduct.getPreparedAt())
                    .setPreparedBy(orderProduct.getPreparedBy())
                    .setProduct(orderProduct.getProduct());
            orderProductInOrderElements.add(orderProductInOrderElement);
        }
        return orderProductInOrderElements;
    }
}
