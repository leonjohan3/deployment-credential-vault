package org.dcv.task;

import org.junit.Test;

import java.io.File;

import static java.util.Collections.EMPTY_MAP;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

public class WriteSecretKeyEntryTaskTests {

    @Test
    public void shouldSuccessfullyCompute() {

        // given: all data (test fixture) preparation
        final String keystorePassword = "keystore";
        final File keystoreFile = new File("target/test-classes", "writeSecretKeyEntryTaskKeystore");
        final String alias = "entry";
        final String entryValue = randomAlphanumeric(512);
//        final ReadSingleSecretKeyEntryTask readSingleSecretKeyEntryTask = new ReadSingleSecretKeyEntryTask(EMPTY_MAP, keystorePassword, keystoreFile, alias);
        final WriteSecretKeyEntryTask writeSecretKeyEntryTask = new WriteSecretKeyEntryTask(EMPTY_MAP, keystorePassword, keystoreFile, alias, entryValue);

        // when : method to be checked invocation
        final Exception exception = writeSecretKeyEntryTask.compute();

        // then : checks and assertions
        assertThat(exception, nullValue());
//        assertThat(exception.hasException(), is(false));

        // given: all data (test fixture) preparation
//        final File readKeystoreFile = new File("target/test-classes", "readSingleSecretKeyEntryTaskKeystore");
//        final String alias = "entry";
        final ReadSingleSecretKeyEntryTask readSingleSecretKeyEntryTask = new ReadSingleSecretKeyEntryTask(EMPTY_MAP, keystorePassword, keystoreFile, alias);

        // when : method to be checked invocation
        final ReadSingleSecretKeyEntryResponse readSingleSecretKeyEntryResponse = readSingleSecretKeyEntryTask.compute();

        // then : checks and assertions
        assertThat(readSingleSecretKeyEntryResponse.getSecretKeyEntry(), is(entryValue));
        assertThat(readSingleSecretKeyEntryResponse.hasException(), is(false));
    }

}
