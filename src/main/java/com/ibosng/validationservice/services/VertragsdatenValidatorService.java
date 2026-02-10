package com.ibosng.validationservice.services;

import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;

public interface VertragsdatenValidatorService {
    Vertragsdaten getVertragsdaten(VertragsdatenDto vertragsdatenDto, Boolean isOnboarding);
}
