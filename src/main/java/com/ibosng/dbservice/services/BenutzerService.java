package com.ibosng.dbservice.services;

import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;

import java.util.List;

public interface BenutzerService extends BaseService<Benutzer> {
    Benutzer getBenutzerByAzureId(String azureId);

    Benutzer findByEmail(String email);

    Benutzer findAllByFirstNameAndLastName(String firstName, String lastName);

    Benutzer createUserIfNotExists(Integer stammdatenId, String workEmail, String createdBy);

    Benutzer getBenutzerByPersonalnummer(String personalnummer);

    Benutzer findByPersonalnummerAndFirmaBmdClient(String personalnummer, Integer firma);

    List<Benutzer> findAllBySamIbosName(String samIbos);

    Benutzer findByUpn(String upn);

    Benutzer findByUpnContaining(String upn);

    Benutzer findByAzureId(String azureId);

    Benutzer findByPersonalnummer(Personalnummer personalnummer);

    Benutzer findByPersonalnummerId(Integer personalnummerId);
}
