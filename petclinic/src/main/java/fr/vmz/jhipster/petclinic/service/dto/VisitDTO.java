package fr.vmz.jhipster.petclinic.service.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class VisitDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private LocalDate visitDate;

    private String description;

    private Long petId;

    private String petName;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) { this.id = id; }

    public LocalDate getVisitDate() {
        return this.visitDate;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPetId() {
        return this.petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Visit{" +
            "id=" + getId() +
            ", visitDate='" + getVisitDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", petName='" + getPetName() + "'" +
            ", petId=" + getPetId() +
            "}";
    }
}
