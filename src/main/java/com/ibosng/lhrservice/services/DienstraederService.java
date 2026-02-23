package com.ibosng.lhrservice.services;

import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.lhrservice.exceptions.LHRWebClientException;

import java.time.LocalDate;

public interface DienstraederService {
    void processDienstraeder(Personalnummer personalnummer, LocalDate eintrittsdatum) throws LHRWebClientException;
}