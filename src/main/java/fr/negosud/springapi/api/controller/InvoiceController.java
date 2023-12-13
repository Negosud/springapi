package fr.negosud.springapi.api.controller;

import fr.negosud.springapi.api.model.entity.Invoice;
import fr.negosud.springapi.api.service.InvoiceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoice")
@Tag(name = "Invoice")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {

        this.invoiceService = invoiceService;
    }

    @GetMapping
    public ResponseEntity<List<Invoice>> getAllInvoices() {

        List<Invoice> invoices = invoiceService.getAllInvoices();
        return new ResponseEntity<>(invoices, HttpStatus.OK);
    }

    @GetMapping("/{invoiceId}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long invoiceId) {

        return invoiceService.getInvoiceById(invoiceId)
                .map(invoice -> new ResponseEntity<>(invoice, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Invoice> createInvoice(@RequestBody Invoice invoice) {

        Invoice createdInvoice= invoiceService.saveInvoice(invoice);
        return new ResponseEntity<>(createdInvoice, HttpStatus.CREATED);
    }
}