package org.dcv.dto;

import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static org.dcv.util.Constants.MAX_SECRET_VALUE_LENGTH;

@Value
@EqualsAndHashCode(callSuper = true)
public class SecretKeyEntry extends SecretKeyEntryKeyName {
    @NotBlank
    @Size(max = MAX_SECRET_VALUE_LENGTH)
    String secretKeyValue;

    public SecretKeyEntry(final String groupId, final String artifactId, final String secretKeyName, final String secretKeyValue) {
        super(groupId, artifactId, secretKeyName);
        this.secretKeyValue = secretKeyValue;
    }
}
