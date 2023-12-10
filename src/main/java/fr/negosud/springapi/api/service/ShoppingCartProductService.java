package fr.negosud.springapi.api.service;

import fr.negosud.springapi.api.model.entity.ShoppingCartProduct;
import fr.negosud.springapi.api.repository.ShoppingCartProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartProductService {

    private final ShoppingCartProductRepository shoppingCartProductRepository;

    @Autowired
    public ShoppingCartProductService(ShoppingCartProductRepository shoppingCartProductRepository) {

        this.shoppingCartProductRepository = shoppingCartProductRepository;
    }

    public List<ShoppingCartProduct> getAllShoppingCartProducts() {

        return shoppingCartProductRepository.findAll();
    }

    public Optional<ShoppingCartProduct> getShoppingCartProductById(Long shoppingCartProductId) {

        return shoppingCartProductRepository.findById(shoppingCartProductId);
    }

    public ShoppingCartProduct saveShoppingCartProduct(ShoppingCartProduct shoppingCartProduct) {

        return shoppingCartProductRepository.save(shoppingCartProduct);
    }

    public void deleteShoppingCartProduct(Long shoppingCartProductId) {

        shoppingCartProductRepository.deleteById(shoppingCartProductId);
    }
}
