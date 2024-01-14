package fr.negosud.springapi.api.repository;

import fr.negosud.springapi.api.model.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
