package com.ibosng.dbservice.entities.reports;

import lombok.Getter;

@Getter
public enum ReportDataSource {
    MARIADB("mariaDb"),
    LHR("lhrDb"),
    POSTGRES("postgres");

    private final String value;

    public static ReportDataSource fromValue(String value) {
        for (ReportDataSource source : ReportDataSource.values()) {
            if (source.getValue().equals(value)) {
                return source;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }

    ReportDataSource(String value) {
        this.value = value;
    }
}

