package fr.negosud.springapi.api.model.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

public class SetProductTransactionTypeRequest {

    @NotBlank
    @Column(length = 100)
    private String name;

    @Column(length = 1000)
    private String description;

    @NotBlank
    private boolean isEntry;

    public SetProductTransactionTypeRequest() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEntry() {
        return isEntry;
    }

    public void setEntry(boolean entry) {
        isEntry = entry;
    }
}
