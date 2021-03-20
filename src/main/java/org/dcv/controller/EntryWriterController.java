package org.dcv.controller;

import lombok.extern.slf4j.Slf4j;
import org.dcv.dto.SecretKeyEntry;
import org.dcv.service.EntryWriterService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Map;
import java.util.Set;

import static java.util.Map.Entry;
import static java.util.Objects.nonNull;
import static org.dcv.util.Constants.ALIAS_NAME_PART_DELIMITER;
import static org.dcv.util.Constants.ERROR_MESSAGE_HEADER_NAME;
import static org.dcv.util.Constants.MASKED_SECRET_KEY_VALUE;
import static org.dcv.util.Constants.REQUEST_ENTRY_ITEM_PATTERN;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/v1/write")
@Validated
@Slf4j
public class EntryWriterController {

    private final Validator validator;
    private final EntryWriterService entryWriterService;

    public EntryWriterController(final Validator validator, final EntryWriterService entryWriterService) {
        this.validator = validator;
        this.entryWriterService = entryWriterService;
    }
/*
//    @PostMapping(value = "/bla", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    @PostMapping(value = "/bla", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @PostMapping(value = "/bla/{groupId}/{artifactId}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
//    @GetMapping(value = "/bla", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(CREATED)
//    public String test(@Valid @RequestBody final ProcessingError processingError) {
    public String test(@PathVariable @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN) final String groupId,
                       @PathVariable @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN) final String artifactId,
                       @RequestParam @NotNull final Map<String, String> secretKeyNames) {
//    public ProcessingError test() {
//        log.info("aaa:{}", processingError.getErrorMessage());
        log.info("start setSecretKeyEntry");
        log.info("aaa:{}", secretKeyNames);
//        log.info("aaa:");
        if (groupId.contains(ALIAS_NAME_PART_DELIMITER)) {
            throw new IllegalArgumentException(String.format("invalid groupId name: %s, using \"%s\"", groupId, ALIAS_NAME_PART_DELIMITER));
        }
        if (artifactId.contains(ALIAS_NAME_PART_DELIMITER)) {
            throw new IllegalArgumentException(String.format("invalid artifactId name: %s, using \"%s\"", artifactId, ALIAS_NAME_PART_DELIMITER));
        }
        return secretKeyNames.get("errorMessage").toUpperCase() + groupId + artifactId;
//        return processingError.getErrorMessage().toUpperCase();
    }
 */

    // TODO - map constraint validation error
    /*
     */

    @PostMapping(value = "/{groupId}/{artifactId}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> setSecretKeyEntry(@PathVariable @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN) final String groupId,
                                                    @PathVariable @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN) final String artifactId,
                                                    @RequestParam @NotNull final Map<String, String> secretKeyNames) {

        log.info("start setSecretKeyEntry");

        // TODO - add exception handler and write tests for these
        if (groupId.contains(ALIAS_NAME_PART_DELIMITER)) {
            throw new IllegalArgumentException(String.format("invalid groupId name: %s, using \"%s\"", groupId, ALIAS_NAME_PART_DELIMITER));
        }
        if (artifactId.contains(ALIAS_NAME_PART_DELIMITER)) {
            throw new IllegalArgumentException(String.format("invalid artifactId name: %s, using \"%s\"", artifactId, ALIAS_NAME_PART_DELIMITER));
        }

        for (final Entry<String, String> entry : secretKeyNames.entrySet()) {

            if (entry.getKey().contains(ALIAS_NAME_PART_DELIMITER)) {
                throw new IllegalArgumentException(String.format("invalid entry name: %s, using \"%s\"", entry.getKey(), ALIAS_NAME_PART_DELIMITER));
            }
            final SecretKeyEntry secretKeyEntry = new SecretKeyEntry(groupId, artifactId, entry.getKey(), entry.getValue());
            final Set<ConstraintViolation<SecretKeyEntry>> validationResults = validator.validate(secretKeyEntry);

            if (!validationResults.isEmpty()) {
                throw new ConstraintViolationException(validationResults);
            }
            log.info("secretKeyEntry: {}, {}, {}, {}", groupId, artifactId, secretKeyEntry.getSecretKeyName(), log.isTraceEnabled() ?
                    secretKeyEntry.getSecretKeyValue() : MASKED_SECRET_KEY_VALUE);
            final Exception exception = entryWriterService.setSecretKeyEntry(secretKeyEntry);

            if (nonNull(exception)) {
//                return ResponseEntity.badRequest().body(exception.getMessage());
                return ResponseEntity.badRequest().header(ERROR_MESSAGE_HEADER_NAME, exception.getMessage()).body(exception.getMessage());
            }
        }
        log.info("finish getSecretKeyEntry, count:{}", secretKeyNames.size());
        return ResponseEntity.status(CREATED).build();
    }
}
