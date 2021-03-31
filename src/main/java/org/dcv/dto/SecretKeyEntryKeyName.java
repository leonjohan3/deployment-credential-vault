package org.dcv.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static org.dcv.util.Constants.ALIAS_NAME_PART_DELIMITER;
import static org.dcv.util.Constants.MAX_SECRET_KEY_LENGTH;
import static org.dcv.util.Constants.REQUEST_ENTRY_ITEM_PATTERN;

//@AllArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = true)
public class SecretKeyEntryKeyName extends SecretKeyEntryBase {

    @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN)
    @Size(max = MAX_SECRET_KEY_LENGTH)
//    @NonNull
    private String secretKeyName;

    public SecretKeyEntryKeyName(final String groupId, final String artifactId, final String secretKeyName) {
        super(groupId, artifactId);
        this.secretKeyName = secretKeyName;
    }

    public String getAlias() {
        return getAliasPrefix() + secretKeyName;
    }

}
