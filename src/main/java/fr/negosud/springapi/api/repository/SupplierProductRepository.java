package fr.negosud.springapi.api.repository;

import fr.negosud.springapi.api.model.entity.SupplierProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierProductRepository extends JpaRepository<SupplierProduct, Long> {

    List<SupplierProduct> findSupplierProductByIdIn(List<Long> idList);
}
