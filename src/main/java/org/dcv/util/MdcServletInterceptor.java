package org.dcv.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.dcv.util.Constants.CORRELATION_ID_HEADER;
import static org.dcv.util.Constants.CORRELATION_ID_LENGTH;

@Slf4j
//@Component
public class MdcServletInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {

        log.info("pre: {}", MDC.getCopyOfContextMap());
        removeCorrIdFromMdcIfExist();

        String corrIdOnRequest = request.getHeader(CORRELATION_ID_HEADER);
        final String corrIdOnResponse = response.getHeader(CORRELATION_ID_HEADER);
        log.info("corrIdOnResponse: {}", corrIdOnResponse);

        if (nonNull(corrIdOnResponse)) {
            corrIdOnRequest = corrIdOnResponse;
        }

        // if request does not have a corrId header, generate one
        if (isNull(corrIdOnRequest)) {
            corrIdOnRequest = RandomStringUtils.randomAlphanumeric(CORRELATION_ID_LENGTH).toLowerCase();
        }
        if (isNull(corrIdOnResponse)) {
            response.addHeader(CORRELATION_ID_HEADER, corrIdOnRequest);
        }

//        if (nonNull(corrId)) {
        MDC.put(CORRELATION_ID_HEADER, corrIdOnRequest);
//        }
        log.info("pre");
        return true;
    }

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ex) {
        log.info("afterC before");
//        final String corrIdOnMdc = MDC.get(CORRELATION_ID_HEADER);
        removeCorrIdFromMdcIfExist();
//        log.info("corrIdOnMdc: {}", corrIdOnMdc);
//        final String corrId = request.getHeader(CORRELATION_ID_HEADER);

//        if (isNull(corrId) && nonNull(corrIdOnMdc)) {
//        if (nonNull(corrIdOnMdc)) {
//            response.addHeader(CORRELATION_ID_HEADER, corrIdOnMdc);
//        }
//        if (nonNull(MDC.get(CORRELATION_ID_HEADER))) {
//            MDC.remove(CORRELATION_ID_HEADER);
//        }
        log.info("afterC after");
    }

    private void removeCorrIdFromMdcIfExist() {
        if (nonNull(MDC.get(CORRELATION_ID_HEADER))) {
            MDC.remove(CORRELATION_ID_HEADER);
        }
    }
}
