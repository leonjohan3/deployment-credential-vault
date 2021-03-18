package org.dcv;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.Enumeration;

@Slf4j
@Disabled
public class MyTests {

    @Test
    public void shouldReadAllPasswords() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException,
            UnrecoverableEntryException {

        char[] password = "oFFline33".toCharArray();

        final KeyStore keyStore = KeyStore.getInstance(new File("src/test/resources", "new_keystore"), password);

        // Secret key entry with algorithm PBEWithMD5AndDES
        // Secret key entry with algorithm PBEWithHmacSHA256
        // Secret key entry with algorithm PBEWithHmacSHA256AndAES_128
        KeyStore.SecretKeyEntry myspass = (KeyStore.SecretKeyEntry) keyStore.getEntry("myspass", new KeyStore.PasswordProtection(password));
        log.info("mypass: {}", new String(myspass.getSecretKey().getEncoded()));
    }

    @Test
    public void shouldDeleteEntry() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, UnrecoverableEntryException {
        char[] password = "oFFline33".toCharArray();
        final KeyStore keyStore = KeyStore.getInstance(new File("src/test/resources", "new_keystore"), password);
        final String alias = "mypass2";
        KeyStore.SecretKeyEntry myspass2;

        if (keyStore.containsAlias(alias)) {
//            myspass2 = (KeyStore.SecretKeyEntry) keyStore.getEntry(alias, new KeyStore.PasswordProtection(password));
            keyStore.deleteEntry(alias);
            // store away the keystore
            try (FileOutputStream fos = new FileOutputStream(new File("src/test/resources", "new_keystore"))) {
                keyStore.store(fos, password);
            }
        }
    }

    @Test
    public void shouldListAllEntries() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, UnrecoverableEntryException {
        char[] password = "oFFline33".toCharArray();

        final KeyStore keyStore = KeyStore.getInstance(new File("src/test/resources", "new_keystore"), password);

        for (Enumeration<String> aliases = keyStore.aliases(); aliases.hasMoreElements(); ) {
            final String alias = aliases.nextElement();

            if (keyStore.entryInstanceOf(alias, KeyStore.SecretKeyEntry.class)) {
                KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry) keyStore.getEntry(alias, new KeyStore.PasswordProtection(password));
                log.info("{}: {}", alias, new String(entry.getSecretKey().getEncoded()));
            }
        }
    }

    @Test
    public void shouldAddOrUpdatePasswordToExistingKeystoreFile() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException,
            InvalidKeySpecException, UnrecoverableEntryException {

        final char[] password = "oFFline33".toCharArray();
        final KeyStore keyStore = KeyStore.getInstance(new File("src/test/resources", "new_keystore"), password);
//        final String alias = "mypass2";
        final String alias = "group.id__artifact_id__secret_key_name";
        KeyStore.SecretKeyEntry myspass2;

        if (keyStore.containsAlias(alias)) {
            myspass2 = (KeyStore.SecretKeyEntry) keyStore.getEntry(alias, new KeyStore.PasswordProtection(password));
        }
//        final SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBEWithHmacSHA256AndAES_128");
        final SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        final char[] savedPassword = "33blaBla".toCharArray();
        final SecretKey mySecretKey = secretKeyFactory.generateSecret(new PBEKeySpec(savedPassword));
        myspass2 = new KeyStore.SecretKeyEntry(mySecretKey);
        keyStore.setEntry(alias, myspass2, new KeyStore.PasswordProtection(password));

        // store away the keystore
        try (FileOutputStream fos = new FileOutputStream(new File("src/test/resources", "new_keystore"))) {
            keyStore.store(fos, password);
        }
    }

    @Test
    public void shouldCreateNewKeystoreFile() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException,
            InvalidKeySpecException {

        char[] password = "oFFline33".toCharArray();

//        final KeyStore keyStore = KeyStore.getInstance(new File("src/test/resources", "new_keystore"), password);
        final KeyStore keyStore = KeyStore.getInstance("pkcs12");
        keyStore.load(null, password);
//        keyStore.

//        final SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBEWithHmacSHA1");
//        final SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBEWithHmacSHA256AndAES_128");
        final SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        final SecretKey mySecretKey = secretKeyFactory.generateSecret(new PBEKeySpec(password));
        final KeyStore.SecretKeyEntry myspass = new KeyStore.SecretKeyEntry(mySecretKey);
        keyStore.setEntry("myspass", myspass, new KeyStore.PasswordProtection(password));

        // store away the keystore
        try (FileOutputStream fos = new FileOutputStream(new File("src/test/resources", "new_keystore"))) {
            keyStore.store(fos, password);
        }
    }
}
