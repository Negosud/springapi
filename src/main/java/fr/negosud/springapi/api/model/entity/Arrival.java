package fr.negosud.springapi.api.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import fr.negosud.springapi.api.audit.AuditListener;
import fr.negosud.springapi.api.audit.FullAuditableEntity;
import fr.negosud.springapi.api.model.dto.ArrivalStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
@EntityListeners(AuditListener.class)
@Table(name = "\"arrival\"")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "reference")
public class Arrival extends FullAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Column(length = 20)
    private String reference;

    @NotBlank
    @Enumerated(value = EnumType.STRING)
    private ArrivalStatus status;

    @ManyToOne
    @NotBlank
    private User suppliedBy;

    @OneToMany(mappedBy = "arrival")
    private List<ArrivalProduct> productList;

    public Arrival() { }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public ArrivalStatus getStatus() {
        return status;
    }

    public void setStatus(ArrivalStatus status) {
        this.status = status;
    }

    public User getSuppliedBy() {
        return suppliedBy;
    }

    public void setSuppliedBy(User suppliedBy) {
        this.suppliedBy = suppliedBy;
    }
}
