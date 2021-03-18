package org.dcv.controller;

import lombok.extern.slf4j.Slf4j;
import org.dcv.dto.SecretKeyEntry;
import org.dcv.service.EntryReaderService;
import org.dcv.task.ReadSingleSecretKeyEntryResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Pattern;

import static java.util.Objects.nonNull;
import static org.dcv.util.Constants.MASKED_SECRET_KEY_VALUE;
import static org.dcv.util.Constants.REQUEST_ENTRY_ITEM_PATTERN;

@RestController
@RequestMapping("/v1/read")
@Validated
@Slf4j
public class EntryReaderController {

//    @Autowired
//    ApplicationConfiguration applicationConfiguration;

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
//    @GetMapping(value = "/read", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getSecretKeyEntry(@PathVariable @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN) final String groupId,
                                                    @PathVariable @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN) final String artifactId,
                                                    @PathVariable @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN) final String secretKeyName)
            throws InterruptedException {

        log.info("start getSecretKeyEntry: {}, {}, {}", groupId, artifactId, secretKeyName);
        final ReadSingleSecretKeyEntryResponse readSingleSecretKeyEntryResponse = entryReaderService.getSecretKeyEntry(new SecretKeyEntry(groupId,
                artifactId, secretKeyName, null));
//        groupId, artifactId,
//                secretKeyName);
        ResponseEntity<String> response;

        if (nonNull(readSingleSecretKeyEntryResponse.getException())) {
            response = ResponseEntity.badRequest().build();
        } else {

            if (nonNull(readSingleSecretKeyEntryResponse.getSecretKeyEntry())) {
                log.info("finish getSecretKeyEntry: {}", log.isDebugEnabled() ? readSingleSecretKeyEntryResponse.getSecretKeyEntry() :
                        MASKED_SECRET_KEY_VALUE);
                response = ResponseEntity.ok().body(readSingleSecretKeyEntryResponse.getSecretKeyEntry());
            } else {
                log.warn("finish getSecretKeyEntry: not found");
                response = ResponseEntity.notFound().build();
            }
        }
//
        return response;
//        return secretKeyValue;
//        return String.format("/%s/%s/%s", groupId, artifactId, secretKeyName);
    }

//    @GetMapping(value = "/read", produces = MediaType.TEXT_PLAIN_VALUE)
//    public @NotNull String getSecretKeyEntry() {
//        return String.format("/%s/%s/%s", "a", "b", "c");
//    }
}
