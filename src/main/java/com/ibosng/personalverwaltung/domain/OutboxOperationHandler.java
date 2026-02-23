package com.ibosng.personalverwaltung.domain;

import com.ibosng.personalverwaltung.persistence.LhrOutboxEntry;
import jakarta.validation.constraints.NotNull;

public abstract class OutboxOperationHandler {

    public abstract @NotNull LhrOutboxEntry.Operation supports();

    public abstract void handle(LhrOutboxEntry entry);
}
