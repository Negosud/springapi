package fr.negosud.springapi.api.repository;

import fr.negosud.springapi.api.model.entity.Invoice;
import fr.negosud.springapi.api.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findAllByOrder(Order order);

}
