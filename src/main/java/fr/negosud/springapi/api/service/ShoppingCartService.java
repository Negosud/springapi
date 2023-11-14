package fr.negosud.springapi.api.service;

import fr.negosud.springapi.api.entity.ShoppingCart;
import fr.negosud.springapi.api.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;

    @Autowired
    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository) {

        this.shoppingCartRepository = shoppingCartRepository;
    }

    public List<ShoppingCart> getAllShoppingCarts() {

        return shoppingCartRepository.findAll();
    }

    public Optional<ShoppingCart> getShoppingCartById(Long shoppingCartId) {

        return shoppingCartRepository.findById(shoppingCartId);
    }

    public ShoppingCart saveShoppingCart(ShoppingCart shoppingCart) {

        return shoppingCartRepository.save(shoppingCart);
    }

    public void deleteShoppingCart(Long shoppingCartId) {

        shoppingCartRepository.deleteById(shoppingCartId);
    }
}
