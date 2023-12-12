package fr.negosud.springapi.api.repository;

import fr.negosud.springapi.api.model.entity.ProductTransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductTransactionTypeRepository extends JpaRepository <ProductTransactionType, Long> {
}
