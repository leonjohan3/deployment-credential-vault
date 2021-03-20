package org.dcv.util;

import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

public final class Constants {
    public final static String CORRELATION_ID_HEADER = "corrId";
    public final static String REQUEST_ENTRY_ITEM_PATTERN = "^[a-zA-Z][a-zA-Z_0-9\\.\\-]{2,}";
    public final static String ALIAS_NAME_PART_DELIMITER = "__";
    public final static String CHARSET_UTF_8 = ";charset=UTF-8";
    public final static String MEDIA_TYPE_TEXT_PLAIN = TEXT_PLAIN_VALUE + CHARSET_UTF_8;
    public final static String MASKED_SECRET_KEY_VALUE = "******";
    public final static String ERROR_MESSAGE_HEADER_NAME = "x-Error-Message";
    public final static int CORRELATION_ID_LENGTH = 6;
}
