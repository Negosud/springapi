package fr.negosud.springapi.model.entity;

import com.fasterxml.jackson.annotation.*;
import fr.negosud.springapi.model.entity.listener.AuditListener;
import fr.negosud.springapi.model.entity.audit.FullAuditableEntity;
import fr.negosud.springapi.util.PermissionNodes;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
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
    private List<PermissionNode> permissionNodes;

    @ManyToOne
    @NotBlank
    private UserGroup userGroup;

    @OneToOne
    private Address mailingAddress;

    @OneToOne
    private Address billingAddress;

    @OneToMany(mappedBy = "supplier")
    private List<SupplierProduct> suppliedProducts;

    public User() {
        this.active = true;
    }

    /**
     * Constructor used by User init method
     */
    public User(String login, String password, String email, String firstName, String lastName, String phoneNumber, UserGroup userGroup, List<PermissionNode> permissionNodes) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.userGroup = userGroup;
        this.permissionNodes = permissionNodes;
        this.active = true;
    }

    public void addPermissionNodeList(List<PermissionNode> permissionNodeList) {
        if (permissionNodeList != null) {
            if (this.permissionNodes == null) {
                this.setPermissionNodes(permissionNodeList);
            } else {
                this.permissionNodes.addAll(permissionNodeList);
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

    public List<PermissionNode> getPermissionNodes() {
        return permissionNodes;
    }

    public void setPermissionNodes(List<PermissionNode> permissionNodeList) {
        this.permissionNodes = permissionNodeList;
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

    public List<SupplierProduct> getSuppliedProducts() {
        return suppliedProducts;
    }

    public void setSuppliedProducts(List<SupplierProduct> suppliedProductList) {
        this.suppliedProducts = suppliedProductList;
    }

    public List<PermissionNode> getCleanedPermissionNodes() {
        List<PermissionNode> cleanedPermissionNodes = new ArrayList<>(permissionNodes);
        List<PermissionNode> groupPermissionNodes = userGroup.getCleanedPermissionNodes();
        if (!groupPermissionNodes.isEmpty())
            cleanedPermissionNodes.addAll(groupPermissionNodes);
        return PermissionNodes.cleanPermissionNodeList(cleanedPermissionNodes);
    }

    @JsonIgnore
    public boolean can(String permissionName) {
        List<PermissionNode> permissionNodes = getCleanedPermissionNodes();
        if (permissionNodes.isEmpty())
            return false;
        String[] permissionNameParts = permissionName.split("\\.");
        StringBuilder checkedPermissionName = new StringBuilder();
        for (String permissionNamePart : permissionNameParts) {
            checkedPermissionName.append(permissionNamePart);
            for (PermissionNode permissionNode : permissionNodes) {
                if (permissionNode.getName().contentEquals(checkedPermissionName))
                    return true;
            }
        }
        return false;
    }
}
