package org.dcv.controller;

import org.dcv.dto.SecretKeyEntryBase;
import org.dcv.dto.SecretKeyEntryKeyName;
import org.dcv.service.EntryReaderService;
import org.dcv.task.ReadSecretKeyEntriesResponse;
import org.dcv.task.ReadSingleSecretKeyEntryResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static java.util.Collections.EMPTY_MAP;
import static org.dcv.util.Constants.CORRELATION_ID_HEADER;
import static org.dcv.util.Constants.ERROR_MESSAGE_HEADER_NAME;
import static org.dcv.util.Constants.MEDIA_TYPE_TEXT_PLAIN;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.text.IsBlankString.blankOrNullString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EntryReaderController.class,
        properties = {
                "logging.level.root=warn",
                "logging.level.org.dcv=warn",
                "spring.main.banner-mode=off"
        })
public class EntryReaderControllerTests {

    private final static String URL_GET_SECRET_KEY_ENTRY_UNDER_TEST = "/v1/read/{groupId}/{artifactId}/{secretKeyName}";
    private final static String URL_GET_SECRET_KEY_ENTRIES_UNDER_TEST = "/v1/read/{groupId}/{artifactId}";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private EntryReaderService entryReaderService;

    @Test
    void shouldSuccessfullyGetSecretKeyEntry() throws Exception {

        // given: all data (test fixture) preparation
        final String corrId = "123";
        final String secretKeyValue = "secr__etKeyV! a  lue";
        final SecretKeyEntryKeyName secretKeyEntry = new SecretKeyEntryKeyName("groupId", "artifact-Id", "secret.Key_Name");

        given(entryReaderService.getSecretKeyEntry(eq(secretKeyEntry))).willReturn(new ReadSingleSecretKeyEntryResponse(secretKeyValue, null));

        // when : method to be checked invocation
        mvc.perform(get(URL_GET_SECRET_KEY_ENTRY_UNDER_TEST, secretKeyEntry.getGroupId(), secretKeyEntry.getArtifactId(), secretKeyEntry.getSecretKeyName())
                .header(CORRELATION_ID_HEADER, corrId))

                // then : checks and assertions
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(CORRELATION_ID_HEADER, nullValue()))
                .andExpect(content().contentType(MEDIA_TYPE_TEXT_PLAIN))
                .andExpect(content().string(secretKeyValue));
    }

    @Test
    void shouldSuccessfullyGetSecretKeyEntries() throws Exception {

        // given: all data (test fixture) preparation
        final String corrId = "123";
        final String exportedSecretKeyEntries = "exportedSecretKeyEntries";
        final SecretKeyEntryBase secretKeyEntry = new SecretKeyEntryBase("groupId", "artifact-Id");

        given(entryReaderService.getExportedSecretKeyEntries(eq(secretKeyEntry))).willReturn(new ReadSecretKeyEntriesResponse(EMPTY_MAP,
                exportedSecretKeyEntries, null));

        // when : method to be checked invocation
        mvc.perform(get(URL_GET_SECRET_KEY_ENTRIES_UNDER_TEST, secretKeyEntry.getGroupId(), secretKeyEntry.getArtifactId())
                .header(CORRELATION_ID_HEADER, corrId))

                // then : checks and assertions
                .andExpect(status().isOk())
                .andExpect(header().string(CORRELATION_ID_HEADER, nullValue()))
                .andExpect(content().contentType(MEDIA_TYPE_TEXT_PLAIN))
                .andExpect(content().string(exportedSecretKeyEntries));
    }

    @Test
    void shouldRespondWithNotFoundWhenSecretKeyEntryNotExist() throws Exception {

        // given: all data (test fixture) preparation
//        final String corrId = "123";
//        final String secretKeyValue = null;
        final SecretKeyEntryKeyName secretKeyEntry = new SecretKeyEntryKeyName("groupId", "artifact-Id", "secret.Key_Name");

        given(entryReaderService.getSecretKeyEntry(eq(secretKeyEntry))).willReturn(new ReadSingleSecretKeyEntryResponse(null, null));

        // when : method to be checked invocation
        mvc.perform(get(URL_GET_SECRET_KEY_ENTRY_UNDER_TEST, secretKeyEntry.getGroupId(), secretKeyEntry.getArtifactId(),
                secretKeyEntry.getSecretKeyName()))

                // then : checks and assertions
//                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(header().string(CORRELATION_ID_HEADER, not(blankOrNullString())));
//                .andExpect(content().contentType(MEDIA_TYPE_TEXT_PLAIN));
//                .andExpect(content().string(secretKeyValue));
    }

    @Test
    void shouldRespondWithNotFoundWhenSecretKeyEntriesNotExist() throws Exception {

        // given: all data (test fixture) preparation
//        final String corrId = "123";
//        final String secretKeyValue = null;
        final SecretKeyEntryBase secretKeyEntry = new SecretKeyEntryBase("groupId", "artifact-Id");

        given(entryReaderService.getExportedSecretKeyEntries(eq(secretKeyEntry))).willReturn(new ReadSecretKeyEntriesResponse(EMPTY_MAP, null, null));

        // when : method to be checked invocation
        mvc.perform(get(URL_GET_SECRET_KEY_ENTRIES_UNDER_TEST, secretKeyEntry.getGroupId(), secretKeyEntry.getArtifactId()))

                // then : checks and assertions
//                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(header().string(ERROR_MESSAGE_HEADER_NAME, containsString("othing found for groupI")))
                .andExpect(header().string(CORRELATION_ID_HEADER, not(blankOrNullString())));
//                .andExpect(content().contentType(MEDIA_TYPE_TEXT_PLAIN));
//                .andExpect(content().string(secretKeyValue));
    }

    @Test
    void shouldRespondWithBadRequestWhenEncounterExceptionForGetSecretKeyEntry() throws Exception {

        // given: all data (test fixture) preparation
//        final String corrId = "123";
        final String exceptionMessage = "exceptionMessage";
        final SecretKeyEntryKeyName secretKeyEntry = new SecretKeyEntryKeyName("groupId", "artifact-Id", "secret.Key_Name");

        given(entryReaderService.getSecretKeyEntry(eq(secretKeyEntry))).willReturn(new ReadSingleSecretKeyEntryResponse(null,
                new IllegalStateException(exceptionMessage)));

        // when : method to be checked invocation
        mvc.perform(get(URL_GET_SECRET_KEY_ENTRY_UNDER_TEST, secretKeyEntry.getGroupId(), secretKeyEntry.getArtifactId(),
                secretKeyEntry.getSecretKeyName()))

                // then : checks and assertions
//                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(header().string(CORRELATION_ID_HEADER, not(blankOrNullString())))
                .andExpect(header().string(ERROR_MESSAGE_HEADER_NAME, exceptionMessage));
//                .andExpect(content().contentType(MEDIA_TYPE_TEXT_PLAIN));
//                .andExpect(content().string(secretKeyValue));
    }

    @Test
    void shouldRespondWithBadRequestWhenEncounterExceptionForGetSecretKeyEntries() throws Exception {

        // given: all data (test fixture) preparation
//        final String corrId = "123";
        final String exceptionMessage = "exceptionMessage";
        final SecretKeyEntryBase secretKeyEntry = new SecretKeyEntryBase("groupId", "artifact-Id");

        given(entryReaderService.getExportedSecretKeyEntries(eq(secretKeyEntry))).willReturn(new ReadSecretKeyEntriesResponse(EMPTY_MAP, null,
                new IllegalStateException(exceptionMessage)));

        // when : method to be checked invocation
        mvc.perform(get(URL_GET_SECRET_KEY_ENTRIES_UNDER_TEST, secretKeyEntry.getGroupId(), secretKeyEntry.getArtifactId()))

                // then : checks and assertions
//                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(header().string(CORRELATION_ID_HEADER, not(blankOrNullString())))
                .andExpect(header().string(ERROR_MESSAGE_HEADER_NAME, exceptionMessage));
//                .andExpect(content().contentType(MEDIA_TYPE_TEXT_PLAIN));
//                .andExpect(content().string(secretKeyValue));
    }
}
