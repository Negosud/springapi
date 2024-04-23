package fr.negosud.springapi.service;

import fr.negosud.springapi.model.dto.request.CreateProductRequest;
import fr.negosud.springapi.model.dto.request.UpdateProductRequest;
import fr.negosud.springapi.model.dto.response.ProductResponse;
import fr.negosud.springapi.model.dto.response.element.ArrivalProductInProductResponseElement;
import fr.negosud.springapi.model.dto.response.element.OrderProductInProductResponseElement;
import fr.negosud.springapi.model.dto.response.element.SupplierProductInProductResponseElement;
import fr.negosud.springapi.model.entity.*;
import fr.negosud.springapi.repository.ProductRepository;
import fr.negosud.springapi.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
    private final ProductTransactionTypeService productTransactionTypeService;

    @Autowired
    public ProductService(@Lazy ProductRepository productRepository, @Lazy ProductFamilyService productFamilyService, @Lazy ProductTransactionService productTransactionService, @Lazy ProductTransactionTypeService productTransactionTypeService) {
        this.productRepository = productRepository;
        this.productFamilyService = productFamilyService;
        this.productTransactionService = productTransactionService;
        this.productTransactionTypeService = productTransactionTypeService;
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
        if (product.getId() == 0) {
            int quantity = product.getQuantity();
            product.setQuantity(0);
            productRepository.save(product);
            makeFirstStockTransaction(product, quantity);
        } else {
            productRepository.save(product);
        }
    }

    private void makeFirstStockTransaction(Product product, int quantity) {
        if (quantity > 0 && product.getOldProduct() == null) {
            ProductTransactionType productTransactionType = productTransactionTypeService.getProductTransactionTypeByCode("TRANSFERT_ANCIEN_PRODUIT")
                    .orElseThrow(() -> new RuntimeException("ProductTransactionType TRANSFERT_ANCIEN_PRODUIT not found"));
            ProductTransaction existingStockTransaction = new ProductTransaction(product, quantity, productTransactionType);
            productTransactionService.saveProductTransaction(existingStockTransaction);
        }
    }

    public void deleteProduct(long productId) {
        productRepository.deleteById(productId);
    }

    public int getMaxOrderableProductQuantity(Product product) {
        int stock = product.getQuantity();
        int outgoing = getOutgoingProductQuantity(product);
        int incoming = getIncomingProductQuantity(product);
        int supplierStock = 0;

        for (SupplierProduct supplierProduct : product.getSuppliers()) {
            // Buying price shouldn't be higher than our selling price
            if (supplierProduct.getUnitPrice().compareTo(product.getUnitPriceVAT()) < 1)
                supplierStock += supplierProduct.getQuantity();
        }

        return stock-outgoing+incoming+supplierStock;
    }

    public int getIncomingProductQuantity(Product product) {
        int incoming = 0;
        for (ArrivalProduct arrivalProduct : product.getArrivals())
            incoming += arrivalProduct.getQuantity();
        return product.getOldProduct() != null ? incoming+getIncomingProductQuantity(product.getOldProduct()) : incoming;
    }

    public int getOutgoingProductQuantity(Product product) {
        int outgoing = 0;
        for (OrderProduct orderProduct : product.getOrders())
            outgoing += orderProduct.getQuantity();
        return product.getOldProduct() != null ? outgoing+getOutgoingProductQuantity(product.getOldProduct()) : outgoing;
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
     * @throws AssertionError Product quantity should be greater than zero
     */
    public Product createProductFromRequest(CreateProductRequest createProductRequest) {
            Product product = new Product();

            product.setName(createProductRequest.getName());
            product.setDescription(createProductRequest.getDescription());
            product.setExpirationDate(createProductRequest.getExpirationDate());
            Integer vintage = createProductRequest.getVintage();
            product.setVintage(vintage != null ? Year.of(vintage) : null);
            product.setProductFamily(productFamilyService.getProductFamilyByCode(createProductRequest.getProductFamilyCode()).orElseThrow(() -> new IllegalArgumentException("ProductFamily not found for productFamilyCode")));
            product.setUnitPrice(createProductRequest.getUnitPrice());
            product.setUnitPriceVAT(createProductRequest.getUnitPrice().multiply(new BigDecimal("1.20")));
            product.setActive(createProductRequest.isActive());
            product.setQuantity(createProductRequest.getQuantity());

            saveProduct(product);
            return product;
    }

    /**
     * @throws IllegalArgumentException Product can't be updated with empty body
     * @throws AssertionError Product quantity should be greater than zero
     */
    public Product updateProductFromRequest(UpdateProductRequest updateProductRequest, Product oldProduct) throws IllegalArgumentException {
        Product newProduct = new Product();
        boolean isNewProductNeeded = false;

        String name = updateProductRequest.getName();
        String description = updateProductRequest.getDescription();
        Date expirationDate = updateProductRequest.getExpirationDate();
        String productFamilyCode = updateProductRequest.getProductFamilyCode();
        ProductFamily productFamily = productFamilyCode != null ? productFamilyService.getProductFamilyByCode(productFamilyCode).orElse(null) : null;
        BigDecimal unitPrice = updateProductRequest.getUnitPrice();
        Boolean active = updateProductRequest.isActive();

        if (name == null && description == null && expirationDate == null && productFamilyCode == null && unitPrice == null && active == null)
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

        if (productFamily != null && !Objects.equals(productFamily, oldProduct.getProductFamily()))
            newProduct.setProductFamily(productFamily);
        else
            newProduct.setProductFamily(oldProduct.getProductFamily());

        if (unitPrice != null && !Objects.equals(unitPrice, oldProduct.getUnitPrice())) {
            newProduct.setUnitPrice(unitPrice);
            newProduct.setUnitPriceVAT(unitPrice.multiply(new BigDecimal("1.20")));
            isNewProductNeeded = true;
        } else {
            newProduct.setUnitPrice(oldProduct.getUnitPrice());
            newProduct.setUnitPriceVAT(oldProduct.getUnitPriceVAT());
        }

        if (active != null && !Objects.equals(active, oldProduct.isActive()))
            newProduct.setActive(active);
        else
            newProduct.setActive(oldProduct.isActive());

        if (isNewProductNeeded)
            newProduct.setOldProduct(oldProduct);
        else
            newProduct.setId(oldProduct.getId());

        saveProduct(newProduct);
        return newProduct;
    }

    public ProductResponse getResponseFromProduct(Product product) {
        return product == null ? null : new ProductResponse()
                .setId(product.getId())
                .setName(product.getName())
                .setDescription(product.getDescription())
                .setQuantity(product.getQuantity())
                .setExpirationDate(product.getExpirationDate())
                .setVintage(product.getVintage())
                .setProductFamily(product.getProductFamily())
                .setUnitPrice(product.getUnitPrice())
                .setUnitPriceVAT(product.getUnitPriceVAT())
                .setActive(product.isActive())
                .setOldProduct(product.getOldProduct())
                .setNewProduct(product.getNewProduct())
                .setSupplierList(getSupplierProductElements(product.getSuppliers()))
                .setArrivalList(getArrivalProductElements(product.getArrivals()))
                .setOrderList(getOrderProductElements(product.getOrders()));
    }

    private List<SupplierProductInProductResponseElement> getSupplierProductElements(List<SupplierProduct> supplierProducts) {
        if (supplierProducts == null || supplierProducts.isEmpty())
            return null;
        List<SupplierProductInProductResponseElement> supplierProductElements = new ArrayList<>();
        for (SupplierProduct supplierProduct : supplierProducts) {
            SupplierProductInProductResponseElement supplierProductInProductResponseElement = new SupplierProductInProductResponseElement();
            supplierProductInProductResponseElement.setQuantity(supplierProduct.getQuantity())
                    .setUnitPrice(supplierProduct.getUnitPrice())
                    .setSupplier(supplierProduct.getSupplier());
            supplierProductElements.add(supplierProductInProductResponseElement);
        }
        return supplierProductElements;
    }

    private List<ArrivalProductInProductResponseElement> getArrivalProductElements(List<ArrivalProduct> arrivalProducts) {
        if (arrivalProducts == null || arrivalProducts.isEmpty())
            return null;
        List<ArrivalProductInProductResponseElement> arrivalProductElements = new ArrayList<>();
        for (ArrivalProduct arrivalProduct : arrivalProducts) {
            ArrivalProductInProductResponseElement arrivalProductInProductResponseElement = new ArrivalProductInProductResponseElement();
            arrivalProductInProductResponseElement.setId(arrivalProduct.getId())
                    .setQuantity(arrivalProduct.getQuantity())
                    .setArrival(arrivalProduct.getArrival())
                    .setProductTransaction(productTransactionService.setElementFromProductTransaction(arrivalProduct.getProductTransaction()));
            arrivalProductElements.add(arrivalProductInProductResponseElement);
        }
        return arrivalProductElements;
    }

    private List<OrderProductInProductResponseElement> getOrderProductElements(List<OrderProduct> orderProducts) {
        if (orderProducts == null || orderProducts.isEmpty())
            return null;
        List<OrderProductInProductResponseElement> orderProductElements = new ArrayList<>();
        for (OrderProduct orderProduct : orderProducts) {
            OrderProductInProductResponseElement orderProductInProductResponseElement = new OrderProductInProductResponseElement();
            orderProductInProductResponseElement.setId(orderProduct.getId())
                    .setQuantity(orderProduct.getQuantity())
                    .setPreparedAt(orderProduct.getPreparedAt())
                    .setPreparedBy(orderProduct.getPreparedBy())
                    .setOrder(orderProduct.getOrder())
                    .setProductTransaction(productTransactionService.setElementFromProductTransaction(orderProduct.getProductTransaction()));
            orderProductElements.add(orderProductInProductResponseElement);
        }
        return orderProductElements;
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
