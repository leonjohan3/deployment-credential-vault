package org.dcv.service;

import lombok.extern.slf4j.Slf4j;
import org.dcv.dto.SecretKeyEntry;
import org.dcv.dto.SecretKeyEntryBase;
import org.dcv.dto.SecretKeyEntryKeyName;
import org.dcv.util.AesCipherProvider;
import org.dcv.util.ConfigurationProvider;
import org.dcv.util.DecryptCipherProvider;
import org.dcv.util.EncryptCipherProvider;
import org.mapdb.BTreeMap;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;
import org.mapdb.serializer.SerializerArrayTuple;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.NotFoundException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentMap;

import static java.lang.Runtime.getRuntime;
import static java.lang.String.format;
import static java.util.Base64.getDecoder;
import static java.util.Base64.getEncoder;
import static java.util.Objects.nonNull;
import static org.dcv.util.Constants.AES_ALGORITHM_AND_TRANSFORMATION;
import static org.dcv.util.Constants.SECRET_KEY_VALUE_DELIMITER;
import static org.mapdb.Serializer.STRING;

//import org.mapdb.volume.
//import org.mapdb.tuple.Tuple3;
//import org.mapdb.Fun.Tuple3;

@Slf4j
@ApplicationScoped
//@Valid
public class EntryService {

    private static final String MAP_NAME = "map_name_dcv_name_map";

    @Inject
//    @Valid
    private ConfigurationProvider configuration;

    @Inject
    private EncryptCipherProvider encryptCipherProvider;

    @Inject
    private DecryptCipherProvider decryptCipherProvider;

    @Inject
    private AesCipherProvider aesCipherProvider;

//    @Inject
//    private DecryptCipherProvider decryptCipherProvider;

//    @Inject
//    private PrivateKeyProvider privateKeyProvider;

    public synchronized void setSecretKeyEntry(@Valid final SecretKeyEntry secretKeyEntry) throws BadPaddingException, IllegalBlockSizeException {
        log.debug("start setSecretKeyEntry: {}", secretKeyEntry.getSecretKeyName());

        // generate a AES key
//        final KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
//        keyGenerator.init(128); // The AES key size in number of bits
//        final SecretKey aesKey = keyGenerator.generateKey();

        // encrypt data using the AES key
//        final Cipher aesCipher = Cipher.getInstance("AES");
//        aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);
//        final byte[] cipherText = aesCipher.doFinal(secretKeyEntry.getSecretKeyValue().getBytes());
        final byte[] cipherText = aesCipherProvider.getCipher().doFinal(secretKeyEntry.getSecretKeyValue().getBytes());
        final SecretKeyEntry encryptedSecretKeyEntry = new SecretKeyEntry(
                secretKeyEntry.getGroupId(),
                secretKeyEntry.getArtifactId(),
                secretKeyEntry.getSecretKeyName(),
                aesCipherProvider.getEncryptedBase64AesKey() + SECRET_KEY_VALUE_DELIMITER + getEncoder().encodeToString(cipherText));
        setSecretKeyEntryInDb(encryptedSecretKeyEntry);
        log.debug("finish setSecretKeyEntry: {}", secretKeyEntry.getSecretKeyName());
    }

    private void setSecretKeyEntryInDb(final SecretKeyEntry secretKeyEntry) {

        log.debug("start setSecretKeyEntryInDb: {}", secretKeyEntry.getSecretKeyName());

        try (final DB db = DBMaker.fileDB(configuration.getMapDbFile())
                .transactionEnable()
                .fileMmapEnableIfSupported()
                .make()) {

            final BTreeMap<Object[], String> map = db.treeMap(MAP_NAME)
                    .keySerializer(new SerializerArrayTuple(STRING, STRING, STRING))
                    .valueSerializer(STRING)
                    .open();
            map.put(new Object[]{secretKeyEntry.getGroupId(), secretKeyEntry.getArtifactId(), secretKeyEntry.getSecretKeyName()},
                    secretKeyEntry.getSecretKeyValue());
//                    getEncoder().encodeToString(encryptCipherProvider.getCipher().doFinal(secretKeyEntry.getSecretKeyValue().getBytes())));
            db.commit();
//            log.info("aaa:{}", secretKeyEntry.getAlias());
//        } catch (Exception e) {
//            log.error("error", e);
        }
        final float a = getRuntime().totalMemory() - getRuntime().freeMemory();
        final float b = getRuntime().totalMemory();
        final float usedMemory = a / b;
        log.info("mem: total:{}MB, free:{}MB, %used:{}", getRuntime().totalMemory() / 1024 / 1024, getRuntime().freeMemory() / 1024 / 1024,
                format("%.1f", usedMemory * 100));
        log.debug("finish setSecretKeyEntryInDb: {}", secretKeyEntry.getSecretKeyName());
        // TODO - remove below
//        if (secretKeyEntry.getSecretKeyName().equalsIgnoreCase("now")) {
//            throw new RuntimeException("bla");
//        }
    }

    public synchronized void removeSecretKeyEntry(@Valid final SecretKeyEntryKeyName secretKeyEntryKeyName) {

        try (final DB db = DBMaker.fileDB(configuration.getMapDbFile())
                .transactionEnable()
                .fileMmapEnableIfSupported()
                .make()) {

            final BTreeMap<Object[], String> map = db.treeMap(MAP_NAME)
                    .keySerializer(new SerializerArrayTuple(STRING, STRING, STRING))
                    .valueSerializer(STRING)
                    .open();
//            final ConcurrentMap<String, String> map = db.hashMap(MAP_NAME, Serializer.STRING, Serializer.STRING).open();
            final String value = map.get(new Object[]{secretKeyEntryKeyName.getGroupId(), secretKeyEntryKeyName.getArtifactId(),
                    secretKeyEntryKeyName.getSecretKeyName()});

            if (nonNull(value)) {
                final boolean success = map.remove(new Object[]{secretKeyEntryKeyName.getGroupId(), secretKeyEntryKeyName.getArtifactId(),
                        secretKeyEntryKeyName.getSecretKeyName()}, value);
                if (success) {
                    db.commit();
                } else {
                    throw new IllegalStateException(format("removeEntry, attempted but failed: %s", secretKeyEntryKeyName));
                }
            } else {
                throw new NotFoundException(format("removeEntry: %s, not found", secretKeyEntryKeyName));
            }
//            map.put(secretKeyEntry.getAlias(), secretKeyEntry.getSecretKeyValue());
//        } catch (Exception e) {
//            log.error("error", e);
        }
    }

    public synchronized @NotNull String getSecretKeyEntry(@Valid final SecretKeyEntryKeyName secretKeyEntryKeyName) throws BadPaddingException,
            IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
//        final String secretKeyValue = getSecretKeyEntryFromDb(secretKeyEntryKeyName);
        return decryptSecretKeyValue(getSecretKeyEntryFromDb(secretKeyEntryKeyName));
    }

    private String decryptSecretKeyValue(final String secretKeyValue) throws BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException {

        final String[] parts = secretKeyValue.split(SECRET_KEY_VALUE_DELIMITER);
//        log.debug("parts[0]: {}", parts[0]);
//        log.debug("parts[1]: {}", parts[1]);

        // decrypt AES key
        final byte[] aesKeyAsByteArray = decryptCipherProvider.getCipher().doFinal(getDecoder().decode(parts[0]));
        final SecretKey aesKey = new SecretKeySpec(aesKeyAsByteArray, 0, aesKeyAsByteArray.length, AES_ALGORITHM_AND_TRANSFORMATION);
        final Cipher cipher = Cipher.getInstance(AES_ALGORITHM_AND_TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, aesKey);

        // decrypt secretKeyValue using AES key
        return new String(cipher.doFinal(getDecoder().decode(parts[1])));
    }

    private String getSecretKeyEntryFromDb(final SecretKeyEntryKeyName secretKeyEntryKeyName) {

        log.debug("start getSecretKeyEntryFromDb: {}", secretKeyEntryKeyName.getSecretKeyName());
        try (final DB db = DBMaker.fileDB(configuration.getMapDbFile())
                .readOnly()
                .fileMmapEnableIfSupported()
                .make()) {

            final BTreeMap<Object[], String> map = db.treeMap(MAP_NAME)
                    .keySerializer(new SerializerArrayTuple(STRING, STRING, STRING))
                    .valueSerializer(STRING)
                    .open();
//            final ConcurrentMap<String, String> map = db.hashMap(MAP_NAME, Serializer.STRING, Serializer.STRING).open();
//            log.info("bbb:{}", secretKeyEntryKeyName.getAlias());
            final String value = map.get(new Object[]{secretKeyEntryKeyName.getGroupId(), secretKeyEntryKeyName.getArtifactId(),
                    secretKeyEntryKeyName.getSecretKeyName()});

            if (nonNull(value)) {
                log.debug("finish getSecretKeyEntryFromDb: {}", secretKeyEntryKeyName.getSecretKeyName());
                return value;
//                return new String(decryptCipherProvider.getCipher().doFinal(getDecoder().decode(value)));
            } else {
                throw new NotFoundException(format("getSecretKeyEntry: %s, not found", secretKeyEntryKeyName));
            }
        }
    }

    public synchronized @NotNull @Size(min = 1) Map<String, String> getSecretKeyEntries(@Valid final SecretKeyEntryBase secretKeyEntryBase) {
        log.debug("start getSecretKeyEntries: {}, {}", secretKeyEntryBase.getGroupId(), secretKeyEntryBase.getArtifactId());
        final Map<String, String> results = new TreeMap<>();

        getSecretKeyEntriesFromDb(secretKeyEntryBase).forEach((key, value) -> {
            try {
//                log.info("aaa: {}, {}", key, value);
                results.put(key, decryptSecretKeyValue(value));
            } catch (IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
                throw new RuntimeException(e);
            }
        });
        log.debug("finish getSecretKeyEntries: {}, {}", secretKeyEntryBase.getGroupId(), secretKeyEntryBase.getArtifactId());
        return results;
    }

    private Map<String, String> getSecretKeyEntriesFromDb(final SecretKeyEntryBase secretKeyEntryBase) {
        log.debug("start getSecretKeyEntriesFromDb: {}, {}", secretKeyEntryBase.getGroupId(), secretKeyEntryBase.getArtifactId());
        try (final DB db = DBMaker.fileDB(configuration.getMapDbFile())
                .readOnly()
                .fileMmapEnableIfSupported()
                .make()) {

            final BTreeMap<Object[], String> map = db.treeMap(MAP_NAME)
                    .keySerializer(new SerializerArrayTuple(STRING, STRING, STRING))
                    .valueSerializer(STRING)
                    .open();
//            map.put(new Object[]{secretKeyEntry.getGroupId(), secretKeyEntry.getArtifactId(), secretKeyEntry.getSecretKeyName()},
//                    secretKeyEntry.getSecretKeyValue());
//            final ConcurrentMap<String, String> map = db.hashMap(MAP_NAME, STRING, STRING).open();
            final Map<String, String> results = new HashMap<>();

            map.prefixSubMap(new Object[]{secretKeyEntryBase.getGroupId(), secretKeyEntryBase.getArtifactId()}).forEach((key, value) -> {

                if (!((String) key[2]).startsWith(".")) {
//                    try {
                    final SecretKeyEntryKeyName secretKeyEntryKeyName = new SecretKeyEntryKeyName((String) key[0], (String) key[1], (String) key[2]);
                    results.put(secretKeyEntryKeyName.getAlias(), value);
//                        results.put(key, new String(decryptCipherProvider.getCipher().doFinal(getDecoder().decode(value))));
//                    } catch (IllegalBlockSizeException | BadPaddingException e) {
//                        throw new RuntimeException(e);
//                    }
                }
            });

            final float a = getRuntime().totalMemory() - getRuntime().freeMemory();
            final float b = getRuntime().totalMemory();
            final float usedMemory = a / b;
            log.info("mem entries: total:{}MB, free:{}MB, %used:{}", getRuntime().totalMemory() / 1024 / 1024, getRuntime().freeMemory() / 1024 / 1024,
                    format("%.1f", usedMemory * 100));
//            map.

//            log.info("bbb:{}", secretKeyEntryKeyName.getAlias());
//            final String value = map.get(secretKeyEntryKeyName.getAlias());

            if (!results.isEmpty()) {
                log.debug("finish getSecretKeyEntriesFromDb: {}, {}, count: {}", secretKeyEntryBase.getGroupId(), secretKeyEntryBase.getArtifactId(),
                        results.size());
                return results;
            } else {
                throw new NotFoundException(format("getSecretKeyEntries: %s, none found", secretKeyEntryBase));
            }
        }
    }
}
