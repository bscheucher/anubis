package com.ibosng.microsoftgraphservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;

@Data
@AllArgsConstructor
public class FileDetails {
    private File file;
    private String filename;
    private String filePath;
    private String sharePointId;
    private boolean isExcel;

}
