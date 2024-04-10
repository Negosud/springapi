package fr.negosud.springapi.service;

import fr.negosud.springapi.model.OrderStatus;
import fr.negosud.springapi.model.dto.request.PlaceOrderRequest;
import fr.negosud.springapi.model.dto.request.element.SetOrderProductRequestElement;
import fr.negosud.springapi.model.dto.response.OrderResponse;
import fr.negosud.springapi.model.dto.response.element.OrderProductInOrderResponseElement;
import fr.negosud.springapi.model.entity.Order;
import fr.negosud.springapi.model.entity.OrderProduct;
import fr.negosud.springapi.model.entity.Product;
import fr.negosud.springapi.repository.OrderRepository;
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
    private final ProductTransactionService productTransactionService;

    @Autowired
    public OrderService(OrderRepository orderRepository, ProductService productService, OrderProductService orderProductService, ProductTransactionService productTransactionService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.orderProductService = orderProductService;
        this.productTransactionService = productTransactionService;
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

    /**
     * @throws AssertionError Order can't be placed
     */
    public Order placeOrderFromRequest(PlaceOrderRequest placeOrderRequest) {
        Order order = new Order();

        List<OrderProduct> orderProducts = new ArrayList<>();
        for (SetOrderProductRequestElement orderProductElement : placeOrderRequest.getOrderProducts()) {
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

    /**
     * @throws AssertionError Order can't be cancelled
     */
    public void cancelOrder(Order order) {
        assert order.getStatus() != OrderStatus.CANCELLED : "Order is already canceled";
        assert order.getStatus() != OrderStatus.COMPLETED : "Order is completed";
        order.setStatus(OrderStatus.CANCELLED);
        saveOrder(order);
    }

    /**
     * @throws AssertionError Order can't be completed
     */
    public void completeOrder(Order order) {
        assert order.getStatus() != OrderStatus.COMPLETED : "Order is already completed";
        assert order.getStatus() != OrderStatus.CANCELLED : "Order is cancelled";
        order.setStatus(OrderStatus.COMPLETED);
        // TODO: THIS NEED TO MAKE PRODUCT FRICKIN TRANSACTION BRO
        saveOrder(order);
    }

    public OrderResponse getResponseFromOrder(Order order) {
        return order == null ? null : new OrderResponse()
                .setId(order.getId())
                .setReference(order.getReference())
                .setStatus(order.getStatus())
                .setPreparedBy(order.getPreparedBy())
                .setPreparedAt(order.getPreparedAt())
                .setInvoice(order.getInvoice())
                .setProductList(getOrderProductElements(order.getProductList()));
    }

    private List<OrderProductInOrderResponseElement> getOrderProductElements(List<OrderProduct> orderProducts) {
        List<OrderProductInOrderResponseElement> orderProductInOrderResponseElements = new ArrayList<>();
        for (OrderProduct orderProduct : orderProducts) {
            OrderProductInOrderResponseElement orderProductInOrderResponseElement = new OrderProductInOrderResponseElement();
            orderProductInOrderResponseElement.setId(orderProduct.getId())
                    .setQuantity(orderProduct.getQuantity())
                    .setPreparedAt(orderProduct.getPreparedAt())
                    .setPreparedBy(orderProduct.getPreparedBy())
                    .setProduct(orderProduct.getProduct())
                    .setProductTransaction(productTransactionService.setElementFromProductTransaction(orderProduct.getProductTransaction()));
            orderProductInOrderResponseElements.add(orderProductInOrderResponseElement);
        }
        return orderProductInOrderResponseElements;
    }
}
