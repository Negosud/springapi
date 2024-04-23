package fr.negosud.springapi.model.entity.listener;

import fr.negosud.springapi.model.entity.Product;
import fr.negosud.springapi.model.entity.ProductTransaction;
import fr.negosud.springapi.model.entity.ProductTransactionType;
import fr.negosud.springapi.service.ProductService;
import fr.negosud.springapi.service.ProductTransactionService;
import fr.negosud.springapi.service.ProductTransactionTypeService;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import static java.lang.System.out;

@Component
public class ProductListener {

    private final ProductTransactionTypeService productTransactionTypeService;
    private final ProductService productService;
    private final ProductTransactionService productTransactionService;

    @Autowired
    public ProductListener(@Lazy ProductTransactionTypeService productTransactionTypeService, @Lazy ProductService productService, @Lazy ProductTransactionService productTransactionService) {
        this.productTransactionTypeService = productTransactionTypeService;
        this.productService = productService;
        this.productTransactionService = productTransactionService;
    }

    @PostPersist
    public void updateOldProductAndMakeNewProductTransaction(Product product) {
        Product oldProduct = product.getOldProduct();
        if (oldProduct != null && oldProduct.isActive()) {
            int baseQuantity = oldProduct.getQuantity();
            oldProduct.setActive(false);
            productService.saveProduct(oldProduct);
            if (baseQuantity > 0) {
                ProductTransactionType productTransactionType = productTransactionTypeService.getProductTransactionTypeByCode("TRANSFERT_ANCIEN_PRODUIT")
                        .orElseThrow(() -> new RuntimeException("ProductTransactionType TRANSFERT_ANCIEN_PRODUIT not found"));
                ProductTransaction newProductTransaction = new ProductTransaction(product, baseQuantity, productTransactionType);
                product.addProductTransaction(newProductTransaction);
                productTransactionService.saveProductTransaction(newProductTransaction);
            }
        }
    }

    @PostUpdate
    public void makeResetTransaction(Product product) {
        out.println("PostUpdate makeResetTransaction on Product " + product.getId());
        Product newProduct = product.getNewProduct();
        int resetQuantity = product.getQuantity();
        // Make Reset Product Transaction on oldProduct if needed
        if (newProduct != null && !product.isActive() && resetQuantity > 0) {
            out.println("Product " + newProduct.getId() + " needs to be reset");
            ProductTransaction resetProductTransaction = new ProductTransaction(product, resetQuantity, productTransactionTypeService.getProductTransactionTypeByCode("TRANSFERT_NOUVEAU_PRODUIT")
                    .orElseThrow(() -> new RuntimeException("ProductTransactionType TRANSFERT_NOUVEAU_PRODUIT not found")));
            product.addProductTransaction(resetProductTransaction);
            productService.saveProduct(product);
        }
    }
}
