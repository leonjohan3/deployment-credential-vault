package org.dcv.util;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import static java.nio.charset.Charset.forName;
import static java.util.Objects.isNull;

//@Slf4j
public class KeystoreEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private final static String KEYSTORE_PASSWORD = "KEYSTORE_PASSWORD";
    private final static String PASSWORD_KEYSTORE = "password.keystore";
    private final static String CERT_PRIVATE_KEY = "CERT_PRIVATE_KEY";

    @Override
    public void postProcessEnvironment(final ConfigurableEnvironment environment, final SpringApplication application) {
//        if (environment.containsProperty("KEYSTORE_PASSWORD")) {
//            System.out.println("aaa:" + environment.getRequiredProperty(KEYSTORE_PASSWORD, String.class));
        final char[] password = environment.getRequiredProperty(KEYSTORE_PASSWORD, String.class).toCharArray();

        try {
//            System.out.println("aaa:[" + environment.getProperty("java.io.tmpdir") + "]");
//            log.info("tmp dir: [{}]", environment.getProperty("java.io.tmpdir"));
            final KeyStore keyStore = KeyStore.getInstance(new File(".keystore"), password);
            setKeystorePassword(environment, keyStore, password);
            saveCertPrivateKeyToTmpFolder(environment, keyStore, password);
//            final KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry) keyStore.getEntry(KEYSTORE_PASSWORD,
//                    new KeyStore.PasswordProtection(password));
//            System.out.println("aaa:" + new String(entry.getSecretKey().getEncoded()));
//            environment.getSystemProperties().put(PASSWORD_KEYSTORE, new String(entry.getSecretKey().getEncoded()));
        } catch (Exception e) {
//            e.printStackTrace();
            throw new IllegalStateException(e);
        }
//        }
//        environment.
    }

    private void setKeystorePassword(final ConfigurableEnvironment environment, final KeyStore keyStore, final char[] password)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, UnrecoverableEntryException {

        final KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry) keyStore.getEntry(KEYSTORE_PASSWORD, new KeyStore.PasswordProtection(password));
        if (isNull(entry)) {
            throw new IllegalStateException("cannot find PASSWORD_KEYSTORE in keystore");
        }
        environment.getSystemProperties().put(PASSWORD_KEYSTORE, new String(entry.getSecretKey().getEncoded()));
    }

    private void saveCertPrivateKeyToTmpFolder(final ConfigurableEnvironment environment, final KeyStore keyStore, final char[] password)
            throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException, IOException {

        final KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry) keyStore.getEntry(CERT_PRIVATE_KEY, new KeyStore.PasswordProtection(password));

        if (isNull(entry)) {
            throw new IllegalStateException("cannot find CERT_PRIVATE_KEY in keystore");
        }
        final Path path = Paths.get(environment.getProperty("java.io.tmpdir"), environment.getProperty("cert.key.filename", "key.pem.b64"));
        Files.write(path, (new String(entry.getSecretKey().getEncoded())).getBytes(forName("UTF-8")));
    }
}
