package org.dcv.controller;

import lombok.extern.slf4j.Slf4j;
import org.dcv.dto.SecretKeyEntry;
import org.dcv.dto.SecretKeyEntryKeyName;
import org.dcv.service.EntryService;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MultivaluedMap;
import java.util.List;

import static java.util.Map.Entry;
import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static org.dcv.util.Constants.ALIAS_NAME_PART_DELIMITER;
import static org.dcv.util.Constants.REQUEST_ENTRY_ITEM_PATTERN;
import static org.dcv.util.Constants.REQUEST_ENTRY_NAME_PATTERN;

@Slf4j
@Path("/v1/write")
public class EntryWriterController {

    @Inject
//    @Resource
//    private ValidatorFactory validatorFactory;
    private EntryService entryService;

    /**
     * This API inserts or updates entries to the secret store
     */
    @POST
    @Path("/{groupId}/{artifactId}")
//    @Produces(TEXT_PLAIN)
    @Consumes(APPLICATION_FORM_URLENCODED)
    public void setSecretKeyEntry(@PathParam("groupId") @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN) final String groupId,
                                             @PathParam("artifactId") @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN) final String artifactId,
                                             @NotNull final MultivaluedMap<String, String> secretKeyNames) throws BadPaddingException, IllegalBlockSizeException {

        log.info("start setSecretKeyEntry, groupId: {}, artifactId: {}, secretKeyNames.size: {}", groupId, artifactId, secretKeyNames.size());

        // double _ not allowed in part of the name as this is used for `getSecretKeyEntries`
        if (groupId.contains(ALIAS_NAME_PART_DELIMITER)) {
            throw new IllegalArgumentException(String.format("invalid groupId name: %s, using \"%s\" not permitted", groupId, ALIAS_NAME_PART_DELIMITER));
        }
        if (artifactId.contains(ALIAS_NAME_PART_DELIMITER)) {
            throw new IllegalArgumentException(String.format("invalid artifactId name: %s, using \"%s\" not permitted", artifactId,
                    ALIAS_NAME_PART_DELIMITER));
        }

        for (Entry<String, List<String>> entry : secretKeyNames.entrySet()) {
            if (entry.getKey().contains(ALIAS_NAME_PART_DELIMITER)) {
                throw new IllegalArgumentException(String.format("invalid entry name: %s, using \"%s\" not permitted", entry.getKey(),
                        ALIAS_NAME_PART_DELIMITER));
            }
            final SecretKeyEntry secretKeyEntry = new SecretKeyEntry(groupId, artifactId, entry.getKey(), entry.getValue().get(0));
            entryService.setSecretKeyEntry(secretKeyEntry);
//            final Validator validator = validatorFactory.getValidator();
//            final Set<ConstraintViolation<SecretKeyEntry>> validationResults = validator.validate(secretKeyEntry);
//
//            if (!validationResults.isEmpty()) {
//                throw new ConstraintViolationException(validationResults);
//            }

        }

//        return "string,";
//        return "string," + secretKeyNames.getFirst("abc");
    }

    /**
     * This API removes/deletes a single entry from the secret store
     */
    @DELETE
    @Path("/{groupId}/{artifactId}/{secretKeyName}")
    @Produces(TEXT_PLAIN)
    public void removeSecretKeyEntry(@PathParam("groupId") @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN) final String groupId,
                                     @PathParam("artifactId") @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN) final String artifactId,
                                     @PathParam("secretKeyName") @Pattern(regexp = REQUEST_ENTRY_NAME_PATTERN) final String secretKeyName) {

        log.info("start removeSecretKeyEntry, groupId: {}, artifactId: {}, secretKeyName: {}", groupId, artifactId, secretKeyName);
        final SecretKeyEntryKeyName secretKeyEntryKeyName = new SecretKeyEntryKeyName(groupId, artifactId, secretKeyName);
        entryService.removeSecretKeyEntry(secretKeyEntryKeyName);
        log.info("finish removeSecretKeyEntry, groupId: {}, artifactId: {}, secretKeyName: {}", groupId, artifactId, secretKeyName);
    }
}
