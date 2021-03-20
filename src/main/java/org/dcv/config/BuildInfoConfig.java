package org.dcv.config;

import lombok.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import static java.time.LocalDateTime.parse;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static java.util.TimeZone.getDefault;
import static org.dcv.util.Constants.REQUEST_ENTRY_ITEM_PATTERN;

@Configuration
@PropertySource("classpath:/META-INF/build-info.properties")
@Lazy
public class BuildInfoConfig {

    private final static String UNKNOWN = "Unknown";

    @Value
    public static class BuildInfo {

        @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN)
        String buildGroup;

        @Pattern(regexp = REQUEST_ENTRY_ITEM_PATTERN)
        String buildArtifact;

        @Pattern(regexp = "^\\d[A-Z0-9\\.\\-]{3,}")
        String version;

        @PastOrPresent
        LocalDateTime buildTime;
    }

    private Environment environment;

    public BuildInfoConfig(final Environment environment) {
        this.environment = environment;
    }

    @Bean
    public BuildInfo getBuildInfo() {
        final ZonedDateTime zonedDateTimeAtUtc = ZonedDateTime.of(parse(environment.getProperty("build.time"), ISO_DATE_TIME), UTC);
        final ZonedDateTime zonedDateTimeAtDefaultTimezone = zonedDateTimeAtUtc.withZoneSameInstant(getDefault().toZoneId());
//        return new BuildInfo(environment.getProperty("build.group", UNKNOWN), environment.getProperty("build.artifact", UNKNOWN),
//                environment.getProperty("build.version", "0.0.0-UNKNOWN"), zonedDateTimeAtDefaultTimezone.toLocalDateTime());
        return new BuildInfo(environment.getProperty("build.group", UNKNOWN), environment.getProperty("build.artifact", UNKNOWN),
                "0.0.0-UNKNOWn", zonedDateTimeAtDefaultTimezone.toLocalDateTime());
    }
}
