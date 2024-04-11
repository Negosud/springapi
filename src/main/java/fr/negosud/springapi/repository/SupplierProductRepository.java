package fr.negosud.springapi.repository;

import fr.negosud.springapi.model.entity.Product;
import fr.negosud.springapi.model.entity.SupplierProduct;
import fr.negosud.springapi.model.entity.User;
import fr.negosud.springapi.model.entity.composite.SupplierProductKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Stack;

public interface SupplierProductRepository extends JpaRepository<SupplierProduct, SupplierProductKey> {

    List<SupplierProduct> findAllBySupplierAndProduct(User supplier, Product product);
    List<SupplierProduct> findAllBySupplier(User supplier);
    List<SupplierProduct> findAllByProduct(Product product);
    Stack<SupplierProduct> findAllByProductOrderByUnitPriceAsc(Product product);

    Optional<SupplierProduct> findBySupplierAndProductId(User supplier, long productId);
}
