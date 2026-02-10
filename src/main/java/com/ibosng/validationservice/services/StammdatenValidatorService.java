package com.ibosng.validationservice.services;

import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;

public interface StammdatenValidatorService {
    Stammdaten getStammdaten(StammdatenDto stammdatenDto);
}
