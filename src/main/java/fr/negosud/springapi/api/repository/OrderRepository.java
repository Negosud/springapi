package fr.negosud.springapi.api.repository;

import fr.negosud.springapi.api.model.dto.OrderStatus;
import fr.negosud.springapi.api.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByStatus(OrderStatus orderStatus);

    Optional<Order> findByReference(String reference);

}
