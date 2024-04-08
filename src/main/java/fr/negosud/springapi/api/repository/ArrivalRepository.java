package fr.negosud.springapi.api.repository;

import fr.negosud.springapi.api.model.ArrivalStatus;
import fr.negosud.springapi.api.model.entity.Arrival;
import fr.negosud.springapi.api.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArrivalRepository extends JpaRepository<Arrival, Long> {

    List<Arrival> findAllByStatusAndSuppliedBy(ArrivalStatus status, User user);
    List<Arrival> findAllBySuppliedBy(User user);
    List<Arrival> findAllByStatus(ArrivalStatus status);

    Optional<Arrival> findByReference(String reference);
}
