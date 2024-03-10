package fr.negosud.springapi.api.repository;

import fr.negosud.springapi.api.model.entity.ProductTransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductTransactionTypeRepository extends JpaRepository <ProductTransactionType, Long> {

    Optional<ProductTransactionType> findByCode(String code);

    List<ProductTransactionType> findAllByisEntry(boolean isEntry);
}
