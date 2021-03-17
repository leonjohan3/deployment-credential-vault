package org.dcv.dto;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static org.dcv.util.Constants.ALIAS_NAME_PART_DELIMITER;
import static org.dcv.util.Constants.REQUEST_ENTRY_ITEM_PATTERN;

@Value
public class SecretKeyEntry {

    @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN)
    String groupId;

    @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN)
    String artifactId;

    @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN)
    String secretKeyName;

    @NotBlank
    String secretKeyValue;

    public String getAlias() {
        return groupId + ALIAS_NAME_PART_DELIMITER + artifactId + ALIAS_NAME_PART_DELIMITER + secretKeyName;
    }
}
