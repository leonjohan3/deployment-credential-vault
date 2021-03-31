package org.dcv.service;

import org.dcv.dto.SecretKeyEntryBase;
import org.dcv.task.ReadSecretKeyEntriesResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class EntryReaderServiceTests {

    @Mock
    private KeystoreService keystoreService;

    @InjectMocks
    private EntryReaderService entryReaderService;

    @Test
    public void shouldReturnResponseAsIsOnException() {

        // given: all data (test fixture) preparation
        final SecretKeyEntryBase secretKeyEntry = new SecretKeyEntryBase("groupId", "artifactId");
        final ReadSecretKeyEntriesResponse expectedReadSecretKeyEntriesResponse = new ReadSecretKeyEntriesResponse(new IllegalStateException());
        given(keystoreService.getSecretKeyEntries(secretKeyEntry)).willReturn(expectedReadSecretKeyEntriesResponse);

        // when : method to be checked invocation
        final ReadSecretKeyEntriesResponse readSecretKeyEntriesResponse = entryReaderService.getExportedSecretKeyEntries(secretKeyEntry);

        // then : checks and assertions
        assertThat(readSecretKeyEntriesResponse, is(expectedReadSecretKeyEntriesResponse));
    }

    @Test
    public void shouldSuccessfullyGetExportedSecretKeyEntries() {

        // given: all data (test fixture) preparation
        final SecretKeyEntryBase secretKeyEntry = new SecretKeyEntryBase("groupId", "artifactId");
        final Map<String, String> secretKeyEntries = new HashMap<>();
        secretKeyEntries.put("abc", "def");
        secretKeyEntries.put("ghi", "jkl");
        final ReadSecretKeyEntriesResponse expectedReadSecretKeyEntriesResponse = new ReadSecretKeyEntriesResponse(secretKeyEntries);
        given(keystoreService.getSecretKeyEntries(secretKeyEntry)).willReturn(expectedReadSecretKeyEntriesResponse);

        // when : method to be checked invocation
        final ReadSecretKeyEntriesResponse readSecretKeyEntriesResponse = entryReaderService.getExportedSecretKeyEntries(secretKeyEntry);

        // then : checks and assertions
        assertThat(readSecretKeyEntriesResponse.getException(), nullValue());
        assertThat(readSecretKeyEntriesResponse.getSecretKeyEntries(), is(secretKeyEntries));
        assertThat(readSecretKeyEntriesResponse.getExportedSecretKeyEntries(),
                allOf(containsString("export "),
                        containsString("\"def\""),
                        containsString(" GHI=\""),
                        containsString("\"jkl\""),
                        containsString(" ABC=\"")));
    }
}
