package com.snapsync.nexus.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public final class Util {
    public static String getEndpoint(String baseUrl, String path) {
        if (isNullOrEmpty(baseUrl) || isNullOrEmpty(path)) {
            throw new RuntimeException("Invalid URL");
        }
        return String.format("%s%s", baseUrl, path);
    }

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.isBlank();
    }

    public static LocalDateTime dateTimeNow() {
        return LocalDateTime.now().atZone(ZoneId.of("UTC")).toLocalDateTime();
    }

    public static LocalDate dateNow() {
        return LocalDateTime.now().atZone(ZoneId.of("UTC")).toLocalDate();
    }
}
