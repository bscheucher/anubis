package com.ibosng.dbservice.services;

import com.ibosng.dbservice.dtos.teilnahme.TeilnahmeCreationDto;
import com.ibosng.dbservice.dtos.teilnahme.TeilnahmeMetadataDto;
import com.ibosng.dbservice.dtos.teilnahme.TeilnahmeOverviewDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnahmerTeilnahme;

import java.time.LocalDate;

public interface TeilnahmerTeilnahmeService extends BaseService <TeilnahmerTeilnahme> {

    TeilnahmeOverviewDto formOverview(int seminarId, LocalDate date);

    TeilnahmeMetadataDto map(TeilnahmerTeilnahme teilnahmerTeilnahme);

    void saveTeilnahme(TeilnahmeCreationDto teilnahmeCreationDto, Benutzer benutzer);
}
