package fr.negosud.springapi.api.service;

import fr.negosud.springapi.api.model.entity.Address;
import fr.negosud.springapi.api.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Optional<Address> getAddressById(long id) {
        return this.addressRepository.findById(id);
    }

}
