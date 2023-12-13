package fr.negosud.springapi.api.repository;

import fr.negosud.springapi.api.model.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

}
