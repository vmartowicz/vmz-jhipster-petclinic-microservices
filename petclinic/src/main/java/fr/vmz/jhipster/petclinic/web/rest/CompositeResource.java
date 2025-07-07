package fr.vmz.jhipster.petclinic.web.rest;

import fr.vmz.jhipster.petclinic.client.CustomersServiceClient;
import fr.vmz.jhipster.petclinic.client.VisitServiceClient;
import fr.vmz.jhipster.petclinic.service.dto.PetDTO;
import fr.vmz.jhipster.petclinic.service.dto.VisitDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static java.util.Objects.nonNull;

/**
 * REST controller for managing Composite request.
 */
@RestController
@RequestMapping("/api/composite")
public class CompositeResource {
    private static final Logger LOG = LoggerFactory.getLogger(CompositeResource.class);
    private final VisitServiceClient visitServiceClient;
    private final CustomersServiceClient customersServiceClient;
    private final ReactiveCircuitBreakerFactory cbFactory;

    public CompositeResource(VisitServiceClient visitServiceClient,
                             CustomersServiceClient customersServiceClient,
                             ReactiveCircuitBreakerFactory cbFactory) {
        this.visitServiceClient = visitServiceClient;
        this.customersServiceClient = customersServiceClient;
        this.cbFactory = cbFactory;
    }

    /**
     * {@code GET  /visits} : get the visits.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the list of visits.
     */
    @GetMapping("/visits")
    public Mono<ResponseEntity<Flux<VisitDTO>>> findAllVisits(ServerHttpRequest httpRequest, @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get all composite Visits");
        return visitServiceClient
            .getVisits(httpRequest)
            .map(responseEntity -> {
                    // Récupérer les headers du service downstream
                    HttpHeaders visitHeaders = responseEntity.getHeaders();

                    // Modifier le flux pour ajouter petName à chaque visite
                    Flux<VisitDTO> visits =
                        responseEntity
                            .getBody()
                            .flatMap(
                                visit ->
                                    customersServiceClient
                                        .getPet(httpRequest, visit.getPetId())
                                        .transform(this::getEmptyPetDTOWhenThrowable)
                                        .map(addPetNameToVisit(visit))

                            )
                            .sort((VisitDTO left, VisitDTO right) -> sortByPetName(left, right, pageable));
                    return ResponseEntity.ok().headers(visitHeaders).body(visits);
                }
            );
    }

    private int sortByPetName(VisitDTO left, VisitDTO right, Pageable pageable) {
        Sort.Order orderForPetName = pageable.getSort().getOrderFor("petName");
        if (nonNull(orderForPetName)) {
            return orderForPetName.getDirection().isAscending() ? sortByPetName(left, right) : sortByPetName(right, left);
        }
        return 0;
    }

    private int sortByPetName(VisitDTO left, VisitDTO right) {
        return left.getPetName().compareTo(right.getPetName());
    }

    private Mono<PetDTO> getEmptyPetDTOWhenThrowable(Mono<PetDTO> it) {
        ReactiveCircuitBreaker cb = cbFactory.create("findAllVisits");
        return cb.run(it, throwable -> {
                LOG.error("{}", throwable.getMessage());
                return Mono.just(new PetDTO());
            }
        );
    }

    private Function<PetDTO, VisitDTO> addPetNameToVisit(VisitDTO visit) {
        return petDTO -> {
            visit.setPetName(petDTO.getName());
            return visit;
        };
    }

}
