package fr.negosud.springapi.api.service;

import fr.negosud.springapi.api.model.ArrivalStatus;
import fr.negosud.springapi.api.model.dto.request.PlaceArrivalRequest;
import fr.negosud.springapi.api.model.dto.request.element.SetArrivalProductElement;
import fr.negosud.springapi.api.model.dto.response.ArrivalResponse;
import fr.negosud.springapi.api.model.dto.response.element.ArrivalProductInArrivalElement;
import fr.negosud.springapi.api.model.entity.*;
import fr.negosud.springapi.api.repository.ArrivalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArrivalService {

    private final ArrivalRepository arrivalRepository;
    private final UserService userService;
    private final ProductService productService;
    private final SupplierProductService supplierProductService;

    @Autowired
    public ArrivalService(ArrivalRepository arrivalRepository, UserService userService, ProductService productService, SupplierProductService supplierProductService) {
        this.arrivalRepository = arrivalRepository;
        this.userService = userService;
        this.productService = productService;
        this.supplierProductService = supplierProductService;
    }

    /**
     * @throws IllegalArgumentException SuppliedById doesn't correspond to a proper user
     */
    public List<Arrival> getAllArrivals(ArrivalStatus status, Long suppliedById) {
        if (suppliedById != null) {
            return userService.getUserById(suppliedById)
                    .map(user-> status != null ?
                            arrivalRepository.findAllByStatusAndSuppliedBy(status, user) :
                            arrivalRepository.findAllBySuppliedBy(user))
                    .orElseThrow(() -> new IllegalArgumentException("SuppliedById doesn't correspond to a proper user"));
        } else {
            return status != null ?
                    arrivalRepository.findAllByStatus(status) :
                    arrivalRepository.findAll();
        }
    }

    public Optional<Arrival> getArrivalByReference(String reference) {
        return arrivalRepository.findByReference(reference);
    }

    public void saveArrival(Arrival arrival) {
        arrivalRepository.save(arrival);
    }

    public Arrival placeArrivalFromRequest(PlaceArrivalRequest placeArrivalRequest) {
        User supplier = userService.getUserById(placeArrivalRequest.getSupplierId())
                .orElseThrow(() -> new IllegalArgumentException("Supplier doesn't exist"));
        if (!supplier.getUserGroup().getName().equals("SUPPLIER"))
            throw new IllegalArgumentException("User doesn't have the SUPPLIER user group");

        Arrival arrival = new Arrival();

        List<ArrivalProduct> arrivalProducts = new ArrayList<>();
        for (SetArrivalProductElement arrivalProductElement : placeArrivalRequest.getArrivalProductElements()) {
            Product product = productService.getProductById(arrivalProductElement.getProductId()).orElse(null);
            assert product != null : "Product Id " + arrivalProductElement.getProductId() + "  doesn't correspond to a proper product";
            assert product.isActive() : "Product for Id " + arrivalProductElement.getProductId() + " isn't active";
            SupplierProduct supplierProduct = supplierProductService.getSupplierProductBySupplierAndProductId(supplier, arrivalProductElement.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Supplier doesn't provide product with id " + arrivalProductElement.getProductId()));
            assert supplierProduct.getQuantity() >= arrivalProductElement.getQuantity() : "Supplier can't provide " + arrivalProductElement.getQuantity() + " of product " + arrivalProductElement.getProductId();
            //TODO: Finish this
        }

        return arrival;
    }

    public ArrivalResponse getResponseFromArrival(Arrival arrival) {
        return new ArrivalResponse().setId(arrival.getId())
                .setReference(arrival.getReference())
                .setStatus(arrival.getStatus())
                .setSuppliedBy(arrival.getSuppliedBy())
                .setComment(arrival.getComment())
                .setProductList(getArrivalProductElements(arrival.getProductList()));
    }

    private List<ArrivalProductInArrivalElement> getArrivalProductElements(List<ArrivalProduct> arrivalProducts) {
        List<ArrivalProductInArrivalElement> arrivalProductElements = new ArrayList<>();
        for (ArrivalProduct arrivalProduct : arrivalProducts) {
            ArrivalProductInArrivalElement arrivalProductElement = new ArrivalProductInArrivalElement();
            arrivalProductElement.setId(arrivalProduct.getId())
                    .setProduct(arrivalProduct.getProduct())
                    .setQuantity(arrivalProduct.getQuantity());
            arrivalProductElements.add(arrivalProductElement);
        }
        return arrivalProductElements;
    }
}
