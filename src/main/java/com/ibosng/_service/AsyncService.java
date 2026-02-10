package com.ibosng._service;

import com.ibosng.dbservice.dtos.ZeitbuchungenDto;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.lhrservice.dtos.zeitdaten.AnfrageSuccessDto;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public interface AsyncService {
    CompletableFuture<Boolean> zeitbuchungOverlapCheck(Personalnummer personalnummer, LocalDate von, LocalDate bis);

    <T> CompletableFuture<T> asyncExecutor(Supplier<T> supplier);

    void asyncExecutorVoid(Runnable runnable);

    CompletableFuture<List<ZeitbuchungenDto>> processLeistungsdatum(Personalnummer personalnummer, String datum, List<ZeitbuchungenDto> zeitbuchungenDto);

    CompletableFuture<ResponseEntity<AnfrageSuccessDto>> pollAuszahlungsanfrage(Supplier<ResponseEntity<AnfrageSuccessDto>> supplier, int maxAttempts);
}
