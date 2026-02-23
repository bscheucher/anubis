package com.ibosng.lhrservice.services;

import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.lhrservice.exceptions.LHRWebClientException;

public interface SondertageService {
    void processSondertageForOnboarding(Personalnummer personalnummer, Integer vertragsdatedId) throws LHRWebClientException;
}