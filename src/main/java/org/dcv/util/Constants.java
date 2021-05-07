package org.dcv.util;

public class Constants {
    public final static String REQUEST_ENTRY_ITEM_PATTERN = "^[a-zA-Z][a-zA-Z_0-9\\.\\-]{2,}";
    public final static String REQUEST_ENTRY_NAME_PATTERN = "^[a-zA-Z\\.][a-zA-Z_0-9\\.\\-]{2,}";
    public final static String ALIAS_NAME_PART_DELIMITER = "__";
    public final static String GROUP_ID_ENV_NAME = "GROUP_ID_ENV_NAME";
    public final static String ARTIFACT_ID_ENV_NAME = "ARTIFACT_ID_ENV_NAME";
    public final static String ERROR_MESSAGE_HEADER_NAME = "x-Error-Message";
    public final static String KEYSTORE_PASSWORD_START_ENV_NAME = "KSP_START";
    public final static String KEYSTORE_PASSWORD_END_ENV_NAME = "KSP_END";
    public final static String CORRELATION_ID_HEADER = "corrId";
    public final static String SECRET_KEY_VALUE_DELIMITER = "~";
    public final static String AES_ALGORITHM_AND_TRANSFORMATION = "AES";

    public final static int MAX_SECRET_VALUE_LENGTH = 65_536;
    public final static int MAX_SECRET_KEY_LENGTH = 160;
}
