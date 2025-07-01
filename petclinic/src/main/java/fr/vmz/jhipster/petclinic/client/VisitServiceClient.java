package fr.vmz.jhipster.petclinic.client;

import fr.vmz.jhipster.petclinic.service.dto.VisitDTO;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;


@Component
public class VisitServiceClient {

    private static final Set<String> AUTHORIZED_SORTS = Set.of("id", "description", "visitDate");
    private final WebClient.Builder webClientBuilder;
    private final DiscoveryClient discoveryClient;

    public VisitServiceClient(DiscoveryClient discoveryClient, WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
        this.discoveryClient = discoveryClient;
    }

    public Mono<ResponseEntity<Flux<VisitDTO>>> getVisits(ServerHttpRequest httpRequest) {
        Optional<ServiceInstance> instanceOptional = discoveryClient
            .getInstances("visit")
            .stream()
            .findFirst();
        return instanceOptional
            .map(serviceInstance -> webClientBuilder
                .build()
                .get()
                .uri(
                    uriBuilder -> uriBuilder
                        .scheme("http")
                        .host(serviceInstance.getHost())
                        .port(serviceInstance.getPort())
                        .path("/api/visits")
                        .queryParams(httpRequest.getQueryParams())
                        .build()
                )
                .headers(headers -> headers.addAll(httpRequest.getHeaders()))
                .retrieve()
                .toEntityFlux(VisitDTO.class)
            )
            .orElse(Mono.just(ResponseEntity.noContent().headers(httpRequest.getHeaders()).build()));
    }
}
