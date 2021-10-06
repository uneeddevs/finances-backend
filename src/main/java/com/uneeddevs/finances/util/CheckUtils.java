package com.uneeddevs.finances.util;

import org.apache.commons.lang3.StringUtils;

public class CheckUtils {

    private CheckUtils(){}

    public static <T> T requireNonNull(T t, String message) {
         if (t == null)
                throw new IllegalArgumentException(message);
        return t;
    }

    public static String requireNotBlank(String text, String message) {
        if (StringUtils.isBlank(text))
            throw new IllegalArgumentException(message);
        return text;
    }

    public static <T extends Number> T requirePositive(T number, String message) {
        if(number.doubleValue() < 0)
            throw new IllegalArgumentException(message);
        return number;
    }
}
