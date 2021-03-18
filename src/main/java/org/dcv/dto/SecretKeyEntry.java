package org.dcv.dto;

import lombok.NonNull;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static org.dcv.util.Constants.ALIAS_NAME_PART_DELIMITER;
import static org.dcv.util.Constants.REQUEST_ENTRY_ITEM_PATTERN;

@Value
public class SecretKeyEntry {

//    public SecretKeyEntry(final String groupId, final String artifactId, final String secretKeyName) {
//        this(groupId, artifactId, secretKeyName, null);
//    }

    @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN)
    @NonNull
    String groupId;

    @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN)
    @NonNull
    String artifactId;

    @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN)
    @NonNull
    String secretKeyName;

    @NotBlank
    String secretKeyValue;

    public String getAlias() {
        return groupId + ALIAS_NAME_PART_DELIMITER + artifactId + ALIAS_NAME_PART_DELIMITER + secretKeyName;
    }
}
