package com.ibosng.validationservice.services;

import com.ibosng.dbservice.dtos.TnBerufserfahrungDto;

public interface TeilnehmerBerufserfahrungValidatorService {

    TnBerufserfahrungDto validateAndSaveTeilnehmerBerufserfahrung(TnBerufserfahrungDto tnBerufserfahrungDto, String changedBy, String teilnehmerId);
}
