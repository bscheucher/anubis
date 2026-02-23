package com.ibosng.dbservice.services.impl.mitarbeiter;


import com.ibosng.dbservice.dtos.mitarbeiter.MitarbeiterDto;
import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenBasicDto;
import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.ZulageDto;
import com.ibosng.dbservice.entities.IbosReference;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.*;
import com.ibosng.dbservice.entities.mitarbeiter.datastatus.VertragsdatenDataStatus;
import com.ibosng.dbservice.repositories.mitarbeiter.VertragsdatenRepository;
import com.ibosng.dbservice.services.IbosReferenceService;
import com.ibosng.dbservice.services.mitarbeiter.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Service
@RequiredArgsConstructor
@Slf4j
public class VertragsdatenServiceImpl implements VertragsdatenService {

    private final VertragsdatenRepository vertragsdatenRepository;
    private final ArbeitszeitenService arbeitszeitenService;
    private final ArbeitszeitenInfoService arbeitszeitenInfoService;
    private final GehaltInfoService gehaltInfoService;
    private final GehaltInfoZulageService gehaltInfoZulageService;
    private final IbosReferenceService ibosReferenceService;

    private static final String FIX_ZULAGE = "Fixzulage";
    private static final String FUNKTIONS_ZULAGE = "Funktionszulage";
    private static final String LEITUNGS_ZULAGE = "Leitungszulage";


    @Override
    public List<Vertragsdaten> findAll() {
        return vertragsdatenRepository.findAll();
    }

    @Override
    public Optional<Vertragsdaten> findById(Integer id) {
        return vertragsdatenRepository.findById(id);
    }

    @Override
    public Vertragsdaten save(Vertragsdaten object) {
        return vertragsdatenRepository.save(object);
    }

    @Override
    public List<Vertragsdaten> saveAll(List<Vertragsdaten> objects) {
        return vertragsdatenRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        vertragsdatenRepository.deleteById(id);
    }

    @Override
    public List<Vertragsdaten> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public List<Vertragsdaten> findByPersonalnummerString(String personalnummer) {
        return vertragsdatenRepository.findByPersonalnummer_Personalnummer(personalnummer)
                .stream()
                .sorted((a1, a2) -> {
                    if (a1.getChangedOn() != null && a2.getChangedOn() != null) {
                        return a2.getChangedOn().compareTo(a1.getChangedOn());
                    } else if (a1.getChangedOn() == null && a2.getChangedOn() != null) {
                        return a2.getChangedOn().compareTo(a1.getCreatedOn());
                    } else if (a1.getChangedOn() != null) {
                        return a2.getCreatedOn().compareTo(a1.getChangedOn());
                    } else {
                        return a2.getCreatedOn().compareTo(a1.getCreatedOn());
                    }
                })
                .toList();
    }

    @Override
    public List<Vertragsdaten> findByPersonalnummerStringAndStatus(String personalnummer, List<MitarbeiterStatus> status) {
        return vertragsdatenRepository.findByPersonalnummer_PersonalnummerAndStatusIn(personalnummer, status);
    }

    @Override
    public List<Vertragsdaten> findByPersonalnummerIdAndStatus(Integer personalnummerId, List<MitarbeiterStatus> status) {
        return vertragsdatenRepository.findByPersonalnummer_IdAndStatusIn(personalnummerId, status);
    }


    @Override
    public Vertragsdaten findByPersonalnummerStringLatest(String personalnummer) {
        return vertragsdatenRepository.findFirstByPersonalnummer_PersonalnummerOrderByCreatedOnDescChangedOnDesc(personalnummer);
    }

    @Override
    public VertragsdatenDto mapVertragsdatenToDto(Vertragsdaten vertragsdaten) {
        VertragsdatenDto vertragsdatenDto = new VertragsdatenDto();
        if (vertragsdaten.getId() != null) {
            vertragsdatenDto.setId(vertragsdaten.getId());
        }
        if (vertragsdaten.getPersonalnummer() != null && vertragsdaten.getPersonalnummer().getPersonalnummer() != null) {
            vertragsdatenDto.setPersonalnummer(vertragsdaten.getPersonalnummer().getPersonalnummer());
        }

        if (vertragsdaten.getEintritt() != null) {
            vertragsdatenDto.setEintritt(vertragsdaten.getEintritt().toString());
        }
        if (vertragsdaten.getAbrechnungsgruppe() != null) {
            vertragsdatenDto.setAbrechnungsgruppe(vertragsdaten.getAbrechnungsgruppe().getBezeichnung());
        }
        if (vertragsdaten.getDienstnehmergruppe() != null) {
            vertragsdatenDto.setDienstnehmergruppe(vertragsdaten.getDienstnehmergruppe().getBezeichnung());
        }
        if (vertragsdaten.getIsBefristet() != null) {
            vertragsdatenDto.setIsBefristet(vertragsdaten.getIsBefristet());
        }
        if (vertragsdaten.getBefristungBis() != null) {
            vertragsdatenDto.setBefristungBis(vertragsdaten.getBefristungBis().toString());
        }
        ArbeitszeitenInfo arbeitszeitenInfo = arbeitszeitenInfoService.findByVertragsdatenId(vertragsdaten.getId());

        if (arbeitszeitenInfo != null) {
            setArbeitszeitenInfo(arbeitszeitenInfo, vertragsdatenDto);
        }

        GehaltInfo gehaltInfo = gehaltInfoService.findByVertragsdatenId(vertragsdaten.getId());

        if (gehaltInfo != null) {
            setGehaltInfo(gehaltInfo, vertragsdatenDto);
            if (gehaltInfo.getLehrjahr() != null) {
                vertragsdatenDto.setLehrjahr(gehaltInfo.getLehrjahr());
            }
            List<ZulageDto> zulageDtos = mapZulagenToDtos(gehaltInfoZulageService.findAllByGehaltInfoId(gehaltInfo.getId()));
            // manually check list elements and map to dto accordingly
            for (ZulageDto zulageDto : zulageDtos) {
                if (zulageDto.getArtDerZulage().equals(FUNKTIONS_ZULAGE)) {
                    vertragsdatenDto.setFunktionsZulage(true);
                    vertragsdatenDto.setZulageInEuroFunktion(zulageDto.getZulageInEuro());
                }
                if (zulageDto.getArtDerZulage().equals(FIX_ZULAGE)) {
                    vertragsdatenDto.setFixZulage(true);
                    vertragsdatenDto.setZulageInEuroFix(zulageDto.getZulageInEuro());

                }
                if (zulageDto.getArtDerZulage().equals(LEITUNGS_ZULAGE)) {
                    vertragsdatenDto.setLeitungsZulage(true);
                    vertragsdatenDto.setZulageInEuroLeitung(zulageDto.getZulageInEuro());
                }
            }
        }


        if (vertragsdaten.getDienstort() != null) {
            vertragsdatenDto.setDienstort(vertragsdaten.getDienstort().getName());
        }
        if (vertragsdaten.getKostenstelle() != null) {
            vertragsdatenDto.setKostenstelle(vertragsdaten.getKostenstelle().getBezeichnung());
        }
        if (vertragsdaten.getFuehrungskraft() != null) {
            vertragsdatenDto.setFuehrungskraft(String.valueOf(vertragsdaten.getFuehrungskraft().getId()));
        }
        if (vertragsdaten.getStartcoach() != null) {
            vertragsdatenDto.setStartcoach(String.valueOf(vertragsdaten.getStartcoach().getId()));
        }
        if (vertragsdaten.getKategorie() != null) {
            vertragsdatenDto.setKategorie(vertragsdaten.getKategorie().getName());
        }
        if (vertragsdaten.getTaetigkeit() != null) {
            vertragsdatenDto.setTaetigkeit(vertragsdaten.getTaetigkeit().getName());
        }
        if (vertragsdaten.getJobBezeichnung() != null) {
            vertragsdatenDto.setJobBezeichnung(vertragsdaten.getJobBezeichnung().getName());
        }
        if (vertragsdaten.getNotizAllgemein() != null) {
            vertragsdatenDto.setNotizAllgemein(vertragsdaten.getNotizAllgemein());
        }

        if (vertragsdaten.getMobileWorking() != null) {
            vertragsdatenDto.setMobileWorking(vertragsdaten.getMobileWorking());
        }
        if (vertragsdaten.getWeitereAdressezuHauptwohnsitz() != null) {
            vertragsdatenDto.setWeitereAdressezuHauptwohnsitz(vertragsdaten.getWeitereAdressezuHauptwohnsitz());
        }
        if (vertragsdaten.getNotizZusatzvereinbarung() != null) {
            vertragsdatenDto.setNotizZusatzvereinbarung(vertragsdaten.getNotizZusatzvereinbarung());
        }
        if (vertragsdaten.getKlasse() != null) {
            vertragsdatenDto.setKlasse(vertragsdaten.getKlasse().getName());
        }
        if (vertragsdaten.getAdresse() != null) {
            if (vertragsdaten.getAdresse().getLand() != null) {
                vertragsdatenDto.setLand(vertragsdaten.getAdresse().getLand().getLandName());
            }
            if (vertragsdaten.getAdresse().getStrasse() != null) {
                vertragsdatenDto.setStrasse(vertragsdaten.getAdresse().getStrasse());
            }
            if (vertragsdaten.getAdresse().getPlz() != null) {
                vertragsdatenDto.setPlz(vertragsdaten.getAdresse().getPlz().getPlzString());
            }
            if (vertragsdaten.getAdresse().getOrt() != null) {
                vertragsdatenDto.setOrt(vertragsdaten.getAdresse().getOrt());
            }
        }
        for (VertragsdatenDataStatus dataStatus : vertragsdaten.getErrors()) {
            vertragsdatenDto.getErrorsMap().put(dataStatus.getError(), dataStatus.getCause());
        }
        vertragsdatenDto.setErrors(new ArrayList<>(vertragsdatenDto.getErrorsMap().keySet()));
        return vertragsdatenDto;
    }

    private void setArbeitszeitenInfo(ArbeitszeitenInfo arbeitszeitenInfo, VertragsdatenBasicDto vertragsdatenDto) {
        if (arbeitszeitenInfo.getStundenaenderung() != null) {
            vertragsdatenDto.setStundenaenderung(arbeitszeitenInfo.getStundenaenderung());
        }
        if (arbeitszeitenInfo.getVerwendungsbereichsaenderung() != null) {
            vertragsdatenDto.setVerwendungsbereichsaenderung(arbeitszeitenInfo.getVerwendungsbereichsaenderung());
        }
        if (arbeitszeitenInfo.getStufenwechsel() != null) {
            vertragsdatenDto.setStufenwechsel(arbeitszeitenInfo.getStufenwechsel());
        }
        if (arbeitszeitenInfo.getGeschaeftsbereichsaenderung() != null) {
            vertragsdatenDto.setGeschaeftsbereichsaenderung(arbeitszeitenInfo.getGeschaeftsbereichsaenderung());
        }
        if (arbeitszeitenInfo.getKvErhoehung() != null) {
            vertragsdatenDto.setKvErhoehung(arbeitszeitenInfo.getKvErhoehung());
        }
        if (arbeitszeitenInfo.getBeschaeftigungsausmass() != null) {
            vertragsdatenDto.setBeschaeftigungsausmass(arbeitszeitenInfo.getBeschaeftigungsausmass().getName());
        }
        if (arbeitszeitenInfo.getBeschaeftigungsstatus() != null) {
            vertragsdatenDto.setBeschaeftigungsstatus(arbeitszeitenInfo.getBeschaeftigungsstatus().getName());
        }
        if (arbeitszeitenInfo.getWochenstunden() != null) {
            vertragsdatenDto.setWochenstunden(arbeitszeitenInfo.getWochenstunden());
        }
        if (arbeitszeitenInfo.getArbeitszeitmodell() != null) {
            vertragsdatenDto.setArbeitszeitmodell(arbeitszeitenInfo.getArbeitszeitmodell().getName());
        }
        if (arbeitszeitenInfo.getArbeitszeitmodellVon() != null) {
            vertragsdatenDto.setArbeitszeitmodellVon(arbeitszeitenInfo.getArbeitszeitmodellVon().toString());
        }
        if (arbeitszeitenInfo.getArbeitszeitmodellBis() != null) {
            vertragsdatenDto.setArbeitszeitmodellBis(arbeitszeitenInfo.getArbeitszeitmodellBis().toString());
        }
        if (arbeitszeitenInfo.getAuswahlBegruendungFuerDurchrechner() != null) {
            vertragsdatenDto.setAuswahlBegruendungFuerDurchrechner(arbeitszeitenInfo.getAuswahlBegruendungFuerDurchrechner());
        }
        if (arbeitszeitenInfo.getSpezielleMittagspausenregelung() != null) {
            vertragsdatenDto.setSpezielleMittagspausenregelung(arbeitszeitenInfo.getSpezielleMittagspausenregelung());
        }
        if (arbeitszeitenInfo.getUrlaubVorabVereinbart() != null) {
            vertragsdatenDto.setUrlaubVorabVereinbart(arbeitszeitenInfo.getUrlaubVorabVereinbart());
        }
        if (arbeitszeitenInfo.getNotizArbeitszeit() != null) {
            vertragsdatenDto.setNotizArbeitszeit(arbeitszeitenInfo.getNotizArbeitszeit());
        }

        List<Arbeitszeiten> arbeitszeitens = arbeitszeitenService.findByArbAndArbeitszeitenInfoId(arbeitszeitenInfo.getId());
        for (Arbeitszeiten arbeitszeiten : arbeitszeitens) {
            if (arbeitszeiten.getKernzeit() != null && arbeitszeiten.getKernzeit()) {
                setArbeitszeitenDurchrechnung(arbeitszeiten, vertragsdatenDto);
            } else {
                setArbeitszeitenGleitzeit(arbeitszeiten, vertragsdatenDto);
            }
        }
    }

    private static void setGehaltInfo(GehaltInfo gehaltInfo, VertragsdatenBasicDto vertragsdatenDto) {
        if (gehaltInfo.getVerwendungsgruppe() != null) {
            vertragsdatenDto.setVerwendungsgruppe(gehaltInfo.getVerwendungsgruppe().getName());
            if (gehaltInfo.getVerwendungsgruppe().getKollektivvertrag() != null) {
                vertragsdatenDto.setKollektivvertrag(gehaltInfo.getVerwendungsgruppe().getKollektivvertrag().getName());
            }
        }
        if (gehaltInfo.getStufe() != null) {
            vertragsdatenDto.setStufe(gehaltInfo.getStufe().getName());
        }
        if (gehaltInfo.getFacheinschlaegigeTaetigkeitenGeprueft() != null) {
            vertragsdatenDto.setFacheinschlaegigeTaetigkeitenGeprueft(gehaltInfo.getFacheinschlaegigeTaetigkeitenGeprueft());
        }
        if (gehaltInfo.getAngerechneteIbisMonate() != null) {
            vertragsdatenDto.setAngerechneteIbisMonate(gehaltInfo.getAngerechneteIbisMonate());
        }
        if (gehaltInfo.getAngerechneteFacheinschlaegigeTaetigkeitenMonate() != null) {
            vertragsdatenDto.setAngerechneteFacheinschlaegigeTaetigkeitenMonate(gehaltInfo.getAngerechneteFacheinschlaegigeTaetigkeitenMonate());
        }
        if (gehaltInfo.getKvGehaltBerechnet() != null) {
            vertragsdatenDto.setKvGehaltBerechnet(gehaltInfo.getKvGehaltBerechnet().doubleValue());
        }
        if (gehaltInfo.getGehaltVereinbart() != null) {
            vertragsdatenDto.setGehaltVereinbart(gehaltInfo.getGehaltVereinbart().doubleValue());
        }
        if (gehaltInfo.getUeberzahlung() != null) {
            vertragsdatenDto.setUeberzahlung(gehaltInfo.getUeberzahlung().doubleValue());
        }
        if (gehaltInfo.getGesamtBrutto() != null) {
            vertragsdatenDto.setGesamtBrutto(gehaltInfo.getGesamtBrutto().doubleValue());
        }
        if (gehaltInfo.getVereinbarungUEberstunden() != null) {
            vertragsdatenDto.setVereinbarungUEberstunden(gehaltInfo.getVereinbarungUEberstunden());
        }
        if (gehaltInfo.getUestPauschale() != null) {
            vertragsdatenDto.setUestPauschale(gehaltInfo.getUestPauschale().doubleValue());
        }
        if (gehaltInfo.getDeckungspruefung() != null) {
            vertragsdatenDto.setDeckungspruefung(gehaltInfo.getDeckungspruefung());
        }
        if (gehaltInfo.getJobticket() != null) {
            vertragsdatenDto.setJobticket(true);
            if (!isNullOrBlank(gehaltInfo.getJobticket().getName())) {
                vertragsdatenDto.setJobticketTitle(gehaltInfo.getJobticket().getName());
            }
        }
        if (gehaltInfo.getNotizGehalt() != null) {
            vertragsdatenDto.setNotizGehalt(gehaltInfo.getNotizGehalt());
        }

        if (gehaltInfo.getNaechsteVorrueckung() != null) {
            vertragsdatenDto.setNaechsteVorrueckung(gehaltInfo.getNaechsteVorrueckung());
        }
    }

    private List<ZulageDto> mapZulagenToDtos(List<GehaltInfoZulage> gehaltInfoZulagen) {
        List<ZulageDto> zulageDtos = new ArrayList<>();
        for (GehaltInfoZulage gehaltInfoZulage : gehaltInfoZulagen) {
            ZulageDto toBeAdded = new ZulageDto();
            toBeAdded.setId(gehaltInfoZulage.getId());
            toBeAdded.setArtDerZulage(gehaltInfoZulage.getArtDerZulage());
            toBeAdded.setZulageInEuro(String.valueOf(gehaltInfoZulage.getZulageInEuro()));
            toBeAdded.setGehaltInfoId(String.valueOf(gehaltInfoZulage.getGehaltInfo().getId()));
            zulageDtos.add(toBeAdded);
        }
        return zulageDtos;
    }

    private static void setArbeitszeitenGleitzeit(Arbeitszeiten arbeitszeiten, VertragsdatenBasicDto vertragsdatenDto) {
        if (arbeitszeiten.getMontagVon() != null) {
            vertragsdatenDto.setAMontagVon(arbeitszeiten.getMontagVon().toString());
        }
        if (arbeitszeiten.getMontagBis() != null) {
            vertragsdatenDto.setAMontagBis(arbeitszeiten.getMontagBis().toString());
        }
        if (arbeitszeiten.getMontagNetto() != null) {
            vertragsdatenDto.setAMontagNetto(arbeitszeiten.getMontagNetto().toString());
        }
        if (arbeitszeiten.getDienstagVon() != null) {
            vertragsdatenDto.setADienstagVon(arbeitszeiten.getDienstagVon().toString());
        }
        if (arbeitszeiten.getDienstagBis() != null) {
            vertragsdatenDto.setADienstagBis(arbeitszeiten.getDienstagBis().toString());
        }
        if (arbeitszeiten.getDienstagNetto() != null) {
            vertragsdatenDto.setADienstagNetto(arbeitszeiten.getDienstagNetto().toString());
        }
        if (arbeitszeiten.getMittwochVon() != null) {
            vertragsdatenDto.setAMittwochVon(arbeitszeiten.getMittwochVon().toString());
        }
        if (arbeitszeiten.getMittwochBis() != null) {
            vertragsdatenDto.setAMittwochBis(arbeitszeiten.getMittwochBis().toString());
        }
        if (arbeitszeiten.getMittwochNetto() != null) {
            vertragsdatenDto.setAMittwochNetto(arbeitszeiten.getMittwochNetto().toString());
        }
        if (arbeitszeiten.getDonnerstagVon() != null) {
            vertragsdatenDto.setADonnerstagVon(arbeitszeiten.getDonnerstagVon().toString());
        }
        if (arbeitszeiten.getDonnerstagBis() != null) {
            vertragsdatenDto.setADonnerstagBis(arbeitszeiten.getDonnerstagBis().toString());
        }
        if (arbeitszeiten.getDonnerstagNetto() != null) {
            vertragsdatenDto.setADonnerstagNetto(arbeitszeiten.getDonnerstagNetto().toString());
        }
        if (arbeitszeiten.getFreitagVon() != null) {
            vertragsdatenDto.setAFreitagVon(arbeitszeiten.getFreitagVon().toString());
        }
        if (arbeitszeiten.getFreitagBis() != null) {
            vertragsdatenDto.setAFreitagBis(arbeitszeiten.getFreitagBis().toString());
        }
        if (arbeitszeiten.getFreitagNetto() != null) {
            vertragsdatenDto.setAFreitagNetto(arbeitszeiten.getFreitagNetto().toString());
        }
        if (arbeitszeiten.getSamstagVon() != null) {
            vertragsdatenDto.setASamstagVon(arbeitszeiten.getSamstagVon().toString());
        }
        if (arbeitszeiten.getSamstagBis() != null) {
            vertragsdatenDto.setASamstagBis(arbeitszeiten.getSamstagBis().toString());
        }
        if (arbeitszeiten.getSamstagNetto() != null) {
            vertragsdatenDto.setASamstagNetto(arbeitszeiten.getSamstagNetto().toString());
        }
        if (arbeitszeiten.getSonntagVon() != null) {
            vertragsdatenDto.setASonntagVon(arbeitszeiten.getSonntagVon().toString());
        }
        if (arbeitszeiten.getSonntagBis() != null) {
            vertragsdatenDto.setASonntagBis(arbeitszeiten.getSonntagBis().toString());
        }
        if (arbeitszeiten.getSonntagNetto() != null) {
            vertragsdatenDto.setASonntagNetto(arbeitszeiten.getSonntagNetto().toString());
        }
    }

    private static void setArbeitszeitenDurchrechnung(Arbeitszeiten arbeitszeiten, VertragsdatenBasicDto vertragsdatenDto) {
        if (arbeitszeiten.getKernzeit() != null && vertragsdatenDto instanceof VertragsdatenDto vDto) {
            vDto.setKernzeit(arbeitszeiten.getKernzeit());
        }
        if (arbeitszeiten.getMontagVon() != null) {
            vertragsdatenDto.setKMontagVon(arbeitszeiten.getMontagVon().toString());
        }
        if (arbeitszeiten.getMontagBis() != null) {
            vertragsdatenDto.setKMontagBis(arbeitszeiten.getMontagBis().toString());
        }
        if (arbeitszeiten.getDienstagVon() != null) {
            vertragsdatenDto.setKDienstagVon(arbeitszeiten.getDienstagVon().toString());
        }
        if (arbeitszeiten.getDienstagBis() != null) {
            vertragsdatenDto.setKDienstagBis(arbeitszeiten.getDienstagBis().toString());
        }
        if (arbeitszeiten.getMittwochVon() != null) {
            vertragsdatenDto.setKMittwochVon(arbeitszeiten.getMittwochVon().toString());
        }
        if (arbeitszeiten.getMittwochBis() != null) {
            vertragsdatenDto.setKMittwochBis(arbeitszeiten.getMittwochBis().toString());
        }
        if (arbeitszeiten.getDonnerstagVon() != null) {
            vertragsdatenDto.setKDonnerstagVon(arbeitszeiten.getDonnerstagVon().toString());
        }
        if (arbeitszeiten.getDonnerstagBis() != null) {
            vertragsdatenDto.setKDonnerstagBis(arbeitszeiten.getDonnerstagBis().toString());
        }
        if (arbeitszeiten.getFreitagVon() != null) {
            vertragsdatenDto.setKFreitagVon(arbeitszeiten.getFreitagVon().toString());
        }
        if (arbeitszeiten.getFreitagBis() != null) {
            vertragsdatenDto.setKFreitagBis(arbeitszeiten.getFreitagBis().toString());
        }
        if (arbeitszeiten.getSamstagVon() != null) {
            vertragsdatenDto.setKSamstagVon(arbeitszeiten.getSamstagVon().toString());
        }
        if (arbeitszeiten.getSamstagBis() != null) {
            vertragsdatenDto.setKSamstagBis(arbeitszeiten.getSamstagBis().toString());
        }
        if (arbeitszeiten.getSonntagVon() != null) {
            vertragsdatenDto.setKSonntagVon(arbeitszeiten.getSonntagVon().toString());
        }
        if (arbeitszeiten.getSonntagBis() != null) {
            vertragsdatenDto.setKSonntagBis(arbeitszeiten.getSonntagBis().toString());
        }
    }

    @Override
    public MitarbeiterDto mapStammdatenToMADto(Stammdaten stammdaten, Vertragsdaten vertragsdaten) {
        //Stammssdaten
        MitarbeiterDto mitarbeiterDto = new MitarbeiterDto();
        if (stammdaten.getPersonalnummer() != null) {
            if (stammdaten.getPersonalnummer().getPersonalnummer() != null) {
                mitarbeiterDto.setPersonalnummer(stammdaten.getPersonalnummer().getPersonalnummer());
            }
            if (stammdaten.getPersonalnummer().getFirma() != null && stammdaten.getPersonalnummer().getFirma().getName() != null) {
                mitarbeiterDto.setFirma(stammdaten.getPersonalnummer().getFirma().getName());
            }
        }
        if (stammdaten.getAnrede() != null) {
            mitarbeiterDto.setAnrede(stammdaten.getAnrede().getName());
        }
        if (stammdaten.getTitel() != null) {
            mitarbeiterDto.setTitel(stammdaten.getTitel().getName());
        }
        if (stammdaten.getTitel2() != null) {
            mitarbeiterDto.setTitel2(stammdaten.getTitel2().getName());
        }
        if (stammdaten.getNachname() != null) {
            mitarbeiterDto.setNachname(stammdaten.getNachname());
        }
        if (stammdaten.getVorname() != null) {
            mitarbeiterDto.setVorname(stammdaten.getVorname());
        }
        if (stammdaten.getGeburtsname() != null) {
            mitarbeiterDto.setGeburtsname(stammdaten.getGeburtsname());
        }
        if (stammdaten.getSvnr() != null) {
            mitarbeiterDto.setSvnr(String.valueOf(stammdaten.getSvnr()));
        }
        if (stammdaten.getEcardStatus() != null) {
            mitarbeiterDto.setEcard(stammdaten.getEcardStatus().getValue());
        }
        if (stammdaten.getGeschlecht() != null) {
            mitarbeiterDto.setGeschlecht(stammdaten.getGeschlecht().getName());
        }
        if (stammdaten.getFamilienstand() != null) {
            mitarbeiterDto.setFamilienstand(stammdaten.getFamilienstand().getName());
        }
        if (stammdaten.getGeburtsdatum() != null) {
            mitarbeiterDto.setGeburtsDatum(stammdaten.getGeburtsdatum().toString());
        }
        if (stammdaten.getLebensalter() != null) {
            mitarbeiterDto.setAlter(String.valueOf(stammdaten.getLebensalter()));
        }
        if (stammdaten.getStaatsbuergerschaft() != null) {
            mitarbeiterDto.setStaatsbuergerschaft(stammdaten.getStaatsbuergerschaft().getLandName());
        }
        if (stammdaten.getMuttersprache() != null) {
            mitarbeiterDto.setMuttersprache(stammdaten.getMuttersprache().getName());
        }
        if (stammdaten.getAdresse() != null) {
            if (stammdaten.getAdresse().getLand() != null) {
                mitarbeiterDto.setLand(stammdaten.getAdresse().getLand().getLandName());
            }
            if (stammdaten.getAdresse().getStrasse() != null) {
                mitarbeiterDto.setStrasse(stammdaten.getAdresse().getStrasse());
            }
            if (stammdaten.getAdresse().getPlz() != null) {
                mitarbeiterDto.setPlz(stammdaten.getAdresse().getPlz().getPlzString());
            }
            if (stammdaten.getAdresse().getOrt() != null) {
                mitarbeiterDto.setOrt(stammdaten.getAdresse().getOrt());
            }
        }
        if (stammdaten.getAbweichendeAdresse() != null) {
            if (stammdaten.getAbweichendeAdresse().getLand() != null) {
                mitarbeiterDto.setALand(stammdaten.getAbweichendeAdresse().getLand().getLandName());
            }
            if (stammdaten.getAbweichendeAdresse().getStrasse() != null) {
                mitarbeiterDto.setAStrasse(stammdaten.getAbweichendeAdresse().getStrasse());
            }
            if (stammdaten.getAbweichendeAdresse().getPlz() != null) {
                mitarbeiterDto.setAPlz(stammdaten.getAbweichendeAdresse().getPlz().getPlzString());
            }
            if (stammdaten.getAbweichendeAdresse().getOrt() != null) {
                mitarbeiterDto.setAOrt(stammdaten.getAbweichendeAdresse().getOrt());
            }
        }

        if (stammdaten.getEmail() != null) {
            mitarbeiterDto.setEmail(stammdaten.getEmail());
        }
        if (stammdaten.getMobilnummer() != null) {
            mitarbeiterDto.setMobilnummer(stammdaten.getMobilnummer().getLand().getTelefonvorwahl() + stammdaten.getMobilnummer().getTelefonnummer());
        }
        if (stammdaten.getBank() != null) {
            if (stammdaten.getBank().getBank() != null) {
                mitarbeiterDto.setBank(stammdaten.getBank().getBank());
            }
            if (stammdaten.getBank().getIban() != null) {
                mitarbeiterDto.setIban(stammdaten.getBank().getIban());
            }
            if (stammdaten.getBank().getBic() != null) {
                mitarbeiterDto.setBic(stammdaten.getBank().getBic());
            }
            if (stammdaten.getBank().getCard() != null) {
                mitarbeiterDto.setBankcard(stammdaten.getBank().getCard().getValue());
            }
        }
        if (stammdaten.getZusatzInfo() != null) {
            if (stammdaten.getZusatzInfo().getGueltigBis() != null) {
                mitarbeiterDto.setGueltigBis(stammdaten.getZusatzInfo().getGueltigBis().toString());
            }
            if (stammdaten.getZusatzInfo().getArbeitsgenehmigung() != null) {
                mitarbeiterDto.setArbeitsgenehmigung(stammdaten.getZusatzInfo().getArbeitsgenehmigung());
            }
        }
        //Vertragsdaten
        if (vertragsdaten.getKostenstelle() != null && vertragsdaten.getKostenstelle().getBezeichnung() != null) {
            mitarbeiterDto.setKostenstelle(vertragsdaten.getKostenstelle().getBezeichnung());
        }
        if (vertragsdaten.getEintritt() != null) {
            mitarbeiterDto.setEintritt(vertragsdaten.getEintritt().toString());
        }
        if (vertragsdaten.getIsBefristet() != null) {
            mitarbeiterDto.setIsBefristet(vertragsdaten.getIsBefristet());
        }
        if (vertragsdaten.getBefristungBis() != null) {
            mitarbeiterDto.setBefristungBis(vertragsdaten.getBefristungBis().toString());
        }
        ArbeitszeitenInfo arbeitszeitenInfo = arbeitszeitenInfoService.findByVertragsdatenId(vertragsdaten.getId());
        if (arbeitszeitenInfo != null) {
            setArbeitszeitenInfo(arbeitszeitenInfo, mitarbeiterDto);
        }
        GehaltInfo gehaltInfo = gehaltInfoService.findByVertragsdatenId(vertragsdaten.getId());
        if (gehaltInfo != null && MitarbeiterStatus.ACTIVE.equals(gehaltInfo.getStatus())) {
            setGehaltInfo(gehaltInfo, mitarbeiterDto);
        }
        if (vertragsdaten.getDienstort() != null) {
            mitarbeiterDto.setDienstort(vertragsdaten.getDienstort().getName());
        }
        if (vertragsdaten.getKostenstelle() != null) {
            mitarbeiterDto.setKostenstelle(vertragsdaten.getKostenstelle().getBezeichnung());
        }
        if (vertragsdaten.getFuehrungskraft() != null) {
            mitarbeiterDto.setFuehrungskraft(vertragsdaten.getFuehrungskraft().getFirstName() + " " + vertragsdaten.getFuehrungskraft().getLastName());
        }
        if (vertragsdaten.getStartcoach() != null) {
            mitarbeiterDto.setStartcoach(vertragsdaten.getStartcoach().getFirstName() + " " + vertragsdaten.getStartcoach().getLastName());
        }
        if (vertragsdaten.getKategorie() != null) {
            mitarbeiterDto.setKategorie(vertragsdaten.getKategorie().getName());
        }
        if (vertragsdaten.getTaetigkeit() != null) {
            mitarbeiterDto.setTaetigkeit(vertragsdaten.getTaetigkeit().getName());
        }
        if (vertragsdaten.getJobBezeichnung() != null) {
            mitarbeiterDto.setJobBezeichnung(vertragsdaten.getJobBezeichnung().getName());
        }
        if (vertragsdaten.getNotizAllgemein() != null) {
            mitarbeiterDto.setNotizAllgemein(vertragsdaten.getNotizAllgemein());
        }

        if (vertragsdaten.getMobileWorking() != null) {
            mitarbeiterDto.setMobileWorking(vertragsdaten.getMobileWorking());
        }
        if (vertragsdaten.getWeitereAdressezuHauptwohnsitz() != null) {
            mitarbeiterDto.setWeitereAdressezuHauptwohnsitz(vertragsdaten.getWeitereAdressezuHauptwohnsitz());
        }
        if (vertragsdaten.getNotizZusatzvereinbarung() != null) {
            mitarbeiterDto.setNotizZusatzvereinbarung(vertragsdaten.getNotizZusatzvereinbarung());
        }
        if (vertragsdaten.getAdresse() != null) {
            if (vertragsdaten.getAdresse().getLand() != null) {
                mitarbeiterDto.setLand(vertragsdaten.getAdresse().getLand().getLandName());
            }
            if (vertragsdaten.getAdresse().getStrasse() != null) {
                mitarbeiterDto.setStrasse(vertragsdaten.getAdresse().getStrasse());
            }
            if (vertragsdaten.getAdresse().getPlz() != null) {
                mitarbeiterDto.setPlz(vertragsdaten.getAdresse().getPlz().getPlzString());
            }
            if (vertragsdaten.getAdresse().getOrt() != null) {
                mitarbeiterDto.setOrt(vertragsdaten.getAdresse().getOrt());
            }
        }
        return mitarbeiterDto;
    }

    @Override
    public LocalDate findEintrittByPersonalnummer(String personalnummer) {
        return vertragsdatenRepository.findEintrittByPersonalnummer(personalnummer);
    }

    @Override
    public List<Vertragsdaten> findAllByCreatedOnOrChangedOnAfter(LocalDateTime after) {
        return vertragsdatenRepository.findAllByCreatedOnAfterOrChangedOnAfter(after, after);
    }

    @Override
    public List<Vertragsdaten> findAllByFuehrungskraftIsNullAndFuehrungskraftRefIsNotNull() {
        return vertragsdatenRepository.findAllByFuehrungskraftIsNullAndFuehrungskraftRefIsNotNull();
    }

    @Override
    public List<Vertragsdaten> findAllByStartcoachIsNullAndStartcoachRefIsNotNull() {
        return vertragsdatenRepository.findAllByStartcoachIsNullAndStartcoachRefIsNotNull();
    }

    @Override
    public List<Vertragsdaten> findAllByPNAndStatusesNotInVertragsdatenaenderungen(String personalnummer, List<MitarbeiterStatus> statuses) {
        return vertragsdatenRepository.findAllByPNAndStatusesNotInVertragsdatenaenderungen(personalnummer, statuses);
    }

    @Override
    public List<Vertragsdaten> findAllByPNAndStatusesNotInVertragsdatenaenderungen(Personalnummer personalnummer, List<MitarbeiterStatus> statuses) {
        return vertragsdatenRepository.findAllByPNAndStatusesNotInVertragsdatenaenderungen(personalnummer, statuses);
    }

    @Override
    public List<Vertragsdaten> findAllByPersonalnummer(Personalnummer personalnummer) {
        return vertragsdatenRepository.findAllByPersonalnummer(personalnummer);
    }

    private IbosReference getIbosReference(String dataKey) {
        IbosReference ibosReference = ibosReferenceService.findAllByData(dataKey)
                .stream().findFirst().orElse(null);

        if (ibosReference != null) {
            IbosReference result = new IbosReference();
            result.setId(ibosReference.getId());
            result.setIbosngId(ibosReference.getIbosngId());
            result.setIbosId(ibosReference.getIbosId());
            result.setData(ibosReference.getData());
            result.setStatus(ibosReference.getStatus());
            return result;
        }

        return null;
    }

}
