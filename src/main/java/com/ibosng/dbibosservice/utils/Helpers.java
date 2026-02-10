package com.ibosng.dbibosservice.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Helpers {

    public static boolean areObjectsNull(Object[] object) {
        if (object == null) {
            log.warn("The array is null");
            return true;
        } else {
            // Check each element in the array
            for (Object element : object) {
                if (element == null) {
                    log.warn("Found a null element in the array");
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isNullOrBlank(String string) {
        return string == null || string.isEmpty() || string.isBlank();
    }
}
