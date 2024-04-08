package fr.negosud.springapi.model.entity;

import com.fasterxml.jackson.annotation.*;
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
    private List<PermissionNode> permissionNodeList;

    public UserGroup() { }

    /**
     * Constructor used by User Group init method
     */
    public UserGroup(String name, UserGroup childUserGroup, List<PermissionNode> permissionNodeList) {
        this.name = name;
        this.childUserGroup = childUserGroup;
        this.permissionNodeList = permissionNodeList;
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

    public List<PermissionNode> getPermissionNodeList() {
        return permissionNodeList;
    }

    public void setPermissionNodeList(List<PermissionNode> permissionNodeList) {
        this.permissionNodeList = permissionNodeList;
    }

    @JsonIdentityReference(alwaysAsId = true)
    @JsonGetter("permissionList")
    public List<PermissionNode> getFullPermissionNodeList() {
        List<PermissionNode> fullPermissionNodeList = new ArrayList<>(this.permissionNodeList);
        if (this.childUserGroup != null)
            fullPermissionNodeList.addAll(this.childUserGroup.getFullPermissionNodeList());
        return this.cleanPermissionNodeList(fullPermissionNodeList);
    }

    /**
     * Don't look at this method : it's just a piece of french engineering right here
     * It does the job still...
     */
    private List<PermissionNode> cleanPermissionNodeList(List<PermissionNode> permissionNodeList) {
        List<PermissionNode> cleanPermissionNodeList = new ArrayList<>(permissionNodeList);
        for (PermissionNode cleanedPermissionNode : permissionNodeList) {
            PermissionNode checkedPermissionNode = cleanedPermissionNode;
            while (checkedPermissionNode.getParentPermissionNode() != null) {
                if (permissionNodeList.contains(checkedPermissionNode.getParentPermissionNode())) {
                    cleanPermissionNodeList.remove(cleanedPermissionNode);
                    break;
                } else {
                    checkedPermissionNode = checkedPermissionNode.getParentPermissionNode();
                }
            }
        }
        return cleanPermissionNodeList;
    }
}
