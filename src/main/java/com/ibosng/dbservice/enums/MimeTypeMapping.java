package com.ibosng.dbservice.enums;

import java.util.HashMap;
import java.util.Map;

public enum MimeTypeMapping {
    TXT("text/plain", ".txt"),
    JPG("image/jpeg", ".jpg"),
    JPEG("image/jpeg", ".jpeg"),
    PNG("image/png", ".png"),
    PDF("application/pdf", ".pdf"),
    DOC("application/msword", ".doc"),
    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document", ".docx"),
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", ".xlsx"),
    GIF("image/gif", ".gif");

    private final String mimeType;
    private final String extension;

    private static final Map<String, String> extensionToMimeType = new HashMap<>();
    private static final Map<String, String> mimeTypeToExtension = new HashMap<>();

    static {
        // Initialize the maps for quick lookups
        for (MimeTypeMapping mapping : values()) {
            extensionToMimeType.put(mapping.extension.toLowerCase(), mapping.mimeType);
            mimeTypeToExtension.put(mapping.mimeType.toLowerCase(), mapping.extension);
        }
    }

    MimeTypeMapping(String mimeType, String extension) {
        this.mimeType = mimeType;
        this.extension = extension;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getExtension() {
        return extension;
    }

    public static String getMimeTypeForExtension(String extension) {
        return extensionToMimeType.getOrDefault(extension.toLowerCase(), "application/octet-stream");
    }

    public static String getExtensionForMimeType(String mimeType) {
        return mimeTypeToExtension.getOrDefault(mimeType.toLowerCase(), "");
    }
}
