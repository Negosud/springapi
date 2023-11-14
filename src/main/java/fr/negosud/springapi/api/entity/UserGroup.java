package fr.negosud.springapi.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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

    public UserGroup() { }

    public UserGroup(Long userGroupId, String name, UserGroup childUserGroup) {
        this.userGroupId = userGroupId;
        this.name = name;
        this.childUserGroup = childUserGroup;
    }
}
