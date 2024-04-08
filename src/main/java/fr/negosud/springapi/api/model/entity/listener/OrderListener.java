package fr.negosud.springapi.api.model.entity.listener;

import fr.negosud.springapi.api.model.OrderStatus;
import fr.negosud.springapi.api.model.entity.Invoice;
import fr.negosud.springapi.api.model.entity.Order;
import fr.negosud.springapi.api.service.InvoiceService;
import jakarta.persistence.PreUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class OrderListener {

    private final InvoiceService invoiceService;

    @Autowired
    public OrderListener(@Lazy InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PreUpdate
    public void makeInvoiceOnStatusComplete(Order order) {
        if (order.getStatus() == OrderStatus.COMPLETED && invoiceService.getInvoicesForOrder(order).isEmpty()) {
            Invoice invoice = new Invoice();
            invoice.setOrder(order);
            invoice.setAddress(order.getCreatedBy().getBillingAddress());
            invoiceService.saveInvoice(invoice);
        }
    }

}
