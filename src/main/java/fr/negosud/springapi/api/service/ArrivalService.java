package fr.negosud.springapi.api.service;

import fr.negosud.springapi.api.model.dto.SetArrivalRequest;
import fr.negosud.springapi.api.model.entity.Arrival;
import fr.negosud.springapi.api.repository.ArrivalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class ArrivalService {

    private final ArrivalRepository arrivalRepository;

    @Autowired
    public ArrivalService(ArrivalRepository arrivalRepository){
        this.arrivalRepository = arrivalRepository;
    }

    public List<Arrival> getAllArrivals() {
        return arrivalRepository.findAll();
    }

    public Optional<Arrival> getArrivalById(Long arrivalId) {
        return arrivalRepository.findById(arrivalId);
    }

    public Arrival saveArrival(Arrival arrival) {
        return arrivalRepository.save(arrival);}

    public Arrival setArrivalFromRequest(SetArrivalRequest setArrivalRequest, Arrival arrival) {
        if(arrival == null)
            arrival = new Arrival();
        arrival.setReference(setArrivalRequest.getReference());
        arrival.setStatus(setArrivalRequest.getStatus());
        arrival.setSuppliedBy(setArrivalRequest.getSuppliedBy());
        arrival.setProductList(setArrivalRequest.getProductList());

        return arrival;
    }
}
