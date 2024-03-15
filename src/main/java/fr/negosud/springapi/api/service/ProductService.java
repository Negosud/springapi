package fr.negosud.springapi.api.service;

import fr.negosud.springapi.api.model.dto.CreateProductRequest;
import fr.negosud.springapi.api.model.dto.UpdateProductRequest;
import fr.negosud.springapi.api.model.entity.Product;
import fr.negosud.springapi.api.model.entity.ProductFamily;
import fr.negosud.springapi.api.model.entity.ProductTransaction;
import fr.negosud.springapi.api.repository.ProductRepository;
import fr.negosud.springapi.api.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.Year;
import java.util.*;

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

    private List<Product> getAllProductsByNameAndVintage(String name, Year vintage) {
        return productRepository.findAllByNameAndVintage(name, vintage);
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
            product.setExpirationDate(createProductRequest.getExpirationDate());
            Integer vintage = createProductRequest.getVintage();
            product.setVintage(vintage != null ? Year.of(vintage) : null);
            product.setProductFamily(productFamilyService.getProductFamilyByCode(createProductRequest.getProductFamilyCode()).orElse(null));
            product.setUnitPrice(createProductRequest.getUnitPrice());
            product.setUnitPriceVAT(createProductRequest.getUnitPrice().multiply(new BigDecimal("1.20")));
            product.setActive(createProductRequest.isActive());

            product.setQuantity(0);
            ProductTransaction productTransaction = productTransactionService.handleProductQuantityDefinition(product, createProductRequest.getQuantity());

            saveProduct(product);
            if (productTransaction != null)
                productTransactionService.saveProductTransaction(productTransaction);

            return product;
    }

    /**
     * @throws IllegalArgumentException Product quantity can't be negative
     * @throws IllegalArgumentException Product can't be updated with empty body
     */
    public Product updateProductFromRequest(UpdateProductRequest updateProductRequest, Product oldProduct) throws IllegalArgumentException {
        Product newProduct = new Product();
        boolean isNewProductNeeded = false;

        String name = updateProductRequest.getName();
        String description = updateProductRequest.getDescription();
        Integer quantity = updateProductRequest.getQuantity();
        Date expirationDate = updateProductRequest.getExpirationDate();
        Year vintage = updateProductRequest.getVintage() != null ? Year.of(updateProductRequest.getVintage()) : null;
        String productFamilyCode = updateProductRequest.getProductFamilyCode();
        ProductFamily productFamily = productFamilyCode != null ? productFamilyService.getProductFamilyByCode(productFamilyCode).orElse(null) : null;
        BigDecimal unitPrice = updateProductRequest.getUnitPrice();
        Boolean active = updateProductRequest.isActive();

        if (name == null && description == null && quantity == null && expirationDate == null && vintage == null && productFamilyCode == null && unitPrice == null && active == null)
            throw new IllegalArgumentException("Product can't be updated with empty body");

        if (name != null && !Objects.equals(name, oldProduct.getName())) {
            newProduct.setName(name);
            isNewProductNeeded = true;
        } else {
            newProduct.setName(oldProduct.getName());
        }

        if (description != null && !Objects.equals(description, oldProduct.getDescription()))
            newProduct.setDescription(description);
        else
            newProduct.setDescription(oldProduct.getDescription());

        if (expirationDate != null && !Objects.equals(expirationDate, oldProduct.getExpirationDate()))
            newProduct.setExpirationDate(expirationDate);
        else
            newProduct.setExpirationDate(oldProduct.getExpirationDate());


        if (vintage != null && !Objects.equals(vintage, oldProduct.getVintage())) {
            newProduct.setVintage(Year.of(updateProductRequest.getVintage()));
            isNewProductNeeded = true;
        } else {
            newProduct.setVintage(vintage);
        }

        if (productFamily != null && !Objects.equals(productFamily, oldProduct.getProductFamily())) {
            newProduct.setProductFamily(productFamily);
            isNewProductNeeded = true;
        } else {
            newProduct.setProductFamily(oldProduct.getProductFamily());
        }

        if (unitPrice != null && !Objects.equals(unitPrice, oldProduct.getUnitPrice())) {
            newProduct.setUnitPrice(unitPrice);
            newProduct.setUnitPriceVAT(unitPrice.multiply(new BigDecimal("1.20")));
            isNewProductNeeded = true;
        } else {
            newProduct.setUnitPrice(oldProduct.getUnitPrice());
            newProduct.setUnitPriceVAT(oldProduct.getUnitPriceVAT());
        }

        newProduct.setQuantity(oldProduct.getQuantity());
        ProductTransaction productTransaction = null;
        if (quantity != null && !Objects.equals(quantity, oldProduct.getQuantity()))
            productTransaction = productTransactionService.handleProductQuantityDefinition(newProduct, quantity);

        if (active != null && !Objects.equals(active, oldProduct.isActive()))
            newProduct.setActive(active);
        else
            newProduct.setActive(oldProduct.isActive());

        if (isNewProductNeeded) {
            oldProduct.setActive(false);
            oldProduct.setQuantity(0);
            saveProduct(oldProduct);
            newProduct.setOldProduct(oldProduct);
            saveProduct(newProduct);
        } else {
            newProduct.setId(oldProduct.getId());
        }

        saveProduct(newProduct);
        if (productTransaction != null)
            productTransactionService.saveProductTransaction(productTransaction);

        return newProduct;
    }

    public boolean initProducts() {
        Yaml yaml = new Yaml();

        try (InputStream inputStream = new ClassPathResource("products.yml").getInputStream()) {
            Map<String, List<Map<String, Object>>> productsMap = yaml.load(inputStream);
            if (productsMap != null && productsMap.containsKey("products")) {
                List<Map<String, Object>> productsInfo = productsMap.get("products");
                for (Map<String, Object> productInfo : productsInfo) {
                    initProduct(productInfo);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void initProduct(Map<String, Object> productInfo) {
        String name = (String) productInfo.get("name");
        Year vintage = productInfo.get("vintage") != null ? Year.of((int) productInfo.get("vintage")) : null;

        if (getAllProductsByNameAndVintage(name, vintage).isEmpty()) {
            String description = (String) productInfo.get("description");
            int quantity = (int) productInfo.get("quantity");
            Date expirationDate = (Date) productInfo.get("expirationDate");
            ProductFamily productFamily = productFamilyService.getProductFamilyByCode(Strings.getCodeFromName((String) productInfo.get("productFamily"))).orElse(null);
            BigDecimal unitPrice = BigDecimal.valueOf((double) productInfo.get("unitPrice"));
            boolean active = productInfo.containsKey("active");
            saveProduct(new Product(name, description, quantity, expirationDate, vintage, productFamily, unitPrice, active));
        }
    }
}
