package org.dcv.controller;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static org.dcv.util.Constants.REQUEST_ENTRY_ITEM_PATTERN;

@RestController
@RequestMapping("/v1/read")
@Validated
public class EntryReaderController {

    // TODO - map constraint validation error
    @GetMapping(value = "/{groupId}/{artifactId}/{secretKeyName}", produces = MediaType.TEXT_PLAIN_VALUE)
//    @GetMapping(value = "/read", produces = MediaType.TEXT_PLAIN_VALUE)
    public @NotBlank String getSecretKeyEntry(@PathVariable @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN) final String groupId,
                                              @PathVariable @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN) final String artifactId,
                                              @PathVariable @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN) final String secretKeyName) {
        return String.format("/%s/%s/%s", groupId, artifactId, secretKeyName);
    }

//    @GetMapping(value = "/read", produces = MediaType.TEXT_PLAIN_VALUE)
//    public @NotNull String getSecretKeyEntry() {
//        return String.format("/%s/%s/%s", "a", "b", "c");
//    }
}
