package com.ibosng.lhrservice.services;

import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.lhrservice.enums.PersoenlicheSatze;
import com.ibosng.lhrservice.exceptions.LHRWebClientException;

import java.time.LocalDate;

public interface PersoenlicheSaetzeService {
    void processPersoenlicheSaetze(Personalnummer personalnummer, LocalDate date, PersoenlicheSatze persoenlicheSatze) throws LHRWebClientException;
    boolean shouldSendGehalt(Integer vertragsdatenId);
}