package fr.negosud.springapi.api.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import fr.negosud.springapi.api.audit.AuditListener;
import fr.negosud.springapi.api.audit.FullAuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@EntityListeners(AuditListener.class)
@Table(name = "\"issue\"")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "reference")
public class Issue extends FullAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Column(length = 20, unique = true)
    private String reference;

    @NotBlank
    private boolean active;

    @NotBlank
    @Column(length = 1000)
    private String description;

    @ManyToOne
    private Arrival arrival;

    @ManyToOne
    private Order order;

    public Issue() { }

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

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
