package org.dcv.util;

import lombok.extern.slf4j.Slf4j;
import org.dcv.dto.ProcessingError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

import static org.dcv.util.Constants.ERROR_MESSAGE_HEADER_NAME;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler({ConstraintViolationException.class, IllegalArgumentException.class})
//    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ProcessingError> handle(final Exception exception) {
        log.error("handle", exception);
        return ResponseEntity.badRequest().header(ERROR_MESSAGE_HEADER_NAME, exception.getMessage()).body(new ProcessingError(exception.getMessage()));
//        return new ProcessingError(exception.getMessage());
    }
}
