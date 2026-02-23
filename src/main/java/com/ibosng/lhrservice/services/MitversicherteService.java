package com.ibosng.lhrservice.services;

import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.lhrservice.exceptions.LHRWebClientException;

public interface MitversicherteService {
    void processMitversicherte(Personalnummer personalnummer, Integer vertragsdatenId) throws LHRWebClientException;
}