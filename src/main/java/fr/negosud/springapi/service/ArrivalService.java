package fr.negosud.springapi.service;

import fr.negosud.springapi.model.ArrivalStatus;
import fr.negosud.springapi.model.dto.request.PlaceArrivalRequest;
import fr.negosud.springapi.model.dto.request.element.SetArrivalProductRequestElement;
import fr.negosud.springapi.model.dto.response.ArrivalResponse;
import fr.negosud.springapi.model.dto.response.element.ArrivalProductInArrivalResponseElement;
import fr.negosud.springapi.model.entity.*;
import fr.negosud.springapi.repository.ArrivalRepository;
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
    private final ProductTransactionService productTransactionService;

    @Autowired
    public ArrivalService(ArrivalRepository arrivalRepository, UserService userService, ProductService productService, SupplierProductService supplierProductService, ProductTransactionService productTransactionService) {
        this.arrivalRepository = arrivalRepository;
        this.userService = userService;
        this.productService = productService;
        this.supplierProductService = supplierProductService;
        this.productTransactionService = productTransactionService;
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
        for (SetArrivalProductRequestElement arrivalProductElement : placeArrivalRequest.getArrivalProducts()) {
            Product product = productService.getProductById(arrivalProductElement.getProductId()).orElse(null);
            assert product != null : "Product Id " + arrivalProductElement.getProductId() + "  doesn't correspond to a proper product";
            assert product.isActive() : "Product for Id " + arrivalProductElement.getProductId() + " isn't active";
            SupplierProduct supplierProduct = supplierProductService.getSupplierProductBySupplierAndProductId(supplier, arrivalProductElement.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Supplier doesn't provide product with id " + arrivalProductElement.getProductId()));
            assert supplierProduct.getQuantity() >= arrivalProductElement.getQuantity() : "Supplier can't provide " + arrivalProductElement.getQuantity() + " of product " + arrivalProductElement.getProductId();
            ArrivalProduct arrivalProduct = new ArrivalProduct(arrivalProductElement.getQuantity(), product);
            arrival.getProductList().add(arrivalProduct);
        }
        arrival.setComment(placeArrivalRequest.getComment());
        arrival.setSuppliedBy(supplier);
        arrival.setStatus(ArrivalStatus.ORDERING);

        saveArrival(arrival);
        return arrival;
    }

    /**
     * @throws RuntimeException ProductTransaction ACHAT_FOURNISSEUR not found
     */
    public void completeArrival(Arrival arrival) {
        for (ArrivalProduct arrivalProduct : arrival.getProductList()) {
            productTransactionService.makeProductTransactionFromArrivalProduct(arrivalProduct);

        }
    }

    public ArrivalResponse getResponseFromArrival(Arrival arrival) {
        return (ArrivalResponse) new ArrivalResponse().setId(arrival.getId())
                .setReference(arrival.getReference())
                .setStatus(arrival.getStatus())
                .setSuppliedBy(arrival.getSuppliedBy())
                .setComment(arrival.getComment())
                .setProductList(getArrivalProductElements(arrival.getProductList()))
                .setCreatedAt(arrival.getCreatedAt())
                .setCreatedBy(arrival.getCreatedBy())
                .setModifiedAt(arrival.getModifiedAt())
                .setModifiedBy(arrival.getModifiedBy());
    }

    private List<ArrivalProductInArrivalResponseElement> getArrivalProductElements(List<ArrivalProduct> arrivalProducts) {
        List<ArrivalProductInArrivalResponseElement> arrivalProductElements = new ArrayList<>();
        for (ArrivalProduct arrivalProduct : arrivalProducts) {
            ArrivalProductInArrivalResponseElement arrivalProductElement = new ArrivalProductInArrivalResponseElement();
            arrivalProductElement.setId(arrivalProduct.getId())
                    .setProduct(arrivalProduct.getProduct())
                    .setQuantity(arrivalProduct.getQuantity())
                    .setProductTransaction(productTransactionService.setElementFromProductTransaction(arrivalProduct.getProductTransaction()));
            arrivalProductElements.add(arrivalProductElement);
        }
        return arrivalProductElements;
    }
}
