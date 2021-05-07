package org.dcv.controller;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.status;
import static org.dcv.util.Constants.ERROR_MESSAGE_HEADER_NAME;

@Slf4j
@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    private static final int SECRET_KEY_VALUE_ERROR_LENGTH = 1024;

    @Override
    public Response toResponse(final ConstraintViolationException constraintViolationException) {
        if (constraintViolationException.toString().toLowerCase().contains("secretkeyvalue")) {
            String errorMessage = constraintViolationException.toString();

            if (constraintViolationException.toString().length() > (SECRET_KEY_VALUE_ERROR_LENGTH * 2)) {

                final String preErrorMessage = constraintViolationException.toString().substring(0, SECRET_KEY_VALUE_ERROR_LENGTH);
                final String postErrorMessage =
                        constraintViolationException.toString().substring(constraintViolationException.toString().length() - SECRET_KEY_VALUE_ERROR_LENGTH);
                errorMessage = preErrorMessage + "..." + postErrorMessage;
            }
            log.error(errorMessage);
            return status(BAD_REQUEST).header(ERROR_MESSAGE_HEADER_NAME, errorMessage).entity(errorMessage).build();
        } else {
            log.error(constraintViolationException.toString(), constraintViolationException);
            return status(BAD_REQUEST).header(ERROR_MESSAGE_HEADER_NAME,
                    constraintViolationException.toString()).entity(constraintViolationException.toString()).build();
        }
    }
}
