package fr.negosud.springapi.api.service;

import fr.negosud.springapi.api.model.entity.Product;
import fr.negosud.springapi.api.model.entity.ProductTransaction;
import fr.negosud.springapi.api.repository.ProductTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductTransactionService {

    private final ProductTransactionRepository productTransactionRepository;
    private final ProductTransactionTypeService productTransactionTypeService;

    @Autowired
    public ProductTransactionService(ProductTransactionRepository productTransactionRepository, ProductTransactionTypeService productTransactionTypeService) {
        this.productTransactionRepository = productTransactionRepository;
        this.productTransactionTypeService = productTransactionTypeService;
    }

    public List<ProductTransaction> getAllProductTransaction() {
        return productTransactionRepository.findAll();
    }

    public Optional<ProductTransaction> getProductTransactionById(long productTransactionId) {
        return productTransactionRepository.findById(productTransactionId);
    }

    public ProductTransaction saveProductTransaction(ProductTransaction productTransaction) {
        return productTransactionRepository.save(productTransaction);
    }

    public void deleteProductTransaction(long productTransactionId) {
        productTransactionRepository.deleteById(productTransactionId);
    }

    /**
     * @throws IllegalArgumentException Product quantity field cannot be negative
     */
    public void handleProductQuantityDefinition(Product product, int newQuantity) {
        int quantity = product.getQuantity();
        if (newQuantity < 0)
            throw new IllegalArgumentException("Product quantity field cannot be negative");
        if (quantity > newQuantity) {
            ProductTransaction productTransaction = new ProductTransaction(product, quantity-newQuantity, productTransactionTypeService.getProductTransactionTypeByCode("SORTIE_A_CLASSIFIER").orElse(null));
            productTransactionRepository.save(productTransaction);
        } else if (quantity < newQuantity) {
            ProductTransaction productTransaction = new ProductTransaction(product, newQuantity - quantity, productTransactionTypeService.getProductTransactionTypeByCode("ENTREE_A_CLASSIFIER").orElse(null));
            productTransactionRepository.save(productTransaction);
        }
        product.setQuantity(newQuantity);
    }
}

