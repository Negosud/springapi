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

import static java.lang.System.out;

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
    public void postPersist(Product product) {
        out.println("PostPersist " + product + " ID : " + product.getId());
        updateOldProductAndMakeNewProductTransaction(product);
        makeFirstStockTransaction(product);
    }

    private void updateOldProductAndMakeNewProductTransaction(Product product) {
        Product oldProduct = product.getOldProduct();
        if (oldProduct != null) {
            int baseQuantity = oldProduct.getQuantity();
            oldProduct.setActive(false);
            productService.saveProduct(oldProduct);
            out.println("Recherche ProductTransactionType");
            ProductTransactionType productTransactionType = productTransactionTypeService.getProductTransactionTypeByCode("TRANSFERT_ANCIEN_PRODUIT")
                    .orElse(null);
            out.println("FIN Recherche ProductTransactionType");
            if (productTransactionType == null) {
                out.println("ERREUR Recherche ProductTransactionType");
                throw new RuntimeException("ProductTransactionType TRANSFERT_ANCIEN_PRODUIT not found");
            }
            out.println(productTransactionType + " " + productTransactionType.getId());
            ProductTransaction newProductTransaction = new ProductTransaction(product, baseQuantity, productTransactionType);
            List<ProductTransaction> productTransactions = new ArrayList<>();
            productTransactions.add(newProductTransaction);
            product.setProductTransactions(productTransactions);
        }
    }

    private void makeFirstStockTransaction(Product product) {
        int quantity = product.getQuantity();
        if (quantity > 0) {
            product.setQuantity(0);
            out.println("Recherche ProductTransactionType");
            ProductTransactionType productTransactionType = productTransactionTypeService.getProductTransactionTypeByCode("TRANSFERT_ANCIEN_PRODUIT")
                    .orElse(null);
            out.println("FIN Recherche ProductTransactionType");
            if (productTransactionType == null) {
                out.println("ERREUR Recherche ProductTransactionType");
                throw new RuntimeException("ProductTransactionType TRANSFERT_ANCIEN_PRODUIT not found");
            }
            out.println(productTransactionType + " " + productTransactionType.getId());
            ProductTransaction existingStockTransaction = new ProductTransaction(product, quantity, productTransactionType);
            List<ProductTransaction> productTransactions = new ArrayList<>();
            productTransactions.add(existingStockTransaction);
            product.setProductTransactions(productTransactions);
            productService.saveProduct(product);
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
