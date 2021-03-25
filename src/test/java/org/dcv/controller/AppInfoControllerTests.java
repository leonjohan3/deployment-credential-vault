package org.dcv.controller;

import org.dcv.config.BuildInfoConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static java.time.LocalDateTime.now;
import static org.dcv.config.BuildInfoConfig.BuildInfo;
import static org.dcv.util.Constants.CORRELATION_ID_HEADER;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.text.IsBlankString.blankOrNullString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AppInfoController.class,
        properties = {
                "logging.level.root=warn",
                "logging.level.org.dcv=warn",
                "spring.main.banner-mode=off"
        })
public class AppInfoControllerTests {

    private final static String URL_UNDER_TEST = "/build-info";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BuildInfoConfig buildInfoConfig;

    @Test
    void shouldSuccessfullyProvideBuildInfo() throws Exception {

        // given: all data (test fixture) preparation
        final String version = "1.1-VER";
        final String corrId = "123";

        given(buildInfoConfig.getBuildInfo()).willReturn(
                new BuildInfo("buildGroup", "buildArtifact", version, now()));

        // when : method to be checked invocation
        mvc.perform(get(URL_UNDER_TEST).header(CORRELATION_ID_HEADER, corrId))

                // then : checks and assertions
                .andExpect(status().isOk())
                .andExpect(header().string(CORRELATION_ID_HEADER, nullValue()))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.version").value(version));
    }

    @Test
    void shouldValidateBuildInfoProperties() throws Exception {

        // given: all data (test fixture) preparation
        final String invalidVersion = "1.1-ver"; // lowercase not allowed

        given(buildInfoConfig.getBuildInfo()).willReturn(
                new BuildInfo("buildGroup", "buildArtifact", invalidVersion, now()));

        // when : method to be checked invocation
        mvc.perform(get(URL_UNDER_TEST))

                // then : checks and assertions
                .andExpect(status().isBadRequest())
                .andExpect(header().string(CORRELATION_ID_HEADER, not(blankOrNullString())))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errorMessage").value(not(blankOrNullString())));
    }
}
