package org.dcv.service;

import org.dcv.dto.SecretKeyEntry;
import org.dcv.task.ReadSingleSecretKeyEntryResponse;
import org.springframework.stereotype.Service;

@Service
public class EntryReaderService {

    private KeystoreService keystoreService;

    public EntryReaderService(KeystoreService keystoreService) {
        this.keystoreService = keystoreService;
    }

    /**
     * Read a single entry from the keystore
     */
    public ReadSingleSecretKeyEntryResponse getSecretKeyEntry(final SecretKeyEntry secretKeyEntry) {
        return keystoreService.getSecretKeyEntry(secretKeyEntry);
    }
}
