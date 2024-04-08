package fr.negosud.springapi.model.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
@Table(name = "\"permission_node\"")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "full_name")
public class PermissionNode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private long id;

    @NotBlank
    private String name;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parent_permission_node_id")
    private PermissionNode parentPermissionNode;

    @JsonIgnore
    @OneToMany(mappedBy = "parentPermissionNode", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PermissionNode> childPermissionNodeList;

    @JsonIgnore
    @ManyToMany(mappedBy = "permissionNodeList", cascade = CascadeType.REMOVE)
    private List<UserGroup> userGroupList;

    @JsonIgnore
    @ManyToMany(mappedBy = "permissionNodeList", cascade = CascadeType.REMOVE)
    private List<User> userList;

    public PermissionNode() { }

    /**
     * Constructor used by Permission Node init method
     */
    public PermissionNode(String name, PermissionNode parentPermissionNode) {
        this.name = name;
        this.parentPermissionNode = parentPermissionNode;
    }

    @Override
    @JsonGetter("full_name")
    public String toString() {
        return ((parentPermissionNode != null) ? parentPermissionNode + "." : "") + name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PermissionNode getParentPermissionNode() {
        return parentPermissionNode;
    }

    public void setParentPermissionNode(PermissionNode parentPermissionNode) {
        this.parentPermissionNode = parentPermissionNode;
    }
}
