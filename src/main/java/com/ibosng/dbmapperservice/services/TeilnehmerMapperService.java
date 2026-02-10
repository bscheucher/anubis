package com.ibosng.dbmapperservice.services;

import com.ibosng.dbibosservice.entities.AdresseIbos;
import com.ibosng.dbibosservice.entities.SeminarIbos;
import com.ibosng.dbibosservice.entities.smtn.SeminarTeilnehmerIbos;
import com.ibosng.dbservice.dtos.TeilnehmerCsvDto;
import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;

public interface TeilnehmerMapperService {
    TeilnehmerStaging adresseIbosToTeilnehmerStaging(AdresseIbos adresseIbos);

    TeilnehmerStaging adresseIbosToTeilnehmerStagingDirect(AdresseIbos adresseIbos);

    void mapSeminarDataToTeilnehmerStaging(TeilnehmerStaging teilnehmerStaging, SeminarTeilnehmerIbos seminarTeilnehmer, SeminarIbos seminarIbos);

    TeilnehmerStaging mapInvalidToTeilnehmerStaging(TeilnehmerDto invalidTeilnehmerDto, String createdBy, String serviceName);

    TeilnehmerCsvDto mapToCsv(Teilnehmer teilnehmer);
}
