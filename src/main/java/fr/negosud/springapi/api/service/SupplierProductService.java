package fr.negosud.springapi.api.service;

import fr.negosud.springapi.api.model.dto.SetUsersSuppliedProductRequest;
import fr.negosud.springapi.api.model.entity.Product;
import fr.negosud.springapi.api.model.entity.SupplierProduct;
import fr.negosud.springapi.api.model.entity.User;
import fr.negosud.springapi.api.repository.SupplierProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
final public class SupplierProductService {

    final private SupplierProductRepository supplierProductRepository;
    final private ProductService productService;

    @Autowired
    public SupplierProductService(SupplierProductRepository supplierProductRepository, ProductService productService) {
        this.supplierProductRepository = supplierProductRepository;
        this.productService = productService;
    }

    public List<SupplierProduct> getAllSupplierProducts(Optional<User> supplier, Optional<Product> product) {
        return null;
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

    public List<SupplierProduct> setUsersSuppliedProductListFromRequest(User supplier, List<SetUsersSuppliedProductRequest> setUsersSuppliedProductRequestList) {
        List<SupplierProduct> supplierProducts = supplier.getSuppliedProductList();
        if (supplierProducts == null)
            supplierProducts = new ArrayList<>();

        for (SetUsersSuppliedProductRequest setUsersSuppliedProductRequest : setUsersSuppliedProductRequestList) {
            SupplierProduct supplierProduct = supplier.getId() != 0 ? getSupplierProductBySupplierAndProductId(supplier, setUsersSuppliedProductRequest.getProductId()).orElse(null) : null;
            supplierProducts.add(setUsersSuppliedProductFromRequest(supplier, setUsersSuppliedProductRequest, supplierProduct));
        }
        return supplierProducts;
    }

    /**
     * @throws IllegalArgumentException Can't find a product with user's supplied product id
     */
    private SupplierProduct setUsersSuppliedProductFromRequest(User supplier, SetUsersSuppliedProductRequest setUsersSuppliedProductRequest, SupplierProduct supplierProduct) {
        if (supplierProduct == null)
            supplierProduct = new SupplierProduct();

        Product product = productService.getProductById(setUsersSuppliedProductRequest.getProductId()).orElse(null);
        if (product == null)
            throw new IllegalArgumentException("Can't find a product with user's supplied product id");

        supplierProduct.setProduct(productService.getProductById(setUsersSuppliedProductRequest.getProductId()).orElse(null));
        supplierProduct.setSupplier(supplier);
        supplierProduct.setQuantity(setUsersSuppliedProductRequest.getQuantity());
        supplierProduct.setUnitPrice(setUsersSuppliedProductRequest.getUnitPrice());

        return supplierProduct;
    }

}
