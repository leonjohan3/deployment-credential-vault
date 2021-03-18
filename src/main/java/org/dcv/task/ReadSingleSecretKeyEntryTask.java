package org.dcv.task;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.io.File;
import java.security.KeyStore;
import java.util.Map;
import java.util.concurrent.RecursiveTask;

import static java.util.Objects.isNull;

@Slf4j
@Value
@EqualsAndHashCode(callSuper = true)
public class ReadSingleSecretKeyEntryTask extends RecursiveTask<ReadSingleSecretKeyEntryResponse> {

    Map<String, String> mdcContext;
    String keystorePassword;
    File keystoreFile;
    String alias;

    @Override
    protected ReadSingleSecretKeyEntryResponse compute() {

        try {

            MDC.setContextMap(mdcContext);
            log.debug("start compute, keyfile:{}, alias:{}", keystoreFile, alias);
            final char[] password = keystorePassword.toCharArray();
            final KeyStore keyStore = KeyStore.getInstance(keystoreFile, password);
            final KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry) keyStore.getEntry(alias, new KeyStore.PasswordProtection(password));
            log.debug("finish compute");
            return new ReadSingleSecretKeyEntryResponse(isNull(entry) ? null : new String(entry.getSecretKey().getEncoded()), null);

        } catch (Exception e) {
            log.error("compute", e);
            return new ReadSingleSecretKeyEntryResponse(null, e);
        } finally {
            MDC.clear();
        }
    }
}
