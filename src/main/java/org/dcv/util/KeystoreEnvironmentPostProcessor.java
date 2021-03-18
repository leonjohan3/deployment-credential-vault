package org.dcv.util;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

import java.io.File;
import java.security.KeyStore;

public class KeystoreEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private final static String KEYSTORE_PASSWORD = "KEYSTORE_PASSWORD";
    private final static String PASSWORD_KEYSTORE = "password.keystore";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
//        System.out.println("aaa:");
//        if (environment.containsProperty("KEYSTORE_PASSWORD")) {
//            System.out.println("aaa:" + environment.getRequiredProperty(KEYSTORE_PASSWORD, String.class));
        final char[] password = environment.getRequiredProperty(KEYSTORE_PASSWORD, String.class).toCharArray();

        try {
            final KeyStore keyStore = KeyStore.getInstance(new File(".keystore"), password);
            final KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry) keyStore.getEntry(KEYSTORE_PASSWORD,
                    new KeyStore.PasswordProtection(password));
//            System.out.println("aaa:" + new String(entry.getSecretKey().getEncoded()));
            environment.getSystemProperties().put(PASSWORD_KEYSTORE, new String(entry.getSecretKey().getEncoded()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
//        }
//        environment.
    }
}
