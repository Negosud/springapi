package fr.negosud.springapi.api.repository;

import fr.negosud.springapi.api.entity.ShoppingCartProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartProductRepository extends JpaRepository<ShoppingCartProduct, Long> {

}
