package fr.negosud.springapi.api.service;

import fr.negosud.springapi.api.model.entity.ProductFamily;
import fr.negosud.springapi.api.repository.ProductFamilyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductFamilyService {

    private final ProductFamilyRepository productFamilyRepository;

    @Autowired
    public ProductFamilyService(ProductFamilyRepository productFamilyRepository) {

        this.productFamilyRepository = productFamilyRepository;
    }

    public List<ProductFamily> getAllProductFamilies() {
        return productFamilyRepository.findAll();
    }

    public Optional<ProductFamily> getProductFamilyById(long productFamilyId) {
        return productFamilyRepository.findById(productFamilyId);
    }


    public Optional<ProductFamily> getProductFamilyByCode(String code) {
        return productFamilyRepository.findByCode(code);
    }

    public ProductFamily saveProductFamily(ProductFamily productFamily) {

        return productFamilyRepository.save(productFamily);
    }

    public void deleteProductFamily(long productFamilyId) {

        productFamilyRepository.deleteById(productFamilyId);
    }
}
