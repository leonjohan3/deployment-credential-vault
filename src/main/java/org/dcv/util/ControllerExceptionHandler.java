package org.dcv.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public String handle(ConstraintViolationException constraintViolationException) {
//        MDC.setContextMap();
//        MDC.getCopyOfContextMap();
        log.error("handle", constraintViolationException);
        return constraintViolationException.getMessage();
    }
}
