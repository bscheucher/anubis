package com.ibosng.dbmapperservice.services.impl;

import com.ibosng.dbibosservice.entities.AdresseIbos;
import com.ibosng.dbibosservice.entities.KeytableIbos;
import com.ibosng.dbibosservice.entities.MutterspracheIbos;
import com.ibosng.dbibosservice.entities.SeminarIbos;
import com.ibosng.dbibosservice.entities.smtn.SeminarTeilnehmerIbos;
import com.ibosng.dbibosservice.services.BenutzerIbosService;
import com.ibosng.dbibosservice.services.KeytableIbosService;
import com.ibosng.dbibosservice.services.MutterspracheIbosService;
import com.ibosng.dbibosservice.services.StandortIbosService;
import com.ibosng.dbservice.dtos.TeilnehmerCsvDto;
import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.dbservice.services.TeilnehmerStagingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static com.ibosng.dbibosservice.utils.Helpers.isNullOrBlank;
import static com.ibosng.dbservice.utils.Helpers.localDateToString;
import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;
import static com.ibosng.dbservice.utils.Parsers.isValidDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeilnehmerMapperServiceImpl implements com.ibosng.dbmapperservice.services.TeilnehmerMapperService {

    private static final String ANREDE = "Anrede";

    private final BenutzerIbosService benutzerIbosService;
    private final StandortIbosService standortIbosService;
    private final TeilnehmerStagingService teilnehmerStagingService;
    private final KeytableIbosService keytableIbosService;
    private final MutterspracheIbosService mutterspracheIbosService;


    @Override
    public TeilnehmerStaging adresseIbosToTeilnehmerStaging(AdresseIbos adresseIbos) {
        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        mapAdresseIbosToTeilnehmerStagingFields(teilnehmerStaging, adresseIbos);
        if (!isNullOrBlank(adresseIbos.getAdgeschlecht())) {
            teilnehmerStaging.setGeschlecht(adresseIbos.getAdgeschlecht());
        }
        if (adresseIbos.getAdanrede() != null) {
            // This is actually the abbreviation W,M, etc.
            String anrede = keytableIbosService.findFirstByKyNameAndKyNrOrderByKyIndex(ANREDE, adresseIbos.getAdanrede()).getKyValueM1();
            if (!isNullOrBlank(anrede)) {
                teilnehmerStaging.setAnrede(mapAnredeIbosToAnrede(anrede));
                if(isNullOrBlank(adresseIbos.getAdgeschlecht())){
                    teilnehmerStaging.setGeschlecht(anrede);
                }
            }
        }
        return teilnehmerStaging;
    }

    @Override
    public TeilnehmerStaging adresseIbosToTeilnehmerStagingDirect(AdresseIbos adresseIbos) {
        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        mapAdresseIbosToTeilnehmerStagingFields(teilnehmerStaging, adresseIbos);
        if (adresseIbos.getAdanrede() != null) {
            String anrede = keytableIbosService.findFirstByKyNameAndKyNrOrderByKyIndex(ANREDE, adresseIbos.getAdanrede()).getKyValueM1();
            if (!isNullOrBlank(anrede)) {
                teilnehmerStaging.setGeschlecht(anrede);
                teilnehmerStaging.setAnrede(mapAnredeIbosToAnrede(anrede));
            }
        }
        return teilnehmerStaging;
    }

    private String mapAnredeIbosToAnrede(String anrede) {
        if ("M".equals(anrede)) {
            return "Herr";
        } else if ("W".equals(anrede)) {
            return "Frau";
        }
        return "keine Angabe";
    }

    private void mapAdresseIbosToTeilnehmerStagingFields(TeilnehmerStaging teilnehmerStaging, AdresseIbos adresseIbos) {
        if (!isNullOrBlank(adresseIbos.getAdvnf2())) {
            teilnehmerStaging.setVorname(adresseIbos.getAdvnf2());
        }
        if (!isNullOrBlank(adresseIbos.getAdznf1())) {
            teilnehmerStaging.setNachname(adresseIbos.getAdznf1());
        }
        if (adresseIbos.getAdgebdatum() != null) {
            teilnehmerStaging.setGeburtsdatum(adresseIbos.getAdgebdatum().toString());
        }
        if (adresseIbos.getAderda() != null && isValidDateTime(adresseIbos.getAderda().toString())) {
            teilnehmerStaging.setCreatedOn(adresseIbos.getAderda());
        }
        if (!isNullOrBlank(adresseIbos.getAdaeuser())) {
            teilnehmerStaging.setChangedBy(adresseIbos.getAdaeuser());
        }
        if (!isNullOrBlank(adresseIbos.getAderuser())) {
            teilnehmerStaging.setCreatedBy(adresseIbos.getAderuser());
        }
        if (!isNullOrBlank(adresseIbos.getAdmobil1())) {
            teilnehmerStaging.setTelefon(adresseIbos.getAdmobil1());
        } else if (!isNullOrBlank(adresseIbos.getAdmobil2())) {
            teilnehmerStaging.setTelefon(adresseIbos.getAdmobil2());
        } else if (!isNullOrBlank(adresseIbos.getAdtelp())) {
            teilnehmerStaging.setTelefon(adresseIbos.getAdtelp());
        }
        if (!isNullOrBlank(adresseIbos.getAdsvnr())) {
            teilnehmerStaging.setSvNummer(adresseIbos.getAdsvnr().replace(" ", ""));
        }
        if (!isNullOrBlank(adresseIbos.getAdemail1())) {
            teilnehmerStaging.setEmail(adresseIbos.getAdemail1());
        }
        if (!isNullOrBlank(adresseIbos.getAdort())) {
            teilnehmerStaging.setOrt(adresseIbos.getAdort());
        }
        if (!isNullOrBlank(adresseIbos.getAdplz())) {
            teilnehmerStaging.setPlz(adresseIbos.getAdplz());
        }
        if (!isNullOrBlank(adresseIbos.getAdstrasse())) {
            teilnehmerStaging.setStrasse(adresseIbos.getAdstrasse());
        }
        if (adresseIbos.getAdtitel() != null) {
            String titel = benutzerIbosService.getTitelFromId(adresseIbos.getAdtitel());
            if (!isNullOrBlank(titel)) {
                teilnehmerStaging.setTitel(titel);
            }
        }
        if (adresseIbos.getAdtitelv() != null) {
            String titel2 = benutzerIbosService.getTitelFromId(adresseIbos.getAdtitelv());
            if (!isNullOrBlank(titel2)) {
                teilnehmerStaging.setTitel2(titel2);
            }
        }

        if (!isNullOrBlank(adresseIbos.getAdgeschlecht())) {
            teilnehmerStaging.setGeschlecht(adresseIbos.getAdgeschlecht());
        }

        if (adresseIbos.getAdanrede() != null) {
            KeytableIbos anredeIbos = keytableIbosService.findFirstByKyNameAndKyNrOrderByKyIndex("Anrede", adresseIbos.getAdanrede());
            teilnehmerStaging.setAnrede(anredeIbos.getKyValueT1());
        }

        if (adresseIbos.getAdstaatsb() != null) {
            String nation = standortIbosService.getLandIdFromId(adresseIbos.getAdstaatsb());
            if (!isNullOrBlank(nation)) {
                teilnehmerStaging.setNation(nation);
            }
        }

        if (adresseIbos.getAdmuttersprache() != null) {
            Optional<MutterspracheIbos> mutterspracheIbos = mutterspracheIbosService.findById(adresseIbos.getAdmuttersprache());
            if (mutterspracheIbos.isPresent() && !isNullOrBlank(mutterspracheIbos.get().getName())) {
                teilnehmerStaging.setMuttersprache(mutterspracheIbos.get().getName());
            }
        }

        if (!isNullOrBlank(adresseIbos.getAdgebort())) {
            teilnehmerStaging.setGerburtsort(adresseIbos.getAdgebort());
        }

        if (!isNullOrBlank(adresseIbos.getAdgebland())) {
            teilnehmerStaging.setUrsprungsland(adresseIbos.getAdgebland());
        }

        List<TeilnehmerStaging> tns = teilnehmerStagingService.findByVornameAndNachname(
                !isNullOrBlank(adresseIbos.getAdvnf2()) ? adresseIbos.getAdvnf2() : null,
                !isNullOrBlank(adresseIbos.getAdznf1()) ? adresseIbos.getAdznf1() : null);
        Optional<TeilnehmerStaging> tn = tns.stream().findFirst();
        tn.ifPresent(staging -> teilnehmerStaging.setTeilnehmerId(staging.getTeilnehmerId()));
    }

    @Override
    public void mapSeminarDataToTeilnehmerStaging(TeilnehmerStaging teilnehmerStaging, SeminarTeilnehmerIbos seminarTeilnehmer, SeminarIbos seminarIbos) {
        if (seminarIbos.getDatumVon() != null) {
            teilnehmerStaging.setSeminarStartDate(seminarIbos.getDatumVon().toString());
        }
        if (seminarIbos.getDatumBis() != null) {
            teilnehmerStaging.setSeminarEndDate(seminarIbos.getDatumBis().toString());
        }
        if (seminarIbos.getBezeichnung1() != null) {
            teilnehmerStaging.setSeminarIdentifier(seminarIbos.getBezeichnung1());
        }
        if (seminarTeilnehmer.getTaanmeldedatum() != null) {
            teilnehmerStaging.setEintritt(seminarTeilnehmer.getTaanmeldedatum().toString());
        }
        if (seminarTeilnehmer.getTateilnahmevon() != null) {
            teilnehmerStaging.setTeilnahmeVon(localDateToString(seminarTeilnehmer.getTateilnahmevon()));
        }
        if (seminarTeilnehmer.getTateilnahmebis() != null) {
            teilnehmerStaging.setTeilnahmeBis(localDateToString(seminarTeilnehmer.getTateilnahmebis()));
        }
        if (seminarTeilnehmer.getTadatumbeginn() != null) {
            teilnehmerStaging.setZubuchung(seminarTeilnehmer.getTadatumbeginn().toString());
        }
        if (seminarTeilnehmer.getTaalternativeMNummer() != null) {
            teilnehmerStaging.setMassnahmennummer(seminarTeilnehmer.getTaalternativeMNummer());
        }
        if (seminarTeilnehmer.getTaalternativeVNummer() != null) {
            teilnehmerStaging.setVeranstaltungsnummer(seminarTeilnehmer.getTaalternativeVNummer());
        }
        if (seminarTeilnehmer.getTargsKYnr() != null) {
            Integer rgs = standortIbosService.getRGSNummer(seminarTeilnehmer.getTargsKYnr());
            if (rgs != null) {
                teilnehmerStaging.setRgs(String.valueOf(rgs));
            }
        }
        if (seminarTeilnehmer.getTaamsbetreuer() != null) {
            String[] betreuerStrings = seminarTeilnehmer.getTaamsbetreuer().split(" ", 2);
            String firstPart = betreuerStrings[0];
            String secondPart = betreuerStrings.length > 1 ? betreuerStrings[1] : "";
            teilnehmerStaging.setBetreuerVorname(firstPart);
            teilnehmerStaging.setBetreuerNachname(secondPart);
        }
        teilnehmerStaging.setId(null);
    }

    @Override
    public TeilnehmerStaging mapInvalidToTeilnehmerStaging(TeilnehmerDto invalidTeilnehmerDto, String createdBy, String serviceName) {
        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        if (invalidTeilnehmerDto.getId() != 0) {
            teilnehmerStaging.setTeilnehmerId(invalidTeilnehmerDto.getId());
        }
        if (invalidTeilnehmerDto.getVorname() != null) {
            teilnehmerStaging.setVorname(invalidTeilnehmerDto.getVorname());
        }
        if (invalidTeilnehmerDto.getNachname() != null) {
            teilnehmerStaging.setNachname(invalidTeilnehmerDto.getNachname());
        }
        if (invalidTeilnehmerDto.getTitel() != null) {
            teilnehmerStaging.setTitel(invalidTeilnehmerDto.getTitel());
        }
        if (!isNullOrBlank(invalidTeilnehmerDto.getGeburtsort())) {
            teilnehmerStaging.setGerburtsort(invalidTeilnehmerDto.getGeburtsort());
        }
        if (invalidTeilnehmerDto.getTitel2() != null) {
            teilnehmerStaging.setTitel2(invalidTeilnehmerDto.getTitel2());
        }
        if (invalidTeilnehmerDto.getGeschlecht() != null) {
            teilnehmerStaging.setGeschlecht(invalidTeilnehmerDto.getGeschlecht());
        }
        if (invalidTeilnehmerDto.getSvNummer() != null) {
            teilnehmerStaging.setSvNummer(invalidTeilnehmerDto.getSvNummer().replace(" ", ""));
        }
        if (invalidTeilnehmerDto.getStrasse() != null) {
            teilnehmerStaging.setStrasse(invalidTeilnehmerDto.getStrasse());
        }
        if (invalidTeilnehmerDto.getGeburtsdatum() != null) {
            teilnehmerStaging.setGeburtsdatum(invalidTeilnehmerDto.getGeburtsdatum());
        }
        if (invalidTeilnehmerDto.getUrsprungsland() != null) {
            teilnehmerStaging.setUrsprungsland(invalidTeilnehmerDto.getUrsprungsland());
        }
        teilnehmerStaging.setPlz(String.valueOf(invalidTeilnehmerDto.getPlz()));
        if (invalidTeilnehmerDto.getOrt() != null) {
            teilnehmerStaging.setOrt(invalidTeilnehmerDto.getOrt());
        }
        if (invalidTeilnehmerDto.getTelefon() != null) {
            teilnehmerStaging.setTelefon(invalidTeilnehmerDto.getTelefon());
        }
        if (invalidTeilnehmerDto.getBuchungsstatus() != null) {
            teilnehmerStaging.setBuchungsstatus(invalidTeilnehmerDto.getBuchungsstatus());
        }
        teilnehmerStaging.setRgs(String.valueOf(invalidTeilnehmerDto.getRgs()));
        if (invalidTeilnehmerDto.getAnmerkung() != null) {
            teilnehmerStaging.setAnmerkung(invalidTeilnehmerDto.getAnmerkung());
        }
        if (invalidTeilnehmerDto.getZubuchung() != null) {
            teilnehmerStaging.setZubuchung(invalidTeilnehmerDto.getZubuchung());
        }
        if (invalidTeilnehmerDto.getGeplant() != null) {
            teilnehmerStaging.setGeplant(invalidTeilnehmerDto.getGeplant());
        }
        if (invalidTeilnehmerDto.getEintritt() != null) {
            teilnehmerStaging.setEintritt(invalidTeilnehmerDto.getEintritt());
        }
        if (invalidTeilnehmerDto.getAustritt() != null) {
            teilnehmerStaging.setAustritt(invalidTeilnehmerDto.getAustritt());
        }
        if (invalidTeilnehmerDto.getBetreuerTitel() != null) {
            teilnehmerStaging.setBetreuerTitel(invalidTeilnehmerDto.getBetreuerTitel());
        }
        if (invalidTeilnehmerDto.getBetreuerVorname() != null) {
            teilnehmerStaging.setBetreuerVorname(invalidTeilnehmerDto.getBetreuerVorname());
        }
        if (invalidTeilnehmerDto.getBetreuerNachname() != null) {
            teilnehmerStaging.setBetreuerNachname(invalidTeilnehmerDto.getBetreuerNachname());
        }
        if (invalidTeilnehmerDto.getMassnahmennummer() != null) {
            teilnehmerStaging.setMassnahmennummer(invalidTeilnehmerDto.getMassnahmennummer());
        }
        if (invalidTeilnehmerDto.getVeranstaltungsnummer() != null) {
            teilnehmerStaging.setVeranstaltungsnummer(invalidTeilnehmerDto.getVeranstaltungsnummer());
        }
        if (invalidTeilnehmerDto.getEmail() != null) {
            teilnehmerStaging.setEmail(invalidTeilnehmerDto.getEmail());
        }
        if (invalidTeilnehmerDto.getSeminarBezeichnung() != null) {
            teilnehmerStaging.setSeminarIdentifier(invalidTeilnehmerDto.getSeminarBezeichnung());
        }
        if (invalidTeilnehmerDto.getNation() != null) {
            teilnehmerStaging.setNation(invalidTeilnehmerDto.getNation());
        }
        if (invalidTeilnehmerDto.getAnrede() != null) {
            teilnehmerStaging.setAnrede(invalidTeilnehmerDto.getAnrede());
        }
        if(invalidTeilnehmerDto.getMuttersprache() != null){
            teilnehmerStaging.setMuttersprache(invalidTeilnehmerDto.getMuttersprache());
        }
        teilnehmerStaging.setSource(TeilnehmerSource.MANUAL);
        teilnehmerStaging.setCreatedBy(createdBy);
        teilnehmerStaging.setImportFilename(serviceName + "_" + getLocalDateNow().truncatedTo(ChronoUnit.SECONDS));
        return teilnehmerStagingService.save(teilnehmerStaging);
    }

    @Override
    public TeilnehmerCsvDto mapToCsv(Teilnehmer teilnehmer) {
        TeilnehmerCsvDto teilnehmerCsvDto = new TeilnehmerCsvDto();
        teilnehmerCsvDto.setId(teilnehmer.getId());
        teilnehmerCsvDto.setNachname(teilnehmer.getNachname());
        teilnehmerCsvDto.setVorname(teilnehmer.getVorname());
        if (teilnehmer.getGeburtsdatum() != null) {
            teilnehmerCsvDto.setGerburtsdatum(teilnehmer.getGeburtsdatum().format(DateTimeFormatter.ISO_DATE));
        }
        if (teilnehmer.getUrsprung() != null) {
            if (teilnehmer.getUrsprung().getLand() != null && !isNullOrBlank(teilnehmer.getUrsprung().getLand().getLandName())) {
                teilnehmerCsvDto.setUrsprungsland(teilnehmer.getUrsprung().getLand().getLandName());
            }

            if (!isNullOrBlank(teilnehmer.getUrsprung().getOrt())) {
                teilnehmerCsvDto.setGerburtsort(teilnehmer.getUrsprung().getOrt());
            }
        }
        teilnehmerCsvDto.setZiel(teilnehmer.getZiel());
        if (teilnehmer.getVermittelbarAb() != null) {
            teilnehmerCsvDto.setVermittelbarAb(teilnehmer.getVermittelbarAb().format(DateTimeFormatter.ISO_DATE));
        }
        teilnehmerCsvDto.setSvnr(teilnehmer.getSvNummer());
        teilnehmerCsvDto.setNotiz(teilnehmer.getVermittlungsNotiz());
        return teilnehmerCsvDto;
    }
}
