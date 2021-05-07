package org.dcv.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;

import static java.lang.System.getProperty;
import static java.lang.System.getenv;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.security.KeyStore.SecretKeyEntry;
import static java.util.Objects.isNull;
import static org.dcv.util.Constants.KEYSTORE_PASSWORD_END_ENV_NAME;
import static org.dcv.util.Constants.KEYSTORE_PASSWORD_START_ENV_NAME;

@Slf4j
//@ApplicationScoped
@WebListener
public class PrivateKeyProvider implements ServletContextListener {

    private final static String CERT_PRIVATE_KEY = "CERT_PRIVATE_KEY";
    private final static String CERT_KEY_FILENAME = "CERT_KEY_FILENAME";
//    @Inject
//    private ConfigurationProvider configuration;

    //    @PostConstruct
    public void contextInitialized(final ServletContextEvent servletContextEvent) {
        log.debug("start PrivateKeyProvider");
        // .key s tore
        final String start = getenv(KEYSTORE_PASSWORD_START_ENV_NAME);

        if (isNull(start)) {
            throw new IllegalStateException("env entry " + KEYSTORE_PASSWORD_START_ENV_NAME + " was not provided");
        }
//        final char[] start = System.getenv("KEYSTORE_PASSWORD_START_ENV_NAME").toCharArray();
        final char[] middle = new char[]{'s', 't'}; // TODO - change before release

        final String end = getProperty(KEYSTORE_PASSWORD_END_ENV_NAME);

        if (isNull(start)) {
            throw new IllegalStateException("system property " + KEYSTORE_PASSWORD_END_ENV_NAME + " was not provided");
        }

//    public PrivateKeyProvider() {

//        log.info("providePrivateKey: {}", start + new String(middle) + end);

        try {
//            System.out.println("aaa:[" + environment.getProperty("java.io.tmpdir") + "]");
//            log.info("tmp dir: [{}]", environment.getProperty("java.io.tmpdir"));
            final KeyStore keyStore = KeyStore.getInstance(new File(".keystore"), (start + new String(middle) + end).toCharArray());
            final SecretKeyEntry entry = (SecretKeyEntry) keyStore.getEntry(CERT_PRIVATE_KEY,
                    new KeyStore.PasswordProtection((start + new String(middle) + end).toCharArray()));

            if (isNull(entry)) {
                throw new IllegalStateException("cannot find CERT_PRIVATE_KEY in keystore");
            }
            final String certKeyFilename = getenv(CERT_KEY_FILENAME);

            if (isNull(certKeyFilename)) {
                throw new IllegalStateException("env entry " + CERT_KEY_FILENAME + " was not provided");
            }
            final Path path = Paths.get(getProperty("java.io.tmpdir"), certKeyFilename);
            Files.write(path, (new String(entry.getSecretKey().getEncoded())).getBytes(UTF_8));
//            setKeystorePassword(environment, keyStore, password);
//            saveCertPrivateKeyToTmpFolder(environment, keyStore, password);
//            final KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry) keyStore.getEntry(KEYSTORE_PASSWORD,
//                    new KeyStore.PasswordProtection(password));
//            System.out.println("aaa:" + new String(entry.getSecretKey().getEncoded()));
//            environment.getSystemProperties().put(PASSWORD_KEYSTORE, new String(entry.getSecretKey().getEncoded()));
        } catch (final Exception e) {
//            e.printStackTrace();
            throw new IllegalStateException(e);
        }

//        servletContextEvent.getServletContext().
//        log.info("providePrivateKey: {}", configuration.getMapDbFile().toString());
        log.debug("finish PrivateKeyProvider");
    }
}
