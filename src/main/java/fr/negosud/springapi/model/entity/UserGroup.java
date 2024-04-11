package fr.negosud.springapi.model.entity;

import com.fasterxml.jackson.annotation.*;
import fr.negosud.springapi.util.PermissionNodes;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "\"user_group\"")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class UserGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    @NotBlank
    @Column(unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "child_user_group_id")
    @JsonIgnore
    private UserGroup childUserGroup;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_group_permission_node",
            joinColumns = @JoinColumn(name = "user_group_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_node_id")
    )
    @JsonIgnore
    private List<PermissionNode> permissionNodes;

    public UserGroup() { }

    /**
     * Constructor used by User Group init method
     */
    public UserGroup(String name, UserGroup childUserGroup, List<PermissionNode> permissionNodes) {
        this.name = name;
        this.childUserGroup = childUserGroup;
        this.permissionNodes = permissionNodes;
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
        return this.name;
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

    public UserGroup getChildUserGroup() {
        return childUserGroup;
    }

    public void setChildUserGroup(UserGroup childUserGroup) {
        this.childUserGroup = childUserGroup;
    }

    public List<PermissionNode> getPermissionNodes() {
        return permissionNodes;
    }

    public void setPermissionNodes(List<PermissionNode> permissionNodeList) {
        this.permissionNodes = permissionNodeList;
    }

    @JsonIdentityReference(alwaysAsId = true)
    @JsonGetter("permissions")
    public List<PermissionNode> getCleanedPermissionNodes() {
        List<PermissionNode> cleanedPermissionNodes = new ArrayList<>(this.permissionNodes);
        if (this.childUserGroup != null)
            cleanedPermissionNodes.addAll(this.childUserGroup.getCleanedPermissionNodes());
        return PermissionNodes.cleanPermissionNodeList(cleanedPermissionNodes);
    }
}
