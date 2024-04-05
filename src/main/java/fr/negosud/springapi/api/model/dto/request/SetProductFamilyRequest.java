package fr.negosud.springapi.api.model.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

public class SetProductFamilyRequest {

    @NotBlank
    @Column(length = 100)
    private String name;

    @Column(length = 1000)
    private String description;

    public SetProductFamilyRequest() { }

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
}
