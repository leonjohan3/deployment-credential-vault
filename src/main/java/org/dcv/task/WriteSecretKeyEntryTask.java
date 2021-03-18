package org.dcv.task;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.File;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.util.Map;
import java.util.concurrent.RecursiveTask;

@Slf4j
@Value
@EqualsAndHashCode(callSuper = true)
public class WriteSecretKeyEntryTask extends RecursiveTask<Exception> {

    private final static String KEYSTORE_TYPE = "pkcs12";
    private final static String SECRET_KEY_ALGORITHM = "PBEWithMD5AndDES";

    Map<String, String> mdcContext;
    String keystorePassword;
    File keystoreFile;
    String alias;
    String value;

    @Override
    protected Exception compute() {

        try {

            MDC.setContextMap(mdcContext);
            log.debug("start compute, keyfile:{}, alias:{}, value:{}", keystoreFile, alias, value);
            final char[] password = keystorePassword.toCharArray();
            KeyStore keyStore;

            // TODO - test sym links
            if (keystoreFile.exists()) {
                keyStore = KeyStore.getInstance(keystoreFile, password);
            } else {
                keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
                keyStore.load(null, password);
            }
            final SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM);
            final SecretKey secretKey = secretKeyFactory.generateSecret(new PBEKeySpec(value.toCharArray()));
            final KeyStore.SecretKeyEntry entry = new KeyStore.SecretKeyEntry(secretKey);
            keyStore.setEntry(alias, entry, new KeyStore.PasswordProtection(password));

            // store away the keystore
            try (final FileOutputStream fos = new FileOutputStream(keystoreFile)) {
                keyStore.store(fos, password);
            }
            log.debug("finish compute");
            return null;

        } catch (Exception e) {
            log.error("compute", e);
            return e;
        } finally {
            MDC.clear();
        }
    }
}
