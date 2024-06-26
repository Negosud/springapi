package fr.negosud.springapi.service;

import fr.negosud.springapi.model.dto.request.element.SetSupplierProductRequestElement;
import fr.negosud.springapi.model.entity.Product;
import fr.negosud.springapi.model.entity.SupplierProduct;
import fr.negosud.springapi.model.entity.User;
import fr.negosud.springapi.model.entity.composite.SupplierProductKey;
import fr.negosud.springapi.repository.SupplierProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

@Service
public class SupplierProductService {

    private final SupplierProductRepository supplierProductRepository;
    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public SupplierProductService(SupplierProductRepository supplierProductRepository, @Lazy ProductService productService, @Lazy UserService userService) {
        this.supplierProductRepository = supplierProductRepository;
        this.productService = productService;
        this.userService = userService;
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

    public List<SupplierProduct> getAllSupplierProducts(Long supplierId, Long productId) {
        Optional<User> supplier = Optional.empty();
        Optional<Product> product = Optional.empty();
        if (supplierId != null) {
            supplier = userService.getUserById(supplierId);
            assert supplier.isPresent() : "Supplier not found";
        }
        if (productId != null) {
            product = productService.getProductById(productId);
            assert product.isPresent() : "Product not found";
        }
        return getAllSupplierProducts(supplier, product);
    }

    public Stack<SupplierProduct> getAllSupplierProductsForProductOrderedByPrice(Product product) {
        return supplierProductRepository.findAllByProductOrderByUnitPriceAsc(product);
    }

    public Optional<SupplierProduct> getSupplierProductById(SupplierProductKey supplierProductKey) {
        return supplierProductRepository.findById(supplierProductKey);
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

    public List<SupplierProduct> setSupplierProductsFromRequest(User supplier, List<SetSupplierProductRequestElement> setSupplierProductElementList) {
        List<SupplierProduct> supplierProducts = supplier.getSuppliedProducts();
        if (supplierProducts == null)
            supplierProducts = new ArrayList<>();

        if (setSupplierProductElementList != null && !setSupplierProductElementList.isEmpty()) {
            for (SetSupplierProductRequestElement setSupplierProductElement : setSupplierProductElementList) {
                SupplierProduct supplierProduct = supplier.getId() != 0 ? getSupplierProductBySupplierAndProductId(supplier, setSupplierProductElement.getProductId()).orElse(null) : null;
                supplierProducts.add(setSupplierProductFromRequestElement(supplier, setSupplierProductElement, supplierProduct));
            }
        }
        return supplierProducts;
    }

    /**
     * @throws IllegalArgumentException Can't find a product with user's supplied product id
     */
    private SupplierProduct setSupplierProductFromRequestElement(User supplier, SetSupplierProductRequestElement setSupplierProductElement, SupplierProduct supplierProduct) {
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
