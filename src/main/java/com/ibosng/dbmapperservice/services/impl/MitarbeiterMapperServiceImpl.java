package com.ibosng.dbmapperservice.services.impl;

import com.ibosng.dbibosservice.entities.*;
import com.ibosng.dbibosservice.entities.mitarbeiter.*;
import com.ibosng.dbibosservice.enums.BooleanStatus;
import com.ibosng.dbibosservice.services.*;
import com.ibosng.dbibosservice.services.mitarbeiter.*;
import com.ibosng.dbmapperservice.services.MitarbeiterMapperService;
import com.ibosng.dbmapperservice.utils.mappers.*;
import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.UnterhaltsberechtigteDto;
import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.VordienstzeitenDto;
import com.ibosng.dbservice.entities.masterdata.IbisFirma;
import com.ibosng.dbservice.entities.masterdata.KVStufe;
import com.ibosng.dbservice.entities.masterdata.Kostenstelle;
import com.ibosng.dbservice.entities.mitarbeiter.BlobStatus;
import com.ibosng.dbservice.services.masterdata.IbisFirmaService;
import com.ibosng.dbservice.services.masterdata.KVStufeService;
import com.ibosng.dbservice.services.masterdata.KostenstelleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.ibosng.dbibosservice.utils.Helpers.isNullOrBlank;
import static com.ibosng.dbservice.utils.Helpers.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class MitarbeiterMapperServiceImpl implements MitarbeiterMapperService {
    private static final String VERTRAGSART_FIX = "Fix";

    private final BenutzerIbosService benutzerIbosService;
    private final StaatenIbosService staatenIbosService;
    private final KeytableIbosService keytableIbosService;
    private final MutterspracheIbosService mutterspracheIbosService;
    private final IbisFirmaIbosService ibisFirmaIbosService;
    private final IbisFirmaService ibisFirmaService;
    private final MitarbeiterKategorieIbosService mitarbeiterKategorieIbosService;
    private final KostenstelleService kostenstelleService;
    private final ArbeitsvertragZusatzIbosService arbeitsvertragZusatzIbosService;
    private final KvStufeIbosService kvStufeIbosService;
    private final KVStufeService kvStufeService;
    private final KvVerwendungsgruppeIbosService kvVerwendungsgruppeIbosService;
    private final ArbeitsvertragFixIbosService arbeitsvertragFixIbosService;
    private final ArbeitsvertragZusatzFixIbosService arbeitsvertragZusatzFixIbosService;
    private final AdresseIbosService adresseIbosService;
    private final StandortIbosService standortIbosService;
    private final ArbeitsvertragIbosService arbeitsvertragIbosService;
    private final DienstvertragIbosService dienstvertragIbosService;
    private final DvZusatzIbosService dvZusatzIbosService;
    private final PersonalbogenIbosService personalbogenIbosService;
    private final KollektivvertragIbosService kollektivvertragIbosService;

    private StammdatenDto mapIbosDataToStammdatenDto(AdresseIbos adresseIbos, ArbeitsvertragIbos arbeitsvertragIbos, ArbeitsvertragZusatzIbos arbeitsvertragZusatzIbos) {
        StammdatenDto stammdatenDto = new StammdatenDto();
        if (arbeitsvertragZusatzIbos != null && !isNullOrBlank(arbeitsvertragZusatzIbos.getPersnr())) {
            stammdatenDto.setPersonalnummer(arbeitsvertragZusatzIbos.getPersnr());
        }

        if (!isNullOrBlank(adresseIbos.getAdsvnr())) {
            stammdatenDto.setSvnr(adresseIbos.getAdsvnr().replace(" ", ""));
            stammdatenDto.setEcard(BlobStatus.VERIFIED.getValue());
        }

        if (!isNullOrBlank(adresseIbos.getAdznf1())) {
            stammdatenDto.setNachname(adresseIbos.getAdznf1());
        }
        if (!isNullOrBlank(adresseIbos.getAdemail1())) {
            stammdatenDto.setEmail(adresseIbos.getAdemail1());
        } else if (!isNullOrBlank(adresseIbos.getAdemail2())) {
            stammdatenDto.setEmail(adresseIbos.getAdemail2());
        }

        if (!isNullOrBlank(adresseIbos.getAdvnf2())) {
            stammdatenDto.setVorname(adresseIbos.getAdvnf2());
        }

        if (adresseIbos.getAdgebdatum() != null) {
            stammdatenDto.setGeburtsDatum(localDateToString(adresseIbos.getAdgebdatum()));
        }

        if (adresseIbos.getAdtitel() != null) {
            String titel = benutzerIbosService.getTitelFromId(adresseIbos.getAdtitel());
            if (!isNullOrBlank(titel)) {
                stammdatenDto.setTitel(titel);
            }
        }

        if (adresseIbos.getAdtitelv() != null) {
            String titel = benutzerIbosService.getTitelFromId(adresseIbos.getAdtitelv());
            if (!isNullOrBlank(titel)) {
                stammdatenDto.setTitel2(titel);
            }
        }

        if (!isNullOrBlank(adresseIbos.getAdgeschlecht())) {
            stammdatenDto.setGeschlecht(adresseIbos.getAdgeschlecht());
        }

        if (adresseIbos.getAdanrede() != null) {
            KeytableIbos anredeIbos = keytableIbosService.findFirstByKyNameAndKyNrOrderByKyIndex("Anrede", adresseIbos.getAdanrede());
            stammdatenDto.setAnrede(anredeIbos.getKyValueT1());
        }
        //Normale Adresse
        if (!isNullOrBlank(adresseIbos.getAdort())) {
            stammdatenDto.setOrt(adresseIbos.getAdort());
        }

        if (!isNullOrBlank(adresseIbos.getAdplz())) {
            stammdatenDto.setPlz(adresseIbos.getAdplz());
        }

        if (!isNullOrBlank(adresseIbos.getAdstrasse())) {
            stammdatenDto.setStrasse(adresseIbos.getAdstrasse());
        }

        if (!isNullOrBlank(adresseIbos.getAdlkz())) {
            stammdatenDto.setLand(adresseIbos.getAdlkz());
        }
        //Abweichende Adresse
        if (!isNullOrBlank(adresseIbos.getAdort2())) {
            stammdatenDto.setAOrt(adresseIbos.getAdort2());
        }

        if (!isNullOrBlank(adresseIbos.getAdplz2())) {
            stammdatenDto.setAPlz(adresseIbos.getAdplz2());
        }

        if (!isNullOrBlank(adresseIbos.getAdstrasse2())) {
            stammdatenDto.setAStrasse(adresseIbos.getAdstrasse2());
        }

        if (!isNullOrBlank(adresseIbos.getAdlkz2())) {
            stammdatenDto.setALand(adresseIbos.getAdlkz2());
        }

        //Staatsbürgerschaft
        if (adresseIbos.getAdstaatsb() != null) {
            Optional<StaatenIbos> staatenIbos = staatenIbosService.findById(adresseIbos.getAdstaatsb());
            staatenIbos.ifPresent(ibos -> stammdatenDto.setStaatsbuergerschaft(ibos.getBezeichnung()));
        }

        //Mobilnummer
        if (!isNullOrBlank(adresseIbos.getAdmobil1())) {
            stammdatenDto.setMobilnummer(adresseIbos.getAdmobil1());
        } else if (!isNullOrBlank(adresseIbos.getAdmobil2())) {
            stammdatenDto.setMobilnummer(adresseIbos.getAdmobil1());
        } else if (!isNullOrBlank(adresseIbos.getAdtelf())) {
            stammdatenDto.setMobilnummer(adresseIbos.getAdtelf());
        } else if (!isNullOrBlank(adresseIbos.getAdtelp())) {
            stammdatenDto.setMobilnummer(adresseIbos.getAdtelp());
        }

        //Familienstand
        if (adresseIbos.getAdfamstand() != null) {
            Optional<KeytableIbos> familienstand = keytableIbosService.findById(adresseIbos.getAdfamstand());
            familienstand.ifPresent(keytableIbos -> stammdatenDto.setFamilienstand(FamilienstandMapper.getIbosFamilienstand(keytableIbos.getKyValueT1())));
        }

        //Bank Daten
        if (!isNullOrBlank(adresseIbos.getAdbank())) {
            stammdatenDto.setBank(adresseIbos.getAdbank());
        }

        if (!isNullOrBlank(adresseIbos.getAdbankiban())) {
            stammdatenDto.setIban(adresseIbos.getAdbankiban());
            stammdatenDto.setBankcard(BlobStatus.VERIFIED.getValue());
        }

        if (!isNullOrBlank(adresseIbos.getAdbankbic())) {
            stammdatenDto.setBic(adresseIbos.getAdbankbic());
        }

        //Muttersprache
        if (adresseIbos.getAdmuttersprache() != null) {
            Optional<MutterspracheIbos> mutterspracheIbos = mutterspracheIbosService.findById(adresseIbos.getAdmuttersprache());
            mutterspracheIbos.ifPresent(muttersprache -> stammdatenDto.setMuttersprache(muttersprache.getName()));
            stammdatenDto.setBank(adresseIbos.getAdbank());
        }

        //Firma
        if (adresseIbos.getAdklientid() != null) {
            Optional<IbisFirmaIbos> ibisFirmaIbos = ibisFirmaIbosService.findById(adresseIbos.getAdklientid());
            if (ibisFirmaIbos.isPresent()) {
                IbisFirma ibisFirma = ibisFirmaService.findByBmdClient(ibisFirmaIbos.get().getBmdKlientId());
                if (ibisFirma != null) {
                    stammdatenDto.setFirma(ibisFirma.getName());
                }
            }
        }

        if (arbeitsvertragIbos != null) {
            if (!isNullOrBlank(arbeitsvertragIbos.getAgen())) {
                stammdatenDto.setArbeitsgenehmigung(arbeitsvertragIbos.getAgen());
            }
            if (arbeitsvertragIbos.getAgenBis() != null) {
                stammdatenDto.setGueltigBis(localDateToString(arbeitsvertragIbos.getAgenBis()));
            }
            if (arbeitsvertragIbos.getAgenKopieUebergeben() != null && arbeitsvertragIbos.getAgenKopieUebergeben()) {
                stammdatenDto.setArbeitsgenehmigungDok(BlobStatus.VERIFIED.getValue());
            }
        }
        return stammdatenDto;
    }

    private VertragsdatenDto mapIbosDataToVertragsdatenDto(AdresseIbos adresseIbos,
                                                           ArbeitsvertragIbos arbeitsvertragIbos,
                                                           ArbeitsvertragZusatzIbos arbeitsvertragZusatzIbos,
                                                           DienstvertragIbos dienstvertragIbos,
                                                           DvZusatzIbos dvZusatzIbos,
                                                           PersonalbogenIbos personalbogenIbos) {
        VertragsdatenDto vertragsdatenDto = new VertragsdatenDto();
        if (arbeitsvertragZusatzIbos != null && !isNullOrBlank(arbeitsvertragZusatzIbos.getPersnr())) {
            vertragsdatenDto.setPersonalnummer(arbeitsvertragZusatzIbos.getPersnr());
        }

        if (adresseIbos.getAdkategorie() != null) {
            Optional<MitarbeiterKategorieIbos> mitarbeiterKategorieIbos = mitarbeiterKategorieIbosService.findById(adresseIbos.getAdkategorie());
            if (mitarbeiterKategorieIbos.isPresent()) {
                String kategorieIbos = mitarbeiterKategorieIbos.get().getBezeichnung();
                if (!isNullOrBlank(kategorieIbos)) {
                    vertragsdatenDto.setKategorie(KategorieMapper.getMitarbeiterKategorie(kategorieIbos));
                }
            }
        }
        if (arbeitsvertragZusatzIbos != null && arbeitsvertragZusatzIbos.getGeschaeftsbereich() != null) {
            Kostenstelle kostenstelle = kostenstelleService.findByNummer(arbeitsvertragZusatzIbos.getGeschaeftsbereich());
            if (kostenstelle != null) {
                vertragsdatenDto.setKostenstelle(kostenstelle.getBezeichnung());
            }
        }
        if (arbeitsvertragIbos != null) {
            List<ArbeitsvertragFixIbos> arbeitsvertragFixIbosList = arbeitsvertragFixIbosService.findAllByArbeitsvertragId(arbeitsvertragIbos.getId());
            ArbeitsvertragFixIbos arbeitsvertragFixIbos = findFirstObject(arbeitsvertragFixIbosList, new HashSet<>(List.of(String.valueOf(arbeitsvertragIbos.getId()))), "ArbeitsvertragFixIbos");
            if (arbeitsvertragFixIbos != null) {
                if (arbeitsvertragFixIbos.getMobileworkingvereinbarung() != null && arbeitsvertragFixIbos.getMobileworkingvereinbarung()) {
                    vertragsdatenDto.setMobileWorking(arbeitsvertragFixIbos.getMobileworkingvereinbarung());
                }
                if (arbeitsvertragFixIbos.getVdGeprueft() != null && arbeitsvertragFixIbos.getVdGeprueft()) {
                    vertragsdatenDto.setFacheinschlaegigeTaetigkeitenGeprueft(arbeitsvertragFixIbos.getVdGeprueft());
                }
                if (arbeitsvertragFixIbos.getStartcoachId() != null) {
                    Optional<AdresseIbos> startcoach = adresseIbosService.findById(arbeitsvertragFixIbos.getStartcoachId());
                    if (startcoach.isPresent()) {
                        BenutzerIbos benutzerIbos = benutzerIbosService.findBenutzerByBnadnr(startcoach.get().getAdadnr());
                        if (benutzerIbos != null) {
                            vertragsdatenDto.setStartcoach(benutzerIbos.getBnadSaman());
                        }
                    }
                }
                if (arbeitsvertragFixIbos.getVorgesetzterId() != null) {
                    Optional<AdresseIbos> fuehrungskraft = adresseIbosService.findById(arbeitsvertragFixIbos.getVorgesetzterId());
                    if (fuehrungskraft.isPresent()) {
                        BenutzerIbos benutzerIbos = benutzerIbosService.findBenutzerByBnadnr(fuehrungskraft.get().getAdadnr());
                        if (benutzerIbos != null) {
                            vertragsdatenDto.setFuehrungskraft(benutzerIbos.getBnadSaman());
                        }
                    }
                }
            }
        }


        if (dienstvertragIbos != null) {
            if (dienstvertragIbos.getDvDatumeintritt() != null) {
                vertragsdatenDto.setEintritt(localDateToString(dienstvertragIbos.getDvDatumeintritt()));
            }
            if (dienstvertragIbos.getDvDatumaustritt() != null) {
                vertragsdatenDto.setBefristungBis(localDateToString(dienstvertragIbos.getDvDatumaustritt()));
                vertragsdatenDto.setIsBefristet(true);
            } else {
                vertragsdatenDto.setIsBefristet(false);
            }

        }
        if (dvZusatzIbos != null) {
            if (dvZusatzIbos.getDzLohn() != null) {
                vertragsdatenDto.setGesamtBrutto(dvZusatzIbos.getDzLohn().doubleValue());
            }
            if (!isNullOrBlank(dvZusatzIbos.getDzBemerkungen())) {
                vertragsdatenDto.setNotizGehalt(dvZusatzIbos.getDzBemerkungen());
            }
            double wochenstunden = 0.0;
            if (dvZusatzIbos.getDzAzsa() != null) {
                vertragsdatenDto.setASamstagNetto(dvZusatzIbos.getDzAzsa().toString());
                wochenstunden += dvZusatzIbos.getDzAzsa().doubleValue();
            }
            if (dvZusatzIbos.getDzAzso() != null) {
                vertragsdatenDto.setASonntagNetto(dvZusatzIbos.getDzAzso().toString());
                wochenstunden += dvZusatzIbos.getDzAzso().doubleValue();
            }
            if (dvZusatzIbos.getDzAzmo() != null) {
                vertragsdatenDto.setAMontagNetto(dvZusatzIbos.getDzAzmo().toString());
                wochenstunden += dvZusatzIbos.getDzAzmo().doubleValue();
            }
            if (dvZusatzIbos.getDzAzdi() != null) {
                vertragsdatenDto.setADienstagNetto(dvZusatzIbos.getDzAzdi().toString());
                wochenstunden += dvZusatzIbos.getDzAzdi().doubleValue();
            }
            if (dvZusatzIbos.getDzAzmi() != null) {
                vertragsdatenDto.setAMittwochNetto(dvZusatzIbos.getDzAzmi().toString());
                wochenstunden += dvZusatzIbos.getDzAzmi().doubleValue();
            }
            if (dvZusatzIbos.getDzAzdo() != null) {
                vertragsdatenDto.setADonnerstagNetto(dvZusatzIbos.getDzAzdo().toString());
                wochenstunden += dvZusatzIbos.getDzAzdo().doubleValue();
            }
            if (dvZusatzIbos.getDzAzfr() != null) {
                vertragsdatenDto.setAFreitagNetto(dvZusatzIbos.getDzAzfr().toString());
                wochenstunden += dvZusatzIbos.getDzAzfr().doubleValue();
            }
            vertragsdatenDto.setWochenstunden(String.valueOf(wochenstunden));
        }

        if (arbeitsvertragZusatzIbos != null) {
            if (arbeitsvertragZusatzIbos.getDienstort() != null) {
                standortIbosService.findById(arbeitsvertragZusatzIbos.getDienstort())
                        .ifPresent(standortIbos -> vertragsdatenDto.setDienstort(standortIbos.getSoBezeichnung()));
            }

            if (arbeitsvertragZusatzIbos.getTaetbezeichnung() != null) {
                Optional<KeytableIbos> taetigkeit = keytableIbosService.findById(arbeitsvertragZusatzIbos.getTaetbezeichnung());
                taetigkeit.ifPresent(taet -> vertragsdatenDto.setTaetigkeit(TaetigkeitMapper.getMitarbeiterKategorie(taetigkeit.get().getKyValueT1())));
            }
            if (arbeitsvertragZusatzIbos.getTaetausmass() != null) {
                Optional<KeytableIbos> taetigkeitAusmass = keytableIbosService.findById(arbeitsvertragZusatzIbos.getTaetausmass());
                taetigkeitAusmass.ifPresent(taet -> vertragsdatenDto.setBeschaeftigungsausmass(BeschaeftigunsausmassMapper.getMitarbeiterBeschaeftigunsausmass(taetigkeitAusmass.get().getKyValueT2())));
            }

            List<ArbeitsvertragZusatzFixIbos> arbeitsvertragZusatzFixIbosList = arbeitsvertragZusatzFixIbosService
                    .findAllByArbeitsvertragZusatzId(arbeitsvertragZusatzIbos.getId());
            if (!arbeitsvertragZusatzFixIbosList.isEmpty()) {
                ArbeitsvertragZusatzFixIbos arbeitsvertragZusatzFixIbos = arbeitsvertragZusatzFixIbosList.get(0);
                if (arbeitsvertragZusatzFixIbos.getGehalt() != null) {
                    vertragsdatenDto.setGehaltVereinbart(arbeitsvertragZusatzFixIbos.getGehalt().doubleValue());
                }
                if (arbeitsvertragZusatzFixIbos.getStdmovon() != null) {
                    vertragsdatenDto.setAMontagVon(arbeitsvertragZusatzFixIbos.getStdmovon().toString());
                }
                if (arbeitsvertragZusatzFixIbos.getStdmobis() != null) {
                    vertragsdatenDto.setAMontagBis(arbeitsvertragZusatzFixIbos.getStdmobis().toString());
                }

                if (arbeitsvertragZusatzFixIbos.getStddivon() != null) {
                    vertragsdatenDto.setADienstagVon(arbeitsvertragZusatzFixIbos.getStddivon().toString());
                }
                if (arbeitsvertragZusatzFixIbos.getStddibis() != null) {
                    vertragsdatenDto.setADienstagBis(arbeitsvertragZusatzFixIbos.getStddibis().toString());
                }

                if (arbeitsvertragZusatzFixIbos.getStdmivon() != null) {
                    vertragsdatenDto.setAMittwochVon(arbeitsvertragZusatzFixIbos.getStdmivon().toString());
                }
                if (arbeitsvertragZusatzFixIbos.getStdmibis() != null) {
                    vertragsdatenDto.setAMittwochBis(arbeitsvertragZusatzFixIbos.getStdmibis().toString());
                }

                if (arbeitsvertragZusatzFixIbos.getStddovon() != null) {
                    vertragsdatenDto.setADonnerstagVon(arbeitsvertragZusatzFixIbos.getStddovon().toString());
                }
                if (arbeitsvertragZusatzFixIbos.getStddobis() != null) {
                    vertragsdatenDto.setADonnerstagBis(arbeitsvertragZusatzFixIbos.getStddobis().toString());
                }

                if (arbeitsvertragZusatzFixIbos.getStdfrvon() != null) {
                    vertragsdatenDto.setAFreitagVon(arbeitsvertragZusatzFixIbos.getStdfrvon().toString());
                }
                if (arbeitsvertragZusatzFixIbos.getStdfrbis() != null) {
                    vertragsdatenDto.setAFreitagBis(arbeitsvertragZusatzFixIbos.getStdfrbis().toString());
                }

                if (arbeitsvertragZusatzFixIbos.getStufe() != null) {
                    Optional<KvStufeIbos> kvStufeIbos = kvStufeIbosService.findById(arbeitsvertragZusatzFixIbos.getStufe());
                    if (kvStufeIbos.isPresent()) {
                        KVStufe kvStufe = kvStufeService.findAllByName(kvStufeIbos.get().getBezeichnung());
                        if (kvStufe != null) {
                            vertragsdatenDto.setStufe(kvStufe.getName());
                        }
                        Optional<KvVerwendungsgruppeIbos> kvVerwendungsgruppeIbos = kvVerwendungsgruppeIbosService.findById(kvStufeIbos.get().getKvVerwendungsgruppeId());
                        if (kvVerwendungsgruppeIbos.isPresent()) {
                            vertragsdatenDto.setVerwendungsgruppe(VerwendungsgruppeMapper.getVerwendungsgruppe(kvVerwendungsgruppeIbos.get().getBezeichnung()));
                            Optional<KollektivvertragIbos> kollektivvertragIbos = kollektivvertragIbosService.findById(kvVerwendungsgruppeIbos.get().getKollektivvertragId());
                            kollektivvertragIbos.ifPresent(ibos -> vertragsdatenDto.setKollektivvertrag(ibos.getBezeichnung()));
                        }
                    }
                }
            }
        }

        if (personalbogenIbos != null) {
            if (!isNullOrBlank(personalbogenIbos.getPbOrt())) {
                vertragsdatenDto.setOrt(personalbogenIbos.getPbOrt());
            }
            if (!isNullOrBlank(personalbogenIbos.getPbStrasse())) {
                vertragsdatenDto.setStrasse(personalbogenIbos.getPbStrasse());
            }

            if (personalbogenIbos.getPbVdGeprueft() != null && BooleanStatus.y.equals(personalbogenIbos.getPbVdGeprueft())) {
                vertragsdatenDto.setFacheinschlaegigeTaetigkeitenGeprueft(true);
            } else {
                vertragsdatenDto.setFacheinschlaegigeTaetigkeitenGeprueft(false);
            }
        }
        return vertragsdatenDto;
    }

    @Override
    public StammdatenDto mapIbosDataToStammdatenDto(AdresseIbos adresseIbos) {
        if (adresseIbos == null) {
            log.warn("AdresseIbos is null");
            return null;
        }
        ArbeitsvertragZusatzIbos arbeitsvertragZusatzIbos = findArbeitsvertragZusatzIbosByAddresse(adresseIbos);
        if (arbeitsvertragZusatzIbos == null) {
            log.warn("ArbeitsvertragZusatzIbos is not present for AdresseIbos id {}", adresseIbos.getAdadnr());
        }

        Optional<ArbeitsvertragIbos> arbeitsvertragIbos = getArbeitsvertragIbos(arbeitsvertragZusatzIbos);
        return arbeitsvertragIbos.map(ibos -> mapIbosDataToStammdatenDto(adresseIbos, ibos, arbeitsvertragZusatzIbos)).orElse(null);
    }

    @Override
    public VertragsdatenDto mapIbosDataToVertragsdatenDto(AdresseIbos adresseIbos) {
        if (adresseIbos == null) {
            log.warn("AdresseIbos is null");
            return null;
        }

        ArbeitsvertragZusatzIbos arbeitsvertragZusatzIbos = findArbeitsvertragZusatzIbosByAddresse(adresseIbos);
        if (arbeitsvertragZusatzIbos == null) {
            log.warn("ArbeitsvertragZusatzIbos is not present for AdresseIbos id {}", adresseIbos.getAdadnr());
        }

        Optional<ArbeitsvertragIbos> arbeitsvertragIbosOptional = getArbeitsvertragIbos(arbeitsvertragZusatzIbos);
        ArbeitsvertragIbos arbeitsvertragIbos = null;
        if (arbeitsvertragIbosOptional.isPresent()) {
            arbeitsvertragIbos = arbeitsvertragIbosOptional.get();
        }

        List<DvZusatzIbos> dvZusatzIbosList = dvZusatzIbosService.findAllByAdAdnr(adresseIbos.getAdadnr());
        DvZusatzIbos dvZusatzIbos = null;
        DienstvertragIbos dienstvertragIbos = null;
        Optional<DvZusatzIbos> dvZusatzIbosMostRecent = dvZusatzIbosList.stream()
                .filter(dvZusatz -> dvZusatz.getDzDatumAb() != null)
                .max(Comparator.comparing(DvZusatzIbos::getDzDatumAb));

        if (dvZusatzIbosMostRecent.isPresent()) {
            dvZusatzIbos = dvZusatzIbosMostRecent.get();
            Optional<DienstvertragIbos> dienstvertragIbosMostRecent = dienstvertragIbosService.findById(dvZusatzIbos.getDvNr());
            if (dienstvertragIbosMostRecent.isEmpty()) {
                log.warn("No DienstvertragIbos found for id {}", dvZusatzIbos.getDvNr());
            } else {
                dienstvertragIbos = dienstvertragIbosMostRecent.get();
            }
        } else {
            log.warn("No DvZusatzIbos found for AdresseIbos id {}", adresseIbos.getAdadnr());
        }


        List<PersonalbogenIbos> personalbogenIbosList = personalbogenIbosService.findAllByAdresseAdnr(adresseIbos.getAdadnr());
        PersonalbogenIbos personalbogenIbos = null;
        if (personalbogenIbosList.isEmpty()) {
            log.warn("No PersonalbogenIbos found for AdresseIbos id {}", adresseIbos.getAdadnr());
        } else {
            personalbogenIbos = personalbogenIbosList.get(0);
        }

        return mapIbosDataToVertragsdatenDto(
                adresseIbos,
                arbeitsvertragIbos,
                arbeitsvertragZusatzIbos,
                dienstvertragIbos,
                dvZusatzIbos,
                personalbogenIbos);
    }

    private Optional<ArbeitsvertragIbos> getArbeitsvertragIbos(ArbeitsvertragZusatzIbos arbeitsvertragZusatzIbos) {
        if (arbeitsvertragZusatzIbos == null) {
            log.warn("ArbeitsvertragZusatzIbos is null");
            return Optional.empty();
        }
        Optional<ArbeitsvertragIbos> arbeitsvertragIbos = arbeitsvertragIbosService.findById(arbeitsvertragZusatzIbos.getArbeitsvertragId());
        if (arbeitsvertragIbos.isEmpty()) {
            log.warn("ArbeitsvertragIbos is not present for id {}", arbeitsvertragZusatzIbos.getArbeitsvertragId());
        }
        return arbeitsvertragIbos;
    }

    private ArbeitsvertragZusatzIbos findArbeitsvertragZusatzIbosByAddresse(AdresseIbos adresseIbos) {
        List<ArbeitsvertragIbos> arbeitsvertragIbosList = arbeitsvertragIbosService.findAllByAddressNo(adresseIbos.getAdadnr());
        List<ArbeitsvertragZusatzIbos> arbeitsvertragZusatzIbosList = new ArrayList<>();
        arbeitsvertragIbosList.forEach(arbeitsvertragIbos -> arbeitsvertragZusatzIbosList.addAll(arbeitsvertragZusatzIbosService.findAllByArbeitsvertragId(arbeitsvertragIbos.getId())));
        List<ArbeitsvertragZusatzIbos> arbeitsvertragZusatzIbosListFiltered = arbeitsvertragZusatzIbosList.stream().filter(arbeitsvertragZusatzIbos -> isDateBetween(null, arbeitsvertragZusatzIbos.getDatumVon(), arbeitsvertragZusatzIbos.getDatumBis())).toList();
        return findFirstObject(arbeitsvertragZusatzIbosListFiltered, new HashSet<>(List.of(String.valueOf(adresseIbos.getAdadnr()))), "ArbeitsvertragZusatzIbos");
    }

    @Override
    public VordienstzeitenDto mapIbosDataToVordienstzeitenDto(VordienstzeitenIbos vordienstzeitenIbos) {
        VordienstzeitenDto dto = new VordienstzeitenDto();
        if (vordienstzeitenIbos != null) {
            if (vordienstzeitenIbos.getWochenstd() != null) {
                dto.setVWochenstunden(vordienstzeitenIbos.getWochenstd().toString());
            }
            if (!isNullOrBlank(vordienstzeitenIbos.getFirma())) {
                dto.setFirma(vordienstzeitenIbos.getFirma());
            }
            if (vordienstzeitenIbos.getVon() != null) {
                dto.setVordienstzeitenVon(localDateToString(vordienstzeitenIbos.getVon()));
            }
            if (vordienstzeitenIbos.getBis() != null) {
                dto.setVordienstzeitenVon(localDateToString(vordienstzeitenIbos.getBis()));
            }
            if (vordienstzeitenIbos.getErfahrungsart().equals(VordienstzeitenIbos.Erfahrungsart.facheinschl)) {
                dto.setAnrechenbar(true);
            } else {
                dto.setAnrechenbar(false);
            }
            if (vordienstzeitenIbos.getNachweis() != null && vordienstzeitenIbos.getNachweis() == 1) {
                dto.setNachweis(BlobStatus.VERIFIED.getValue());
            }
            if (vordienstzeitenIbos.getAnstellungsart().equals(VordienstzeitenIbos.Anstellungsart.fix)) {
                dto.setVertragsart(VERTRAGSART_FIX);
            }
        }
        return dto;
    }

    @Override
    public UnterhaltsberechtigteDto mapIbosDataToUnterhaltsberechtigteDto(KinderIbos kinderIbos) {
        UnterhaltsberechtigteDto dto = new UnterhaltsberechtigteDto();
        if (kinderIbos != null) {
            if (!isNullOrBlank(kinderIbos.getVorname())) {
                dto.setUVorname(kinderIbos.getVorname());
            }
            if (!isNullOrBlank(kinderIbos.getNachname())) {
                dto.setUNachname(kinderIbos.getNachname());
            }
            if (!isNullOrBlank(kinderIbos.getSvnr())) {
                dto.setUSvnr(kinderIbos.getSvnr());
            }
            if (kinderIbos.getGebdatum() != null) {
                dto.setUGeburtsdatum(localDateToString(kinderIbos.getGebdatum()));
            }
        }
        return dto;
    }
}
