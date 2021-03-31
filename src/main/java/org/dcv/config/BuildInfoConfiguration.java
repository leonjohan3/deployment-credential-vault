package org.dcv.config;

import org.dcv.dto.BuildInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import static java.time.LocalDateTime.parse;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static java.util.Objects.isNull;
import static java.util.TimeZone.getDefault;

@Configuration
@PropertySource("classpath:/META-INF/build-info.properties")
@Lazy
public class BuildInfoConfiguration {

    private final static String UNKNOWN = "Unknown";

    private Environment environment;

    public BuildInfoConfiguration(final Environment environment) {
        this.environment = environment;
    }

    @Bean
    public BuildInfo getBuildInfo() {
        final LocalDateTime localDateTime = isNull(environment.getProperty("build.time")) ? LocalDateTime.now() : parse(environment.getProperty(
                "build.time"), ISO_DATE_TIME);
        final ZonedDateTime zonedDateTimeAtUtc = ZonedDateTime.of(localDateTime, UTC);
        final ZonedDateTime zonedDateTimeAtDefaultTimezone = zonedDateTimeAtUtc.withZoneSameInstant(getDefault().toZoneId());
        return new BuildInfo(environment.getProperty("build.group", UNKNOWN), environment.getProperty("build.artifact", UNKNOWN),
                environment.getProperty("build.version", "0.0.0-UNKNOWN"), zonedDateTimeAtDefaultTimezone.toLocalDateTime());
//        return new BuildInfo(environment.getProperty("build.group", UNKNOWN), environment.getProperty("build.artifact", UNKNOWN),
//                "0.0.0-UNKNOWn", zonedDateTimeAtDefaultTimezone.toLocalDateTime());
    }
}
