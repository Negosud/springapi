package fr.negosud.springapi.model.dto.request;

import fr.negosud.springapi.model.dto.request.element.SetSupplierProductElement;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public class SetUserRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String userGroup;

    private boolean active;

    @Email
    @Size(max = 320)
    private String login;

    @Size(max = 60)
    private String password;

    @Size(max = 14)
    private String phoneNumber;

    private List<String> permissionList;

    private long mailingAddress;

    private long billingAddress;

    private List<SetSupplierProductElement> supplierProducts;

    public SetUserRequest() {
        this.active = true;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<String> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<String> permissionList) {
        this.permissionList = permissionList;
    }

    public long getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(long mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public long getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(long billingAddress) {
        this.billingAddress = billingAddress;
    }

    public List<SetSupplierProductElement> getSupplierProducts() {
        return supplierProducts;
    }

    public void setSupplierProducts(List<SetSupplierProductElement> supplierProducts) {
        this.supplierProducts = supplierProducts;
    }
}
