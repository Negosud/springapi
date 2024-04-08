package fr.negosud.springapi.repository;

import fr.negosud.springapi.model.entity.ProductTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductTransactionRepository  extends JpaRepository<ProductTransaction, Long> {
}