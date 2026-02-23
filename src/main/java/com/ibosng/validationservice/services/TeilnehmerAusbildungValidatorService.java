package com.ibosng.validationservice.services;

import com.ibosng.dbservice.dtos.TnAusbildungDto;

public interface TeilnehmerAusbildungValidatorService {

    TnAusbildungDto validateAndSaveTeilnehmerAusbildung(TnAusbildungDto tnAusbildungDto, String changedBy, String teilnehmerId);
}
