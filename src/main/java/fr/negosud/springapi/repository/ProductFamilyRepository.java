package fr.negosud.springapi.repository;

import fr.negosud.springapi.model.entity.ProductFamily;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductFamilyRepository extends JpaRepository<ProductFamily, Long> {

    Optional<ProductFamily> findByCode(String code);

}
