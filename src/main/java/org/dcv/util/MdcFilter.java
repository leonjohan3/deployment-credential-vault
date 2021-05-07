package org.dcv.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

import static java.util.Objects.nonNull;
import static org.dcv.util.Constants.CORRELATION_ID_HEADER;

@Slf4j
@Provider
public class MdcFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Override
    public void filter(final ContainerRequestContext containerRequestContext) throws IOException {
        removeCorrIdFromMdcIfExist();
        final String corrId = containerRequestContext.getHeaderString(CORRELATION_ID_HEADER);

        if (nonNull(corrId)) {
            MDC.put(CORRELATION_ID_HEADER, corrId);
            log.debug("add MDC, corrId: {}", corrId);
        }
    }

    @Override
    public void filter(final ContainerRequestContext containerRequestContext, final ContainerResponseContext containerResponseContext) throws IOException {
//        log.debug("should remove MDC: {}", containerResponseContext.getStatus());
        removeCorrIdFromMdcIfExist();
    }

    private void removeCorrIdFromMdcIfExist() {
        final String corrIdOnMdc = MDC.get(CORRELATION_ID_HEADER);

        if (nonNull(corrIdOnMdc)) {
            MDC.remove(CORRELATION_ID_HEADER);
            log.debug("removed from MDC, corrId: {}", corrIdOnMdc);
        }
    }
}
