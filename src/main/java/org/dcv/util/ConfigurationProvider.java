package org.dcv.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import static java.lang.String.format;
import static java.lang.System.getenv;
import static java.lang.reflect.Modifier.isFinal;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.dcv.util.Constants.ARTIFACT_ID_ENV_NAME;
import static org.dcv.util.Constants.GROUP_ID_ENV_NAME;

@Slf4j
@ApplicationScoped
@Getter
public class ConfigurationProvider {
    private final static String ENV_FORMAT_STRING = "%s__%s__%s";

    @NotNull
    private File mapDbFile;

    @NotNull
    private File publicKeyPath;

    @NotNull
    private File privateKeyPath;

    @Valid
    public ConfigurationProvider() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        final String groupId = getenv(GROUP_ID_ENV_NAME);
        final String artifactId = getenv(ARTIFACT_ID_ENV_NAME);

        for (final Field field : this.getClass().getDeclaredFields()) {

            if (!isFinal(field.getModifiers())) {
                final String value;

                if (nonNull(groupId) && nonNull(artifactId)) {
                    log.debug("using env: {}", format(ENV_FORMAT_STRING, groupId, artifactId, field.getName().toUpperCase()));
                    value = getenv(format(ENV_FORMAT_STRING, groupId, artifactId, field.getName().toUpperCase()));
                } else {
                    value = getenv(field.getName().toUpperCase());
                }
                log.debug("value: {}", value);

                if (nonNull(value)) {
                    if (field.getGenericType().equals(int.class)) {
                        field.setInt(this, Integer.valueOf(isNull(value) ? "0" : value));
                    } else if (field.getGenericType().equals(boolean.class)) {
                        field.setBoolean(this, Boolean.valueOf(isNull(value) ? "false" : value));
                    } else if (field.getGenericType().equals(String.class)) {
                        field.set(this, value);
                    } else {
                        field.set(this, field.getType().getDeclaredConstructor(String.class).newInstance(value));
                    }
                }
            }
        }
    }
}
