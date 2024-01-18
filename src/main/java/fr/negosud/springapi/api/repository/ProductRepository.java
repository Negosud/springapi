package fr.negosud.springapi.api.repository;

import fr.negosud.springapi.api.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByActiveAndProductFamilyName(boolean active, String productFamilyName);
    List<Product> findAllByProductFamilyName(String productFamilyName);
    List<Product> findAllByActive(boolean active);
}
