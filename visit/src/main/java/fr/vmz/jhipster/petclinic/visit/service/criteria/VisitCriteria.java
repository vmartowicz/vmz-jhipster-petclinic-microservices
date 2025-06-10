package fr.vmz.jhipster.petclinic.visit.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.vmz.jhipster.petclinic.visit.domain.Visit} entity. This class is used
 * in {@link fr.vmz.jhipster.petclinic.visit.web.rest.VisitResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /visits?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VisitCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter visitDate;

    private StringFilter description;

    private LongFilter petId;

    private Boolean distinct;

    public VisitCriteria() {}

    public VisitCriteria(VisitCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.visitDate = other.optionalVisitDate().map(LocalDateFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.petId = other.optionalPetId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public VisitCriteria copy() {
        return new VisitCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getVisitDate() {
        return visitDate;
    }

    public Optional<LocalDateFilter> optionalVisitDate() {
        return Optional.ofNullable(visitDate);
    }

    public LocalDateFilter visitDate() {
        if (visitDate == null) {
            setVisitDate(new LocalDateFilter());
        }
        return visitDate;
    }

    public void setVisitDate(LocalDateFilter visitDate) {
        this.visitDate = visitDate;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LongFilter getPetId() {
        return petId;
    }

    public Optional<LongFilter> optionalPetId() {
        return Optional.ofNullable(petId);
    }

    public LongFilter petId() {
        if (petId == null) {
            setPetId(new LongFilter());
        }
        return petId;
    }

    public void setPetId(LongFilter petId) {
        this.petId = petId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final VisitCriteria that = (VisitCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(visitDate, that.visitDate) &&
            Objects.equals(description, that.description) &&
            Objects.equals(petId, that.petId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, visitDate, description, petId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VisitCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalVisitDate().map(f -> "visitDate=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalPetId().map(f -> "petId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
