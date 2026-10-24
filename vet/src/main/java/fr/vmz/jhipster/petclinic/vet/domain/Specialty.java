package fr.vmz.jhipster.petclinic.vet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Specialty.
 */
@Entity
@Table(name = "specialties")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Specialty implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 80)
    @Column(name = "name", length = 80, nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "specialtieses")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "specialtieses" }, allowSetters = true)
    private Set<Vet> vetses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Specialty id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Specialty name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Vet> getVetses() {
        return this.vetses;
    }

    public void setVetses(Set<Vet> vets) {
        if (this.vetses != null) {
            this.vetses.forEach(i -> i.removeSpecialties(this));
        }
        if (vets != null) {
            vets.forEach(i -> i.addSpecialties(this));
        }
        this.vetses = vets;
    }

    public Specialty vetses(Set<Vet> vets) {
        this.setVetses(vets);
        return this;
    }

    public Specialty addVets(Vet vet) {
        this.vetses.add(vet);
        vet.getSpecialtieses().add(this);
        return this;
    }

    public Specialty removeVets(Vet vet) {
        this.vetses.remove(vet);
        vet.getSpecialtieses().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Specialty)) {
            return false;
        }
        return getId() != null && getId().equals(((Specialty) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Specialty{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
