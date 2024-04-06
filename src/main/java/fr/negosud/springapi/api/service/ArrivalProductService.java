package fr.negosud.springapi.api.service;

import fr.negosud.springapi.api.model.entity.ArrivalProduct;
import fr.negosud.springapi.api.repository.ArrivalProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArrivalProductService {

    final private ArrivalProductRepository arrivalProductRepository;

    @Autowired
    public ArrivalProductService(ArrivalProductRepository arrivalProductRepository) {
        this.arrivalProductRepository = arrivalProductRepository;
    }

    public void saveArrivalProduct(ArrivalProduct arrivalProduct) {
        arrivalProductRepository.save(arrivalProduct);
    }

    public List<ArrivalProduct> getUnlinkedArrivalProducts() {
        return arrivalProductRepository.findAllByArrivalIsNull();
    }
}
