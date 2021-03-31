package org.dcv.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.dcv.util.Constants.CORRELATION_ID_HEADER;
import static org.dcv.util.Constants.CORRELATION_ID_LENGTH;

@Slf4j
public class MdcServletInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {

        removeCorrIdFromMdcIfExist();

        String corrIdOnRequest = request.getHeader(CORRELATION_ID_HEADER);
        final String corrIdOnResponse = response.getHeader(CORRELATION_ID_HEADER);
        log.debug("corrIdOnRequest: {}, corrIdOnResponse: {}", corrIdOnRequest, corrIdOnResponse);

        if (nonNull(corrIdOnResponse)) {
            corrIdOnRequest = corrIdOnResponse;
        }

        // if request does not have a corrId header, generate one
        if (isNull(corrIdOnRequest)) {
            corrIdOnRequest = randomAlphanumeric(CORRELATION_ID_LENGTH).toLowerCase();
            log.debug("generated random corrId: {}", corrIdOnRequest);
        }
        MDC.put(CORRELATION_ID_HEADER, corrIdOnRequest);

        if (isNull(corrIdOnResponse) && CORRELATION_ID_LENGTH == corrIdOnRequest.length()) {
            response.addHeader(CORRELATION_ID_HEADER, corrIdOnRequest);
            log.debug("added HTTP header corrId: {}", corrIdOnRequest);
        }
        return true;
    }

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ex) {
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
