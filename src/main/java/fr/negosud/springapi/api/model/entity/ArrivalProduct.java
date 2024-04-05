package fr.negosud.springapi.api.model.entity;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import fr.negosud.springapi.api.audit.AuditListener;
import fr.negosud.springapi.api.audit.CreationAuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@EntityListeners(AuditListener.class)
@Table(name="\"arrival_product\"")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ArrivalProduct extends CreationAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private int quantity;

    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private Arrival arrival;

    @ManyToOne
    @NotBlank
    @JsonIdentityReference(alwaysAsId = true)
    private Product product;

    public ArrivalProduct() { }

    /**
     * Constructor used by OrderProductListener PostPersist Method
     */
    public ArrivalProduct(int quantity, Product product) {
        this.quantity = quantity;
        this.product = product;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Arrival getArrival() {
        return arrival;
    }

    public void setArrival(Arrival arrival) {
        this.arrival = arrival;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
