package fr.negosud.springapi.api.service;

import fr.negosud.springapi.api.model.dto.CreateProductRequest;
import fr.negosud.springapi.api.model.dto.UpdateProductRequest;
import fr.negosud.springapi.api.model.entity.Product;
import fr.negosud.springapi.api.model.entity.ProductFamily;
import fr.negosud.springapi.api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.Year;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    public Product getNewestProduct(Product product) {
        Product newProduct = product.getNewProduct();
        return newProduct == null ? product : getNewestProduct(newProduct);
    }

    public Product getOldestProduct(Product product) {
        Product oldProduct = product.getOldProduct();
        return oldProduct == null ? product : getOldestProduct(oldProduct);
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
    public Product updateProductFromRequest(UpdateProductRequest updateProductRequest, Product oldProduct) throws IllegalArgumentException {
        Product newProduct = new Product();
        boolean isUpdateNeeded = false;

        String name = updateProductRequest.getName();
        String description = updateProductRequest.getDescription();
        Year vintage = updateProductRequest.getVintage() != null ? Year.of(updateProductRequest.getVintage()) : null;
        String productFamilyCode = updateProductRequest.getProductFamilyCode();
        ProductFamily productFamily = productFamilyCode != null ? productFamilyService.getProductFamilyByCode(productFamilyCode).orElse(null) : null;
        BigDecimal unitPrice = updateProductRequest.getUnitPrice();
        Integer quantity = updateProductRequest.getQuantity();
        Boolean active = updateProductRequest.isActive();

        if (name != null && !Objects.equals(name, oldProduct.getName())) {
            newProduct.setName(name);
            isUpdateNeeded = true;
        } else {
            newProduct.setName(oldProduct.getName());
        }

        if (description != null && !Objects.equals(description, oldProduct.getDescription())) {
            newProduct.setDescription(description);
            isUpdateNeeded = true;
        } else {
            newProduct.setDescription(oldProduct.getDescription());
        }

        if (vintage != null && !Objects.equals(vintage, oldProduct.getVintage())) {
            newProduct.setVintage(Year.of(updateProductRequest.getVintage()));
            isUpdateNeeded = true;
        } else {
            newProduct.setVintage(vintage);
        }

        if (productFamily != null && !Objects.equals(productFamily, oldProduct.getProductFamily())) {
            newProduct.setProductFamily(productFamily);
            isUpdateNeeded = true;
        } else {
            newProduct.setProductFamily(oldProduct.getProductFamily());
        }

        if (unitPrice != null && !Objects.equals(unitPrice, oldProduct.getUnitPrice())) {
            newProduct.setUnitPrice(unitPrice);
            newProduct.setUnitPriceVAT(unitPrice.multiply(new BigDecimal("1.20")));
            isUpdateNeeded = true;
        } else {
            newProduct.setUnitPrice(oldProduct.getUnitPrice());
            newProduct.setUnitPriceVAT(oldProduct.getUnitPriceVAT());
        }

        if (isUpdateNeeded) {
            newProduct.setQuantity(oldProduct.getQuantity());
            if (quantity != null && !Objects.equals(quantity, oldProduct.getQuantity()))
                productTransactionService.handleProductQuantityDefinition(newProduct, quantity);

            if (active != null && !Objects.equals(active, oldProduct.isActive()))
                newProduct.setActive(active);

            oldProduct.setActive(false);
            oldProduct.setQuantity(0);

            return newProduct;
        }

        if (quantity != null && !Objects.equals(quantity, oldProduct.getQuantity())) {
            productTransactionService.handleProductQuantityDefinition(oldProduct, quantity);
            isUpdateNeeded = true;
        }

        if (active != null && active != oldProduct.isActive()) {
            oldProduct.setActive(active);
            isUpdateNeeded = true;
        }

        if (!isUpdateNeeded)
            throw new IllegalArgumentException("No fields were changed or the request body is empty.");

        return null;
    }

    public boolean initProducts() {
        Yaml yaml = new Yaml();

        try (InputStream inputStream = new ClassPathResource("products.yml").getInputStream()) {
            Map<String, List<Map<String, Object>>> productsMap = yaml.load(inputStream);
            if (productsMap != null && productsMap.containsKey("products")) {
                List<Map<String, Object>> productsInfo = productsMap.get("products");
                for (Map<String, Object> productInfo : productsInfo) {
                    Product product = new Product();
                    // Make a initProduct method, review what need to be stored in a product
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
