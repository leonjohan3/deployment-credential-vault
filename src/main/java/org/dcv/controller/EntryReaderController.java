package org.dcv.controller;

import lombok.extern.slf4j.Slf4j;
import org.dcv.dto.SecretKeyEntryBase;
import org.dcv.dto.SecretKeyEntryKeyName;
import org.dcv.service.EntryService;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import static java.util.Map.Entry;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static org.dcv.util.AliasToEnvVariableConverter.convertToEnvVariable;
import static org.dcv.util.Constants.REQUEST_ENTRY_ITEM_PATTERN;
import static org.dcv.util.Constants.REQUEST_ENTRY_NAME_PATTERN;

@Slf4j
@Path("/v1/read")
public class EntryReaderController {

    @Inject
    private EntryService entryService;

    /**
     * This API reads a single entry from the secret store
     */
    @GET
    @Path("/{groupId}/{artifactId}/{secretKeyName}")
    @Produces(TEXT_PLAIN)
    public @NotNull String getSecretKeyEntry(@PathParam("groupId") @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN) final String groupId,
                                             @PathParam("artifactId") @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN) final String artifactId,
                                             @PathParam("secretKeyName") @Pattern(regexp = REQUEST_ENTRY_NAME_PATTERN) final String secretKeyName) throws BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {

        log.info("start getSecretKeyEntry, groupId: {}, artifactId: {}, secretKeyName: {}", groupId, artifactId, secretKeyName);
        final String result = entryService.getSecretKeyEntry(new SecretKeyEntryKeyName(groupId, artifactId, secretKeyName));
//        final String result =
//                "valuevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevaluevalue";
        log.info("finish getSecretKeyEntry, groupId: {}, artifactId: {}, secretKeyName: {}", groupId, artifactId, secretKeyName);
        return result;
    }

    /**
     * This API reads all entries from the secret store for an alias prefix and provides them in bash export format
     */
    @GET
    @Path(value = "/{groupId}/{artifactId}")
    @Produces(TEXT_PLAIN)
    public @NotNull String getSecretKeyEntries(@PathParam("groupId") @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN) final String groupId,
                                               @PathParam("artifactId") @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN) final String artifactId) {

        log.info("start getSecretKeyEntries, groupId: {}, artifactId: {}", groupId, artifactId);
        final Map<String, String> entries = entryService.getSecretKeyEntries(new SecretKeyEntryBase(groupId, artifactId));
        final StringBuilder stringBuilder = new StringBuilder();

        for (final Entry<String, String> entry : entries.entrySet()) {
            stringBuilder.append(String.format("export %s=\"%s\"%n", convertToEnvVariable(entry.getKey()), entry.getValue()));
        }
        log.info("finish getSecretKeyEntries, groupId: {}, artifactId: {}, count:{}", groupId, artifactId, entries.size());
        return stringBuilder.toString();
    }
}
