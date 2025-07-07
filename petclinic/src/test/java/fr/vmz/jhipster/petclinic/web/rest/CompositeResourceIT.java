package fr.vmz.jhipster.petclinic.web.rest;

import fr.vmz.jhipster.petclinic.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.wiremock.spring.EnableWireMock;

import java.net.URI;
import java.util.List;
import java.util.stream.IntStream;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static fr.vmz.jhipster.petclinic.web.rest.AccountResourceIT.TEST_USER_LOGIN;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Integration tests for the {@link CompositeResource} REST controller.
 */
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_TIMEOUT)
@IntegrationTest
@EnableWireMock
class CompositeResourceIT {

    @Value("${wiremock.server.baseUrl}")
    private String wireMockUrl;

    @Value("${wiremock.server.port}")
    private int wireMockPort;

    @MockitoBean
    private DiscoveryClient discoveryClient;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setup() {

        ServiceInstance si = mock(ServiceInstance.class);
        when(si.getHost()).thenReturn("localhost");
        when(si.getPort()).thenReturn(wireMockPort);
        when(discoveryClient.getInstances(anyString())).thenReturn(List.of(si));

        stubFor(
            get(urlPathEqualTo("/api/visits"))
                .withQueryParam("page", equalTo("0"))
                .withQueryParam("size", equalTo("20"))
                .withQueryParam("sort", equalTo("id,asc"))
                .willReturn(
                    okJson("[" +
                            "{\"id\":1,\"petId\":1,\"visitDate\":\"2021-10-01T00:00:00Z\",\"description\":\"Visit 1\"}," +
                            "{\"id\":2,\"petId\":2,\"visitDate\":\"2022-10-01T00:00:00Z\",\"description\":\"Visit 2\"}" +
                        "]")
                )
        );

        IntStream.range(1,3).forEach(id -> {
            stubFor(
                get(urlPathTemplate("/api/pets/{id}"))
                    .withPathParam("id", equalTo(String.valueOf(id)))
                    .willReturn(
                        okJson("{\"id\":" + id + ",\"name\":\"PetName " + id + "\",\"type\":\"Dog\",\"birthDate\":\"202" + id + "-01-01T00:00:00Z\"}")
                    )
            );
        });
    }

    @Test
    @WithMockUser(TEST_USER_LOGIN)
    void shouldGetAllVisits() {
        webTestClient
            .get()
            .uri(uriBuilder ->
                uriBuilder
                    .path("/api/composite/visits")
                    .queryParam("page", "0")
                    .queryParam("size", "20")
                    .queryParam("sort", "id,asc")
                    .build())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .expectBody()

            .jsonPath("$.[*].id").value(hasItem(1))
            .jsonPath("$.[*].petId").value(hasItem(1))
            .jsonPath("$.[*].description").value(hasItem("Visit 1"))
            .jsonPath("$.[*].visitDate").value(hasItem("2021-10-01"))
            .jsonPath("$.[*].petName").value(hasItem("PetName 1"))

            .jsonPath("$.[*].id").value(hasItem(2))
            .jsonPath("$.[*].petId").value(hasItem(2))
            .jsonPath("$.[*].description").value(hasItem("Visit 2"))
            .jsonPath("$.[*].visitDate").value(hasItem("2022-10-01"))
            .jsonPath("$.[*].petName").value(hasItem("PetName 2"))
        ;


    }

}
