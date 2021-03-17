package org.dcv;

import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClientBuilder;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.HttpComponentsClientHttpConnector;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "debug=false",
                "logging.level.root=info",
                "logging.level.org.dcv=warn",
                "spring.main.banner-mode=off",
        })

public class ApplicationTests {

    private WebTestClient client;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void runBeforeEachMethod() {
        final HttpAsyncClientBuilder clientBuilder = HttpAsyncClients.custom();
//        clientBuilder.setDefaultRequestConfig()
        final CloseableHttpAsyncClient closeableHttpAsyncClient = clientBuilder.build();
        final ClientHttpConnector connector = new HttpComponentsClientHttpConnector(closeableHttpAsyncClient);

        client = WebTestClient.bindToServer(connector)
                .baseUrl(String.format("http://localhost:%d/dcv", port))
                .build();
    }

    @Test
    public void should() {
        // given: all data (test fixture) preparation
        // when : method to be checked invocation
        client.get().uri("/ping")
//                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8")
                .expectBody().equals("pong");

        // then : checks and assertions
        assertThat(1 + 1, is(2));

    }
}
