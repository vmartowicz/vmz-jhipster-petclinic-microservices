package fr.vmz.jhipster.petclinic.client;

import fr.vmz.jhipster.petclinic.service.dto.PetDTO;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class CustomersServiceClient {

    private final WebClient.Builder webClientBuilder;
    private final DiscoveryClient discoveryClient;

    public CustomersServiceClient(DiscoveryClient discoveryClient, WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
        this.discoveryClient = discoveryClient;
    }

    public Mono<PetDTO> getPet(ServerHttpRequest httpRequest, Long petId) {
        Optional<ServiceInstance> instanceOptional = discoveryClient
            .getInstances("customer")
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
                        .path("/api/pets/{id}")
                        .build(petId)
                )
                .headers(headers -> headers.addAll(httpRequest.getHeaders()))
                .retrieve()
                .bodyToMono(PetDTO.class))
            .orElseGet(Mono::empty);
    }
}
