package com.ibosng.validationservice.services;

import com.ibosng.dbservice.dtos.mitarbeiter.VordienstzeitenDto;
import com.ibosng.dbservice.entities.mitarbeiter.Vordienstzeiten;

public interface VordienstzeitenValidatorService {
    Vordienstzeiten getVordienstzeiten(VordienstzeitenDto vordienstzeitenDto);
}
