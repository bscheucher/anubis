package com.ibosng.gatewayservice.services;

import com.ibosng.dbservice.dtos.teilnahme.TeilnahmeCreationDto;
import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import org.springframework.data.domain.Sort.Direction;

import javax.annotation.Nullable;
import java.util.Optional;

public interface SeminarResponseService {
    PayloadResponse validateTeilnehmer(TeilnehmerDto invalidTeilnehmerDto, String createdBy, Optional<Boolean> savePLZOrt);
    PayloadResponse getSeminarByStatusAndProjectName(Boolean isActive, Boolean isKorrigieren, String projectName, @Nullable Benutzer benutzer);
    PayloadResponse getAllSeminars(Boolean isUeba, int page, int size);
    PayloadResponse getSeminarAnAbwesenheitDto(String token, boolean isAdmin, Boolean isActive,
                                               String projectName, String seminarName,
                                               String kursEndeFrom, String kursEndeTo, Boolean verzoegerung,
                                               String sortProperty, Direction sortDirection, int page, int size);
    PayloadResponse getAllMassnahmenummer();

    PayloadResponse getTeilnahme(int id, String date);

    PayloadResponse postTeilnahme(Benutzer benutzer, TeilnahmeCreationDto teilnahmeCreationDto);
}