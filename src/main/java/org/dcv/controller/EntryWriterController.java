package org.dcv.controller;

import lombok.extern.slf4j.Slf4j;
import org.dcv.dto.SecretKeyEntry;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Map;
import java.util.Set;

import static java.util.Map.Entry;

import static org.dcv.util.Constants.REQUEST_ENTRY_ITEM_PATTERN;

@RestController
@RequestMapping("/v1/write")
@Validated
@Slf4j
public class EntryWriterController {

    private final Validator validator;

    public EntryWriterController(final Validator validator) {
        this.validator = validator;
    }

    // TODO - map constraint validation error
    @PostMapping(value = "/{groupId}/{artifactId}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
//    @GetMapping(value = "/read", produces = MediaType.TEXT_PLAIN_VALUE)
//    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public void setSecretKeyEntry(@PathVariable @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN) final String groupId,
                                  @PathVariable @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN) final String artifactId,
                                  @RequestParam @NotNull final Map<String, String> secretKeyNames) {

        for (final Entry<String, String> entry : secretKeyNames.entrySet()) {
            final SecretKeyEntry secretKeyEntry = new SecretKeyEntry(groupId, artifactId, entry.getKey(), entry.getValue());
            final Set<ConstraintViolation<SecretKeyEntry>> validationResults = validator.validate(secretKeyEntry);

            if (!validationResults.isEmpty()) {
                throw new ConstraintViolationException(validationResults);
            }
        }
        log.info("aaa:{}", secretKeyNames);
//        return String.format("/%s/%s/%s", groupId, artifactId, secretKeyName);
    }

//    @GetMapping(value = "/read", produces = MediaType.TEXT_PLAIN_VALUE)
//    public @NotNull String getSecretKeyEntry() {
//        return String.format("/%s/%s/%s", "a", "b", "c");
//    }
}
