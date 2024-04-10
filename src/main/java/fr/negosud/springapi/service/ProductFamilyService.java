package fr.negosud.springapi.service;

import fr.negosud.springapi.model.dto.request.SetProductFamilyRequest;
import fr.negosud.springapi.model.entity.Product;
import fr.negosud.springapi.model.entity.ProductFamily;
import fr.negosud.springapi.repository.ProductFamilyRepository;
import fr.negosud.springapi.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductFamilyService {

    private final ProductFamilyRepository productFamilyRepository;
    private final ProductService productService;

    @Autowired
    public ProductFamilyService(ProductFamilyRepository productFamilyRepository, ProductService productService) {
        this.productFamilyRepository = productFamilyRepository;
        this.productService = productService;
    }

    public List<ProductFamily> getAllProductFamilies() {
        return productFamilyRepository.findAll();
    }

    public Optional<ProductFamily> getProductFamilyById(long productFamilyId) {
        return productFamilyRepository.findById(productFamilyId);
    }


    public Optional<ProductFamily> getProductFamilyByCode(String code) {
        return productFamilyRepository.findByCode(code);
    }

    /**
     * @throws DuplicateKeyException A productFamily with processed name as code already exist
     */
    public void saveProductFamily(ProductFamily productFamily) {
        if (productFamily.getId() == 0 && getProductFamilyByCode(productFamily.getCode()).isPresent())
            throw new DuplicateKeyException("A productFamily with processed name as code already exist");
        productFamilyRepository.save(productFamily);
    }

    public void deleteProductFamily(ProductFamily productFamily) {
        productFamilyRepository.delete(productFamily);
    }

    public void safeDeleteProductFamily(ProductFamily productFamily, ProductFamily replacingProductFamily) {
        for (Product product : productFamily.getProducts()) {
            product.setProductFamily(replacingProductFamily);
            productService.saveProduct(product);
        }
        productFamilyRepository.delete(productFamily);
    }

    public ProductFamily setProductFamilyFromRequest(SetProductFamilyRequest setProductFamilyRequest, ProductFamily productFamily) {
        boolean create = productFamily == null;
        if (create)
            productFamily = new ProductFamily();

        String name = setProductFamilyRequest.getName();
        productFamily.setCode(create ? Strings.getCodeFromName(name) : productFamily.getCode());
        productFamily.setName(name);
        productFamily.setDescription(create ?
                setProductFamilyRequest.getDescription() :
                setProductFamilyRequest.getDescription() == null ? productFamily.getDescription() : setProductFamilyRequest.getDescription());

        return productFamily;
    }

    public boolean initProductFamilies() {
        Yaml yaml = new Yaml();

        try (InputStream inputStream = new ClassPathResource("productFamilies.yml").getInputStream()) {
            Map<String, List<Map<String, String>>> productFamiliesMap = yaml.load(inputStream);
            if (productFamiliesMap != null && productFamiliesMap.containsKey("productFamilies")) {
                List<Map<String, String>> productFamiliesInfo = productFamiliesMap.get("productFamilies");
                for (Map<String, String> productFamilyInfo : productFamiliesInfo) {
                    ProductFamily productFamily = new ProductFamily(productFamilyInfo.get("name"), productFamilyInfo.get("description"));
                    if (productFamilyRepository.findByCode(productFamily.getCode()).isEmpty())
                        saveProductFamily(productFamily);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
