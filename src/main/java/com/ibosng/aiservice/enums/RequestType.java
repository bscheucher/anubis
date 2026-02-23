package com.ibosng.aiservice.enums;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public enum RequestType {
    ROUTING("routing", null),
    CHAT("konversation", null),
    SEMINAR_VERTRETUNG("seminarVertretung", "com.ibosng.aiservice.services.SeminarvertretungService"),
    TRAINER_ELECTION("trainerElection", null);

    private final String value;
    private final String serviceClassName;

    RequestType(String value, String serviceClassName) {
        this.value = value;
        this.serviceClassName = serviceClassName;
    }


    public static RequestType fromValue(String value) {
        for (RequestType request : RequestType.values()) {
            if (request.getValue().equals(value)) {
                return request;
            }
        }
        log.error("Unknown enum value: {}", value);
        return null;
    }
}
