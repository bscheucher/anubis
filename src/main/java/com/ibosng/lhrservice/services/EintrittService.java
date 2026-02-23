package com.ibosng.lhrservice.services;

import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.lhrservice.exceptions.LHRWebClientException;

public interface EintrittService {
    void processEintritt(Personalnummer personalnummer, String eintrittsdatum) throws LHRWebClientException;
}