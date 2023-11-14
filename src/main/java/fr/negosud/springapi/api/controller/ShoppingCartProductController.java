package fr.negosud.springapi.api.controller;

import fr.negosud.springapi.api.entity.ShoppingCartProduct;
import fr.negosud.springapi.api.service.ShoppingCartProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shoppingCartProduct")
public class ShoppingCartProductController {

    private final ShoppingCartProductService shoppingCartProductService;

    @Autowired
    public ShoppingCartProductController(ShoppingCartProductService shoppingCartProductService) {

        this.shoppingCartProductService = shoppingCartProductService;
    }

    @GetMapping
    public ResponseEntity<List<ShoppingCartProduct>> getAllShoppingCartProducts() {

        List<ShoppingCartProduct> shoppingCartProducts = shoppingCartProductService.getAllShoppingCartProducts();
        return new ResponseEntity<>(shoppingCartProducts, HttpStatus.OK);
    }

    @GetMapping("/{shoppingCartProductId}")
    public ResponseEntity<ShoppingCartProduct> getShoppingCartProductById(@PathVariable Long shoppingCartProductId) {

        return shoppingCartProductService.getShoppingCartProductById(shoppingCartProductId)
                .map(shoppingCartProduct -> new ResponseEntity<>(shoppingCartProduct, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<ShoppingCartProduct> createShoppingCartProduct(@RequestBody ShoppingCartProduct shoppingCartProduct) {

        ShoppingCartProduct createdShoppingCartProduct= shoppingCartProductService.saveShoppingCartProduct(shoppingCartProduct);
        return new ResponseEntity<>(createdShoppingCartProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{shoppingCartProductId}")
    public ResponseEntity<ShoppingCartProduct> updateShoppingCartProduct(@PathVariable Long shoppingCartProductId, @RequestBody ShoppingCartProduct shoppingCartProduct) {

        if (shoppingCartProductService.getShoppingCartProductById(shoppingCartProductId).isPresent()) {
            shoppingCartProduct.setShoppingCartProductId(shoppingCartProductId);
            ShoppingCartProduct updatedShoppingCartProduct = shoppingCartProductService.saveShoppingCartProduct(shoppingCartProduct);
            return new ResponseEntity<>(updatedShoppingCartProduct, HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{shoppingCartProductId}")
    public ResponseEntity<Void> deleteShoppingCartProduct(@PathVariable Long shoppingCartProductId) {

        if (shoppingCartProductService.getShoppingCartProductById(shoppingCartProductId).isPresent()) {
            shoppingCartProductService.deleteShoppingCartProduct(shoppingCartProductId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}