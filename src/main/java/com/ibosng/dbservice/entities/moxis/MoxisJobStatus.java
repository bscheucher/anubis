package com.ibosng.dbservice.entities.moxis;

import com.ibosng.dbservice.entities.zeitbuchung.MoxisStatus;
import lombok.Getter;

@Getter
public enum MoxisJobStatus {
    NEW(0),
    IN_PROGRESS(1),
    SUCCESS(2),
    ERROR(3),
    SIGNATURE_DENIED(4),
    TIMEOUT(5),
    CANCELLED(6),
    RETRY(7),
    PENDING(8);

    private final int code;

    MoxisJobStatus(int code) {
        this.code = code;
    }

    public static MoxisJobStatus mapFromMoxisStatus(MoxisStatus moxisServerStatus) {
        return switch (moxisServerStatus) {
            case PROCESSING, POST_PROCESSING -> IN_PROGRESS;
            case SUCCESS -> SUCCESS;
            case FINISHED_FAILED -> ERROR;
            case DENIED -> SIGNATURE_DENIED;
            case TIMEOUT -> TIMEOUT;
            case CANCELLED -> CANCELLED;
        };
    }

}
