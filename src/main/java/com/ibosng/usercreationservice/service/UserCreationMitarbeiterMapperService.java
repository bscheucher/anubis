package com.ibosng.usercreationservice.service;

import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.usercreationservice.dto.UserAnlageDto;
import com.ibosng.usercreationservice.exception.TechnicalException;

public interface UserCreationMitarbeiterMapperService {

    UserAnlageDto toDto(Stammdaten stammdaten, Vertragsdaten vertragsdaten) throws TechnicalException;
}
