package fr.negosud.springapi.api.repository;

import fr.negosud.springapi.api.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
