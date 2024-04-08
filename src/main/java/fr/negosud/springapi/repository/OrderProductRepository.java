package fr.negosud.springapi.repository;

import fr.negosud.springapi.model.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
}
