package fr.negosud.springapi.api.repository;

import fr.negosud.springapi.api.model.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
}
