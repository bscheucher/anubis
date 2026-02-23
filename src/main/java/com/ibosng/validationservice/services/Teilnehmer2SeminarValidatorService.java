package com.ibosng.validationservice.services;

import com.ibosng.dbservice.dtos.SeminarDto;

public interface Teilnehmer2SeminarValidatorService {

    SeminarDto validateTeilnehmerSeminar(SeminarDto seminarDto, String changedBy, String teilnehmerId);
}
