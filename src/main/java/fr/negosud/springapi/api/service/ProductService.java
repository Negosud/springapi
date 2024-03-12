package fr.negosud.springapi.api.service;

import fr.negosud.springapi.api.model.dto.CreateProductRequest;
import fr.negosud.springapi.api.model.dto.UpdateProductRequest;
import fr.negosud.springapi.api.model.entity.Product;
import fr.negosud.springapi.api.model.entity.ProductFamily;
import fr.negosud.springapi.api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Year;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductFamilyService productFamilyService;
    private final ProductTransactionService productTransactionService;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductFamilyService productFamilyService, ProductTransactionService productTransactionService) {
        this.productRepository = productRepository;
        this.productFamilyService = productFamilyService;
        this.productTransactionService = productTransactionService;
    }

    public List<Product> getAllProducts(Optional<Boolean> active, String productFamilyName) {
        return (productFamilyName == null) ?
                (active.isEmpty() ? productRepository.findAll() : productRepository.findAllByActive(active.get())) :
                (active.isEmpty() ? productRepository.findAllByProductFamilyName(productFamilyName) : productRepository.findAllByActiveAndProductFamilyName(active.get(), productFamilyName));
    }

    public Optional<Product> getProductById(long productId) {
        return productRepository.findById(productId);
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    public void deleteProduct(long productId) {
        productRepository.deleteById(productId);
    }

    /**
     * @throws IllegalArgumentException Product quantity can't be negative
     */
    public Product createProductFromRequest(CreateProductRequest createProductRequest) {
        Product product = new Product();

        product.setName(createProductRequest.getName());
        product.setDescription(createProductRequest.getDescription());
        product.setVintage(Year.of(createProductRequest.getVintage()));
        product.setProductFamily(productFamilyService.getProductFamilyByCode(createProductRequest.getProductFamilyCode()).orElse(null));
        product.setUnitPrice(createProductRequest.getUnitPrice());
        product.setUnitPriceVAT(createProductRequest.getUnitPrice().multiply(new BigDecimal("1.20")));
        product.setActive(createProductRequest.isActive());

        productTransactionService.handleProductQuantityDefinition(product, createProductRequest.getQuantity());

        return product;
    }

    /**
     * @throws IllegalArgumentException Product quantity can't be negative
     */
    public Product updateProductFromRequest(UpdateProductRequest updateProductRequest, Product oldProduct) {
        Product newProduct = new Product();

        String name = updateProductRequest.getName();
        newProduct.setName(name != null ? name : oldProduct.getName());

        String description = updateProductRequest.getDescription();
        newProduct.setDescription(description != null ? description : oldProduct.getDescription());

        Integer vintage = updateProductRequest.getVintage();
        newProduct.setVintage(vintage != null ? Year.of(updateProductRequest.getVintage()) : oldProduct.getVintage());

        String productFamilyCode = updateProductRequest.getProductFamilyCode();
        ProductFamily productFamily = productFamilyCode != null ? productFamilyService.getProductFamilyByCode(productFamilyCode).orElse(null) : null;
        newProduct.setProductFamily(productFamily != null ? productFamily : oldProduct.getProductFamily());

        BigDecimal unitPrice = updateProductRequest.getUnitPrice();
        if (unitPrice != null) {
            newProduct.setUnitPrice(unitPrice);
            newProduct.setUnitPriceVAT(unitPrice.multiply(new BigDecimal("1.20")));
        } else {
            newProduct.setUnitPrice(oldProduct.getUnitPrice());
            newProduct.setUnitPriceVAT(oldProduct.getUnitPriceVAT());
        }

        Integer quantity = updateProductRequest.getQuantity();
        newProduct.setQuantity(oldProduct.getQuantity());
        if (quantity != null)
            productTransactionService.handleProductQuantityDefinition(newProduct, updateProductRequest.getQuantity());

        return newProduct;
    }
}
