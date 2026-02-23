package com.ibosng.personalverwaltung;

import com.ibosng.personalverwaltung.domain.CreateAbwesenheitRequestOutboxOperationHandler;
import com.ibosng.personalverwaltung.domain.LhrOutboxScheduler;
import com.ibosng.personalverwaltung.domain.exceptions.LhrOutboxProcessingException;
import com.ibosng.personalverwaltung.persistence.LhrOutboxEntry;
import com.ibosng.personalverwaltung.persistence.LhrOutboxEntryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.ibosng.personalverwaltung.persistence.LhrOutboxEntry.Operation.CREATE_ABWESENHEIT_REQUEST;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LhrOutboxSchedulerTest {

    @Mock
    private LhrOutboxEntryRepository repositoryMock;

    @Mock
    private CreateAbwesenheitRequestOutboxOperationHandler createAbwesenheitRequestOutboxOperationHandlerMock;

    private LhrOutboxScheduler sut;

    @Test
    public void readsFromOutbox() {

        sut = new LhrOutboxScheduler(repositoryMock, List.of());

        sut.process();

        verify(repositoryMock, times(1)).findByStatusOrderById(LhrOutboxEntry.Status.NEW);
    }

    @Test
    public void entriesAreProcessedByAppropriateHandler() {

        sut = getSchedulerWithDefinedHandlers();

        var singleEntryRetrieved = getMockPersistedEntry();
        when(repositoryMock.findByStatusOrderById(LhrOutboxEntry.Status.NEW)).thenReturn(List.of(singleEntryRetrieved));

        sut.process();

        verify(createAbwesenheitRequestOutboxOperationHandlerMock, times(1)).handle(singleEntryRetrieved);
    }

    @Test
    public void processedEntryGetsMarkedAsProcessed() {

        sut = getSchedulerWithDefinedHandlers();

        var singleEntryRetrieved = getMockPersistedEntry();
        when(repositoryMock.findByStatusOrderById(LhrOutboxEntry.Status.NEW)).thenReturn(List.of(singleEntryRetrieved));
        doNothing().when(createAbwesenheitRequestOutboxOperationHandlerMock).handle(singleEntryRetrieved);

        sut.process();

        verify(repositoryMock, times(1)).markAsProcessed(singleEntryRetrieved.getId());
    }

    @Test
    public void noHandlerForOperation_markAsError() {

        sut = new LhrOutboxScheduler(repositoryMock, List.of());

        var singleEntryRetrieved = getMockPersistedEntry();
        when(repositoryMock.findByStatusOrderById(LhrOutboxEntry.Status.NEW)).thenReturn(List.of(singleEntryRetrieved));

        sut.process();

        verify(repositoryMock, times(1)).markAsError(singleEntryRetrieved.getId(), "No OperationHandler available for this entry's Operation");

    }

    @Test
    public void handlerThrowsKnownException_catchAndMarkAsError() {

        sut = getSchedulerWithDefinedHandlers();

        var singleEntryRetrieved = getMockPersistedEntry();
        when(repositoryMock.findByStatusOrderById(LhrOutboxEntry.Status.NEW)).thenReturn(List.of(singleEntryRetrieved));
        doThrow(new LhrOutboxProcessingException("Could not sync: Details containing helpful info"))
                .when(createAbwesenheitRequestOutboxOperationHandlerMock).handle(singleEntryRetrieved);

        sut.process();

        verify(repositoryMock, times(1)).markAsError(singleEntryRetrieved.getId(), "LhrOutboxProcessingException: Could not sync: Details containing helpful info");

    }

    @Test
    public void handlerThrowsUnknownException_catchAndMarkAsError() {

        sut = getSchedulerWithDefinedHandlers();

        var singleEntryRetrieved = getMockPersistedEntry();
        when(repositoryMock.findByStatusOrderById(LhrOutboxEntry.Status.NEW)).thenReturn(List.of(singleEntryRetrieved));
        doThrow(new RuntimeException("Something unexpected happened"))
                .when(createAbwesenheitRequestOutboxOperationHandlerMock).handle(singleEntryRetrieved);

        sut.process();

        verify(repositoryMock, times(1)).markAsError(singleEntryRetrieved.getId(), "RuntimeException: Something unexpected happened");

    }

    private LhrOutboxScheduler getSchedulerWithDefinedHandlers() {
        when(createAbwesenheitRequestOutboxOperationHandlerMock.supports()).thenReturn(CREATE_ABWESENHEIT_REQUEST);
        return new LhrOutboxScheduler(repositoryMock, List.of(createAbwesenheitRequestOutboxOperationHandlerMock));
    }

    private LhrOutboxEntry getMockPersistedEntry() {
        var entry = LhrOutboxEntry.forCreateAbwesenheitRequest(1);
        entry.setId(123);
        return entry;
    }
}
