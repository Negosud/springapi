package fr.negosud.springapi.api.service;

import fr.negosud.springapi.api.model.dto.SetProductFamilyRequest;
import fr.negosud.springapi.api.model.entity.Product;
import fr.negosud.springapi.api.model.entity.ProductFamily;
import fr.negosud.springapi.api.repository.ProductFamilyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductFamilyService {

    private final ProductFamilyRepository productFamilyRepository;

    @Autowired
    public ProductFamilyService(ProductFamilyRepository productFamilyRepository) {
        this.productFamilyRepository = productFamilyRepository;
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
     * @throws org.springframework.dao.DuplicateKeyException A productFamily with processed name as code already exist
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
        for (Product product : productFamily.getProductList()) {
            product.setProductFamily(replacingProductFamily);
        }
        productFamilyRepository.delete(productFamily);
    }

    public String getCodeFromName(String name) {
        return Normalizer.normalize(name, Normalizer.Form.NFD)
                .replaceAll("[^a-zA-Z0-9]", "")
                .replaceAll("\\s", "_");
    }

    public ProductFamily setProductFamilyFromRequest(SetProductFamilyRequest setProductFamilyRequest, ProductFamily productFamily) {
        boolean create = productFamily == null;
        if (create)
            productFamily = new ProductFamily();

        String name = setProductFamilyRequest.getName();
        productFamily.setCode(create ? getCodeFromName(name) : productFamily.getCode());
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
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
