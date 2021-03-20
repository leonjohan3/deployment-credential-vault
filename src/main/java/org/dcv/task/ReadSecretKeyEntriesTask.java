package org.dcv.task;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.io.File;
import java.security.KeyStore;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.RecursiveTask;

import static java.util.Collections.EMPTY_MAP;
import static org.dcv.util.Constants.ALIAS_NAME_PART_DELIMITER;

@Slf4j
@Value
@EqualsAndHashCode(callSuper = true)
public class ReadSecretKeyEntriesTask extends RecursiveTask<ReadSecretKeyEntriesResponse> {

    Map<String, String> mdcContext;
    String keystorePassword;
    File keystoreFile;
    String aliasPrefix;

    @Override
    protected ReadSecretKeyEntriesResponse compute() {

        try {

            MDC.setContextMap(mdcContext);
            log.debug("start compute, keystore file:{}, alias prefix:{}", keystoreFile, aliasPrefix);
            final ReadSecretKeyEntriesResponse response = new ReadSecretKeyEntriesResponse(new TreeMap<>(), null, null);
            final char[] password = keystorePassword.toCharArray();
            final KeyStore keyStore = KeyStore.getInstance(keystoreFile, password);

            for (final Enumeration<String> aliases = keyStore.aliases(); aliases.hasMoreElements(); ) {
                final String alias = aliases.nextElement();
                final String[] aliasParts = alias.split(ALIAS_NAME_PART_DELIMITER);

//                if (keyStore.entryInstanceOf(alias, KeyStore.SecretKeyEntry.class) && alias.matches("^" + aliasPrefix)) {
                if (keyStore.entryInstanceOf(alias, KeyStore.SecretKeyEntry.class)
                        && aliasPrefix.equals(aliasParts[0] + ALIAS_NAME_PART_DELIMITER + aliasParts[1] + ALIAS_NAME_PART_DELIMITER)) {
                    log.debug("aaa:{}", alias);
                    final KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry) keyStore.getEntry(alias, new KeyStore.PasswordProtection(password));
                    response.getSecretKeyEntries().put(alias, new String(entry.getSecretKey().getEncoded()));
                }
            }
            log.debug("finish compute, map size: {}", response.getSecretKeyEntries().size());
            return response;

        } catch (Exception e) {
            log.error("compute", e);
            return new ReadSecretKeyEntriesResponse(EMPTY_MAP, null, e);
        } finally {
            MDC.clear();
        }
    }
}
