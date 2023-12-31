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
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String userGroup;

    @Email
    @Column(unique = true, length = 320)
    private String login;

    @Column(length = 60)
    private String password;

    @Column(length = 14)
    private String phoneNumber;

    private List<String> permissionList;

    private long mailingAddress;

    private long billingAddress;

    private List<Long> productList;

    public SetUserRequest(String email, String firstName, String lastName, String userGroup, String login, String password, String phoneNumber, List<String> permissionList, long mailingAddress, long billingAddress, List<Long> productList) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userGroup = userGroup;
        this.login = login;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.permissionList = permissionList;
        this.mailingAddress = mailingAddress;
        this.billingAddress = billingAddress;
        this.productList = productList;
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

    public List<Long> getProductList() {
        return productList;
    }

    public void setProductList(List<Long> productList) {
        this.productList = productList;
    }
}
