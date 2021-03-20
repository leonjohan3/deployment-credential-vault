package org.dcv.config;

import lombok.Data;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * Typesafe and @Validated application configuration/properties values/parameters.
 * See also file application.yml
 */
//@Value
@ConstructorBinding
@Data
@ConfigurationProperties(prefix = "dcv", ignoreUnknownFields = false)
@Validated
public class ApplicationConfiguration {

    @NotNull
    File keystore;
}
