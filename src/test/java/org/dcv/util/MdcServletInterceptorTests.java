package org.dcv.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.dcv.util.Constants.CORRELATION_ID_HEADER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.text.IsBlankString.blankOrNullString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class MdcServletInterceptorTests {
    private MdcServletInterceptor mdcServletInterceptor;

    @BeforeEach
    public void runBeforeEachTestMethod() {
        mdcServletInterceptor = new MdcServletInterceptor();
    }

    @Test
    public void shouldGenerateRandomCorrId() {

        // given: all data (test fixture) preparation
        final HttpServletRequest mockHttpServletRequest = mock(HttpServletRequest.class);
        final HttpServletResponse mockHttpServletResponse = mock(HttpServletResponse.class);

        given(mockHttpServletRequest.getHeader(CORRELATION_ID_HEADER)).willReturn(null);
        given(mockHttpServletResponse.getHeader(CORRELATION_ID_HEADER)).willReturn(null);

        // when : method to be checked invocation
        assertTrue(mdcServletInterceptor.preHandle(mockHttpServletRequest, mockHttpServletResponse, null));

        // then : checks and assertions
        final String corrId = MDC.get(CORRELATION_ID_HEADER);
        assertThat(corrId, not(blankOrNullString()));
        verify(mockHttpServletResponse).addHeader(eq(CORRELATION_ID_HEADER), eq(corrId));
    }

    @Test
    public void shouldUseProvidedCorrId() {

        // given: all data (test fixture) preparation
        final String providedCorrId = "providedCorrId";
        final HttpServletRequest mockHttpServletRequest = mock(HttpServletRequest.class);
        final HttpServletResponse mockHttpServletResponse = mock(HttpServletResponse.class);

        given(mockHttpServletRequest.getHeader(CORRELATION_ID_HEADER)).willReturn(providedCorrId);
        given(mockHttpServletResponse.getHeader(CORRELATION_ID_HEADER)).willReturn(null);

        // when : method to be checked invocation
        assertTrue(mdcServletInterceptor.preHandle(mockHttpServletRequest, mockHttpServletResponse, null));

        // then : checks and assertions
        final String corrId = MDC.get(CORRELATION_ID_HEADER);
        assertThat(corrId, is(providedCorrId));
        verify(mockHttpServletResponse).addHeader(eq(CORRELATION_ID_HEADER), eq(providedCorrId));
    }

    @Test
    public void shouldNotAddAdditionalHeader() {

        // given: all data (test fixture) preparation
        final String providedCorrIdOnRequest = "providedCorrIdOnRequest";
        final String providedCorrIdOnResponse = "providedCorrIdOnResponse";
        final HttpServletRequest mockHttpServletRequest = mock(HttpServletRequest.class);
        final HttpServletResponse mockHttpServletResponse = mock(HttpServletResponse.class);

        given(mockHttpServletRequest.getHeader(CORRELATION_ID_HEADER)).willReturn(providedCorrIdOnRequest);
        given(mockHttpServletResponse.getHeader(CORRELATION_ID_HEADER)).willReturn(providedCorrIdOnResponse);

        // when : method to be checked invocation
        assertTrue(mdcServletInterceptor.preHandle(mockHttpServletRequest, mockHttpServletResponse, null));

        // then : checks and assertions
        final String corrId = MDC.get(CORRELATION_ID_HEADER);
        assertThat(corrId, is(providedCorrIdOnResponse));
        verify(mockHttpServletResponse, never()).addHeader(any(), any());
    }

    @Test
    public void shouldRemoveCorrIdFromMdcIfExist() {

        // given: all data (test fixture) preparation
        final String providedCorrId = "providedCorrId";
        MDC.put(CORRELATION_ID_HEADER, providedCorrId);

        // when : method to be checked invocation
        mdcServletInterceptor.afterCompletion(null, null, null, null);

        // then : checks and assertions
        final String corrId = MDC.get(CORRELATION_ID_HEADER);
        assertThat(corrId, nullValue());
    }

    @Test
    public void shouldNotRemoveCorrIdFromMdcIfNotExist() {

        // when : method to be checked invocation
        mdcServletInterceptor.afterCompletion(null, null, null, null);

        // then : checks and assertions
        final String corrId = MDC.get(CORRELATION_ID_HEADER);
        assertThat(corrId, nullValue());
    }
}
