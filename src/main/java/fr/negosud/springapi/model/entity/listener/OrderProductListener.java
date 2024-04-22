package fr.negosud.springapi.model.entity.listener;

import fr.negosud.springapi.model.ArrivalStatus;
import fr.negosud.springapi.model.OrderStatus;
import fr.negosud.springapi.model.entity.*;
import fr.negosud.springapi.service.ArrivalService;
import fr.negosud.springapi.service.OrderService;
import fr.negosud.springapi.service.ProductService;
import fr.negosud.springapi.service.SupplierProductService;
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
    private final ArrivalService arrivalService;
    private final OrderService orderService;

    @Autowired
    public OrderProductListener(@Lazy ProductService productService, @Lazy SupplierProductService supplierProductService, @Lazy ArrivalService arrivalService, @Lazy OrderService orderService) {
        this.productService = productService;
        this.supplierProductService = supplierProductService;
        this.arrivalService = arrivalService;
        this.orderService = orderService;
    }

    @PostPersist
    public void autoMakeArrivals(OrderProduct orderProduct) {
        Product product = orderProduct.getProduct();
        int diff = product.getQuantity() + productService.getIncomingProductQuantity(product) - productService.getOutgoingProductQuantity(product);
        if (diff < 0) {
            Stack<SupplierProduct> supplierProducts = supplierProductService.getAllSupplierProductsForProductOrderedByPrice(product);
            while (diff < 0) {
                SupplierProduct supplierProduct = supplierProducts.pop();
                Arrival arrival = new Arrival();
                arrival.setStatus(ArrivalStatus.ORDERING);
                arrival.setSuppliedBy(supplierProduct.getSupplier());
                if (supplierProduct.getQuantity() <= diff) {
                    ArrivalProduct arrivalProduct = new ArrivalProduct(supplierProduct.getQuantity(), product);
                    arrival.addArrivalProduct(arrivalProduct);
                    arrival.setComment(arrivalProduct.getQuantity() + " " + product.getName() + " doivent être commandés auprès du fournisseur.\nIls sont nécessaires pour préparer la commande " + orderProduct.getOrder().getReference() + ".");
                    diff -= supplierProduct.getQuantity();
                    supplierProductService.deleteSupplierProduct(supplierProduct);
                } else {
                    ArrivalProduct arrivalProduct = new ArrivalProduct(diff, product);
                    arrival.addArrivalProduct(arrivalProduct);
                    arrival.setComment(arrivalProduct.getQuantity() + " " + product.getName() + " doivent être commandés auprès du fournisseur.\nIls sont nécessaires pour préparer la commande " + orderProduct.getOrder().getReference() + ".");
                    supplierProduct.setQuantity(supplierProduct.getQuantity() - diff);
                    supplierProductService.saveSupplierProduct(supplierProduct);
                }
                arrivalService.saveArrival(arrival);
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
