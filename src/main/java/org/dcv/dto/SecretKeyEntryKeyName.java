package org.dcv.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static org.dcv.util.Constants.MAX_SECRET_KEY_LENGTH;
import static org.dcv.util.Constants.REQUEST_ENTRY_NAME_PATTERN;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SecretKeyEntryKeyName extends SecretKeyEntryBase {

    @Pattern(regexp = REQUEST_ENTRY_NAME_PATTERN)
    @Size(max = MAX_SECRET_KEY_LENGTH)
    private final String secretKeyName;

    public SecretKeyEntryKeyName(final String groupId, final String artifactId, final String secretKeyName) {
        super(groupId, artifactId);
        this.secretKeyName = secretKeyName.toLowerCase();
    }

    public String getAlias() {
        return getAliasPrefix() + secretKeyName;
    }
}
