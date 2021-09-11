package com.kangmin.app.util;

public class CommonUtil {

    public static String getFileExtension(final String name) {
        final String extension;
        if (name == null || name.isEmpty()) {
            extension = "";
        } else {
            final String[] parts = name.split("\\.");
            if (parts.length > 1) {
                extension = "." + parts[parts.length - 1];
            } else {
                extension = ".jpg";
            }
        }
        return extension;
    }
}
