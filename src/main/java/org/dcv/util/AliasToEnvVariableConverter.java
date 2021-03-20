package org.dcv.util;

import static java.lang.Character.isLetterOrDigit;

public final class AliasToEnvVariableConverter {
    private final static char UNDERSCORE = '_';

    private AliasToEnvVariableConverter() {
    }

    public static String convertToEnvVariable(final String alias) {
        // env vars should be uppercase, contain only letters, numbers, and underscores and do not start with a number
        final StringBuilder result = new StringBuilder();

        for (final char character : alias.toCharArray()) {
            if (UNDERSCORE == character || isLetterOrDigit(character)) {
                result.append(character);
            } else {
                result.append(UNDERSCORE);
            }
        }
        return result.toString().toUpperCase();
    }
}
