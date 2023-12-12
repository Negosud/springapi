package fr.negosud.springapi.api.service;

import fr.negosud.springapi.api.model.entity.ProductTransaction;
import fr.negosud.springapi.api.repository.ProductTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductTransactionService {

    private final ProductTransactionRepository productTransactionRepository;

    @Autowired
    public ProductTransactionService(ProductTransactionRepository productTransactionRepository) {
        this.productTransactionRepository = productTransactionRepository;
    }

    public List<ProductTransaction> getAllProductTransaction() {
        return productTransactionRepository.findAll();
    }

    public Optional<ProductTransaction> getProductTransactionById(Long productTransactionId) {
        return productTransactionRepository.findById(productTransactionId);
    }

    public ProductTransaction saveProductTransaction(ProductTransaction productTransaction) {
        return productTransactionRepository.save(productTransaction);
    }

    public void deleteProductTransaction(Long productTransactionId) {
        productTransactionRepository.deleteById(productTransactionId);
    }
}

