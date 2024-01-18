package fr.negosud.springapi.api.service;

import fr.negosud.springapi.api.model.entity.SupplierProduct;
import fr.negosud.springapi.api.repository.SupplierProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
final public class SupplierProductService {

    final private SupplierProductRepository supplierProductRepository;

    @Autowired
    public SupplierProductService(SupplierProductRepository supplierProductRepository) {
        this.supplierProductRepository = supplierProductRepository;
    }

    public List<SupplierProduct> getSupplierProductListByIdList(List<Long> supplierProductList) {
        return this.supplierProductRepository.findSupplierProductByIdIn(supplierProductList);
    }

}
