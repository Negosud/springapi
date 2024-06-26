package fr.negosud.springapi.model.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "\"permission_node\"")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "full_name")
public class PermissionNode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private String name;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parent_permission_node_id")
    private PermissionNode parentPermissionNode;

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
