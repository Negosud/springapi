package fr.negosud.springapi.api.service;

import fr.negosud.springapi.api.model.dto.SetProductRequest;
import fr.negosud.springapi.api.model.entity.Product;
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

    @Autowired
    public ProductService(ProductRepository productRepository, ProductFamilyService productFamilyService) {
        this.productRepository = productRepository;
        this.productFamilyService = productFamilyService;
    }

    public List<Product> getAllProducts(Optional<Boolean> active, String productFamilyName) {
        return (productFamilyName == null) ?
                (active.isEmpty() ? productRepository.findAll() : productRepository.findAllByActive(active.get())) :
                (active.isEmpty() ? productRepository.findAllByProductFamilyName(productFamilyName) : productRepository.findAllByActiveAndProductFamilyName(active.get(), productFamilyName));
    }

    public Optional<Product> getProductById(long productId) {
        return productRepository.findById(productId);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(long productId) {
        productRepository.deleteById(productId);
    }

    public Product setProductFromRequest(SetProductRequest setProductRequest, Product product) {
        boolean create = product == null;
        if (create)
            product = new Product();

        product.setName(setProductRequest.getName());
        product.setDescription(setProductRequest.getDescription());
        product.setVintage(Year.of(setProductRequest.getVintage()));
        product.setProductFamily(productFamilyService.getProductFamilyByCode(setProductRequest.getProductFamilyCode()).orElse(null));
        product.setUnitPrice(setProductRequest.getUnitPrice());
        product.setUnitPriceVAT(setProductRequest.getUnitPrice().multiply(new BigDecimal("1.20")));

        Integer quantity = setProductRequest.getQuantity();
        product.setQuantity(create ?
                (quantity == null ? 0 : quantity) :
                (quantity == null ? product.getQuantity() : quantity));

        Boolean active = setProductRequest.isActive();
        product.setActive(create ?
                (active != null ? active : (quantity != null && quantity != 0)) :
                (active == null) ? product.isActive() : active);

        return product;
    }
}
