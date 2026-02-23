package com.ibosng.dbservice.entities.zeitbuchung;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MoxisStatus {
    PROCESSING("PROCESSING"),
    POST_PROCESSING("POST_PROCESSING"),
    DENIED("FINISHED_SIGNATURE_DENIED"),
    CANCELLED("FINISHED_WF_CANCELLED"),
    TIMEOUT("FINISHED_TIMEOUT"),
    SUCCESS("FINISHED_SUCCESS"),
    FINISHED_FAILED("FINISHED_FAILED");

    private final String moxisName;

    public static MoxisStatus findByMoxisName(String moxisName) {
        for (MoxisStatus status : values()) {
            if (status.moxisName.equals(moxisName)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown Moxis status: " + moxisName);
    }
}
