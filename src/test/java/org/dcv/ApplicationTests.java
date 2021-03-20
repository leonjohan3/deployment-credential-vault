package org.dcv;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.dcv.util.Constants.MEDIA_TYPE_TEXT_PLAIN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "debug=false",
                "logging.level.root=warn",
                "logging.level.org.dcv=debug",
                "spring.main.banner-mode=off"
//                "dcv.keystore=src/test/resources/keystore"
        })
//@AutoConfigureWebTestClient
public class ApplicationTests {

//    public final static String MEDIA_TYPE_TEXT_PLAIN = MediaType.TEXT_PLAIN_VALUE + CHARSET_UTF_8;

    @Autowired
    private WebTestClient client;

//    @LocalServerPort
//    private int port;

//        clientBuilder.setDefaultRequestConfig()
//    @BeforeEach
//    public void runBeforeEachMethod() {
//        final HttpAsyncClientBuilder clientBuilder = HttpAsyncClients.custom();
//        final CloseableHttpAsyncClient closeableHttpAsyncClient = clientBuilder.build();
//        final ClientHttpConnector connector = new HttpComponentsClientHttpConnector(closeableHttpAsyncClient);
//
//        client = WebTestClient.bindToServer(connector)
//                .baseUrl(String.format("http://localhost:%d/dcv", port))
//                .build();
//    }

    @Disabled
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

    @Disabled
    @Test
    public void shouldGetSecretKeyEntry() {

        // when : method to be checked invocation
        client.get().uri("/v1/read/group.id/artifact_id/secret_key_name")
//                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MEDIA_TYPE_TEXT_PLAIN)
                .expectBody(String.class).isEqualTo("bla");
//                .equals("bla");
    }

    @Disabled
    @Test
    public void shouldGetExportedSecretKeyEntries() {

        // when : method to be checked invocation
        client.get().uri("/v1/read/group.id/artifact_id")
//                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MEDIA_TYPE_TEXT_PLAIN)
                .expectBody(String.class).isEqualTo("bla");
//                .equals("bla");
    }
}
