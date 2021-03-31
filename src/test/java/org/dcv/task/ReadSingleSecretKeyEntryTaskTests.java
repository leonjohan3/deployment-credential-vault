package org.dcv.task;

import org.junit.Test;

import java.io.File;

import static java.util.Collections.EMPTY_MAP;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;

public class ReadSingleSecretKeyEntryTaskTests {

    @Test
    public void shouldSuccessfullyCompute() {

        // given: all data (test fixture) preparation
        final String keystorePassword = "keystore";
        final File keystoreFile = new File("target/test-classes", "readSingleSecretKeyEntryTaskKeystore");
        final String alias = "entry";
        final ReadSingleSecretKeyEntryTask readSingleSecretKeyEntryTask = new ReadSingleSecretKeyEntryTask(EMPTY_MAP, keystorePassword, keystoreFile, alias);

        // when : method to be checked invocation
        final ReadSingleSecretKeyEntryResponse readSingleSecretKeyEntryResponse = readSingleSecretKeyEntryTask.compute();

        // then : checks and assertions
        assertThat(readSingleSecretKeyEntryResponse.getSecretKeyEntry(), is("password"));
        assertThat(readSingleSecretKeyEntryResponse.hasException(), is(false));
    }

    @Test
    public void shouldComputeAndHandleException() {

        // given: all data (test fixture) preparation
        final String keystorePassword = "not_the_keystore_password";
        final File keystoreFile = new File("target/test-classes", "readSingleSecretKeyEntryTaskKeystore");
        final String alias = "entry";
        final ReadSingleSecretKeyEntryTask readSingleSecretKeyEntryTask = new ReadSingleSecretKeyEntryTask(EMPTY_MAP, keystorePassword, keystoreFile, alias);

        // when : method to be checked invocation
        final ReadSingleSecretKeyEntryResponse readSingleSecretKeyEntryResponse = readSingleSecretKeyEntryTask.compute();

        // then : checks and assertions
        assertThat(readSingleSecretKeyEntryResponse.hasException(), is(true));
//        assertThat(readSingleSecretKeyEntryResponse.getException(), notNullValue());
        assertThat(readSingleSecretKeyEntryResponse.getException().getMessage(), containsString("ntegrity check faile"));
        assertThat(readSingleSecretKeyEntryResponse.getSecretKeyEntry(), nullValue());
    }
}
