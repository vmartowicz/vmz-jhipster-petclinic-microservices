package fr.vmz.jhipster.petclinic.vet.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class VetCriteriaTest {

    @Test
    void newVetCriteriaHasAllFiltersNullTest() {
        var vetCriteria = new VetCriteria();
        assertThat(vetCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void vetCriteriaFluentMethodsCreatesFiltersTest() {
        var vetCriteria = new VetCriteria();

        setAllFilters(vetCriteria);

        assertThat(vetCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void vetCriteriaCopyCreatesNullFilterTest() {
        var vetCriteria = new VetCriteria();
        var copy = vetCriteria.copy();

        assertThat(vetCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(vetCriteria)
        );
    }

    @Test
    void vetCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var vetCriteria = new VetCriteria();
        setAllFilters(vetCriteria);

        var copy = vetCriteria.copy();

        assertThat(vetCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(vetCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var vetCriteria = new VetCriteria();

        assertThat(vetCriteria).hasToString("VetCriteria{}");
    }

    private static void setAllFilters(VetCriteria vetCriteria) {
        vetCriteria.id();
        vetCriteria.firstName();
        vetCriteria.lastName();
        vetCriteria.specialtiesId();
        vetCriteria.distinct();
    }

    private static Condition<VetCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFirstName()) &&
                condition.apply(criteria.getLastName()) &&
                condition.apply(criteria.getSpecialtiesId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<VetCriteria> copyFiltersAre(VetCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFirstName(), copy.getFirstName()) &&
                condition.apply(criteria.getLastName(), copy.getLastName()) &&
                condition.apply(criteria.getSpecialtiesId(), copy.getSpecialtiesId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
