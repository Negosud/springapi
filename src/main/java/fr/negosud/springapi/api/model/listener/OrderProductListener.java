package fr.negosud.springapi.api.model.listener;

import fr.negosud.springapi.api.model.entity.OrderProduct;
import fr.negosud.springapi.api.model.entity.Product;
import fr.negosud.springapi.api.service.ProductService;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@SuppressWarnings("ALL")
@Component
public class OrderProductListener {

    private final ProductService productService;

    @Autowired
    public OrderProductListener(@Lazy ProductService productService) {
        this.productService = productService;
    }

    @PostPersist
    public void postPersist(OrderProduct orderProduct) {
        Product product = orderProduct.getProduct();
        if (product.getQuantity() + productService.getIncomingProductQuantity(product) - productService.getOutgoingProductQuantity(product) < 0) {
            // TODO: Make the ArrivalProduct base on SupplierProducts (ordered by supplier prices)
        }
    }
}
