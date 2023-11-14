package fr.negosud.springapi.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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

    public PermissionNode() { }

    public PermissionNode(String name, PermissionNode parentPermissionNode) {
        this.name = name;
        this.parentPermissionNode = parentPermissionNode;
    }

    @Override
    public String toString() {
        return ((parentPermissionNode != null) ? parentPermissionNode + "." : "") + name;
    }
}
