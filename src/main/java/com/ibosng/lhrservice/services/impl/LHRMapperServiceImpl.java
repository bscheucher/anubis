package com.ibosng.lhrservice.services.impl;

import com.ibosng.dbservice.dtos.mitarbeiter.AbwesenheitDto;
import com.ibosng.dbservice.dtos.teilnehmer.AbmeldungDto;
import com.ibosng.dbservice.dtos.zeiterfassung.zeitdaten.BuchungDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.Zeitausgleich;
import com.ibosng.dbservice.entities.lhr.Abwesenheit;
import com.ibosng.dbservice.entities.lhr.AbwesenheitStatus;
import com.ibosng.dbservice.entities.lhr.Erreichbarkeit;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.*;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.urlaub.Anspruch;
import com.ibosng.dbservice.entities.urlaub.Urlaubsdaten;
import com.ibosng.dbservice.services.TeilnehmerService;
import com.ibosng.dbservice.services.lhr.AbwesenheitService;
import com.ibosng.dbservice.services.lhr.ErreichbarkeitService;
import com.ibosng.dbservice.services.mitarbeiter.*;
import com.ibosng.dbservice.services.urlaub.AnspuruchService;
import com.ibosng.dbservice.services.urlaub.UrlaubsdatenService;
import com.ibosng.lhrservice.dtos.*;
import com.ibosng.lhrservice.dtos.urlaube.UrlaubswertDto;
import com.ibosng.lhrservice.dtos.variabledaten.EintrittDto;
import com.ibosng.lhrservice.dtos.variabledaten.ZeitangabeDto;
import com.ibosng.lhrservice.dtos.zeitdaten.DnZeitdatenDto;
import com.ibosng.lhrservice.exceptions.LHRException;
import com.ibosng.lhrservice.services.HelperService;
import com.ibosng.lhrservice.services.LHREnvironmentService;
import com.ibosng.lhrservice.services.LHRMapperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static com.ibosng.dbservice.utils.Parsers.*;
import static com.ibosng.lhrservice.utils.Constants.BUCHUNGANFRAGE_ZEITAUSGLEICH;
import static com.ibosng.lhrservice.utils.Constants.LHR_SERVICE;
import static com.ibosng.lhrservice.utils.Parsers.parseStringToInteger;

@Service
@Slf4j
@RequiredArgsConstructor
public class LHRMapperServiceImpl implements LHRMapperService {

    private static final String ERREICHBARKEIT_EMAIL_PRIVAT = "E-Mail-Adresse privat";
    private static final String ERREICHBARKEIT_TELEFON_PRIVAT = "Telefonnummer privat";

    private final ModelMapper modelMapper;
    private final StammdatenService stammdatenService;
    private final VertragsdatenService vertragsdatenService;

    private final GehaltInfoService gehaltsinfoService;
    private final ErreichbarkeitService erreichbarkeitService;
    private final ArbeitszeitenInfoService arbeitszeitenInfoService;
    private final PersonalnummerService personalnummerService;
    private final AbwesenheitService abwesenheitService;
    private final LHREnvironmentService LHREnvironmentService;
    private final TeilnehmerService teilnehmerService;
    private final AnspuruchService anspuruchService;
    private final UrlaubsdatenService urlaubsdatenService;
    private final HelperService helperService;

    @Override
    public Abwesenheit saveIfNotExists(EintrittDto eintritt, Personalnummer personalnummer, AbwesenheitStatus status) {
        Abwesenheit abwesenheit = abwesenheitService.findByPersonalnummerVonAndBis(
                personalnummer.getId(),
                LocalDate.parse(eintritt.getZeitangabe().getVon()),
                LocalDate.parse(eintritt.getZeitangabe().getBis()));
        if (abwesenheit == null) {
            abwesenheit = new Abwesenheit();
            abwesenheit.setGrund(eintritt.getGrund());
            abwesenheit.setBeschreibung(eintritt.getBeschreibung());
            abwesenheit.setKommentar(eintritt.getKommentar());
            abwesenheit.setArt(eintritt.getArt());
            abwesenheit.setIdLhr(eintritt.getId());
            abwesenheit.setStatus(status);
            abwesenheit.setPersonalnummer(personalnummer);
            Benutzer fuehrungskraft = helperService.getFuehrungskraefte(personalnummer);
            if (fuehrungskraft != null) {
                Set<Benutzer> fuehrungskraefte = new HashSet<>();
                fuehrungskraefte.add(fuehrungskraft);
                abwesenheit.setFuehrungskraefte(fuehrungskraefte);
            }
            if (eintritt.getZeitangabe() != null) {
                if (eintritt.getZeitangabe().getVon() != null) {
                    abwesenheit.setVon(LocalDate.parse(eintritt.getZeitangabe().getVon()));
                }
                if (eintritt.getZeitangabe().getBis() != null) {
                    abwesenheit.setBis(LocalDate.parse(eintritt.getZeitangabe().getBis()));
                }
            }
        } else {
            abwesenheit.setGrund(eintritt.getGrund());
            abwesenheit.setBeschreibung(eintritt.getBeschreibung());
            abwesenheit.setKommentar(eintritt.getKommentar());
            abwesenheit.setArt(eintritt.getArt());
        }
        abwesenheit.setCreatedBy(LHR_SERVICE);
        abwesenheit = abwesenheitService.save(abwesenheit);
        return abwesenheit;
    }

    @Override
    public AbwesenheitDto mapDnEintrittDto(EintrittDto eintrittDto) {
        AbwesenheitDto abwesenheitDto = new AbwesenheitDto();
        try {
            abwesenheitDto.setType(AbwesenheitType.valueOf(eintrittDto.getGrund()));
        } catch (IllegalArgumentException ex) {
            log.debug("Abwesenheit type {} not identified", eintrittDto.getGrund());
        }
        if (eintrittDto.getZeitangabe() != null) {
            if (eintrittDto.getZeitangabe().getVon() != null) {
                abwesenheitDto.setStartDate(LocalDate.parse(eintrittDto.getZeitangabe().getVon()));
            }
            if (eintrittDto.getZeitangabe().getBis() != null) {
                abwesenheitDto.setEndDate(LocalDate.parse(eintrittDto.getZeitangabe().getBis()));
            }
        }
        abwesenheitDto.setComment(eintrittDto.getKommentar());
        return abwesenheitDto;
    }


    @Override
    public DnStammStandaloneDto updateExistingDnStammStandalone(DnStammStandaloneDto dnStammStandalone, DnStammStandaloneDto existingDnStammStandalone) {
        if (dnStammStandalone != null && existingDnStammStandalone != null) {
            try {
                if (dnStammStandalone.getDienstnehmer() != null && existingDnStammStandalone.getDienstnehmer() != null) {
                    modelMapper.map(dnStammStandalone.getDienstnehmer(), existingDnStammStandalone.getDienstnehmer());
                }
                if (dnStammStandalone.getPrimaryDienstnehmer() != null && existingDnStammStandalone.getPrimaryDienstnehmer() != null) {
                    modelMapper.map(dnStammStandalone.getPrimaryDienstnehmer(), existingDnStammStandalone.getPrimaryDienstnehmer());
                }
                if (dnStammStandalone.getDienstnehmerstamm() != null && existingDnStammStandalone.getDienstnehmerstamm() != null) {
                    modelMapper.map(dnStammStandalone.getDienstnehmerstamm(), existingDnStammStandalone.getDienstnehmerstamm());
                }

                if (dnStammStandalone.getDienstnehmerstamm() != null &&
                        dnStammStandalone.getDienstnehmerstamm().getBankverbindung() != null && dnStammStandalone.getDienstnehmerstamm().getBankverbindung().getAuftraggeberBank() != null
                        && existingDnStammStandalone.getDienstnehmerstamm() != null &&
                        existingDnStammStandalone.getDienstnehmerstamm().getBankverbindung() != null && existingDnStammStandalone.getDienstnehmerstamm().getBankverbindung().getAuftraggeberBank() != null) {
                    modelMapper.map(dnStammStandalone.getDienstnehmerstamm().getBankverbindung().getAuftraggeberBank(),
                            existingDnStammStandalone.getDienstnehmerstamm().getBankverbindung().getAuftraggeberBank());
                }
            } catch (MappingException e) {
                log.error("Error during object mapping for personalnummer {}", dnStammStandalone.getDienstnehmer().getDnNr(), e);
            }
            return existingDnStammStandalone;
        }
        log.error("No dienstnehmer found");
        return null;
    }


    @Override
    public DnStammStandaloneDto mapStammdatenAndVertragsdaten(Personalnummer personalnummer) {
        DnStammStandaloneDto dnStammStandalone = new DnStammStandaloneDto();
        Stammdaten stammdaten = stammdatenService.findByPersonalnummerId(personalnummer.getId());
        Vertragsdaten vertragsdaten = vertragsdatenService.findByPersonalnummerIdAndStatus(personalnummer.getId(), List.of(MitarbeiterStatus.VALIDATED))
                .stream().findFirst().orElse(null);
        if (stammdaten == null) {
            throw new LHRException("Stammdaten are null, stopping process!!!", new RuntimeException());
        }
        if (vertragsdaten == null && Boolean.FALSE.equals(personalnummer.getIsIbosngOnboarded())) {
            vertragsdaten = vertragsdatenService.findByPersonalnummerIdAndStatus(personalnummer.getId(), List.of(MitarbeiterStatus.NOT_VALIDATED, MitarbeiterStatus.ACTIVE))
                    .stream().findFirst().orElse(null);
        }
        if (vertragsdaten == null) {
            throw new LHRException("Vertragsdaten are null in mapStammdatenAndVertragsdaten, stopping process!!!", new RuntimeException());
        }
        dnStammStandalone.setDienstnehmer(createDienstnehmerRefDto(vertragsdaten));
        setDataFromStammdaten(stammdaten, dnStammStandalone);
        setDataFromVertragsdaten(vertragsdaten, dnStammStandalone);
        return dnStammStandalone;
    }

    @Override
    public AbwesenheitDto updateAbwesenheit(DnEintritteDto dnEintritte, Integer abwesenheitId, AbwesenheitStatus status) {
        if (dnEintritte.getEintritte() == null || dnEintritte.getEintritte().size() != 1) {
            return null;
        }
        return updateAbwesenheit(new DnEintrittDto(dnEintritte.getDienstnehmer(), dnEintritte.getEintritte().get(0)), abwesenheitId, status);
    }

    @Override
    public AbwesenheitDto updateAbwesenheit(DnEintrittDto dnEintritt, Integer abwesenheitId, AbwesenheitStatus status) {
        if (dnEintritt == null || dnEintritt.getDienstnehmer() == null || dnEintritt.getEintritt() == null || abwesenheitId == null) {
            return null;
        }

        Optional<Abwesenheit> abwesenheitOpt = abwesenheitService.findById(abwesenheitId);
        if (abwesenheitOpt.isEmpty()) {
            return null;
        }
        Abwesenheit abwesenheit = abwesenheitOpt.get();
        abwesenheit.setGrund(dnEintritt.getEintritt().getGrund());
        abwesenheit.setBeschreibung(dnEintritt.getEintritt().getBeschreibung());
        abwesenheit.setKommentar(dnEintritt.getEintritt().getKommentar());
        abwesenheit.setArt(dnEintritt.getEintritt().getArt());
        abwesenheit.setIdLhr(dnEintritt.getEintritt().getId());
        abwesenheit.setStatus(status);
        if (dnEintritt.getEintritt().getZeitangabe() != null) {
            if (dnEintritt.getEintritt().getZeitangabe().getVon() != null) {
                abwesenheit.setVon(LocalDate.parse(dnEintritt.getEintritt().getZeitangabe().getVon()));
            }
            if (dnEintritt.getEintritt().getZeitangabe().getBis() != null) {
                abwesenheit.setBis(LocalDate.parse(dnEintritt.getEintritt().getZeitangabe().getBis()));
            }
        }
        return abwesenheitService.mapToAbwesenheitDto(abwesenheitService.save(abwesenheit));
    }


    private void setDataFromStammdaten(Stammdaten stammdaten, DnStammStandaloneDto dnStammStandalone) {
        DienstnehmerstammDto dienstnehmerstamm = new DienstnehmerstammDto();
        List<DnErreichbarkeitDto> erreichbarkeitList = new ArrayList<>();

        if (!isNullOrBlank(stammdaten.getVorname())) {
            dienstnehmerstamm.setVorname(stammdaten.getVorname());
        }
        if (!isNullOrBlank(stammdaten.getNachname())) {
            dienstnehmerstamm.setName(stammdaten.getNachname());
        }
        if (stammdaten.getSvnr() != null) {
            String svn = String.valueOf(stammdaten.getSvnr());
            if (svn.length() == 6) {
                svn = "0000" + svn;
            }
            dienstnehmerstamm.setSvNummer(svn);
        }
        if (stammdaten.getFamilienstand() != null) {
            dienstnehmerstamm.setFamilienstand(stammdaten.getFamilienstand().getAbbreviation());
        }
        if (stammdaten.getGeburtsdatum() != null) {
            dienstnehmerstamm.setGeburtsdatum(stammdaten.getGeburtsdatum());
        }
        if (stammdaten.getGeschlecht() != null) {
            dienstnehmerstamm.setGeschlecht(stammdaten.getGeschlecht().getAbbreviation());
        }
        if (stammdaten.getStaatsbuergerschaft() != null) {
            dienstnehmerstamm.setStaatsbuergerschaft(stammdaten.getStaatsbuergerschaft().getLhrKz());
        }
        if (stammdaten.getTitel() != null) {
            dienstnehmerstamm.setTitel(stammdaten.getTitel().getName());
        }
        if (stammdaten.getTitel2() != null) {
            dienstnehmerstamm.setTitel2(stammdaten.getTitel2().getName());
        }
        if (stammdaten.getMobilnummer() != null && stammdaten.getMobilnummer().getTelefonnummer() != null) {
            String telefonString = String.valueOf(stammdaten.getMobilnummer().getTelefonnummer());
            dienstnehmerstamm.setTelefon(telefonString);
            Erreichbarkeit erreichbarkeit = erreichbarkeitService.findByErreichbarkeitsart(ERREICHBARKEIT_TELEFON_PRIVAT);
            if (erreichbarkeit != null) {
                DnErreichbarkeitDto dnErreichbarkeit = createErreichbarkeitDto(erreichbarkeit, telefonString);
                erreichbarkeitList.add(dnErreichbarkeit);
            }
        }
        AdresseDto adresse = getAdresseDto(stammdaten);
        if (adresse != null) {
            dienstnehmerstamm.setAdresse(adresse);
        }
        DnBankverbindungDto dnBankverbindung = getDnBankverbindungDto(stammdaten);
        if (dnBankverbindung != null) {
            dienstnehmerstamm.setBankverbindung(dnBankverbindung);
        }
        if (!isNullOrBlank(stammdaten.getGeburtsname())) {
            dienstnehmerstamm.setGeburtsname(stammdaten.getGeburtsname());
        }

        if (!isNullOrBlank(stammdaten.getEmail())) {
            Erreichbarkeit erreichbarkeit = erreichbarkeitService.findByErreichbarkeitsart(ERREICHBARKEIT_EMAIL_PRIVAT);
            if (erreichbarkeit != null) {
                DnErreichbarkeitDto dnErreichbarkeit = createErreichbarkeitDto(erreichbarkeit, stammdaten.getEmail());
                erreichbarkeitList.add(dnErreichbarkeit);
            }
        }
        dienstnehmerstamm.setErreichbarkeiten(erreichbarkeitList);
        dnStammStandalone.setDienstnehmerstamm(dienstnehmerstamm);
    }

    private void setDataFromVertragsdaten(Vertragsdaten vertragsdaten, DnStammStandaloneDto dnStammStandalone) {
        GehaltInfo gehaltInfo = gehaltsinfoService.findByVertragsdatenId(vertragsdaten.getId());
        if (gehaltInfo != null && gehaltInfo.getLhrGehaltsinfo() != null) {
            DnGehaltsinfoDto dnGehaltsinfo = new DnGehaltsinfoDto();
            if (!isNullOrBlank(gehaltInfo.getLhrGehaltsinfo().getKz())) {
                dnGehaltsinfo.setKz(gehaltInfo.getLhrGehaltsinfo().getKz());
            }
            if (!isNullOrBlank(gehaltInfo.getLhrGehaltsinfo().getName())) {
                dnGehaltsinfo.setName(gehaltInfo.getLhrGehaltsinfo().getName());
            }
            if (!isNullOrBlank(gehaltInfo.getLhrGehaltsinfo().getPasswort())) {
                dnGehaltsinfo.setPasswort(gehaltInfo.getLhrGehaltsinfo().getPasswort());
            }
            dnStammStandalone.getDienstnehmerstamm().setGehaltsinfo(dnGehaltsinfo);
        }

        ArbeitszeitenInfo arbeitszeitenInfo = arbeitszeitenInfoService.findByVertragsdatenId(vertragsdaten.getId());
        if (arbeitszeitenInfo != null) {
            DienstnehmerstammZeiterfassungDto zeiterfassungDto = new DienstnehmerstammZeiterfassungDto();
            zeiterfassungDto.setZeitrelevant(true);
            if (arbeitszeitenInfo.getArbeitszeitmodell() != null && arbeitszeitenInfo.getArbeitszeitmodell().getLhrNr() != null) {
                zeiterfassungDto.setFirstZeitmodell(arbeitszeitenInfo.getArbeitszeitmodell().getLhrNr());
            }
            dnStammStandalone.getDienstnehmerstamm().setZeiterfassung(zeiterfassungDto);
        }
    }

    private DnErreichbarkeitDto createErreichbarkeitDto(Erreichbarkeit erreichbarkeitEntity, String erreichbarkeit) {
        DnErreichbarkeitDto dnErreichbarkeit = new DnErreichbarkeitDto();
        dnErreichbarkeit.setKz(erreichbarkeitEntity.getKz());
        dnErreichbarkeit.setWert(erreichbarkeit);
        return dnErreichbarkeit;
    }

    private DnBankverbindungDto getDnBankverbindungDto(Stammdaten stammdaten) {
        DnBankverbindungDto dnBankverbindung = null;
        if (stammdaten.getBank() != null) {
            dnBankverbindung = new DnBankverbindungDto();
            BankDaten bankDaten = stammdaten.getBank();
            if (!isNullOrBlank(bankDaten.getIban())) {
                dnBankverbindung.setIbanOrKtoNummer(bankDaten.getIban());
            }
            if (bankDaten.getLand() != null) {
                dnBankverbindung.setLand(bankDaten.getLand().getLhrKz());
                if (!bankDaten.getLand().getIsInEuEeaCh()) {
                    if (!isNullOrBlank(bankDaten.getBank())) {
                        dnBankverbindung.setBankbezeichnung(bankDaten.getBank());
                    }
                    if (!isNullOrBlank(bankDaten.getBlz())) {
                        dnBankverbindung.setBankleitzahl(bankDaten.getIban().substring(4, 12));
                    }
                }
            }
            dnBankverbindung.setLautendAuf(stammdaten.getVorname() + " " + stammdaten.getNachname());
            if (stammdaten.getPersonalnummer() != null && stammdaten.getPersonalnummer().getFirma() != null && stammdaten.getPersonalnummer().getFirma().getLhrBankNr() != null) {
                DnAuftraggeberBankDto dnAuftraggeberBank = new DnAuftraggeberBankDto();
                dnAuftraggeberBank.setFirmenbankNummer(stammdaten.getPersonalnummer().getFirma().getLhrBankNr());
                dnBankverbindung.setAuftraggeberBank(dnAuftraggeberBank);
            }
        }
        return dnBankverbindung;
    }

    private AdresseDto getAdresseDto(Stammdaten stammdaten) {
        AdresseDto adresse = null;
        if (stammdaten.getAdresse() != null) {
            adresse = new AdresseDto();
            GemeindeDto gemeinde = new GemeindeDto();
            if (stammdaten.getAdresse().getLand() != null) {
                gemeinde.setStaat(stammdaten.getAdresse().getLand().getLhrKz());
            }
            if (stammdaten.getAdresse().getPlz() != null) {
                gemeinde.setPostleitzahl(stammdaten.getAdresse().getPlz().getPlzString());
            }
            if (!isNullOrBlank(stammdaten.getAdresse().getOrt())) {
                gemeinde.setName(stammdaten.getAdresse().getOrt());
            }
            adresse.setGemeinde(gemeinde);
            if (!isNullOrBlank(stammdaten.getAdresse().getStrasse())) {
                adresse.setStrasse(stammdaten.getAdresse().getStrasse());
            }
        }
        return adresse;
    }

    private DienstnehmerRefDto createDienstnehmerRefDto(Vertragsdaten vertragsdaten) {
        DienstnehmerRefDto dienstnehmerRef = new DienstnehmerRefDto();
        dienstnehmerRef.setDnNr(parseStringToInteger(vertragsdaten.getPersonalnummer().getPersonalnummer()));
        if (vertragsdaten.getPersonalnummer() != null && vertragsdaten.getPersonalnummer().getFirma() != null) {
            dienstnehmerRef.setFaNr(LHREnvironmentService.getFaNr(vertragsdaten.getPersonalnummer().getFirma()));
            dienstnehmerRef.setFaKz(LHREnvironmentService.getFaKz(vertragsdaten.getPersonalnummer().getFirma()));
        } else {
            log.error("IbisFirma is null for personalnummer {}", vertragsdaten.getPersonalnummer().getPersonalnummer());
        }

        return dienstnehmerRef;
    }

    /**
     * Creates a new 'DnEintritte' entry for a participant's exit (Abmeldung) based on the given 'AbmeldungDto'.
     * <ul>
     *   <li>Retrieves the 'Teilnehmer' (participant) information using the 'teilnehmerId' provided in 'abmeldungDto'.
     *   <li>Extracts the 'personalnummer' from the 'Teilnehmer' entity.
     *   <li>Fetches the 'Personalnummer' entity using the extracted 'personalnummer'.</li>
     * </ul>
     *
     * @param abmeldungDto
     * @return 'DnEintritte' object containing the abmeldung details, or 'null' if required data is missing
     */
    @Override
    public DnEintritteDto createEintritteForAbmeldung(AbmeldungDto abmeldungDto) {

        Teilnehmer teilnehmer = teilnehmerService.findById(abmeldungDto.getTeilnehmerId()).orElse(null);
        if (teilnehmer == null) {
            log.warn("Teilnehmer not found for ID: {}", abmeldungDto.getTeilnehmerId());
            return null;
        }

        String personalnummer = Optional.ofNullable(teilnehmer.getPersonalnummer())
                .map(Personalnummer::getPersonalnummer)
                .orElse(null);
        if (personalnummer == null) {
            log.warn("Personalnummer not found for Teilnehmer ID: {}", abmeldungDto.getTeilnehmerId());
            return null;
        }

        Personalnummer personalnummerEntity = personalnummerService.findByPersonalnummer(personalnummer);
        if (personalnummerEntity == null) {
            log.warn("Personalnummer entity not found for personalnummer: {}", personalnummer);
            return null;
        }

        DienstnehmerRefDto dienstnehmerRef = new DienstnehmerRefDto();
        if (personalnummerEntity.getFirma() != null) {
            dienstnehmerRef.setFaNr(LHREnvironmentService.getFaNr(personalnummerEntity.getFirma()));
            dienstnehmerRef.setFaKz(LHREnvironmentService.getFaKz(personalnummerEntity.getFirma()));
        } else {
            log.error("IbisFirma is null for personalnummer {}", personalnummerEntity.getPersonalnummer());
            return null;
        }

        dienstnehmerRef.setDnNr(Integer.valueOf(personalnummer));

        DnEintritteDto dn = new DnEintritteDto();
        dn.setDienstnehmer(dienstnehmerRef);

        EintrittDto eintritt = new EintrittDto();
        eintritt.setGrund(abmeldungDto.getAustrittsgrund());
        eintritt.setKommentar(abmeldungDto.getBemerkung());

        String austrittsDatum = Optional.ofNullable(abmeldungDto.getAustrittsDatum())
                .orElse(null);
        eintritt.setZeitangabe(new ZeitangabeDto(austrittsDatum, null));

        List<EintrittDto> eintritts = new ArrayList<>();
        eintritts.add(eintritt);
        dn.setEintritte(eintritts);

        return dn;
    }

    @Override
    public List<Zeitausgleich> getZeitausgleichen(DnZeitdatenDto[] zeitdaten, Personalnummer personalnummer) {
        LinkedList<Zeitausgleich> result = new LinkedList<>();
        if (zeitdaten == null) {
            return result;
        }
        for (DnZeitdatenDto dnZeitdaten : zeitdaten) {
            LocalDate datum = parseDate(dnZeitdaten.getDate());
            if (dnZeitdaten.getBuchungen() == null) {
                continue;
            }
            for (BuchungDto buchung : dnZeitdaten.getBuchungen()) {
                Zeitausgleich currentZeitausgleich;
                if (result.isEmpty() || result.peekLast().getTimeBis() != null) {
                    currentZeitausgleich = new Zeitausgleich();
                    currentZeitausgleich.setPersonalnummer(personalnummer);
                    result.add(currentZeitausgleich);
                } else {
                    currentZeitausgleich = result.peekLast();
                }

                if (BUCHUNGANFRAGE_ZEITAUSGLEICH.equals(String.valueOf(buchung.getZeitspeicherNummer()))) {
                    LocalTime timeVon = parseTime(buchung.getEffectiveTime());
                    currentZeitausgleich.setTimeVon(timeVon);
                    currentZeitausgleich.setDatum(datum);
                }
                if ((currentZeitausgleich.getTimeVon() != null) && (currentZeitausgleich.getTimeBis() == null) && (buchung.getZeitspeicherNummer() == 0)) {
                    LocalTime timeBis = parseTime(buchung.getEffectiveTime());
                    currentZeitausgleich.setTimeBis(timeBis);
                }
            }
        }
        return result;
    }

    @Override
    public List<Urlaubsdaten> mapUrlaubsdatenAndUpdate(UrlaubsdatenStandaloneDto standalone, Personalnummer personalnummer) {
        if (standalone.getUrlaubsdaten() == null) {
            return Collections.emptyList();
        }
        List<Urlaubsdaten> result = new ArrayList<>();
        for (UrlaubswertDto urlaubswert : standalone.getUrlaubsdaten()) {
            if (urlaubswert.getAnspruchsangaben() == null && urlaubswert.getAnspruchsverlauf() == null) {
                continue;
            }

            Urlaubsdaten urlaubsdaten = new Urlaubsdaten();
            urlaubsdaten.setPersonalnummer(personalnummer);

            if (urlaubswert.getAnspruchsangaben() != null) {
                if (urlaubswert.getAnspruchsangaben().getAnspruch() != null) {
                    Anspruch anspuruch = anspuruchService.findByLhrId(urlaubswert.getAnspruchsangaben().getAnspruch().getNr());
                    if (anspuruch != null) {
                        urlaubsdaten.setAnspruchType(anspuruch);
                    }
                }

                urlaubsdaten.setMonth(urlaubswert.getAnspruchsangaben().getMonth());
                urlaubsdaten.setFrom(urlaubswert.getAnspruchsangaben().getFrom());
                urlaubsdaten.setNextAnspruch(urlaubswert.getAnspruchsangaben().getNextAnspruch());
            }

            if (urlaubswert.getAnspruchsverlauf() != null) {
                urlaubsdaten.setKuerzung(urlaubswert.getAnspruchsverlauf().getKuerzung());
                urlaubsdaten.setVerjaehrung(urlaubswert.getAnspruchsverlauf().getVerjaehrung());
                urlaubsdaten.setAnspruch(urlaubswert.getAnspruchsverlauf().getAnspruch());
                urlaubsdaten.setKonsum(urlaubswert.getAnspruchsverlauf().getKonsum());
                urlaubsdaten.setRest(urlaubswert.getAnspruchsverlauf().getRest());
            }

            urlaubsdaten.setCreatedOn(getLocalDateNow());
            urlaubsdaten.setCreatedBy(LHR_SERVICE);

            if (urlaubsdatenService.isExists(
                    personalnummer.getId(),
                    (urlaubsdaten.getAnspruchType() != null) ? urlaubsdaten.getAnspruchType().getLhrId() : null,
                    urlaubsdaten.getMonth(),
                    urlaubsdaten.getFrom(),
                    urlaubsdaten.getNextAnspruch())
            ) {
                Urlaubsdaten existedUrlaubsdaten = urlaubsdatenService.findUrlaubsdaten(personalnummer.getId(),
                        (urlaubsdaten.getAnspruchType() != null) ? urlaubsdaten.getAnspruchType().getLhrId() : null,
                        urlaubsdaten.getMonth(),
                        urlaubsdaten.getFrom(),
                        urlaubsdaten.getNextAnspruch());
                urlaubsdatenService.deleteById(existedUrlaubsdaten.getId());
            }
            result.add(urlaubsdatenService.save(urlaubsdaten));
        }

        return result;
    }
}