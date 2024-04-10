package fr.negosud.springapi.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import fr.negosud.springapi.model.entity.listener.AuditListener;
import fr.negosud.springapi.model.entity.audit.FullAuditableEntity;
import fr.negosud.springapi.model.entity.annotation.AutoReference;
import fr.negosud.springapi.model.entity.constraint.ReferencedEntityConstraint;
import fr.negosud.springapi.model.ArrivalStatus;
import fr.negosud.springapi.model.entity.listener.ReferenceListener;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
@EntityListeners({AuditListener.class, ReferenceListener.class})
@Table(name = "\"arrival\"")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "reference")
@AutoReference(referenceCode = "ARVL")
public class Arrival extends FullAuditableEntity implements ReferencedEntityConstraint {

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

    @Column(length = 1000)
    private String comment;

    @OneToMany(mappedBy = "arrival")
    private List<ArrivalProduct> arrivalProducts;

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<ArrivalProduct> getArrivalProducts() {
        return arrivalProducts;
    }

    public void setArrivalProducts(List<ArrivalProduct> productList) {
        this.arrivalProducts = productList;
    }
}
