package com.ibosng.personalverwaltung.domain;


import com.ibosng.personalverwaltung.persistence.LhrOutboxEntry;
import com.ibosng.personalverwaltung.persistence.LhrOutboxEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@ConditionalOnProperty(prefix = "jobs", name = "lhrOutboxSchedulerEnabled", havingValue = "true")
public class LhrOutboxScheduler {

    private final LhrOutboxEntryRepository repository;
    private final Map<LhrOutboxEntry.Operation, OutboxOperationHandler> allHandlersBySupportedOperation;

    public LhrOutboxScheduler(LhrOutboxEntryRepository repository, List<OutboxOperationHandler> handlers) {
        this.repository = repository;
        this.allHandlersBySupportedOperation = handlers.stream()
                .collect(Collectors.toUnmodifiableMap(OutboxOperationHandler::supports, h -> h));
    }

    @Scheduled(fixedDelayString = "${jobs.lhrOutboxSchedulerDelayInMilliseconds}")
    public void process() {
        var entries = repository.findByStatusOrderById(LhrOutboxEntry.Status.NEW);
        entries.forEach(entry -> {
            var appropriateHandler = allHandlersBySupportedOperation.get(entry.getOperation());
            if (appropriateHandler == null) {
                log.error("Processing LhrOutboxEntry with id {} failed. No OperationHandler available for this entry's Operation.", entry.getId());
                markEntryAsErrorSinceNoOperationHandlerAvailable(entry);
                return;
            }
            try {
                appropriateHandler.handle(entry);
            } catch (Exception exception) {
                log.error("Could not process LhrOutboxEntry {}", entry.getId(), exception);
                markEntryAsError(entry, exception);
                return;
            }
            repository.markAsProcessed(entry.getId());
        });
    }

    private void markEntryAsError(LhrOutboxEntry entry, Exception exception) {
        repository.markAsError(entry.getId(), exception.getClass().getSimpleName() + ": " + exception.getMessage());
    }

    private void markEntryAsErrorSinceNoOperationHandlerAvailable(LhrOutboxEntry entry) {
        repository.markAsError(entry.getId(), "No OperationHandler available for this entry's Operation");
    }
}
