package fr.negosud.springapi.service;

import fr.negosud.springapi.model.dto.response.element.ProductTransactionInProductResponseElement;
import fr.negosud.springapi.model.entity.ArrivalProduct;
import fr.negosud.springapi.model.entity.Product;
import fr.negosud.springapi.model.entity.ProductTransaction;
import fr.negosud.springapi.repository.ProductTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductTransactionService {

    private final ProductTransactionRepository productTransactionRepository;
    private final ProductTransactionTypeService productTransactionTypeService;
    private final ProductService productService;

    @Autowired
    public ProductTransactionService(ProductTransactionRepository productTransactionRepository, ProductTransactionTypeService productTransactionTypeService, ProductService productService) {
        this.productTransactionRepository = productTransactionRepository;
        this.productTransactionTypeService = productTransactionTypeService;
        this.productService = productService;
    }

    public List<ProductTransaction> getAllProductTransaction() {
        return productTransactionRepository.findAll();
    }

    public Optional<ProductTransaction> getProductTransactionById(long productTransactionId) {
        return productTransactionRepository.findById(productTransactionId);
    }

    public void saveProductTransaction(ProductTransaction productTransaction) {
        productTransactionRepository.save(productTransaction);
    }

    public void deleteProductTransaction(long productTransactionId) {
        productTransactionRepository.deleteById(productTransactionId);
    }

    /**
     * @throws IllegalArgumentException Product quantity field cannot be negative
     */
    public ProductTransaction handleProductQuantityDefinition(Product product, int newQuantity) {
        int quantity = product.getQuantity();
        ProductTransaction productTransaction = null;
        if (newQuantity < 0)
            throw new IllegalArgumentException("Product quantity field cannot be negative");
        if (quantity > newQuantity) {
            productTransaction = new ProductTransaction(product, quantity - newQuantity, productTransactionTypeService.getProductTransactionTypeByCode("SORTIE_A_CLASSIFIER").orElse(null));
        } else if (quantity < newQuantity) {
            productTransaction = new ProductTransaction(product, newQuantity - quantity, productTransactionTypeService.getProductTransactionTypeByCode("ENTREE_A_CLASSIFIER").orElse(null));
        }
        product.setQuantity(newQuantity);
        return productTransaction;
    }

    /**
     * @throws RuntimeException ProductTransaction ACHAT_FOURNISSEUR not found
     */
    public void makeProductTransactionFromArrivalProduct(ArrivalProduct arrivalProduct) {
        Product product = productService.getNewestProduct(arrivalProduct.getProduct());
        ProductTransaction productTransaction = new ProductTransaction(product, arrivalProduct.getQuantity(), productTransactionTypeService.getProductTransactionTypeByCode("ACHAT_FOURNISSEUR")
                .orElseThrow(() -> new RuntimeException("ProductTransactionType ACHAT_FOURNISSEUR not found")));

    }

    public ProductTransactionInProductResponseElement setElementFromProductTransaction(ProductTransaction productTransaction) {
        return (ProductTransactionInProductResponseElement) new ProductTransactionInProductResponseElement()
                .setId(productTransaction.getId())
                .setQuantity(productTransaction.getQuantity())
                .setCreatedAt(productTransaction.getCreatedAt())
                .setCreatedBy(productTransaction.getCreatedBy())
                .setModifiedAt(productTransaction.getModifiedAt())
                .setModifiedBy(productTransaction.getModifiedBy());
    }
}

