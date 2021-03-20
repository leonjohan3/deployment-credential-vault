package org.dcv.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Pattern;

import static org.dcv.util.Constants.ALIAS_NAME_PART_DELIMITER;
import static org.dcv.util.Constants.REQUEST_ENTRY_ITEM_PATTERN;

@AllArgsConstructor
//@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class SecretKeyEntryBase {

    @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN)
    @NonNull
    String groupId;

    @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN)
    @NonNull
    String artifactId;

    public String getAliasPrefix() {
        return groupId + ALIAS_NAME_PART_DELIMITER + artifactId + ALIAS_NAME_PART_DELIMITER;
    }
}
