package fr.negosud.springapi.api.repository;

import fr.negosud.springapi.api.model.entity.ArrivalProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArrivalProductRepository extends JpaRepository<ArrivalProduct, Long> {

    List<ArrivalProduct> findAllByArrivalIsNull();
}
