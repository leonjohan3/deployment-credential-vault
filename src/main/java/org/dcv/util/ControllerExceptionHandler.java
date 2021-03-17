package org.dcv.util;

import lombok.extern.slf4j.Slf4j;
import org.dcv.dto.Error;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handle(ConstraintViolationException constraintViolationException) {
//        MDC.setContextMap();
//        MDC.getCopyOfContextMap();
        log.error("", constraintViolationException);
        return new Error(constraintViolationException.getMessage());
    }
}
