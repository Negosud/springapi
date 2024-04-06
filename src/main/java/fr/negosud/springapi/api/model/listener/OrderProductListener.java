package fr.negosud.springapi.api.model.listener;

import fr.negosud.springapi.api.model.dto.OrderStatus;
import fr.negosud.springapi.api.model.entity.*;
import fr.negosud.springapi.api.service.ArrivalProductService;
import fr.negosud.springapi.api.service.OrderService;
import fr.negosud.springapi.api.service.ProductService;
import fr.negosud.springapi.api.service.SupplierProductService;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Stack;

@SuppressWarnings("ALL")
@Component
public class OrderProductListener {

    private final ProductService productService;
    private final SupplierProductService supplierProductService;
    private final ArrivalProductService arrivalProductService;
    private final OrderService orderService;

    @Autowired
    public OrderProductListener(@Lazy ProductService productService, @Lazy SupplierProductService supplierProductService, @Lazy ArrivalProductService arrivalProductService, @Lazy OrderService orderService) {
        this.productService = productService;
        this.supplierProductService = supplierProductService;
        this.arrivalProductService = arrivalProductService;
        this.orderService = orderService;
    }

    @PostPersist
    public void autoMakeArrivalProducts(OrderProduct orderProduct) {
        Product product = orderProduct.getProduct();
        int diff = product.getQuantity() + productService.getIncomingProductQuantity(product) - productService.getOutgoingProductQuantity(product);
        if (diff < 0) {
            Stack<SupplierProduct> supplierProducts = supplierProductService.getAllSupplierProductsForProductOrderedByPrice(product);
            while (diff < 0) {
                SupplierProduct supplierProduct = supplierProducts.pop();
                if (supplierProduct.getQuantity() <= diff) {
                    ArrivalProduct arrivalProduct = new ArrivalProduct(supplierProduct.getQuantity(), product);
                    diff -= supplierProduct.getQuantity();
                    supplierProductService.deleteSupplierProduct(supplierProduct);
                    arrivalProductService.saveArrivalProduct(arrivalProduct);
                } else {
                    ArrivalProduct arrivalProduct = new ArrivalProduct(diff, product);
                    supplierProduct.setQuantity(supplierProduct.getQuantity() - diff);
                    supplierProductService.saveSupplierProduct(supplierProduct);
                    arrivalProductService.saveArrivalProduct(arrivalProduct);
                }
            }
        }
    }

    @PostUpdate
    public void autoSetOrderStatusProcessing(OrderProduct orderProduct) {
        Order order = orderProduct.getOrder();
        if (order.getStatus() == OrderStatus.PENDING) {
            order.setStatus(OrderStatus.PROCESSING);
            orderService.saveOrder(order);
        }
    }
}
