package fr.negosud.springapi.api.model.entity;

import fr.negosud.springapi.api.audit.AuditListener;
import fr.negosud.springapi.api.audit.FullAuditableEntity;
import jakarta.persistence.*;

@Entity
@EntityListeners(AuditListener.class)
final public class Address extends FullAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @Column(length = 100)
    private String country;

    @Column(length = 100)
    private String administrativeDivision;

    @Column(length = 100)
    private String city;

    @Column(length = 10)
    private String postalCode;

    private String addressLine1;

    private String addressLine2;

    private boolean active;

    public Address() { }

    public Address(Long addressId, String country, String administrativeDivision, String city, String postalCode, String addressLine1, String addressLine2, boolean active) {
        this.addressId = addressId;
        this.country = country;
        this.administrativeDivision = administrativeDivision;
        this.city = city;
        this.postalCode = postalCode;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.active = active;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAdministrativeDivision() {
        return administrativeDivision;
    }

    public void setAdministrativeDivision(String administrativeDivision) {
        this.administrativeDivision = administrativeDivision;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
