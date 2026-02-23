package com.ibosng.dbservice.entities.workflows;

import lombok.Getter;

@Getter
public enum WWorkflowStatus {
    NEW(0),
    IN_PROGRESS(1),
    COMPLETED(2),
    ERROR(3),
    DISABLED(4),
    PENDING(5),
    CANCELED(6);

    private final int code;

    WWorkflowStatus(int code) {
        this.code = code;
    }

    public static WWorkflowStatus fromCode(int code) {
        for (WWorkflowStatus status : WWorkflowStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status code: " + code);
    }
}