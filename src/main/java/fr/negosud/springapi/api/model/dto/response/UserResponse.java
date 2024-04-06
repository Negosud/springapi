package fr.negosud.springapi.api.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import fr.negosud.springapi.api.model.dto.response.element.SupplierProductInUserElement;
import fr.negosud.springapi.api.model.entity.Address;
import fr.negosud.springapi.api.model.entity.PermissionNode;
import fr.negosud.springapi.api.model.entity.UserGroup;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class UserResponse {

    private long id;

    @Email
    @Column(unique = true, length = 320)
    private String login;

    @NotBlank
    @Email
    @Column(length = 320)
    private String email;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Column(length = 14)
    private String phoneNumber;

    @NotNull
    private boolean active;

    @JsonIdentityReference(alwaysAsId = true)
    private List<PermissionNode> permissionNodeList;

    @NotBlank
    @JsonIdentityReference(alwaysAsId = true)
    private UserGroup userGroup;

    @JsonIdentityReference(alwaysAsId = true)
    private Address mailingAddress;

    @JsonIdentityReference(alwaysAsId = true)
    private Address billingAddress;

    private List<SupplierProductInUserElement> suppliedProductList;

    public UserResponse() { }

    public long getId() {
        return id;
    }

    public UserResponse setId(long id) {
        this.id = id;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public UserResponse setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserResponse setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserResponse setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserResponse setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UserResponse setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public UserResponse setActive(boolean active) {
        this.active = active;
        return this;
    }

    public List<PermissionNode> getPermissionNodeList() {
        return permissionNodeList;
    }

    public UserResponse setPermissionNodeList(List<PermissionNode> permissionNodeList) {
        this.permissionNodeList = permissionNodeList;
        return this;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public UserResponse setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
        return this;
    }

    public Address getMailingAddress() {
        return mailingAddress;
    }

    public UserResponse setMailingAddress(Address mailingAddress) {
        this.mailingAddress = mailingAddress;
        return this;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public UserResponse setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
        return this;
    }

    public List<SupplierProductInUserElement> getSuppliedProductList() {
        return suppliedProductList;
    }

    public UserResponse setSuppliedProductList(List<SupplierProductInUserElement> suppliedProductList) {
        this.suppliedProductList = suppliedProductList;
        return this;
    }
}
