package com.ibosng.dbmapperservice.services;

import com.ibosng.dbibosservice.entities.pzleistung.Pzleistung;
import com.ibosng.dbservice.dtos.ZeitbuchungenDto;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.zeitbuchung.Zeitbuchung;

public interface ZeitbuchungenMapperService {
    ZeitbuchungenDto mapToDto(Pzleistung pzleistung, Personalnummer personalnummer);

    @Deprecated
    ZeitbuchungenDto mapToDto(Pzleistung pzleistung, String personalnummer);
    ZeitbuchungenDto mapToDto(Zeitbuchung zeitbuchung);
}
