package fr.negosud.springapi.model.dto.response.audit;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import fr.negosud.springapi.model.entity.User;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public abstract class FullAuditedResponse {

    @NotNull
    protected Date createdAt;

    @NotNull
    @JsonIdentityReference(alwaysAsId = true)
    protected User createdBy;

    @NotNull
    protected Date modifiedAt;

    @NotNull
    @JsonIdentityReference(alwaysAsId = true)
    protected User modifiedBy;

    public FullAuditedResponse() { }

    public Date getCreatedAt() {
        return createdAt;
    }

    public FullAuditedResponse setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public FullAuditedResponse setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public FullAuditedResponse setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
        return this;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public FullAuditedResponse setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }
}
