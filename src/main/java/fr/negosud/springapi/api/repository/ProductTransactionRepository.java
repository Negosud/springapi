package fr.negosud.springapi.api.repository;

import fr.negosud.springapi.api.model.entity.ProductTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductTransactionRepository  extends JpaRepository<ProductTransaction, Long> {
}