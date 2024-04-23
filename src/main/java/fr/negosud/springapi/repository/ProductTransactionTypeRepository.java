package fr.negosud.springapi.repository;

import fr.negosud.springapi.model.entity.ProductTransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductTransactionTypeRepository extends JpaRepository <ProductTransactionType, String> {

    List<ProductTransactionType> findAllByisEntry(boolean isEntry);
}
