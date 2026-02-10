package com.ibosng.lhrservice.services;

import com.ibosng.dbservice.dtos.mitarbeiter.AbwesenheitDto;
import com.ibosng.dbservice.dtos.teilnehmer.AbmeldungDto;
import com.ibosng.dbservice.entities.Zeitausgleich;
import com.ibosng.dbservice.entities.lhr.Abwesenheit;
import com.ibosng.dbservice.entities.lhr.AbwesenheitStatus;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.urlaub.Urlaubsdaten;
import com.ibosng.lhrservice.dtos.DnEintrittDto;
import com.ibosng.lhrservice.dtos.DnEintritteDto;
import com.ibosng.lhrservice.dtos.DnStammStandaloneDto;
import com.ibosng.lhrservice.dtos.UrlaubsdatenStandaloneDto;
import com.ibosng.lhrservice.dtos.variabledaten.EintrittDto;
import com.ibosng.lhrservice.dtos.zeitdaten.DnZeitdatenDto;

import java.util.List;


public interface LHRMapperService {

    Abwesenheit saveIfNotExists(EintrittDto eintritt, Personalnummer personalnummer, AbwesenheitStatus status);

    AbwesenheitDto mapDnEintrittDto(EintrittDto eintrittDto);

    DnStammStandaloneDto updateExistingDnStammStandalone(DnStammStandaloneDto dnStammStandalone, DnStammStandaloneDto existingDnStammStandalone);

    DnStammStandaloneDto mapStammdatenAndVertragsdaten(Personalnummer personalnummer);

    DnEintritteDto createEintritteForAbmeldung(AbmeldungDto abmeldungDto);

    AbwesenheitDto updateAbwesenheit(DnEintritteDto dnEintritte, Integer abwesenheitId, AbwesenheitStatus status);

    AbwesenheitDto updateAbwesenheit(DnEintrittDto dnEintritt, Integer abwesenheitId, AbwesenheitStatus status);

    List<Zeitausgleich> getZeitausgleichen(DnZeitdatenDto[] zeitdaten, Personalnummer personalnummer);

    List<Urlaubsdaten> mapUrlaubsdatenAndUpdate(UrlaubsdatenStandaloneDto standalone, Personalnummer personalnummer);
}
