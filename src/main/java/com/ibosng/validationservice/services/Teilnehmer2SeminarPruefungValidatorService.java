package com.ibosng.validationservice.services;

import com.ibosng.dbservice.dtos.SeminarPruefungDto;

public interface Teilnehmer2SeminarPruefungValidatorService {

    SeminarPruefungDto validateTeilnehmerSeminarPruefung(SeminarPruefungDto seminarPruefungDto, String changedBy, String teilnehmerId, String seminarId);
}
