package org.dcv.dto;

import lombok.Value;

import javax.validation.constraints.Pattern;

/**
 * A response to read a secret key entry. The groupId and artifactId could be based on the same values when using a maven based project.
 * The secret key name is the name of the alias to retrieve from the keyfile
 * https://docs.oracle.com/en/java/javase/11/security/java-cryptography-architecture-jca-reference-guide.html#GUID-09050137-31F1-468A-A552-B051A4E35876
 */
@Value
public class ReadEntryResponse {
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z_0-9\\.]{3,}")
    String groupId;

    @Pattern(regexp = "^[a-zA-Z][a-zA-Z_0-9\\.]{3,}")
    String artifactId;

    @Pattern(regexp = "^[a-zA-Z][a-zA-Z_0-9\\.]{3,}")
    String secretKeyName;
}
