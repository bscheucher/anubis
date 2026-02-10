package com.ibosng._service.impl;

import com.ibosng._service.AsyncService;
import com.ibosng.dbservice.dtos.ZeitbuchungenDto;
import com.ibosng.dbservice.entities.Zeitausgleich;
import com.ibosng.dbservice.entities.lhr.Abwesenheit;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.zeitbuchung.Leistungserfassung;
import com.ibosng.dbservice.entities.zeitbuchung.Zeitbuchung;
import com.ibosng.dbservice.services.ZeitausgleichService;
import com.ibosng.dbservice.services.lhr.AbwesenheitService;
import com.ibosng.dbservice.services.zeitbuchung.LeistungserfassungService;
import com.ibosng.dbservice.services.zeitbuchung.ZeitbuchungService;
import com.ibosng.lhrservice.dtos.zeitdaten.AnfrageSuccessDto;
import com.ibosng.validationservice.services.Validation2LHRService;
import com.ibosng.validationservice.services.ValidatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

import static com.ibosng.dbibosservice.utils.Helpers.isNullOrBlank;
import static com.ibosng.validationservice.utils.Constants.VALIDATION_SERVICE;

/**
 * Implementation of {@link AsyncService} handled by Spring's Async infrastructure.
 * <p>
 * The class-level {@link Async} annotation ensures that all methods are executed using the
 * "executorWithTaskDecorator" bean. This pool is configured with a {@link org.springframework.core.task.TaskDecorator}
 * that copies security and request contexts (like UserID and Username) from the calling thread
 * to the background thread.
 */
@Slf4j
@Component("asyncService")
public class AsyncServiceImpl implements AsyncService {

    private final Validation2LHRService validation2LHRService;
    private final AbwesenheitService abwesenheitService;
    private final ZeitbuchungService zeitbuchungService;
    private final LeistungserfassungService leistungserfassungService;
    private final ZeitausgleichService zeitausgleichService;
    private final ValidatorService validatorService;
    private final Executor executorWithTaskDecorator;

    public AsyncServiceImpl(@Lazy Validation2LHRService validation2LHRService,
                            AbwesenheitService abwesenheitService,
                            ZeitbuchungService zeitbuchungService,
                            LeistungserfassungService leistungserfassungService,
                            ZeitausgleichService zeitausgleichService,
                            ValidatorService validatorService,
                            @Qualifier("executorWithTaskDecorator") Executor executorWithTaskDecorator) {
        this.validation2LHRService = validation2LHRService;
        this.abwesenheitService = abwesenheitService;
        this.zeitbuchungService = zeitbuchungService;
        this.leistungserfassungService = leistungserfassungService;
        this.zeitausgleichService = zeitausgleichService;
        this.validatorService = validatorService;
        this.executorWithTaskDecorator = executorWithTaskDecorator;
    }

    /**
     * Executes a supplier logic in a background thread provided by the "executorWithTaskDecorator" pool.
     * <p>
     * <b>Implementation Note:</b> We use {@link CompletableFuture#supplyAsync(Supplier, Executor)}
     * with our custom executor to ensure the {@link org.springframework.core.task.TaskDecorator}
     * captures and propagates the security/request context correctly.
     *
     * @param supplier The logic to execute
     * @return A CompletableFuture representing the result of the execution
     */
    @Override
    public <T> CompletableFuture<T> asyncExecutor(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(supplier, executorWithTaskDecorator);
    }

    /**
     * Executes a runnable logic in a background thread.
     * Like {@link #asyncExecutor(Supplier)}, this relies on Spring's proxy to handle the thread start
     * to ensure TaskDecorator consistency.
     */
    @Override
    public void asyncExecutorVoid(Runnable runnable) {
        executorWithTaskDecorator.execute(runnable);
    }

    @Override
    public CompletableFuture<Boolean> zeitbuchungOverlapCheck(Personalnummer personalnummer, LocalDate von, LocalDate bis) {
        return CompletableFuture.supplyAsync(() -> {

            log.info("Overlapping check for [{}]-[{}] pn-{}", von, bis, personalnummer.getPersonalnummer());
            List<Leistungserfassung> leistungserfassungen = leistungserfassungService.findByPersonalnummerInPeriod(personalnummer, von.format(DateTimeFormatter.ISO_DATE), bis.format(DateTimeFormatter.ISO_DATE));

            List<Zeitbuchung> zeitbuchungen = leistungserfassungen.stream()
                    .flatMap(leistungserfassung -> zeitbuchungService.getZeitbuchungenByListungserfassen(leistungserfassung).stream())
                    .toList();

            for (Zeitbuchung zeitbuchung : zeitbuchungen) {
                if (Boolean.FALSE.equals(zeitbuchung.getAnAbwesenheit())) {
                    continue;
                }
                List<Abwesenheit> abwesenheiten = abwesenheitService.findAbwesenheitBetweenDates(personalnummer.getId(), von, bis);
                List<Zeitausgleich> zeitausgleichList = zeitausgleichService.findByPersonalnummerInPeriod(personalnummer.getId(), von, bis);
                abwesenheiten
                        .stream()
                        .filter(abw ->
                                !zeitbuchung.getLeistungserfassung().getLeistungsdatum().isBefore(abw.getVon()) &&
                                        !zeitbuchung.getLeistungserfassung().getLeistungsdatum().isAfter(abw.getBis()))
                        .forEach(abw -> validation2LHRService.deleteAbwesenheit(abwesenheitService.mapToAbwesenheitDto(abw)));

                zeitausgleichList.forEach(z -> validation2LHRService.deleteZeitausgleich(z.getPersonalnummer().getId(), z.getDatum().format(DateTimeFormatter.ISO_DATE), true));
            }
            return Boolean.TRUE;
        }, executorWithTaskDecorator);
    }


    // TODO - this is not even async + its only usage is waiting for the method to complete anyway
    @Override
    public CompletableFuture<List<ZeitbuchungenDto>> processLeistungsdatum(Personalnummer personalnummer, String datum, List<ZeitbuchungenDto> zeitbuchungenDto) {
        return CompletableFuture.supplyAsync(() -> {

            List<Leistungserfassung> leistungserfassungen = leistungserfassungService.findByPersonalnummerAndDate(personalnummer, datum);
            List<ZeitbuchungenDto> zeitbuchungenDtos = new ArrayList<>();

            for (Leistungserfassung leistungserfassung : leistungserfassungen) {
                if (Boolean.TRUE.equals(leistungserfassung.getIsSyncedWithLhr())) {
                    validation2LHRService.deleteLeistungserfassung(personalnummer.getId(), leistungserfassung.getLeistungsdatum().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                }
                List<Zeitbuchung> zeitbuchungsToDelete = zeitbuchungService.getZeitbuchungenByListungserfassen(leistungserfassung);
                zeitbuchungsToDelete.forEach(zeitbuchung -> zeitbuchungService.deleteById(zeitbuchung.getId()));
            }

            for (ZeitbuchungenDto zeitbuchungDto : zeitbuchungenDto) {
                zeitbuchungenDtos.add(validatorService.validateZeitbuchung(zeitbuchungDto, VALIDATION_SERVICE).getBody());
                this.asyncExecutor(() -> {
                    HttpStatusCode result = validation2LHRService.syncLeistungerfassung(personalnummer.getId(), zeitbuchungDto.getLeistungsdatum()).getStatusCode();
                    if (result.is2xxSuccessful()) {
                        log.info("Successfully synced with lhr pn-{}, {}", personalnummer, zeitbuchungDto.getLeistungsdatum());
                    } else {
                        log.error("Failed to sync with lhr pn-{}, {}, status: {}", personalnummer, zeitbuchungDto.getLeistungsdatum(), result);
                    }
                    return result.is2xxSuccessful();
                });
            }
            return zeitbuchungenDtos;
        }, executorWithTaskDecorator);

    }

    @Override
    public CompletableFuture<ResponseEntity<AnfrageSuccessDto>> pollAuszahlungsanfrage(Supplier<ResponseEntity<AnfrageSuccessDto>> supplier, int maxAttempts) {
        return CompletableFuture.supplyAsync(() -> {

            int attempts = 0;
            while (attempts++ < maxAttempts) {
                ResponseEntity<AnfrageSuccessDto> response = supplier.get();
                if (response != null && response.getBody() != null && !isNullOrBlank(response.getBody().getStatus()) &&
                        "done".equalsIgnoreCase(response.getBody().getStatus())) {
                    log.info("Poll finished");
                    return response;
                }
                try {
                    Thread.sleep(1000); // wait 1 sec between polls
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error("Interrupted");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            }
            log.error("Timed out");
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
        }, executorWithTaskDecorator);
    }
}
