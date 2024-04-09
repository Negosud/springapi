package fr.negosud.springapi.model.entity.listener;

import fr.negosud.springapi.model.entity.Product;
import fr.negosud.springapi.model.entity.ProductTransaction;
import fr.negosud.springapi.service.ProductTransactionService;
import fr.negosud.springapi.service.ProductTransactionTypeService;
import jakarta.persistence.PrePersist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class ProductListener {

    private final ProductTransactionService productTransactionService;
    private final ProductTransactionTypeService productTransactionTypeService;

    @Autowired
    public ProductListener(@Lazy ProductTransactionService productTransactionService, ProductTransactionTypeService productTransactionTypeService) {
        this.productTransactionService = productTransactionService;
        this.productTransactionTypeService = productTransactionTypeService;
    }

    @PrePersist
    public void updateOldProductAndMakeTransactions(Product product) {
        Product oldProduct = product.getOldProduct();
        if (oldProduct != null) {
            int baseQuantity = oldProduct.getQuantity();
            ProductTransaction oldProductReset = new ProductTransaction(oldProduct, baseQuantity, productTransactionTypeService.getProductTransactionTypeByCode("TRANSFERT_NOUVEAU_PRODUIT")
                    .orElseThrow(() -> new RuntimeException("ProductTransactionType TRANSFERT_NOUVEAU_PRODUIT not found")));
            productTransactionService.saveProductTransaction(oldProductReset);
            ProductTransaction newProductDefinition = new ProductTransaction(product, baseQuantity, productTransactionTypeService.getProductTransactionTypeByCode("TRANSFERT_ANCIEN_PRODUIT")
                    .orElseThrow(() -> new RuntimeException("ProductTransactionType TRANSFERT_ANCIEN_PRODUIT not found")));
        }
    }
}
