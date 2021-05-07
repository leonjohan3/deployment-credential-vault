package org.dcv.controller;

import org.jboss.resteasy.client.jaxrs.BasicAuthentication;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.lang.System.getProperty;
import static java.nio.file.Files.readAllBytes;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import static org.dcv.util.TestConstants.IT_TEST_URL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.AnyOf.anyOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.core.StringContains.containsStringIgnoringCase;

//@Disabled
public class EntryReaderControllerIT {

    private static final Client client = ClientBuilder.newClient();
    private static final BasicAuthentication writerBasicAuthentication = new BasicAuthentication("writer", "password");
    private static final BasicAuthentication readerBasicAuthentication = new BasicAuthentication("reader", "password");
    private static String testUrl;

    static {
        try {
            testUrl = new URL(getProperty(IT_TEST_URL)).toString();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

//        @Disabled
    @Test
    public void shouldSuccessfullyGetSecretKeyEntry() {

        // given: all data (test fixture) preparation
        final String groupAndArtifactIds = "groupId/artifactId";
        final String secretKeyName = "secretKeyName";
        final String secretKeyValue = "secretKeyValue";
        WebTarget target = client.target(testUrl + "write/" + groupAndArtifactIds);
        target.register(writerBasicAuthentication);
        final MultivaluedMap<String, String> secretKeyNames = new MultivaluedHashMap<>();
        secretKeyNames.putSingle(secretKeyName, secretKeyValue);
        final Entity<Form> entity = Entity.form(secretKeyNames);

        try (final Response response = target.request().post(entity)) {
            assertThat(response.getStatus(), is(NO_CONTENT.getStatusCode()));
        }

        target = client.target(testUrl + "read/" + groupAndArtifactIds + "/" + secretKeyName);
        target.register(readerBasicAuthentication);

        // when : method to be checked invocation
        try (final Response response = target.request().get()) {

            // then : checks and assertions
            assertThat(response.getStatus(), is(OK.getStatusCode()));
            assertThat(response.readEntity(String.class), is(secretKeyValue));
        }
    }

//        @Disabled
    @Test
    public void shouldSupportSecretKeyNameStartingWithDot() {

        // given: all data (test fixture) preparation
        final String groupAndArtifactIds = "groupId/artifactId";
        final String secretKeyName = ".secretKeyName";
        final String secretKeyValue = "secretKeyValue";
        WebTarget target = client.target(testUrl + "write/" + groupAndArtifactIds + "/" + secretKeyName);
        target.register(writerBasicAuthentication);

        try (final Response response = target.request().delete()) {
            assertThat(response.getStatus(), anyOf(is(NO_CONTENT.getStatusCode()), is(NOT_FOUND.getStatusCode())));
        }

        target = client.target(testUrl + "write/" + groupAndArtifactIds);
        target.register(writerBasicAuthentication);
        final MultivaluedMap<String, String> secretKeyNames = new MultivaluedHashMap<>();
        secretKeyNames.putSingle(secretKeyName, secretKeyValue);
        final Entity<Form> entity = Entity.form(secretKeyNames);

        try (final Response response = target.request().post(entity)) {
            assertThat(response.getStatus(), is(NO_CONTENT.getStatusCode()));
        }

        target = client.target(testUrl + "read/" + groupAndArtifactIds + "/" + secretKeyName);
        target.register(readerBasicAuthentication);

        // when : method to be checked invocation
        try (final Response response = target.request().get()) {

            // then : checks and assertions
            assertThat(response.getStatus(), is(OK.getStatusCode()));
            assertThat(response.readEntity(String.class), is(secretKeyValue));
        }
    }

    //    @Disabled
    @Test
    public void shouldSuccessfullyGetSecretKeyEntries() {

        // given: all data (test fixture) preparation
        final String groupAndArtifactIds = "groupId/artifactId";
        final String secretKeyNameA = "secretKeyNameA";
        final String secretKeyNameB = "secretKeyNameB";
        final String secretKeyNameC = ".secretKeyNameC";
        final String secretKeyValueA = "secretKeyValueA";
        final String secretKeyValueB = "secretKeyValueB";
        final String secretKeyValueC = "secretKeyValueC";
        WebTarget target = client.target(testUrl + "write/" + groupAndArtifactIds);
        target.register(writerBasicAuthentication);
        final MultivaluedMap<String, String> secretKeyNames = new MultivaluedHashMap<>();
        secretKeyNames.putSingle(secretKeyNameA, secretKeyValueA);
        secretKeyNames.putSingle(secretKeyNameB, secretKeyValueB);
        secretKeyNames.putSingle(secretKeyNameC, secretKeyValueC);
        final Entity<Form> entity = Entity.form(secretKeyNames);

        try (final Response response = target.request().post(entity)) {
            assertThat(response.getStatus(), is(NO_CONTENT.getStatusCode()));
        }

        target = client.target(testUrl + "read/" + groupAndArtifactIds);
        target.register(readerBasicAuthentication);

        // when : method to be checked invocation
        try (final Response response = target.request().get()) {

            // then : checks and assertions
            assertThat(response.getStatus(), is(OK.getStatusCode()));
            assertThat(response.readEntity(String.class), allOf(containsString(secretKeyValueA), containsString(secretKeyValueB),
                    not(containsString(secretKeyValueC))));
        }
    }

//    @Disabled
    @Test
    public void shouldAllowMaxLengthForGroupIdAndOthers() {

        // given: all data (test fixture) preparation
        final String groupId = "groupId890groupId890groupId890groupId890groupId890groupId890groupId890groupId890groupId890groupId890groupId890groupId890groupId890groupId890groupId890groupId890";
        final String artifactId = "/artifactIdartifactIdartifactIdartifactIdartifactIdartifactIdartifactIdartifactIdartifactIdartifactIdartifactIdartifactIdartifactIdartifactIdartifactIdartifactId";

        WebTarget target = client.target(testUrl + "write/" + groupId + artifactId);
        target.register(writerBasicAuthentication);

        final String secretKeyName = "secretKeyName4567890secretKeyName4567890secretKeyName4567890secretKeyName4567890secretKeyName4567890secretKeyName4567890secretKeyName4567890secretKeyName4567890";
        final String secretKeyValue = "secretKeyValue";
        final MultivaluedMap<String, String> secretKeyNames = new MultivaluedHashMap<>();
        secretKeyNames.putSingle(secretKeyName, secretKeyValue);
        final Entity<Form> entity = Entity.form(secretKeyNames);

        // when : method to be checked invocation
        try (final Response response = target.request().post(entity)) {
            // then : checks and assertions
            assertThat(response.getStatus(), is(NO_CONTENT.getStatusCode()));
        }
    }

//    @Disabled
    @Test
    public void shouldValidateMaxSecretKeyLengthForGroupId() {

        // given: all data (test fixture) preparation
        final String groupId =
                "groupId890groupId890groupId890groupId890groupId890groupId890groupId890groupId890groupId890groupId890groupId890groupId890groupId890groupId890groupId890groupId890g";
        final String artifactId = "/artifact";

        WebTarget target = client.target(testUrl + "write/" + groupId + artifactId);
        target.register(writerBasicAuthentication);

        final String secretKeyName = "secretKeyName";
        final String secretKeyValue = "secretKeyValue";
        final MultivaluedMap<String, String> secretKeyNames = new MultivaluedHashMap<>();
        secretKeyNames.putSingle(secretKeyName, secretKeyValue);
        final Entity<Form> entity = Entity.form(secretKeyNames);

        // when : method to be checked invocation
        try (final Response response = target.request().post(entity)) {
            // then : checks and assertions
            assertThat(response.getStatus(), is(BAD_REQUEST.getStatusCode()));
        }
    }

//    @Disabled
    @Test
    public void shouldValidateMaxSecretKeyLengthForArtifactId() {

        // given: all data (test fixture) preparation
        final String groupId = "groupId";
        final String artifactId =
                "/artifactIdartifactIdartifactIdartifactIdartifactIdartifactIdartifactIdartifactIdartifactIdartifactIdartifactIdartifactIdartifactIdartifactIdartifactIdartifactIda";

        WebTarget target = client.target(testUrl + "write/" + groupId + artifactId);
        target.register(writerBasicAuthentication);

        final String secretKeyName = "secretKeyName";
        final String secretKeyValue = "secretKeyValue";
        final MultivaluedMap<String, String> secretKeyNames = new MultivaluedHashMap<>();
        secretKeyNames.putSingle(secretKeyName, secretKeyValue);
        final Entity<Form> entity = Entity.form(secretKeyNames);

        // when : method to be checked invocation
        try (final Response response = target.request().post(entity)) {
            // then : checks and assertions
            assertThat(response.getStatus(), is(BAD_REQUEST.getStatusCode()));
        }
    }

//    @Disabled
    @Test
    public void shouldValidateMaxSecretKeyLengthForSecretKeyName() {

        // given: all data (test fixture) preparation
        final String groupId = "groupId";
        final String artifactId = "/artifactId";

        WebTarget target = client.target(testUrl + "write/" + groupId + artifactId);
        target.register(writerBasicAuthentication);

        final String secretKeyName =
                "secretKeyName4567890secretKeyName4567890secretKeyName4567890secretKeyName4567890secretKeyName4567890secretKeyName4567890secretKeyName4567890secretKeyName4567890s";
        final String secretKeyValue = "secretKeyValue";
        final MultivaluedMap<String, String> secretKeyNames = new MultivaluedHashMap<>();
        secretKeyNames.putSingle(secretKeyName, secretKeyValue);
        final Entity<Form> entity = Entity.form(secretKeyNames);

        // when : method to be checked invocation
        try (final Response response = target.request().post(entity)) {
            // then : checks and assertions
            assertThat(response.getStatus(), is(BAD_REQUEST.getStatusCode()));
        }
    }

//    @Disabled
    @Test
    public void shouldValidateMaxSecretKeyLengthForSecretKeyValue() throws URISyntaxException, IOException {
        // given: all data (test fixture) preparation
        final String groupId = "groupId";
        final String artifactId = "/artifactId";

        WebTarget target = client.target(testUrl + "write/" + groupId + artifactId);
        target.register(writerBasicAuthentication);

        final String secretKeyName = "secretKeyName";
        final String secretKeyValue = new String(readAllBytes(Paths.get(getClass().getClassLoader().getResource("tooLargeSecretKeyValue.txt").toURI())));
        final MultivaluedMap<String, String> secretKeyNames = new MultivaluedHashMap<>();
        secretKeyNames.putSingle(secretKeyName, secretKeyValue);
        final Entity<Form> entity = Entity.form(secretKeyNames);

        // when : method to be checked invocation
        try (final Response response = target.request().post(entity)) {
            // then : checks and assertions
            assertThat(response.getStatus(), is(BAD_REQUEST.getStatusCode()));
        }
    }

//    @Disabled
    @Test
    public void shouldSupportLargeSecretKeyValue() throws URISyntaxException, IOException {

        // given: all data (test fixture) preparation
        final String groupId = "groupId";
        final String artifactId = "/artifactId";

        WebTarget target = client.target(testUrl + "write/" + groupId + artifactId);
        target.register(writerBasicAuthentication);

        final String secretKeyName = "secretKeyName";
        final String secretKeyValue = new String(readAllBytes(Paths.get(getClass().getClassLoader().getResource("largeSecretKeyValue.txt").toURI())));
        final MultivaluedMap<String, String> secretKeyNames = new MultivaluedHashMap<>();
        secretKeyNames.putSingle(secretKeyName, secretKeyValue);
        final Entity<Form> entity = Entity.form(secretKeyNames);

        // when : method to be checked invocation
        try (final Response response = target.request().post(entity)) {
            // then : checks and assertions
            assertThat(response.getStatus(), is(NO_CONTENT.getStatusCode()));
        }
    }

//    @Disabled
    @Test
    public void shouldValidateGroupIdForGetSecretKeyEntry() {

        // given: all data (test fixture) preparation
        final String invalidGroupId = "1groupId";
        final WebTarget target = client.target(testUrl + "read/" + invalidGroupId + "/artifactId/secretKeyName");
        target.register(readerBasicAuthentication);

        // when : method to be checked invocation
        try (final Response response = target.request().get()) {

            // then : checks and assertions
            assertThat(response.getStatus(), is(BAD_REQUEST.getStatusCode()));
            assertThat(response.readEntity(String.class), allOf(containsStringIgnoringCase("must match"), containsString(invalidGroupId)));
        }
    }

//    @Disabled
    @Test
    public void shouldValidateArtifactIdForGetSecretKeyEntry() {

        // given: all data (test fixture) preparation
        final String invalidArtifactId = "1artifactId";
        final WebTarget target = client.target(testUrl + "read/groupId/" + invalidArtifactId + "/secretKeyName");
        target.register(readerBasicAuthentication);

        // when : method to be checked invocation
        try (final Response response = target.request().get()) {

            // then : checks and assertions
            assertThat(response.getStatus(), is(BAD_REQUEST.getStatusCode()));
            assertThat(response.readEntity(String.class), allOf(containsStringIgnoringCase("must match"), containsString(invalidArtifactId)));
        }
    }

//    @Disabled
    @Test
    public void shouldValidateSecretKeyNameForGetSecretKeyEntry() {

        // given: all data (test fixture) preparation
        final String invalidSecretKeyName = "1secretKeyName";
        final WebTarget target = client.target(testUrl + "read/groupId/artifactId/" + invalidSecretKeyName);
        target.register(readerBasicAuthentication);

        // when : method to be checked invocation
        try (final Response response = target.request().get()) {

            // then : checks and assertions
            assertThat(response.getStatus(), is(BAD_REQUEST.getStatusCode()));
            assertThat(response.readEntity(String.class), allOf(containsStringIgnoringCase("must match"), containsString(invalidSecretKeyName)));
        }
    }

//    @Disabled
    @Test
    public void shouldValidateGroupIdForGetSecretKeyEntries() {

        // given: all data (test fixture) preparation
        final String invalidGroupId = "1groupId";
        final WebTarget target = client.target(testUrl + "read/" + invalidGroupId + "/artifactId");
        target.register(readerBasicAuthentication);

        // when : method to be checked invocation
        try (final Response response = target.request().get()) {

            // then : checks and assertions
            assertThat(response.getStatus(), is(BAD_REQUEST.getStatusCode()));
            assertThat(response.readEntity(String.class), allOf(containsStringIgnoringCase("must match"), containsString(invalidGroupId)));
        }
    }

//    @Disabled
    @Test
    public void shouldValidateArtifactIdForGetSecretKeyEntries() {

        // given: all data (test fixture) preparation
        final String invalidArtifactId = "1artifactId";
        final WebTarget target = client.target(testUrl + "read/groupId/" + invalidArtifactId);
        target.register(readerBasicAuthentication);

        // when : method to be checked invocation
        try (final Response response = target.request().get()) {

            // then : checks and assertions
            assertThat(response.getStatus(), is(BAD_REQUEST.getStatusCode()));
            assertThat(response.readEntity(String.class), allOf(containsStringIgnoringCase("must match"), containsString(invalidArtifactId)));
        }
    }

}
