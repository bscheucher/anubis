package com.ibosng.validationservice.services;

import com.ibosng.dbibosservice.entities.AdresseIbos;
import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;

import java.util.Set;

public interface TeilnehmerValidatorService {
    Teilnehmer getTeilnehmer(TeilnehmerStaging teilnehmerStaging);

    Teilnehmer getTeilnehmerForManual(TeilnehmerDto teilnehmerDto);

    Teilnehmer getTeilnehmerByTeilnehmerId(int teilnehmerId);

    Teilnehmer getTeilnehmerFromSVNOrNames(TeilnehmerDto teilnehmerDto);

    Teilnehmer getTeilnehmerForZeiterfassung(TeilnehmerDto teilnehmerDto);

    void mapTeilnehmerIbosToTeilnehmer(Set<AdresseIbos> adressesIbos, Integer seminarNummer);
}
