package fr.negosud.springapi.api.service;

import fr.negosud.springapi.api.model.dto.SetProductTransactionTypeRequest;
import fr.negosud.springapi.api.model.entity.ProductTransaction;
import fr.negosud.springapi.api.model.entity.ProductTransactionType;
import fr.negosud.springapi.api.repository.ProductTransactionTypeRepository;
import fr.negosud.springapi.api.util.Strings;
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
public class ProductTransactionTypeService {

    private final ProductTransactionTypeRepository productTransactionTypeRepository;

    @Autowired
    public ProductTransactionTypeService(ProductTransactionTypeRepository productTransactionTypeRepository) {
        this.productTransactionTypeRepository = productTransactionTypeRepository;
    }

    public List<ProductTransactionType> getAllProductTransactionType(Boolean isEntry) {
        return isEntry != null ?
                productTransactionTypeRepository.findAllByisEntry(isEntry) :
                productTransactionTypeRepository.findAll();
    }

    public Optional<ProductTransactionType> getProductTransactionTypeById(long productTransactionTypeId) {
        return productTransactionTypeRepository.findById(productTransactionTypeId);
    }

    public Optional<ProductTransactionType> getProductTransactionTypeByCode(String code) {
        return productTransactionTypeRepository.findByCode(code);
    }

    /**
     * @throws DuplicateKeyException A productTransactionType with processed name as code already exist
     */
    public void saveProductTransactionType(ProductTransactionType productTransactionType) {
        if (productTransactionType.getId() == 0 && getProductTransactionTypeByCode(productTransactionType.getCode()).isPresent())
            throw new DuplicateKeyException("A productTransactionType with processed name as code already exist");
        productTransactionTypeRepository.save(productTransactionType);
    }

    public void deleteProductTransactionType(ProductTransactionType productTransactionType) {
        productTransactionTypeRepository.delete(productTransactionType);
    }

    public void safeDeleteProductTransactionType(ProductTransactionType productTransactionType, ProductTransactionType replacingProductTransactionType) {
        for (ProductTransaction productTransaction : productTransactionType.getProductTransactionList()) {
            productTransaction.setProdutTransactionType(replacingProductTransactionType);
        }
        productTransactionTypeRepository.delete(productTransactionType);
    }

    /**
     * @throws IllegalArgumentException ProductTransactionType isEntry field cannot be changed
     */
    public ProductTransactionType setProductTransactionTypeFromRequest(SetProductTransactionTypeRequest setProductTransactionTypeRequest, ProductTransactionType productTransactionType) {
        boolean create = productTransactionType == null;
        if (create) {
            productTransactionType = new ProductTransactionType();
            productTransactionType.setEntry(setProductTransactionTypeRequest.isEntry());
        } else if (productTransactionType.isEntry() != setProductTransactionTypeRequest.isEntry() && !productTransactionType.getProductTransactionList().isEmpty()) {
            throw new IllegalArgumentException("ProductTransactionType isEntry field cannot be changed");
        }

        String name = setProductTransactionTypeRequest.getName();
        productTransactionType.setCode(create ? Strings.getCodeFromName(name) : productTransactionType.getCode());
        productTransactionType.setName(name);
        productTransactionType.setDescription(create ?
                setProductTransactionTypeRequest.getDescription() :
                setProductTransactionTypeRequest.getDescription() == null ? productTransactionType.getDescription() : setProductTransactionTypeRequest.getDescription());

    return productTransactionType;
    }

    public boolean initProductTransactionTypes() {
        Yaml yaml = new Yaml();

        try (InputStream inputStream = new ClassPathResource("productTransactionTypes.yml").getInputStream()) {
            Map<String, List<Map<String, Object>>> productTransactionTypesMap = yaml.load(inputStream);
            if (productTransactionTypesMap != null && productTransactionTypesMap.containsKey("productTransactionTypes")) {
                List<Map<String, Object>> productTransactionTypesInfo = productTransactionTypesMap.get("productTransactionTypes");
                for (Map<String, Object> productTransactionTypeInfo : productTransactionTypesInfo) {
                    ProductTransactionType productTransactionType = new ProductTransactionType((String) productTransactionTypeInfo.get("name"), (String) productTransactionTypeInfo.get("description"),  productTransactionTypeInfo.containsKey("isEntry"));
                    if (productTransactionTypeRepository.findByCode(productTransactionType.getCode()).isEmpty())
                        saveProductTransactionType(productTransactionType);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}


