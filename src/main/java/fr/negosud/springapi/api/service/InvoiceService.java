package fr.negosud.springapi.api.service;

import fr.negosud.springapi.api.model.entity.Invoice;
import fr.negosud.springapi.api.model.entity.Order;
import fr.negosud.springapi.api.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Optional<Invoice> getInvoiceById(long invoiceId) {
        return invoiceRepository.findById(invoiceId);
    }

    public List<Invoice> getInvoicesForOrder(Order order) {
        return invoiceRepository.findAllByOrder(order);
    }

    public void saveInvoice(Invoice invoice) {
        invoiceRepository.save(invoice);
    }
}
