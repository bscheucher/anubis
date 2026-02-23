package com.ibosng.validationservice.services;

import com.ibosng.dbservice.dtos.TeilnehmerNotizDto;

public interface TeilnehmerNotizValidatorService {

    TeilnehmerNotizDto validateAndSaveTeilnehmerNotiz(TeilnehmerNotizDto teilnehmerNotizDto, String changedBy, String teilnehmerId);
}
