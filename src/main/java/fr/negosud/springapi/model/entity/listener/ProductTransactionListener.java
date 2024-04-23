package fr.negosud.springapi.model.entity.listener;

import fr.negosud.springapi.model.entity.Product;
import fr.negosud.springapi.model.entity.ProductTransaction;
import fr.negosud.springapi.service.ProductService;
import jakarta.persistence.PrePersist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import static java.lang.System.out;

@Component
public class ProductTransactionListener {

    private final ProductService productService;

    @Autowired
    public ProductTransactionListener(@Lazy ProductService productService) {
        this.productService = productService;
    }

    /**
     * @throws AssertionError Product quantity should be greater than zero
     */
    @PrePersist
    public void updateProductQuantity(ProductTransaction productTransaction) {
        out.println("PrePersist updateProductQuantity on ProductTransaction " + productTransaction.getId());
        Product product = productTransaction.getProduct();
        if (product != null && product.getId() != 0) {
            int quantity = product.getQuantity();
            quantity = productTransaction.getProdutTransactionType().isEntry() ?
                    quantity + productTransaction.getQuantity() :
                    quantity - productTransaction.getQuantity();
            assert quantity > 0 : "Product quantity should be greater than zero";
            product.setQuantity(quantity);
            productService.saveProduct(product);
        }
    }
}
