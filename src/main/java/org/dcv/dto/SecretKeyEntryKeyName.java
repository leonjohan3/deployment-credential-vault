package org.dcv.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import javax.validation.constraints.Pattern;

import static org.dcv.util.Constants.ALIAS_NAME_PART_DELIMITER;
import static org.dcv.util.Constants.REQUEST_ENTRY_ITEM_PATTERN;

//@AllArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = true)
public class SecretKeyEntryKeyName extends SecretKeyEntryBase {

    @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN)
    @NonNull
    String secretKeyName;

    public SecretKeyEntryKeyName(final String groupId, final String artifactId, final String secretKeyName) {
        super(groupId, artifactId);
        this.secretKeyName = secretKeyName;
    }

    public String getAlias() {
        return groupId + ALIAS_NAME_PART_DELIMITER + artifactId + ALIAS_NAME_PART_DELIMITER + secretKeyName;
    }

}
