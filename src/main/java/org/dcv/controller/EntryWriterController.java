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

    // TODO - map constraint validation error
    @PostMapping(value = "/{groupId}/{artifactId}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
//    @GetMapping(value = "/read", produces = MediaType.TEXT_PLAIN_VALUE)
//    @ResponseBody
//    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> setSecretKeyEntry(@PathVariable @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN) final String groupId,
                                                    @PathVariable @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN) final String artifactId,
                                                    @RequestParam @NotNull final Map<String, String> secretKeyNames) {

        log.info("start setSecretKeyEntry");

        for (final Entry<String, String> entry : secretKeyNames.entrySet()) {
            final SecretKeyEntry secretKeyEntry = new SecretKeyEntry(groupId, artifactId, entry.getKey(), entry.getValue());
            final Set<ConstraintViolation<SecretKeyEntry>> validationResults = validator.validate(secretKeyEntry);

            if (!validationResults.isEmpty()) {
                throw new ConstraintViolationException(validationResults);
            }
            log.info("secretKeyEntry: {}, {}, {}, {}", groupId, artifactId, secretKeyEntry.getSecretKeyName(), log.isDebugEnabled() ?
                    secretKeyEntry.getSecretKeyValue() : MASKED_SECRET_KEY_VALUE);
            final Exception exception = entryWriterService.setSecretKeyEntry(secretKeyEntry);

            if (nonNull(exception)) {
                return ResponseEntity.badRequest().body(exception.getMessage());
            }
        }
//        log.info("aaa:{}", secretKeyNames);
        log.info("finish getSecretKeyEntry, count:{}", secretKeyNames.size());
        return ResponseEntity.status(CREATED).build();
//        return String.format("/%s/%s/%s", groupId, artifactId, secretKeyName);
    }

//    @GetMapping(value = "/read", produces = MediaType.TEXT_PLAIN_VALUE)
//    public @NotNull String getSecretKeyEntry() {
//        return String.format("/%s/%s/%s", "a", "b", "c");
//    }
}
