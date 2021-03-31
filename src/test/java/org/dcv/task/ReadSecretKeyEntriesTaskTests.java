package org.dcv.task;

import org.junit.Test;

import java.io.File;

import static java.util.Collections.EMPTY_MAP;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;

public class ReadSecretKeyEntriesTaskTests {

    @Test
    public void shouldSuccessfullyCompute() {

        // given: all data (test fixture) preparation
        final String keystorePassword = "keystore";
        final File keystoreFile = new File("target/test-classes", "readSecretKeyEntriesTaskKeystore");
        final String aliasPrefix = "group__artifact__";
        final ReadSecretKeyEntriesTask readSecretKeyEntriesTask = new ReadSecretKeyEntriesTask(EMPTY_MAP, keystorePassword, keystoreFile, aliasPrefix);

        // when : method to be checked invocation
        final ReadSecretKeyEntriesResponse readSecretKeyEntriesResponse = readSecretKeyEntriesTask.compute();

        // then : checks and assertions
//        assertThat(readSecretKeyEntriesResponse.getSecretKeyEntry().get(), is("password"));
        assertThat(readSecretKeyEntriesResponse.hasException(), is(false));
        assertThat(readSecretKeyEntriesResponse.getSecretKeyEntries().entrySet(), hasSize(2));
//        assertThat(readSecretKeyEntriesResponse.getSecretKeyEntries().entrySet(), hasSize(2));
        assertThat(readSecretKeyEntriesResponse.getExportedSecretKeyEntries(), nullValue());
//                allOf(containsString("export "),
//                        containsString("\"def\""),
//                        containsString(" GHI=\""),
//                        containsString("\"jkl\""),
//                        containsString(" ABC=\"")));
    }

    @Test
    public void shouldComputeAndHandleException() {

        // given: all data (test fixture) preparation
        final String keystorePassword = "not_the_keystore_password";
        final File keystoreFile = new File("target/test-classes", "readSecretKeyEntriesTaskKeystore");
//        final String alias = "entry";
        final String aliasPrefix = "group__artifact__";
//        final ReadSingleSecretKeyEntryTask readSingleSecretKeyEntryTask = new ReadSingleSecretKeyEntryTask(EMPTY_MAP, keystorePassword, keystoreFile, alias);
        final ReadSecretKeyEntriesTask readSecretKeyEntriesTask = new ReadSecretKeyEntriesTask(EMPTY_MAP, keystorePassword, keystoreFile, aliasPrefix);

        // when : method to be checked invocation
//        final ReadSingleSecretKeyEntryResponse readSingleSecretKeyEntryResponse = readSingleSecretKeyEntryTask.compute();
        final ReadSecretKeyEntriesResponse readSecretKeyEntriesResponse = readSecretKeyEntriesTask.compute();

        // then : checks and assertions
        assertThat(readSecretKeyEntriesResponse.hasException(), is(true));
//        assertThat(readSingleSecretKeyEntryResponse.getException(), notNullValue());
        assertThat(readSecretKeyEntriesResponse.getException().getMessage(), containsString("ntegrity check faile"));
        assertThat(readSecretKeyEntriesResponse.getExportedSecretKeyEntries(), nullValue());
        assertThat(readSecretKeyEntriesResponse.getSecretKeyEntries().entrySet(), hasSize(0));
    }
}
