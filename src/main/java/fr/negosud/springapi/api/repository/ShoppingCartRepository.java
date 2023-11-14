package fr.negosud.springapi.api.repository;

import fr.negosud.springapi.api.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

}
