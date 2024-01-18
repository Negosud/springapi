package fr.negosud.springapi.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class SetUserRequest {

    @NotBlank
    @Email
    @Schema(description = "User's contact email")
    private String email;

    @NotBlank
    @Schema(description = "User's first name")
    private String firstName;

    @NotBlank
    @Schema(description = "User's last name")
    private String lastName;

    @NotBlank
    @Schema(description = "User's userGroup name")
    private String userGroup;

    @Schema(description = "Is user active")
    private boolean active;

    @Email
    @Column(unique = true, length = 320)
    @Schema(description = "User's login email")
    private String login;

    @Column(length = 60)
    @Schema(description = "User's password hashed by a front app")
    private String password;

    @Column(length = 14)
    @Schema(description = "User's international phone number")
    private String phoneNumber;

    @Schema(description = "User's permission list as permission full names")
    private List<String> permissionList;

    @Schema(description = "User's mailing address id")
    private long mailingAddress;

    @Schema(description = "User's billing address id")
    private long billingAddress;

    @Schema(description = "User's supplied product list as ids")
    private List<Long> suppliedProductList;

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

    public List<Long> getSuppliedProductList() {
        return suppliedProductList;
    }

    public void setSuppliedProductList(List<Long> suppliedProductList) {
        this.suppliedProductList = suppliedProductList;
    }
}
