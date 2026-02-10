package com.ibosng.gatewayservice.enums;

import lombok.Getter;

@Getter
public enum ReportOutputFormat {
    PDF("pdf"),
    XLS("xls"),
    XLSX("xlsx"),
    CSV("csv");

    private final String value;

    ReportOutputFormat(String value) {
        this.value = value;
    }

    public static ReportOutputFormat fromValue(String value) {
        for (ReportOutputFormat format : ReportOutputFormat.values()) {
            if (format.getValue().equalsIgnoreCase(value)) {
                return format;
            }
        }
        throw new IllegalArgumentException("Unknown report output format: " + value);
    }
}

