package fr.negosud.springapi.api.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Long invoiceId;

    @NotBlank
    @Column(nullable = false, length = 20)
    private String reference;

    public  Invoice(){
    }

    public Invoice(Long invoiceId, String reference) {
        this.invoiceId = invoiceId;
        this.reference = reference;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
