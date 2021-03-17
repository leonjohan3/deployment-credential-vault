package org.dcv.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.validation.constraints.Future;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Slf4j
@Configuration
@PropertySource("classpath:/META-INF/build-info.properties")
@Lazy
public class BuildInfoConfig {

    @Value
    public static class BuildInfo {
//        @Pattern(regexp = "^\\d[A-Z0-9\\.]{3,}")
        @Pattern(regexp = "^\\d[A-Z0-9\\.\\-]{3,}")
        String version;

//        @Future
        @PastOrPresent
        LocalDateTime buildTime;
    }

    @Autowired
    private
    Environment environment;

    @Bean
    public BuildInfo buildInfo() {
        log.info("start");
//        return new BuildInfo("0.0.0-UNKNOWN", LocalDateTime.now());
        return new BuildInfo(environment.getProperty("build.version", "0.0.0-UNKNOWN"), LocalDateTime.now());
    }
}
