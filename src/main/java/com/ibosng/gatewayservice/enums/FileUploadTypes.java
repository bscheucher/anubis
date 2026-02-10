package com.ibosng.gatewayservice.enums;

import lombok.Getter;

@Getter
public enum FileUploadTypes {
    ECARD("ecard"),
    BANKCARD("bankcard"),
    ARBEITSGENEHMIGUNG("arbeitsgenehmigung"),
    FOTO("foto"),
    VORDIENSTZEITEN_NACHWEIS("vordienstzeit"),
    DIENSTVERTRAG("dienstvertrag");

    private final String value;

    public static FileUploadTypes fromValue(String value) {
        for (FileUploadTypes request : FileUploadTypes.values()) {
            if (request.getValue().equals(value)) {
                return request;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
    FileUploadTypes(String value) {
        this.value = value;
    }
}
