package org.dcv.service;

import org.dcv.dto.SecretKeyEntry;
import org.dcv.dto.SecretKeyEntryBase;
import org.dcv.dto.SecretKeyEntryKeyName;
import org.dcv.task.ReadSecretKeyEntriesResponse;
import org.dcv.task.ReadSingleSecretKeyEntryResponse;
import org.springframework.stereotype.Service;

import static java.util.Map.Entry;
import static java.util.Objects.isNull;
import static org.dcv.util.AliasToEnvVariableConverter.convertToEnvVariable;

@Service
public class EntryReaderService {

    private KeystoreService keystoreService;

    public EntryReaderService(KeystoreService keystoreService) {
        this.keystoreService = keystoreService;
    }

    /**
     * Read a single entry from the keystore
     */
    public ReadSingleSecretKeyEntryResponse getSecretKeyEntry(final SecretKeyEntryKeyName secretKeyEntry) {
        return keystoreService.getSecretKeyEntry(secretKeyEntry);
    }

    /**
     * Read all entries from the keystore for a alias prefix and convert to export format
     */
    public ReadSecretKeyEntriesResponse getExportedSecretKeyEntries(final SecretKeyEntryBase secretKeyEntry) {
        final ReadSecretKeyEntriesResponse response = keystoreService.getSecretKeyEntries(secretKeyEntry);

        if (isNull(response.getException())) {
            final StringBuilder stringBuilder = new StringBuilder();

            for (final Entry<String, String> entry : response.getSecretKeyEntries().entrySet()) {
                stringBuilder.append(String.format("export %s=\"%s\"%n", convertToEnvVariable(entry.getKey()), entry.getValue()));
            }
            return new ReadSecretKeyEntriesResponse(response.getSecretKeyEntries(), stringBuilder.toString(), null);
        } else {
            return response;
        }
    }
}
