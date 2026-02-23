package com.ibosng.personalverwaltung.domain.exceptions;

import lombok.Getter;

@Getter
public class DmsFolderStructureServiceException extends RuntimeException {

    public DmsFolderStructureServiceException(String message) {
        super(message);
    }

    public DmsFolderStructureServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
