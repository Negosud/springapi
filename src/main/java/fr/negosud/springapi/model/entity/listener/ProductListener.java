package fr.negosud.springapi.model.entity.listener;

import fr.negosud.springapi.model.entity.Product;
import fr.negosud.springapi.model.entity.ProductTransaction;
import fr.negosud.springapi.model.entity.ProductTransactionType;
import fr.negosud.springapi.service.ProductService;
import fr.negosud.springapi.service.ProductTransactionTypeService;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductListener {

    private final ProductTransactionTypeService productTransactionTypeService;
    private final ProductService productService;

    @Autowired
    public ProductListener(@Lazy ProductTransactionTypeService productTransactionTypeService, @Lazy ProductService productService) {
        this.productTransactionTypeService = productTransactionTypeService;
        this.productService = productService;
    }

    @PostPersist
    public void updateOldProductAndMakeNewProductTransaction(Product product) {
        Product oldProduct = product.getOldProduct();
        if (oldProduct != null) {
            int baseQuantity = oldProduct.getQuantity();
            oldProduct.setActive(false);
            productService.saveProduct(oldProduct);
            ProductTransactionType productTransactionType = productTransactionTypeService.getProductTransactionTypeByCode("TRANSFERT_ANCIEN_PRODUIT")
                    .orElseThrow(() -> new RuntimeException("ProductTransactionType TRANSFERT_ANCIEN_PRODUIT not found"));
            ProductTransaction newProductTransaction = new ProductTransaction(product, baseQuantity, productTransactionType);
            List<ProductTransaction> productTransactions = new ArrayList<>();
            productTransactions.add(newProductTransaction);
            product.setProductTransactions(productTransactions);
        }
    }

    @PostUpdate
    private void makeResetTransaction(Product product) {
        Product newProduct = product.getNewProduct();
        int resetQuantity = product.getQuantity();
        // Make Reset Product Transaction on oldProduct if needed
        if (newProduct != null && !product.isActive() && resetQuantity > 0) {
            ProductTransaction resetProductTransaction = new ProductTransaction(product, resetQuantity, productTransactionTypeService.getProductTransactionTypeByCode("TRANSFERT_NOUVEAU_PRODUIT")
                    .orElseThrow(() -> new RuntimeException("ProductTransactionType TRANSFERT_NOUVEAU_PRODUIT not found")));
            List<ProductTransaction> productTransactions = product.getProductTransactions();
            productTransactions.add(resetProductTransaction);
            product.setProductTransactions(productTransactions);
            productService.saveProduct(product);
        }
    }
}
