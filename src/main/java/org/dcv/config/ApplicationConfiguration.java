package org.dcv.config;

import lombok.Data;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.File;

/**
 * Typesafe and @Validated application configuration/properties values/parameters.
 * See also file application.yml
 */
//@AllArgsConstructor
//@Getter
@Value
@ConstructorBinding
@Data
@ConfigurationProperties(prefix = "dcv", ignoreUnknownFields = false)
@Validated
public class ApplicationConfiguration {
//    @NotBlank
//    @Size(min = 8)
//    String keystorePassword;

    @NotNull
    File keystore;

//    @NotNull
//    File keystoreKeystore;
}
