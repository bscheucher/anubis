package com.ibosng.personalverwaltung.domain.exceptions;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LhrOutboxProcessingException extends RuntimeException {
    private final String message;

    public LhrOutboxProcessingException(@NotNull String message) {
        super(message);
        this.message = message;
    }

    public static LhrOutboxProcessingException fromEntityNotFound(Integer entryId, String associatedEntityType, String associatedEntityId) {
        var message = "Could not process LhrOutboxEntry %d : The associated entity of type %s with id %s was not found."
                .formatted(entryId, associatedEntityType, associatedEntityId);
        return new LhrOutboxProcessingException(message);
    }

    public static LhrOutboxProcessingException fromEssentialPropertyNull(Integer entryId, String associatedEntityType, String associatedEntityId, String nullField) {
        var message = "Could not process LhrOutboxEntry %d : The associated entity of type %s with id %s contains a null-field essential for calling LHR: %s."
                .formatted(entryId, associatedEntityType, associatedEntityId,  nullField);
        return new LhrOutboxProcessingException(message);
    }
}
