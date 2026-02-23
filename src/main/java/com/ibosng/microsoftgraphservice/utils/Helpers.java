package com.ibosng.microsoftgraphservice.utils;

import com.ibosng.microsoftgraphservice.dtos.FileDetails;

import java.io.File;
import java.util.List;

public final class Helpers {

    public static boolean deleteLocalFile(String filePath) {
        File file = new File(filePath);
        return file.delete();
    }

    public static void deleteLocalFiles(List<FileDetails> files) {
        for(FileDetails file : files) {
            deleteLocalFile(file.getFilePath());
        }
    }

    public static String updateSubdirectoryName(String personalnummer, String vorname, String nachname) {
        String name = personalnummer + "_" + vorname.trim().toUpperCase() + "_" + nachname.trim().toUpperCase();
    // Remove control characters (like \n, \r, etc.)
        name = name.replaceAll("[\\p{Cntrl}]", "");

    // Remove invalid filesystem characters: \ / : * ? " < > |
        name = name.replaceAll("[\\\\/:*?\"<>|]", "").trim();

    // (Optional) Collapse multiple spaces or underscores
        name = name.replaceAll("\\s+", "_").trim();
        return name;
    }

    public static String updateFileName(String existingFileName, String vorname, String nachname) {
        if (existingFileName.contains("DIENSTVERTRAG")) {
            return existingFileName;
        }
        int extensionIndex = existingFileName.lastIndexOf(".");
        if(extensionIndex == -1) {
            return existingFileName + "_" + vorname.toUpperCase() + "_" + nachname.toUpperCase();
        }
        return existingFileName.substring(0, extensionIndex) + "_" + vorname.toUpperCase() + "_" + nachname.toUpperCase()
                    + existingFileName.substring(extensionIndex);
    }

    @SafeVarargs
    public static <T> Object[] toObjectArray(T... args) {
        return args;
    }
}
