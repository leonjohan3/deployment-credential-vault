package org.dcv.service;

import org.dcv.dto.SecretKeyEntry;
import org.springframework.stereotype.Service;

@Service
public class EntryWriterService {

    private KeystoreService keystoreService;

    public EntryWriterService(KeystoreService keystoreService) {
        this.keystoreService = keystoreService;
    }

    /**
     * Write an entry to the keystore
     */
    public Exception setSecretKeyEntry(final SecretKeyEntry secretKeyEntry) {
        return keystoreService.setSecretKeyEntry(secretKeyEntry);
    }
}
