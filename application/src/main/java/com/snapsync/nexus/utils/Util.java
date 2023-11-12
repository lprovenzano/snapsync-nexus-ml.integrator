package com.snapsync.nexus.utils;

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
}
