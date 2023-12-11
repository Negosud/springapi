package fr.negosud.springapi.api.repository;

import fr.negosud.springapi.api.model.entity.ProductFamily;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductFamilyRepository extends JpaRepository<ProductFamily, Long> {

}
