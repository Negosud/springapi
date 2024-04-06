package fr.negosud.springapi.api.model.entity;

import com.fasterxml.jackson.annotation.*;
import fr.negosud.springapi.api.audit.AuditListener;
import fr.negosud.springapi.api.audit.FullAuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
@EntityListeners(AuditListener.class)
@Table(name="\"user\"")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User extends FullAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Email
    @Column(unique = true, length = 320)
    private String login;

    private String password;

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

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "user_permission_node",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_node_id")
    )
    @JsonIdentityReference(alwaysAsId = true)
    private List<PermissionNode> permissionNodeList;

    @ManyToOne
    @NotBlank
    @JsonIdentityReference(alwaysAsId = true)
    @JoinColumn(referencedColumnName = "name")
    private UserGroup userGroup;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIdentityReference(alwaysAsId = true)
    private Address mailingAddress;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIdentityReference(alwaysAsId = true)
    private Address billingAddress;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<SupplierProduct> suppliedProductList;

    public User() {
        this.active = true;
    }

    /**
     * Constructor used by User init method
     */
    public User(String login, String password, String email, String firstName, String lastName, String phoneNumber, UserGroup userGroup, List<PermissionNode> permissionNodeList) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.userGroup = userGroup;
        this.permissionNodeList = permissionNodeList;
        this.active = true;
    }

    public void addPermissionNodeList(List<PermissionNode> permissionNodeList) {
        if (permissionNodeList != null) {
            if (this.permissionNodeList == null) {
                this.setPermissionNodeList(permissionNodeList);
            } else {
                this.permissionNodeList.addAll(permissionNodeList);
            }
        }
    }

    @Override
    public String toString() {
        return this.firstName + " " + this.lastName;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<PermissionNode> getPermissionNodeList() {
        return permissionNodeList;
    }

    public void setPermissionNodeList(List<PermissionNode> permissionNodeList) {
        this.permissionNodeList = permissionNodeList;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    public Address getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(Address mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<SupplierProduct> getSuppliedProductList() {
        return suppliedProductList;
    }

    public void setSuppliedProductList(List<SupplierProduct> suppliedProductList) {
        this.suppliedProductList = suppliedProductList;
    }
}
