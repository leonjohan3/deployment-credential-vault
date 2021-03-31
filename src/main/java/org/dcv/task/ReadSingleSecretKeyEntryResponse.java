package org.dcv.task;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import static java.util.Objects.nonNull;

//@Value
//@RequiredArgsConstructor
@NoArgsConstructor
@Getter
public class ReadSingleSecretKeyEntryResponse {
    private String secretKeyEntry;
    private Exception exception;

    public ReadSingleSecretKeyEntryResponse(@NonNull final String secretKeyEntry) {
        this.secretKeyEntry = secretKeyEntry;
    }

    public ReadSingleSecretKeyEntryResponse(@NonNull final Exception exception) {
        this.exception = exception;
    }

    public boolean hasException() {
        return nonNull(exception);
    }
}
