package com.ibosng.lhrservice.services;

import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.lhrservice.exceptions.LHRWebClientException;

public interface VariableDatenService {
    void processVariableDaten(Personalnummer personalnummer, String eintrittsdatum) throws LHRWebClientException;
}