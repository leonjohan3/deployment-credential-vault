package org.dcv.controller;

import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.status;
import static org.dcv.util.Constants.ERROR_MESSAGE_HEADER_NAME;

@Slf4j
@Provider
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {

    @Override
    public Response toResponse(final IllegalArgumentException illegalArgumentException) {
        log.error(illegalArgumentException.toString(), illegalArgumentException);
        return status(BAD_REQUEST).header(ERROR_MESSAGE_HEADER_NAME,
                illegalArgumentException.toString()).entity(illegalArgumentException.toString()).build();
    }
}
