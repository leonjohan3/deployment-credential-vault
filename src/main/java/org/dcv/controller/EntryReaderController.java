package org.dcv.controller;

import lombok.extern.slf4j.Slf4j;
import org.dcv.dto.SecretKeyEntryBase;
import org.dcv.dto.SecretKeyEntryKeyName;
import org.dcv.service.EntryReaderService;
import org.dcv.task.ReadSecretKeyEntriesResponse;
import org.dcv.task.ReadSingleSecretKeyEntryResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Pattern;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.dcv.util.Constants.ERROR_MESSAGE_HEADER_NAME;
import static org.dcv.util.Constants.MASKED_SECRET_KEY_VALUE;
import static org.dcv.util.Constants.REQUEST_ENTRY_ITEM_PATTERN;

@RestController
@RequestMapping("/v1/read")
@Validated
@Slf4j
public class EntryReaderController {

    private final EntryReaderService entryReaderService;

    public EntryReaderController(final EntryReaderService entryReaderService) {
        this.entryReaderService = entryReaderService;
    }

    // TODO - map constraint validation error
    // TODO - add open API docs

    /**
     * This API reads a single entry from the keystore
     */
    @GetMapping(value = "/{groupId}/{artifactId}/{secretKeyName}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getSecretKeyEntry(@PathVariable @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN) final String groupId,
                                                    @PathVariable @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN) final String artifactId,
                                                    @PathVariable @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN) final String secretKeyName) {

        log.info("start getSecretKeyEntry: {}, {}, {}", groupId, artifactId, secretKeyName);
        final ReadSingleSecretKeyEntryResponse readSingleSecretKeyEntryResponse = entryReaderService.getSecretKeyEntry(new SecretKeyEntryKeyName(groupId,
                artifactId, secretKeyName));
        ResponseEntity<String> response;

        if (readSingleSecretKeyEntryResponse.hasException()) {
//            response = ResponseEntity.badRequest().build();
            response = ResponseEntity.badRequest().header(ERROR_MESSAGE_HEADER_NAME, readSingleSecretKeyEntryResponse.getException().getMessage()).build();
        } else {

            if (nonNull(readSingleSecretKeyEntryResponse.getSecretKeyEntry())) {
                log.info("finish getSecretKeyEntry: {}", log.isTraceEnabled() ? readSingleSecretKeyEntryResponse.getSecretKeyEntry() :
                        MASKED_SECRET_KEY_VALUE);
                response = ResponseEntity.ok().body(readSingleSecretKeyEntryResponse.getSecretKeyEntry());
            } else {
                log.warn("finish getSecretKeyEntry: not found");
                response = ResponseEntity.notFound().build();
            }
        }
        return response;
    }

    /**
     * This API reads all entries from the keystore for an alias prefix and provides them in bash export format
     */
    @GetMapping(value = "/{groupId}/{artifactId}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getSecretKeyEntries(@PathVariable @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN) final String groupId,
                                                      @PathVariable @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN) final String artifactId) {

        log.info("start getSecretKeyEntries: {}, {}", groupId, artifactId);
        // TODO - use inheritance for SecretKeyEntry sub types [done]
        final ReadSecretKeyEntriesResponse readSecretKeyEntriesResponse = entryReaderService.getExportedSecretKeyEntries(new SecretKeyEntryBase(groupId,
                artifactId));
        ResponseEntity<String> response;

        if (readSecretKeyEntriesResponse.hasException()) {
            // TODO - change other 400s and 404s
            response = ResponseEntity.badRequest().header(ERROR_MESSAGE_HEADER_NAME, readSecretKeyEntriesResponse.getException().getMessage()).build();
        } else {

            if (isNull(readSecretKeyEntriesResponse.getExportedSecretKeyEntries())) {
                log.warn("finish getSecretKeyEntries: not found");
//                response = ResponseEntity.notFound().build();
                response = ResponseEntity.notFound().header(ERROR_MESSAGE_HEADER_NAME, String.format("nothing found for groupId: %s and artifactId: %s", groupId, artifactId)).build();
            } else {
                log.info("finish getSecretKeyEntries, count: {}", readSecretKeyEntriesResponse.getSecretKeyEntries().size());
                response = ResponseEntity.ok().body(readSecretKeyEntriesResponse.getExportedSecretKeyEntries());
            }
        }
        return response;
    }
}
