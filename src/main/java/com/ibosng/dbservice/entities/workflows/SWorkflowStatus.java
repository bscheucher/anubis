package com.ibosng.dbservice.entities.workflows;

import lombok.Getter;

@Getter
public enum SWorkflowStatus {
    NEW(0),
    ACTIVE(1),
    INACTIVE(2);

    private final int code;

    SWorkflowStatus(int code) {
        this.code = code;
    }


}
