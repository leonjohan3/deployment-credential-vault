package org.dcv.controller;

import org.dcv.dto.SecretKeyEntry;
import org.dcv.service.EntryWriterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.dcv.util.Constants.CORRELATION_ID_HEADER;
import static org.dcv.util.Constants.ERROR_MESSAGE_HEADER_NAME;
import static org.dcv.util.Constants.MEDIA_TYPE_TEXT_PLAIN;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.text.IsBlankString.blankOrNullString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EntryWriterController.class,
        properties = {
                "logging.level.root=warn",
                "logging.level.org.dcv=warn",
                "spring.main.banner-mode=off"
        })
public class EntryWriterControllerTests {

    private final static String URL_UNDER_TEST = "/v1/write/{groupId}/{artifactId}";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private EntryWriterService entryWriterService;

    @Test
    void shouldHandleEmptySecretKeyNamesCollection() throws Exception {

        // when : method to be checked invocation
        mvc.perform(post(URL_UNDER_TEST, "group.id", "some-artifact_id")
                .contentType(APPLICATION_FORM_URLENCODED_VALUE)
                .content(""))

                // then : checks and assertions
                .andExpect(status().isCreated());
    }

    @Test
    void shouldSuccessfullySetSecretKeyEntry() throws Exception {

        // given: all data (test fixture) preparation
        final String corrId = "123";

        // when : method to be checked invocation
        mvc.perform(post(URL_UNDER_TEST, "group.id", "some-artifact_id")
                .contentType(APPLICATION_FORM_URLENCODED_VALUE)
                .content("secret_key_name=secret_key_value&secret_key_other_name=secret_key_other_value"))

                // then : checks and assertions
                .andExpect(status().isCreated());

        verify(entryWriterService, times(2)).setSecretKeyEntry(any(SecretKeyEntry.class));
    }

    @Test
    void shouldRespondWithBadRequestOnInvalidKeyName() throws Exception {

        // when : method to be checked invocation
        mvc.perform(post(URL_UNDER_TEST, "group.id", "some-artifact_id")
                .contentType(APPLICATION_FORM_URLENCODED_VALUE)
                .content("secret_key__name=secret_key_value"))

                // then : checks and assertions
                .andExpect(header().string(CORRELATION_ID_HEADER, not(blankOrNullString())))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("secret_key__name")))
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    @Test
    void shouldRespondWithBadRequestOnInvalidGroupId() throws Exception {

        // when : method to be checked invocation
        mvc.perform(post(URL_UNDER_TEST, "gro__up.id", "some-artifact_id")
                .contentType(APPLICATION_FORM_URLENCODED_VALUE)
                .content("secret_key_name=secret_key__value"))

                // then : checks and assertions
                .andExpect(header().string(CORRELATION_ID_HEADER, not(blankOrNullString())))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("gro__up.id")))
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    @Test
    void shouldRespondWithBadRequestOnInvalidArtifactId() throws Exception {

        // when : method to be checked invocation
        mvc.perform(post(URL_UNDER_TEST, "group.id", "some-artifact__id")
                .contentType(APPLICATION_FORM_URLENCODED_VALUE)
                .content("secret_key_name=secret_key__value"))

                // then : checks and assertions
                .andExpect(header().string(CORRELATION_ID_HEADER, not(blankOrNullString())))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("some-artifact__id")))
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    @Test
    void shouldRespondWithBadRequestOnInvalidKeyNameUsingSpecialCharacters() throws Exception {

        // when : method to be checked invocation
        mvc.perform(post(URL_UNDER_TEST, "group.id", "some-artifact_id")
                .contentType(APPLICATION_FORM_URLENCODED_VALUE)
                .content("1secret_key_name=secret_key__value"))

                // then : checks and assertions
                .andExpect(header().string(CORRELATION_ID_HEADER, not(blankOrNullString())))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("ecretKeyName: must matc")))
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    @Test
    void shouldRespondWithBadRequestOnInvalidGroupIdUsingSpecialCharacters() throws Exception {

        // when : method to be checked invocation
        mvc.perform(post(URL_UNDER_TEST, "g!roup.id", "some-artifact_id")
                .contentType(APPLICATION_FORM_URLENCODED_VALUE)
                .content("secret_key_name=secret_key__value"))

                // then : checks and assertions
//                .andDo(print())
                .andExpect(header().string(CORRELATION_ID_HEADER, not(blankOrNullString())))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("roupId: must matc")))
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    @Test
    void shouldRespondWithBadRequestOnInvalidArtifactIdUsingSpecialCharacters() throws Exception {

        // when : method to be checked invocation
        mvc.perform(post(URL_UNDER_TEST, "group.id", "some-art(i)fact_id")
                .contentType(APPLICATION_FORM_URLENCODED_VALUE)
                .content("secret_key_name=secret_key__value"))

                // then : checks and assertions
                .andExpect(header().string(CORRELATION_ID_HEADER, not(blankOrNullString())))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("rtifactId: must matc")))
                .andExpect(header().string(ERROR_MESSAGE_HEADER_NAME, containsString("etSecretKeyEntry.artifactId: must m")))
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    @Test
    void shouldRespondWithBadRequestOnExceptionFromSetSecretKeyEntry() throws Exception {

        // given: all data (test fixture) preparation
        final String exceptionDescription = "shouldRespondWithBadRequestOnExceptionFromSetSecretKeyEntry";
        given(entryWriterService.setSecretKeyEntry(any(SecretKeyEntry.class))).willReturn(new IllegalStateException(exceptionDescription));

        // when : method to be checked invocation
        mvc.perform(post(URL_UNDER_TEST, "group.id", "some-artifact_id")
                .contentType(APPLICATION_FORM_URLENCODED_VALUE)
                .content("secret_key_name=secret_key_value"))

                // then : checks and assertions
                .andExpect(header().string(CORRELATION_ID_HEADER, not(blankOrNullString())))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(exceptionDescription))
                .andExpect(header().string(ERROR_MESSAGE_HEADER_NAME, exceptionDescription))
                .andExpect(content().contentType(MEDIA_TYPE_TEXT_PLAIN));
    }
}
