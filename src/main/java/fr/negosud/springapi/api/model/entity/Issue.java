package fr.negosud.springapi.api.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long issueId;

    @NotBlank
    @Column(nullable = false, length = 20)
    private String reference;

    @NotBlank
    @Column(nullable = false)
    private Boolean active;

    @NotBlank
    @Column(nullable = false, length = 1000)
    private String description;

    public Issue() {
    }

    public Issue(Long issueId, String reference, Boolean active, String description) {
        this.issueId = issueId;
        this.reference = reference;
        this.active = active;
        this.description = description;
    }

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
