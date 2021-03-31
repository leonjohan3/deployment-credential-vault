package org.dcv.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static org.dcv.util.Constants.ALIAS_NAME_PART_DELIMITER;
import static org.dcv.util.Constants.MAX_SECRET_KEY_LENGTH;
import static org.dcv.util.Constants.REQUEST_ENTRY_ITEM_PATTERN;

@AllArgsConstructor
//@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class SecretKeyEntryBase {

    @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN)
    @Size(max = MAX_SECRET_KEY_LENGTH)
//    @NonNull
    private String groupId;

    @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN)
    @Size(max = MAX_SECRET_KEY_LENGTH)
//    @NonNull
    private String artifactId;

    public String getAliasPrefix() {
        return groupId + ALIAS_NAME_PART_DELIMITER + artifactId + ALIAS_NAME_PART_DELIMITER;
    }
}
