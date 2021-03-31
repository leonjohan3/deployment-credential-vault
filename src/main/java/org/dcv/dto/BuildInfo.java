package org.dcv.dto;

import lombok.Value;

import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

import static org.dcv.util.Constants.REQUEST_ENTRY_ITEM_PATTERN;

@Value
public class BuildInfo {
    @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN)
    String buildGroup;

    @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN)
    String buildArtifact;

    @Pattern(regexp = "^\\d[A-Z0-9\\.\\-]{3,}")
    String version;

    @PastOrPresent
    LocalDateTime buildTime;
}
