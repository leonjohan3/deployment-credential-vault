package org.dcv.task;

import lombok.Value;

@Value
public class ReadSingleSecretKeyEntryResponse {
    String secretKeyEntry;
    Exception exception;
}
