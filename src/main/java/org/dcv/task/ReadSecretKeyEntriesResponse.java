package org.dcv.task;

import lombok.Value;

import java.util.Map;

@Value
public class ReadSecretKeyEntriesResponse {
    Map<String, String> secretKeyEntries;
    String exportedSecretKeyEntries;
    Exception exception;
}
