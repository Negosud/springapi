package fr.negosud.springapi.api.repository;

import fr.negosud.springapi.api.model.entity.Product;
import fr.negosud.springapi.api.model.entity.SupplierProduct;
import fr.negosud.springapi.api.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Stack;

public interface SupplierProductRepository extends JpaRepository<SupplierProduct, Long> {

    List<SupplierProduct> findAllBySupplierAndProduct(User supplier, Product product);
    List<SupplierProduct> findAllBySupplier(User supplier);
    List<SupplierProduct> findAllByProduct(Product product);
    Stack<SupplierProduct> findAllByProductOrderByUnitPriceAsc(Product product);

    Optional<SupplierProduct> findBySupplierAndProductId(User supplier, long productId);
}
