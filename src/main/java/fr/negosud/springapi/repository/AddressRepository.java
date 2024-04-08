package fr.negosud.springapi.repository;

import fr.negosud.springapi.model.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
