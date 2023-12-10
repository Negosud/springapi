package fr.negosud.springapi.api.controller;

import fr.negosud.springapi.api.model.entity.ShoppingCart;
import fr.negosud.springapi.api.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Tag(name = "ShoppingCart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService) {

        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping
    public ResponseEntity<List<ShoppingCart>> getAllShoppingCarts() {

        List<ShoppingCart> shoppingCarts = shoppingCartService.getAllShoppingCarts();
        return new ResponseEntity<>(shoppingCarts, HttpStatus.OK);
    }

    @GetMapping("/{shoppingCartId}")
    public ResponseEntity<ShoppingCart> getShoppingCartById(@PathVariable Long shoppingCartId) {

        return shoppingCartService.getShoppingCartById(shoppingCartId)
                .map(shoppingCart -> new ResponseEntity<>(shoppingCart, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<ShoppingCart> createShoppingCart(@RequestBody ShoppingCart shoppingCart) {

        ShoppingCart createdShoppingCart= shoppingCartService.saveShoppingCart(shoppingCart);
        return new ResponseEntity<>(createdShoppingCart, HttpStatus.CREATED);
    }

    @PutMapping("/{shoppingCartId}")
    public ResponseEntity<ShoppingCart> updateShoppingCart(@PathVariable Long shoppingCartId, @RequestBody ShoppingCart shoppingCart) {

        if (shoppingCartService.getShoppingCartById(shoppingCartId).isPresent()) {
            shoppingCart.setShoppingCartId(shoppingCartId);
            ShoppingCart updatedShoppingCart = shoppingCartService.saveShoppingCart(shoppingCart);
            return new ResponseEntity<>(updatedShoppingCart, HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{shoppingCartId}")
    public ResponseEntity<Void> deleteShoppingCart(@PathVariable Long shoppingCartId) {

        if (shoppingCartService.getShoppingCartById(shoppingCartId).isPresent()) {
            shoppingCartService.deleteShoppingCart(shoppingCartId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}