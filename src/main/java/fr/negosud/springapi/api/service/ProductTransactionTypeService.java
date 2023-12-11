package fr.negosud.springapi.api.service;

import fr.negosud.springapi.api.model.entity.ProductTransactionType;
import fr.negosud.springapi.api.repository.ProductTransactionTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class ProductTransactionTypeService {

    private final ProductTransactionTypeRepository productTransactionTypeRepository;

    @Autowired
    public ProductTransactionTypeService(ProductTransactionTypeRepository productTransactionTypeRepository) {
        this.productTransactionTypeRepository = productTransactionTypeRepository;
    }

    public List<ProductTransactionType> getAllProductTransactionType() {
        return productTransactionTypeRepository.findAll();
    }

    public Optional<ProductTransactionType> getProductTransactionTypeById(Long productTransactionTypeId) {
        return productTransactionTypeRepository.findById(productTransactionTypeId);
    }

    public ProductTransactionType saveProductTransactionType(ProductTransactionType productTransactionType) {
        return productTransactionTypeRepository.save(productTransactionType);
    }

    public void deleteProductTransactionType(Long productTransactionTypeId) {
        productTransactionTypeRepository.deleteById(productTransactionTypeId);
    }
}


