package fr.negosud.springapi.api.model.entity;

import fr.negosud.springapi.api.audit.AuditListener;
import fr.negosud.springapi.api.audit.ModificationAuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;
import java.util.List;

@Entity
@EntityListeners(AuditListener.class)
@Table(name="user")
final public class User extends ModificationAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Email
    @Column(unique = true, length = 320)
    private String login;

    @Column(length = 60)
    private String password;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true, length = 320)
    private String email;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Column(length = 14)
    private String phoneNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @ManyToMany
    @JoinTable(
            name = "user_permission_node",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_node_id")
    )
    private List<PermissionNode> permissionNodeList;

    @ManyToOne
    @NotBlank
    private UserGroup userGroup;

    public User() { }

    public User(Long userId, String login, String password, String email, String firstName, String lastName, String phoneNumber, Date createdAt, List<PermissionNode> permissionNodeList) {
        this.userId = userId;
        this.login = login;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
        this.permissionNodeList = permissionNodeList;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.createdAt = new Date();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<PermissionNode> getPermissionNodeList() {
        return permissionNodeList;
    }

    public void setPermissionNodeList(List<PermissionNode> permissionNodeList) {
        this.permissionNodeList = permissionNodeList;
    }
}
