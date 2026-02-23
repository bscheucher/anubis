package com.ibosng.dbservice.entities.mitarbeiter;

import lombok.Getter;

@Getter
public enum BlobStatus {
    NONE("none"),
    NOT_VERIFIED("not_verified"),
    VERIFIED("verified");

    private final String value;

    public static BlobStatus fromValue(String value) {
        for (BlobStatus request : BlobStatus.values()) {
            if (request.getValue().equals(value)) {
                return request;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
    BlobStatus(String value) {
        this.value = value;
    }
}
