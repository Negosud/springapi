package fr.negosud.springapi.api.repository;

import fr.negosud.springapi.api.model.entity.Arrival;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArrivalRepository extends JpaRepository<Arrival, Long> {
}
