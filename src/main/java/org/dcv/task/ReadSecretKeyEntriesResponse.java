package org.dcv.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.Map;

import static java.util.Collections.EMPTY_MAP;
import static java.util.Objects.nonNull;

@RequiredArgsConstructor
@Getter
public class ReadSecretKeyEntriesResponse {
    @NonNull
//    @lombok.NonNull
    private final Map<String, String> secretKeyEntries;

    //    @Setter
//    @NonNull
    private String exportedSecretKeyEntries;

    //    @Setter
//    @NonNull
    private Exception exception;

    public ReadSecretKeyEntriesResponse(@NonNull final Map<String, String> secretKeyEntries, @NonNull final String exportedSecretKeyEntries) {
        this.secretKeyEntries = secretKeyEntries;
        this.exportedSecretKeyEntries = exportedSecretKeyEntries;
    }

//    public ReadSecretKeyEntriesResponse(@NonNull final String exportedSecretKeyEntries) {
//        this.secretKeyEntries = EMPTY_MAP;
//        this.exportedSecretKeyEntries = of(exportedSecretKeyEntries);
//    }

    public ReadSecretKeyEntriesResponse(@NonNull final Exception exception) {
        this.secretKeyEntries = EMPTY_MAP;
        this.exception = exception;
    }

    public boolean hasException() {
        return nonNull(exception);
    }
}
