package org.dcv.controller;

import org.dcv.util.TestConstants;
import org.jboss.resteasy.client.jaxrs.BasicAuthentication;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.System.getProperty;
import static javax.ws.rs.core.HttpHeaders.LOCATION;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.MOVED_PERMANENTLY;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static org.dcv.util.Constants.ERROR_MESSAGE_HEADER_NAME;
import static org.dcv.util.TestConstants.IT_TEST_URL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.core.StringContains.containsStringIgnoringCase;

//@Disabled
//@ExtendWith(MockitoExtension.class)
public class EntryWriterControllerIT {

    private static final Client client = ClientBuilder.newClient();
    private static final BasicAuthentication basicAuthentication = new BasicAuthentication("writer", "password");
    private static String testUrl;

    static {
        try {
            testUrl = new URL(getProperty(IT_TEST_URL)).toString();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }


//    @Mock
//    private MyWebResource myWebResource;

    //        final MyWebResource myWebResource = new MyWebResource();
//        final String name = "name";

//    @Test
//    @Disabled
//    public void should() {
//
//        // given: all data (test fixture) preparation
//        final Client client = ClientBuilder.newClient();
//        final WebTarget target = client.target("http://localhost/dcv/v1/hello?name=johan");
//
//        // when : method to be checked invocation
//        try (final Response response = target.request().get()) {
//            // then : checks and assertions
//            assertThat(response.getStatus(), is(HttpStatus.SC_OK));
//            assertThat(response.readEntity(String.class), is("hello johan"));
//        }
//
//    }

    //        final String result = myWebResource.hello(name);
//        assertThat(result, is("hello " + name));

//    @Disabled
    @Test
    public void shouldValidateGroupIdForSetSecretKeyEntry() {

        // given: all data (test fixture) preparation
        final String invalidGroupId = "1groupId";
        final WebTarget target = client.target(testUrl + "write/" + invalidGroupId + "/ar_ti.fact-Id");
        target.register(basicAuthentication);
        final MultivaluedMap<String, String> secretKeyNames = new MultivaluedHashMap<>();
        secretKeyNames.putSingle("abc", "def");
        final Entity<Form> entity = Entity.form(secretKeyNames);

        // when : method to be checked invocation
        try (final Response response = target.request().post(entity)) {

            // then : checks and assertions
            assertThat(response.getStatus(), is(BAD_REQUEST.getStatusCode()));
            assertThat(response.getHeaderString(ERROR_MESSAGE_HEADER_NAME), containsString(invalidGroupId));
            assertThat(response.readEntity(String.class), allOf(containsStringIgnoringCase("must match"), containsString(invalidGroupId)));
        }
    }

//    @Disabled
    @Test
    public void shouldValidateArtifactIdForSetSecretKeyEntry() {

        // given: all data (test fixture) preparation
        final String invalidArtifactId = "1artifactId";
        final WebTarget target = client.target(testUrl + "write/gr.ou_p-Id/" + invalidArtifactId);
        target.register(basicAuthentication);
        final MultivaluedMap<String, String> secretKeyNames = new MultivaluedHashMap<>();
        secretKeyNames.putSingle("abc", "def");
        final Entity<Form> entity = Entity.form(secretKeyNames);

        // when : method to be checked invocation
        try (final Response response = target.request().post(entity)) {

            // then : checks and assertions
            assertThat(response.getStatus(), is(BAD_REQUEST.getStatusCode()));
            assertThat(response.getHeaderString(ERROR_MESSAGE_HEADER_NAME), containsString(invalidArtifactId));
            assertThat(response.readEntity(String.class), allOf(containsStringIgnoringCase("must match"), containsString(invalidArtifactId)));
        }
    }

//    @Disabled
    @Test
    public void shouldValidateSecretKeyEntryForSetSecretKeyEntry() {

        // given: all data (test fixture) preparation
        final String invalidEntryName =
                "e1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
        final WebTarget target = client.target(testUrl + "write/groupId/artifactId");
        target.register(basicAuthentication);
        final MultivaluedMap<String, String> secretKeyNames = new MultivaluedHashMap<>();
        secretKeyNames.putSingle(invalidEntryName, "def");
        final Entity<Form> entity = Entity.form(secretKeyNames);

        // when : method to be checked invocation
        try (final Response response = target.request().post(entity)) {

            // then : checks and assertions
            assertThat(response.getStatus(), is(BAD_REQUEST.getStatusCode()));
            assertThat(response.getHeaderString(ERROR_MESSAGE_HEADER_NAME), containsString("size must be between"));
            assertThat(response.readEntity(String.class), allOf(containsStringIgnoringCase("javax.validation.constraints.Size"), containsString(
                    "secretKeyName")));
        }
    }

    @Test
    public void shouldSuccessfullyWriteEntry() {

        // given: all data (test fixture) preparation
        final WebTarget target = client.target(testUrl + "write/groupId/artifactId");
        target.register(basicAuthentication);
        MultivaluedMap<String, String> secretKeyNames = new MultivaluedHashMap<>();
        secretKeyNames.putSingle("abc", "def");
        secretKeyNames.putSingle("ghi", "jkl");
        Entity<Form> entity = Entity.form(secretKeyNames);
//        final Entity<Form> entity = Entity.form((MultivaluedMap<String, String>) null);

        // when : method to be checked invocation
        try (final Response response = target.request().post(entity)) {

            // then : checks and assertions
//            assertThat(response.readEntity(String.class), is("string,"));
            assertThat(response.getStatus(), is(NO_CONTENT.getStatusCode()));
//            assertThat(response.readEntity(String.class), allOf(containsStringIgnoringCase("must match"), containsString(invalidArtifactId)));
        }
//        secretKeyNames = new MultivaluedHashMap<>();
//        secretKeyNames.putSingle("now", "def");
//        entity = Entity.form(secretKeyNames);

//        try (final Response response = target.request().post(entity)) {
//
//            // then : checks and assertions
//            assertThat(response.getStatus(), is(BAD_REQUEST.getStatusCode()));
//            assertThat(response.getHeaderString(ERROR_MESSAGE_HEADER_NAME), containsString("bla"));
////            assertThat(response.getStatus(), is(OK.getStatusCode()));
////            assertThat(response.readEntity(String.class), is("string,"));
//        }
//            assertThat(response.readEntity(String.class), allOf(containsStringIgnoringCase("must match"), containsString(invalidArtifactId)));
    }

//    @Disabled
    @Test
    public void shouldRequireHttpsAndNotJustHttp() {

        // given: all data (test fixture) preparation
        final WebTarget target = client.target(testUrl.replaceFirst("https", "http") + "write/groupId/artifactId");
        target.register(basicAuthentication);
        MultivaluedMap<String, String> secretKeyNames = new MultivaluedHashMap<>();
        secretKeyNames.putSingle("abc", "def");
        secretKeyNames.putSingle("ghi", "jkl");
        Entity<Form> entity = Entity.form(secretKeyNames);
//        final Entity<Form> entity = Entity.form((MultivaluedMap<String, String>) null);

        // when : method to be checked invocation
        try (final Response response = target.request().post(entity)) {

            // then : checks and assertions
//            assertThat(response.readEntity(String.class), is("string,"));
            assertThat(response.getStatus(), is(MOVED_PERMANENTLY.getStatusCode()));
            assertThat(response.getHeaderString(LOCATION), is(testUrl + "write/groupId/artifactId"));
//            assertThat(response.readEntity(String.class), allOf(containsStringIgnoringCase("must match"), containsString(invalidArtifactId)));
        }
//        secretKeyNames = new MultivaluedHashMap<>();
//        secretKeyNames.putSingle("now", "def");
//        entity = Entity.form(secretKeyNames);

//        try (final Response response = target.request().post(entity)) {
//
//            // then : checks and assertions
//            assertThat(response.getStatus(), is(BAD_REQUEST.getStatusCode()));
//            assertThat(response.getHeaderString(ERROR_MESSAGE_HEADER_NAME), containsString("bla"));
////            assertThat(response.getStatus(), is(OK.getStatusCode()));
////            assertThat(response.readEntity(String.class), is("string,"));
//        }
//            assertThat(response.readEntity(String.class), allOf(containsStringIgnoringCase("must match"), containsString(invalidArtifactId)));
    }

//    @Disabled
    @Test
    public void shouldRequireValidUsernameAndPassword() {
        // given: all data (test fixture) preparation
        final WebTarget target = client.target(testUrl + "write/groupId/artifactId");
//        target.register(basicAuthentication);
        MultivaluedMap<String, String> secretKeyNames = new MultivaluedHashMap<>();
        secretKeyNames.putSingle("abcf", "def");
        Entity<Form> entity = Entity.form(secretKeyNames);
//        final Entity<Form> entity = Entity.form((MultivaluedMap<String, String>) null);

        // when : method to be checked invocation
        try (final Response response = target.request().post(entity)) {

            // then : checks and assertions
//            assertThat(response.readEntity(String.class), is("string,"));
            assertThat(response.getStatus(), is(UNAUTHORIZED.getStatusCode()));
//            assertThat(response.readEntity(String.class), allOf(containsStringIgnoringCase("must match"), containsString(invalidArtifactId)));
        }
    }

//    @Disabled
    @Test
    public void shouldSuccessfullyRemoveSingleEntry() {

        // given: all data (test fixture) preparation
//        final Credentials credentials = new UsernamePasswordCredentials("writer", "password");
//        final BasicAuthentication basicAuthentication = new BasicAuthentication("writer", "password");
        final String secretKeyName = "removeSingleEntryTest";
//        client.get

//        final DefaultHttpClient httpClient = new DefaultHttpClient();
//        final ApacheHttpClient43Engine engine = new ApacheHttpClient43Engine(httpClient, new HttpContextProvider() {
//            @Override
//            public HttpContext getContext() {
////                final AuthScheme authScheme = new BasicAuthentication("writer", "password");
//                final AuthCache authCache = new BasicAuthCache();
//                final BasicHttpContext basicHttpContext = new BasicHttpContext();
////                basicHttpContext.
//                return basicHttpContext;
//            }
//        })
//
//        final ResteasyClient resteasyClient = new ResteasyClientBuilder().build();
////        resteasyClient.httpEngine().
//        final ResteasyWebTarget resteasyWebTarget = resteasyClient.target("http://foo.com/resource");
////        resteasyWebTarget.

//        Configuration configuration = client.getConfiguration();
//        configuration.
        WebTarget target = client
//                .register(basicAuthentication)
                .target("https://integrate.zapto.org/dcv/v1/write/groupId/artifactId");
        target.register(basicAuthentication);

        MultivaluedMap<String, String> secretKeyNames = new MultivaluedHashMap<>();
        secretKeyNames.putSingle(secretKeyName, "abc");
        Entity<Form> entity = Entity.form(secretKeyNames);

        try (final Response response = target.request().post(entity)) {
            assertThat(response.getStatus(), is(NO_CONTENT.getStatusCode()));
        }
        target = client
//                .register(basicAuthentication)
                .target("https://integrate.zapto.org/dcv/v1/write/groupId/artifactId/" + secretKeyName);
        target.register(basicAuthentication);

        // when : method to be checked invocation
        try (final Response response = target.request().delete()) {

            // then : checks and assertions
            assertThat(response.getStatus(), is(NO_CONTENT.getStatusCode()));
        }
    }

//    @Disabled
    @Test
    public void shouldValidateWhenRemoveSingleEntryNotFound() {

        // given: all data (test fixture) preparation
        final String secretKeyNameNotFound = "secretKeyNameThatShouldNotBeFound";
        final WebTarget target = client.target(testUrl + "write/groupId/artifactId/" + secretKeyNameNotFound);
        target.register(basicAuthentication);
//        MultivaluedMap<String, String> secretKeyNames = new MultivaluedHashMap<>();
//        secretKeyNames.putSingle("abcf", "def");
//        Entity<Form> entity = Entity.form(secretKeyNames);
//        final Entity<Form> entity = Entity.form((MultivaluedMap<String, String>) null);

        // when : method to be checked invocation
        try (final Response response = target.request().delete()) {

            // then : checks and assertions
            assertThat(response.getStatus(), is(NOT_FOUND.getStatusCode()));
//            assertThat(response.readEntity(String.class), allOf(containsStringIgnoringCase("not found"), containsString(secretKeyNameNotFound)));
            assertThat(response.getHeaderString(ERROR_MESSAGE_HEADER_NAME), allOf(containsStringIgnoringCase("not found"),
                    containsStringIgnoringCase(secretKeyNameNotFound)));
//            assertThat(response.readEntity(String.class), allOf(containsStringIgnoringCase("must match"), containsString(invalidArtifactId)));
        }
//        secretKeyNames = new MultivaluedHashMap<>();
//        secretKeyNames.putSingle("abcg", "def");
//        secretKeyNames.putSingle("now", "def");
//        entity = Entity.form(secretKeyNames);
//
//        try (final Response response = target.request().post(entity)) {
//
//            // then : checks and assertions
//            assertThat(response.getStatus(), is(BAD_REQUEST.getStatusCode()));
//            assertThat(response.getHeaderString(ERROR_MESSAGE_HEADER_NAME), containsString("bla"));
////            assertThat(response.getStatus(), is(OK.getStatusCode()));
////            assertThat(response.readEntity(String.class), is("string,"));
//        }
////            assertThat(response.readEntity(String.class), allOf(containsStringIgnoringCase("must match"), containsString(invalidArtifactId)));
    }

//    @Disabled
    @Test
    public void shouldValidateGroupIdForSetSecretKeyEntryForDoubleUnderscore() {

        // given: all data (test fixture) preparation
        final String invalidGroupId = "group__Id";
        final WebTarget target = client.target(testUrl + "write/" + invalidGroupId + "/artifactId");
        target.register(basicAuthentication);
        final MultivaluedMap<String, String> secretKeyNames = new MultivaluedHashMap<>();
        secretKeyNames.putSingle("abc", "def");
        final Entity<Form> entity = Entity.form(secretKeyNames);

        // when : method to be checked invocation
        try (final Response response = target.request().post(entity)) {

            // then : checks and assertions
            assertThat(response.getStatus(), is(BAD_REQUEST.getStatusCode()));
            assertThat(response.getHeaderString(ERROR_MESSAGE_HEADER_NAME), containsString(invalidGroupId));
            assertThat(response.readEntity(String.class), allOf(containsStringIgnoringCase("not permitted"), containsString(invalidGroupId)));
        }
    }

//    @Disabled
    @Test
    public void shouldValidateArtifactIdForSetSecretKeyEntryForDoubleUnderscore() {

        // given: all data (test fixture) preparation
        final String invalidArtifactId = "artifact__Id";
        final WebTarget target = client.target(testUrl + "write/groupId/" + invalidArtifactId);
        target.register(basicAuthentication);
        final MultivaluedMap<String, String> secretKeyNames = new MultivaluedHashMap<>();
        secretKeyNames.putSingle("abc", "def");
        final Entity<Form> entity = Entity.form(secretKeyNames);

        // when : method to be checked invocation
        try (final Response response = target.request().post(entity)) {

            // then : checks and assertions
            assertThat(response.getStatus(), is(BAD_REQUEST.getStatusCode()));
            assertThat(response.getHeaderString(ERROR_MESSAGE_HEADER_NAME), containsString(invalidArtifactId));
            assertThat(response.readEntity(String.class), allOf(containsStringIgnoringCase("not permitted"), containsString(invalidArtifactId)));
        }
    }

//    @Disabled
    @Test
    public void shouldValidateEntryNameForSetSecretKeyEntryForDoubleUnderscore() {

        // given: all data (test fixture) preparation
        final String invalidEntryName = "entry__name";
        final WebTarget target = client.target(testUrl + "write/groupId/artifactId");
        target.register(basicAuthentication);
        final MultivaluedMap<String, String> secretKeyNames = new MultivaluedHashMap<>();
        secretKeyNames.putSingle(invalidEntryName, "def");
        final Entity<Form> entity = Entity.form(secretKeyNames);

        // when : method to be checked invocation
        try (final Response response = target.request().post(entity)) {

            // then : checks and assertions
            assertThat(response.getStatus(), is(BAD_REQUEST.getStatusCode()));
            assertThat(response.getHeaderString(ERROR_MESSAGE_HEADER_NAME), containsString(invalidEntryName));
            assertThat(response.readEntity(String.class), allOf(containsStringIgnoringCase("not permitted"), containsString(invalidEntryName)));
        }
    }

}
