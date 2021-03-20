package org.dcv.dto;

import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
@EqualsAndHashCode(callSuper = true)
public class SecretKeyEntry extends SecretKeyEntryKeyName {
    @NotBlank
    String secretKeyValue;

    public SecretKeyEntry(final String groupId, final String artifactId, final String secretKeyName, final String secretKeyValue) {
        super(groupId, artifactId, secretKeyName);
        this.secretKeyValue = secretKeyValue;
    }
}
