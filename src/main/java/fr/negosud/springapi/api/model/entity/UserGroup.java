package fr.negosud.springapi.api.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
public class UserGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Long userGroupId;

    @NotNull
    @NotBlank
    private String name;

    @ManyToOne
    @JoinColumn(name = "child_user_group_id")
    private UserGroup childUserGroup;

    @ManyToMany
    @JoinTable(
            name = "user_group_permission_node",
            joinColumns = @JoinColumn(name = "user_group_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_node_id")
    )
    private List<PermissionNode> permissionNodeList;

    public UserGroup() { }

    public UserGroup(Long userGroupId, String name, UserGroup childUserGroup, List<PermissionNode> permissionNodeList) {
        this.userGroupId = userGroupId;
        this.name = name;
        this.childUserGroup = childUserGroup;
        this.permissionNodeList = permissionNodeList;
    }

    public Long getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Long userGroupId) {
        this.userGroupId = userGroupId;
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
}
