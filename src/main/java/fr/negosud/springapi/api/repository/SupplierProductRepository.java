package fr.negosud.springapi.api.repository;

import fr.negosud.springapi.api.model.entity.SupplierProduct;
import fr.negosud.springapi.api.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupplierProductRepository extends JpaRepository<SupplierProduct, Long> {

    Optional<SupplierProduct> findBySupplierAndProductId(User supplier, long productId);
}
