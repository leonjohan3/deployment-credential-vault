package org.dcv.controller;

import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.status;
import static org.dcv.util.Constants.ERROR_MESSAGE_HEADER_NAME;

@Slf4j
@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(final NotFoundException notFoundException) {
//        log.info("{}", exception.toString());
        log.warn(notFoundException.toString());

//        if (exception instanceof IllegalArgumentException) {
//            return status(BAD_REQUEST).header(ERROR_MESSAGE_HEADER_NAME, exception.getMessage()).entity(exception.getMessage()).build();
//        } else {
        return status(NOT_FOUND).header(ERROR_MESSAGE_HEADER_NAME, notFoundException.toString()).build();
//        }
    }
}
