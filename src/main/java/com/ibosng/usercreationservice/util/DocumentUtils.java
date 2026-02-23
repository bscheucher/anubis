package com.ibosng.usercreationservice.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DocumentUtils {

    public static boolean deleteLocalFile(String filePath) {
        File file = new File(filePath);
        return file.delete();
    }

    public static String getPrefixFromFilename(String filename) {
        return filename.substring(0, filename.lastIndexOf("."));
    }

    public static String getContentFromFile(File file) throws IOException {
        try (BOMInputStream bomInputStream = BOMInputStream.builder()
                .setInputStream(new FileInputStream(file))
                .setByteOrderMarks(ByteOrderMark.UTF_8)  // Add other BOMs if needed
                .setInclude(false)  // Exclude BOM from the content
                .get()) {
            return IOUtils.toString(bomInputStream, StandardCharsets.UTF_8);
        }
    }

    public static String getFilenameWithDate(String file) {
        String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd'T'HH_mm_ss")) + "_" + file;
        filename = filename.replaceAll(":", "_");
        return filename.replaceAll("-", "_");
    }
}
