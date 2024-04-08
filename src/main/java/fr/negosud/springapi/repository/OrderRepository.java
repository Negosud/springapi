package fr.negosud.springapi.repository;

import fr.negosud.springapi.model.OrderStatus;
import fr.negosud.springapi.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByStatus(OrderStatus orderStatus);

    Optional<Order> findByReference(String reference);

}
