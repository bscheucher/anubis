package com.ibosng.dbmapperservice.services.impl;

import com.ibosng.dbibosservice.entities.pzleistung.Pzleistung;
import com.ibosng.dbibosservice.services.KeytableIbosService;
import com.ibosng.dbibosservice.services.SeminarIbosService;
import com.ibosng.dbmapperservice.services.ZeitbuchungenMapperService;
import com.ibosng.dbservice.dtos.ZeitbuchungenDto;
import com.ibosng.dbservice.dtos.ZeitbuchungenType;
import com.ibosng.dbservice.dtos.zeiterfassung.BasicSeminarDto;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.zeitbuchung.Leistungserfassung;
import com.ibosng.dbservice.entities.zeitbuchung.Zeitbuchung;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Service
@RequiredArgsConstructor
public class ZeitbuchungenMapperServiceImpl implements ZeitbuchungenMapperService {

    private final SeminarIbosService seminarIbosService;
    private final KeytableIbosService keytableIbosService;

    @Override
    public ZeitbuchungenDto mapToDto(Pzleistung pzleistung, Personalnummer personalnummer) {
        ZeitbuchungenDto dto = new ZeitbuchungenDto();
        dto.setJahr(pzleistung.getId().getPMjahr());
        dto.setMonat(pzleistung.getId().getPMmonat());
        if (pzleistung.getLztyp() != null) {
            dto.setLeistungstyp(pzleistung.getLztyp().equals("l") ? "Leistung" : "Spesen");
        }
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (pzleistung.getLzdatum() != null) {
            dto.setLeistungsdatum(pzleistung.getLzdatum().format(dateFormatter));
        }
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        if (pzleistung.getLzdatumt() != null) {
            dto.setVon(pzleistung.getLzdatumt().format(timeFormatter));
        }
        if (pzleistung.getLzbist() != null) {
            dto.setBis(pzleistung.getLzbist().format(timeFormatter));
        }
        if (pzleistung.getLzpauseVon() != null) {
            dto.setPauseVon(pzleistung.getLzpauseVon().format(timeFormatter));
        }
        if (pzleistung.getLzpauseBis() != null) {
            dto.setPauseBis(pzleistung.getLzpauseBis().format(timeFormatter));
        }
        dto.setDauerStd(pzleistung.getLzdauer() != null ? pzleistung.getLzdauer().doubleValue() : null);

        if (pzleistung.getLztaettyp() != null) {
            dto.setAnAbwesenheit((pzleistung.getLztaettyp().equals("anw") ?
                    ZeitbuchungenType.ANWESENHEIT : ZeitbuchungenType.ABWESENHEIT));
        }
        BasicSeminarDto basicSeminarDto = new BasicSeminarDto();
        if (pzleistung.getLzsmnr() != null) {
            basicSeminarDto.setSeminarNumber(pzleistung.getLzsmnr());
        }

        seminarIbosService.findById(pzleistung.getLzsmnr()).ifPresent(
                seminarIbos -> basicSeminarDto.setSeminarBezeichnung(seminarIbos.getBezeichnung1()));

        dto.setSeminar(basicSeminarDto);
        dto.setKostenstellenummer(pzleistung.getLzkstgr());
        dto.setLeistungsort(pzleistung.getLzhomeoffice());
        if (personalnummer != null) {
            dto.setPersonalnummer(personalnummer.getPersonalnummer());
            if (personalnummer.getFirma() !=null) {
                dto.setBmdClient(personalnummer.getFirma().getBmdClient());
            }
        }

        if(pzleistung.getLztaetnr() != null) {
            keytableIbosService.findById(pzleistung.getLztaetnr()).ifPresent(
                    keytableIbos -> dto.setTaetigkeit(keytableIbos.getKyValueT2())
            );
        }

        return dto;
    }

    @Override
    @Deprecated
    public ZeitbuchungenDto mapToDto(Pzleistung pzleistung, String personalnummer) {
        ZeitbuchungenDto dto = new ZeitbuchungenDto();
        dto.setJahr(pzleistung.getId().getPMjahr());
        dto.setMonat(pzleistung.getId().getPMmonat());
        if (pzleistung.getLztyp() != null) {
            dto.setLeistungstyp(pzleistung.getLztyp().equals("l") ? "Leistung" : "Spesen");
        }
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (pzleistung.getLzdatum() != null) {
            dto.setLeistungsdatum(pzleistung.getLzdatum().format(dateFormatter));
        }
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        if (pzleistung.getLzdatumt() != null) {
            dto.setVon(pzleistung.getLzdatumt().format(timeFormatter));
        }
        if (pzleistung.getLzbist() != null) {
            dto.setBis(pzleistung.getLzbist().format(timeFormatter));
        }
        if (pzleistung.getLzpauseVon() != null) {
            dto.setPauseVon(pzleistung.getLzpauseVon().format(timeFormatter));
        }
        if (pzleistung.getLzpauseBis() != null) {
            dto.setPauseBis(pzleistung.getLzpauseBis().format(timeFormatter));
        }
        dto.setDauerStd(pzleistung.getLzdauer() != null ? pzleistung.getLzdauer().doubleValue() : null);

        if (pzleistung.getLztaettyp() != null) {
            dto.setAnAbwesenheit((pzleistung.getLztaettyp().equals("anw") ?
                    ZeitbuchungenType.ANWESENHEIT : ZeitbuchungenType.ABWESENHEIT));
        }
        BasicSeminarDto basicSeminarDto = new BasicSeminarDto();
        if (pzleistung.getLzsmnr() != null) {
            basicSeminarDto.setSeminarNumber(pzleistung.getLzsmnr());
        }

        seminarIbosService.findById(pzleistung.getLzsmnr()).ifPresent(
                seminarIbos -> basicSeminarDto.setSeminarBezeichnung(seminarIbos.getBezeichnung1()));

        dto.setSeminar(basicSeminarDto);
        dto.setKostenstellenummer(pzleistung.getLzkstgr());
        dto.setLeistungsort(pzleistung.getLzhomeoffice());
        dto.setPersonalnummer(personalnummer);

        if(pzleistung.getLztaetnr() != null) {
            keytableIbosService.findById(pzleistung.getLztaetnr()).ifPresent(
                    keytableIbos -> dto.setTaetigkeit(keytableIbos.getKyValueT2())
            );
        }

        return dto;
    }

    @Override
    public ZeitbuchungenDto mapToDto(Zeitbuchung zeitbuchung) {
        if (zeitbuchung == null) {
            return null;
        }
        ZeitbuchungenDto dto = new ZeitbuchungenDto();

        if (zeitbuchung.getLeistungserfassung() != null) {
            Leistungserfassung leistungserfassung = zeitbuchung.getLeistungserfassung();
            if (leistungserfassung.getLeistungsdatum() != null) {
                dto.setJahr(leistungserfassung.getLeistungsdatum().getYear());
                dto.setMonat(leistungserfassung.getLeistungsdatum().getMonthValue());
                dto.setLeistungsdatum(leistungserfassung.getLeistungsdatum().format(DateTimeFormatter.ISO_LOCAL_DATE));
            }
            if (leistungserfassung.getLeistungstyp() != null) {
                dto.setLeistungstyp(leistungserfassung.getLeistungstyp().getValue());
            }
            if (leistungserfassung.getPersonalnummer() != null) {
                dto.setPersonalnummer(leistungserfassung.getPersonalnummer().getPersonalnummer());
                if (leistungserfassung.getPersonalnummer().getFirma() != null) {
                    dto.setBmdClient(leistungserfassung.getPersonalnummer().getFirma().getBmdClient());
                }
            }
        }

        dto.setVon(zeitbuchung.getVon() != null ? zeitbuchung.getVon().format(DateTimeFormatter.ISO_LOCAL_TIME) : null);
        dto.setBis(zeitbuchung.getBis() != null ? zeitbuchung.getBis().format(DateTimeFormatter.ISO_LOCAL_TIME) : null);
        dto.setPauseVon(zeitbuchung.getPauseVon() != null ? zeitbuchung.getPauseVon().format(DateTimeFormatter.ISO_LOCAL_TIME) : null);
        dto.setPauseBis(zeitbuchung.getPauseBis() != null ? zeitbuchung.getPauseBis().format(DateTimeFormatter.ISO_LOCAL_TIME) : null);

        dto.setDauerStd(zeitbuchung.getDauerStd());

        if (zeitbuchung.getZeitbuchungstyp() != null && !isNullOrBlank(zeitbuchung.getZeitbuchungstyp().getType())) {
            dto.setTaetigkeit(zeitbuchung.getZeitbuchungstyp().getType());
        }

        if (zeitbuchung.getAnAbwesenheit() != null) {
            dto.setAnAbwesenheit(Boolean.TRUE.equals(zeitbuchung.getAnAbwesenheit()) ? ZeitbuchungenType.ANWESENHEIT : ZeitbuchungenType.ABWESENHEIT);
        }

        if (zeitbuchung.getSeminar() != null) {
            BasicSeminarDto seminarDto = new BasicSeminarDto();
            if (!isNullOrBlank(zeitbuchung.getSeminar().getBezeichnung())) {
                seminarDto.setSeminarBezeichnung(zeitbuchung.getSeminar().getBezeichnung());
            }
            if (zeitbuchung.getSeminar().getSeminarNummer() != null) {
                seminarDto.setSeminarNumber(zeitbuchung.getSeminar().getSeminarNummer());
            }
            if (zeitbuchung.getSeminar().getId() != null) {
                seminarDto.setId(zeitbuchung.getSeminar().getId());
            }
            dto.setSeminar(seminarDto);

        }

        if (zeitbuchung.getKostenstelle() != null) {
            if (!isNullOrBlank(zeitbuchung.getKostenstelle().getBezeichnung())) {
                dto.setKostenstelle(zeitbuchung.getKostenstelle().getBezeichnung());
                dto.setKostentraeger(zeitbuchung.getKostenstelle().getBezeichnung());
            }
            if (zeitbuchung.getKostenstelle().getId() != null) {
                dto.setKostenstellenummer(zeitbuchung.getKostenstelle().getId());
            }
        }
        if (isNullOrBlank(dto.getKostentraeger()) && dto.getSeminar() != null && !isNullOrBlank(dto.getSeminar().getSeminarBezeichnung())) {
            dto.setKostentraeger(dto.getSeminar().getSeminarBezeichnung());
        }

        dto.setLeistungsort(zeitbuchung.getLeistungsort() != null ? zeitbuchung.getLeistungsort().getValue() : null);

        return dto;
    }
}
