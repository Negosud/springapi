package fr.negosud.springapi.api.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
final public class PermissionNode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Long permissionNodeId;

    @NotNull
    @NotBlank
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_permission_node_id")
    private PermissionNode parentPermissionNode;

    @OneToMany(mappedBy = "parentPermissionNode")
    private List<PermissionNode> childPermissionNodeList;

    @ManyToMany(mappedBy = "permissionNodeList")
    private List<UserGroup> userGroupList;

    @ManyToMany(mappedBy = "permissionNodeList")
    private List<User> userList;

    public PermissionNode() { }

    public PermissionNode(String name, PermissionNode parentPermissionNode) {
        this.name = name;
        this.parentPermissionNode = parentPermissionNode;
    }

    public PermissionNode(Long permissionNodeId, String name, PermissionNode parentPermissionNode, List<UserGroup> userGroupList, List<User> userList) {
        this.permissionNodeId = permissionNodeId;
        this.name = name;
        this.parentPermissionNode = parentPermissionNode;
        this.userGroupList = userGroupList;
        this.userList = userList;
    }

    @Override
    public String toString() {
        return ((parentPermissionNode != null) ? parentPermissionNode + "." : "") + name;
    }

    public Long getPermissionNodeId() {
        return permissionNodeId;
    }

    public void setPermissionNodeId(Long permissionNodeId) {
        this.permissionNodeId = permissionNodeId;
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
