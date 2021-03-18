package org.dcv.util;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class WebConfigTests {

    @Test
    public void shouldAddMdcServletInterceptor() {

        // given: all data (test fixture) preparation
        final WebConfig webConfig = new WebConfig();
        final InterceptorRegistry mockInterceptorRegistry = mock(InterceptorRegistry.class);

        // when : method to be checked invocation
        webConfig.addInterceptors(mockInterceptorRegistry);

        // then : checks and assertions
        verify(mockInterceptorRegistry).addInterceptor(any(MdcServletInterceptor.class));
    }
}
