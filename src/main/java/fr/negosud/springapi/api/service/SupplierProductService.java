package fr.negosud.springapi.api.service;

import fr.negosud.springapi.api.model.dto.request.element.SetSupplierProductElement;
import fr.negosud.springapi.api.model.entity.Product;
import fr.negosud.springapi.api.model.entity.SupplierProduct;
import fr.negosud.springapi.api.model.entity.User;
import fr.negosud.springapi.api.repository.SupplierProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

@Service
public class SupplierProductService {

    final private SupplierProductRepository supplierProductRepository;
    final private ProductService productService;

    @Autowired
    public SupplierProductService(SupplierProductRepository supplierProductRepository, ProductService productService) {
        this.supplierProductRepository = supplierProductRepository;
        this.productService = productService;
    }

    public List<SupplierProduct> getAllSupplierProducts(Optional<User> supplier, Optional<Product> product) {
        return supplier.isPresent() ?
                product.isPresent() ?
                        supplierProductRepository.findAllBySupplierAndProduct(supplier.get(), product.get()) :
                        supplierProductRepository.findAllBySupplier(supplier.get()) :
                product.isPresent() ?
                        supplierProductRepository.findAllByProduct(product.get()) :
                        supplierProductRepository.findAll();
    }

    public Stack<SupplierProduct> getAllSupplierProductsForProductOrderedByPrice(Product product) {
        return supplierProductRepository.findAllByProductOrderByUnitPriceAsc(product);
    }

    public Optional<SupplierProduct> getSupplierProductById(long supplierProductId) {
        return supplierProductRepository.findById(supplierProductId);
    }

    public Optional<SupplierProduct> getSupplierProductBySupplierAndProductId(User supplier, long productId) {
        return supplierProductRepository.findBySupplierAndProductId(supplier, productId);
    }

    public void saveSupplierProduct(SupplierProduct supplierProduct) {
        supplierProductRepository.save(supplierProduct);
    }

    public void deleteSupplierProduct(SupplierProduct supplierProduct) {
        supplierProductRepository.delete(supplierProduct);
    }

    public List<SupplierProduct> setUsersSuppliedProductListFromRequest(User supplier, List<SetSupplierProductElement> setSupplierProductElementList) {
        List<SupplierProduct> supplierProducts = supplier.getSuppliedProductList();
        if (supplierProducts == null)
            supplierProducts = new ArrayList<>();

        for (SetSupplierProductElement setSupplierProductElement : setSupplierProductElementList) {
            SupplierProduct supplierProduct = supplier.getId() != 0 ? getSupplierProductBySupplierAndProductId(supplier, setSupplierProductElement.getProductId()).orElse(null) : null;
            supplierProducts.add(setUsersSuppliedProductFromRequest(supplier, setSupplierProductElement, supplierProduct));
        }
        return supplierProducts;
    }

    /**
     * @throws IllegalArgumentException Can't find a product with user's supplied product id
     */
    private SupplierProduct setUsersSuppliedProductFromRequest(User supplier, SetSupplierProductElement setSupplierProductElement, SupplierProduct supplierProduct) {
        if (supplierProduct == null)
            supplierProduct = new SupplierProduct();

        Product product = productService.getProductById(setSupplierProductElement.getProductId()).orElse(null);
        if (product == null)
            throw new IllegalArgumentException("Can't find a product with user's supplied product id");

        supplierProduct.setProduct(productService.getProductById(setSupplierProductElement.getProductId()).orElse(null));
        supplierProduct.setSupplier(supplier);
        supplierProduct.setQuantity(setSupplierProductElement.getQuantity());
        supplierProduct.setUnitPrice(setSupplierProductElement.getUnitPrice());

        return supplierProduct;
    }

}
