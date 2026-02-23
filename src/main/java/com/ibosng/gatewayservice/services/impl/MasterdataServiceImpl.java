package com.ibosng.gatewayservice.services.impl;


import com.ibosng.dbibosservice.dtos.BasicPersonDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.Dienstort;
import com.ibosng.dbservice.entities.Land;
import com.ibosng.dbservice.entities.SprachkenntnisNiveau;
import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.ai.AiUiShortcut;
import com.ibosng.dbservice.entities.masterdata.*;
import com.ibosng.dbservice.entities.mitarbeiter.VertragsaenderungStatus;
import com.ibosng.dbservice.entities.seminar.SeminarAustrittsgrund;
import com.ibosng.dbservice.entities.seminar.SeminarPruefungArt;
import com.ibosng.dbservice.entities.seminar.SeminarPruefungBegruendung;
import com.ibosng.dbservice.entities.seminar.SeminarPruefungBezeichnung;
import com.ibosng.dbservice.entities.seminar.SeminarPruefungErgebnisType;
import com.ibosng.dbservice.entities.seminar.SeminarPruefungGegenstand;
import com.ibosng.dbservice.entities.seminar.SeminarPruefungInstitut;
import com.ibosng.dbservice.entities.seminar.SeminarPruefungNiveau;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerNotizKategorie;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerNotizType;
import com.ibosng.dbservice.entities.teilnehmer.TnAusbildungType;
import com.ibosng.dbservice.services.BenutzerService;
import com.ibosng.dbservice.services.DienstortService;
import com.ibosng.dbservice.services.IbosReferenceService;
import com.ibosng.dbservice.services.LandService;
import com.ibosng.dbservice.services.ai.AiUiShortcutService;
import com.ibosng.dbservice.services.masterdata.*;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.gatewayservice.dtos.LandDto;
import com.ibosng.gatewayservice.dtos.masterdata.MitarbeiterMasterdata;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.dtos.response.PayloadType;
import com.ibosng.gatewayservice.dtos.response.PayloadTypeList;
import com.ibosng.gatewayservice.enums.PayloadTypes;
import com.ibosng.gatewayservice.services.Gateway2AiService;
import com.ibosng.gatewayservice.services.MasterdataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Service
@Slf4j
@CacheConfig(cacheNames = {"masterdataCache"})
public class MasterdataServiceImpl implements MasterdataService {

    private final static String FUEHRUNGSKRAFT = "FUEHRUNGSKRAFT";
    private final static String STARTCOACH = "STARTCOACH";

    private final TitelService titelService;
    private final AnredeService anredeService;
    private final GeschlechtService geschlechtService;
    private final FamilienstandService familienstandService;
    private final ArbeitsgenehmigungService arbeitsgenehmigungService;
    private final LandService landService;
    private final BeschaeftigungsausmassService beschaeftigungsausmassService;
    private final BeschaeftigungsstatusService beschaeftigungsstatusService;
    private final JobbeschreibungService jobbeschreibungService;
    private final KategorieService kategorieService;
    private final KollektivvertragService kollektivvertragService;
    private final TaetigkeitService taetigkeitService;
    private final VerwendungsgruppeService verwendungsgruppeService;
    private final AbgeltungUeberstundenService abgeltungUeberstundenService;
    private final ArbeitszeitmodellService arbeitszeitmodellService;
    private final ArtDerZulageService artDerZulageService;
    private final IbisFirmaService ibisFirmaService;
    private final VertragsartService vertragsartService;
    private final IbisFirma2KostenstelleService ibisFirma2KostenstelleService;
    private Collator germanCollator;
    private final KostenstelleService kostenstelleService;
    private final AbrechnungsgruppeService abrechnungsgruppeService;
    private final DienstnehmergruppeService dienstnehmergruppeService;
    private final PersonalnummerService personalnummerService;
    private final JobticketService jobticketService;
    private final KlasseService klasseService;
    private final VerwandtschaftService verwandtschaftService;
    private final MutterspracheService mutterspracheService;
    private final DienstortService dienstortService;
    private final IbosReferenceService ibosReferenceService;
    private final BenutzerService benutzerService;
    private final AiUiShortcutService aiUiShortcutService;
    private final TeilnehmerNotizKategorieService teilnehmerNotizKategorieService;
    private final TeilnehmerNotizTypeService teilnehmerNotizTypeService;
    private final SeminarAustrittsgrundService seminarAustrittsgrundService;
    private final SprachkenntnisNiveauService sprachkenntnisNiveauService;
    private final SeminarPruefungBezeichnungService seminarPruefungBezeichnungService;
    private final SeminarPruefungArtService seminarPruefungArtService;
    private final SeminarPruefungGegenstandService seminarPruefungGegenstandService;
    private final SeminarPruefungNiveauService seminarPruefungNiveauService;
    private final SeminarPruefungInstitutService seminarPruefungInstitutService;
    private final SeminarPruefungBegruendungService seminarPruefungBegruendungService;
    private final SeminarPruefungErgebnisTypeService seminarPruefungErgebnisTypeService;
    private final TeilnehmerAusbildungTypeService teilnehmerAusbildungTypeService;
    private final Gateway2AiService gateway2AiService;

    public MasterdataServiceImpl(TitelService titelService,
                                 AnredeService anredeService,
                                 GeschlechtService geschlechtService,
                                 FamilienstandService familienstandService,
                                 ArbeitsgenehmigungService arbeitsgenehmigungService,
                                 LandService landService,
                                 BeschaeftigungsausmassService beschaeftigungsausmassService,
                                 BeschaeftigungsstatusService beschaeftigungsstatusService,
                                 JobbeschreibungService jobbeschreibungService,
                                 KategorieService kategorieService,
                                 KollektivvertragService kollektivvertragService,
                                 TaetigkeitService taetigkeitService,
                                 VerwendungsgruppeService verwendungsgruppeService,
                                 AbgeltungUeberstundenService abgeltungUeberstundenService,
                                 ArbeitszeitmodellService arbeitszeitmodellService,
                                 ArtDerZulageService artDerZulageService,
                                 IbisFirmaService ibisFirmaService,
                                 VertragsartService vertragsartService,
                                 IbisFirma2KostenstelleService ibisFirma2KostenstelleService,
                                 KostenstelleService kostenstelleService,
                                 AbrechnungsgruppeService abrechnungsgruppeService,
                                 DienstnehmergruppeService dienstnehmergruppeService,
                                 KommunalsteuergemeindeService kommunalsteuergemeindeService,
                                 PersonalnummerService personalnummerService,
                                 JobticketService jobticketService,
                                 KlasseService klasseService,
                                 VerwandtschaftService verwandtschaftService,
                                 MutterspracheService mutterspracheService,
                                 DienstortService dienstortService,
                                 IbosReferenceService ibosReferenceService,
                                 BenutzerService benutzerService,
                                 AiUiShortcutService aiUiShortcutService,
                                 TeilnehmerNotizKategorieService teilnehmerNotizKategorieService,
                                 SeminarAustrittsgrundService seminarAustrittsgrundService,
                                 SprachkenntnisNiveauService sprachkenntnisNiveauService,
                                 TeilnehmerNotizTypeService teilnehmerNotizTypeService,
                                 SeminarPruefungBezeichnungService seminarPruefungBezeichnungService,
                                 SeminarPruefungArtService seminarPruefungArtService,
                                 SeminarPruefungGegenstandService seminarPruefungGegenstandService,
                                 SeminarPruefungNiveauService seminarPruefungNiveauService,
                                 SeminarPruefungInstitutService seminarPruefungInstitutService,
                                 SeminarPruefungBegruendungService seminarPruefungBegruendungService,
                                 SeminarPruefungErgebnisTypeService seminarPruefungErgebnisTypeService,
                                 TeilnehmerAusbildungTypeService teilnehmerAusbildungTypeService, Gateway2AiService gateway2AiService
    ) {
        this.titelService = titelService;
        this.anredeService = anredeService;
        this.geschlechtService = geschlechtService;
        this.familienstandService = familienstandService;
        this.arbeitsgenehmigungService = arbeitsgenehmigungService;
        this.landService = landService;
        this.mutterspracheService = mutterspracheService;
        this.beschaeftigungsausmassService = beschaeftigungsausmassService;
        this.beschaeftigungsstatusService = beschaeftigungsstatusService;
        this.jobbeschreibungService = jobbeschreibungService;
        this.kategorieService = kategorieService;
        this.kollektivvertragService = kollektivvertragService;
        this.taetigkeitService = taetigkeitService;
        this.verwendungsgruppeService = verwendungsgruppeService;
        this.abgeltungUeberstundenService = abgeltungUeberstundenService;
        this.arbeitszeitmodellService = arbeitszeitmodellService;
        this.artDerZulageService = artDerZulageService;
        this.ibisFirma2KostenstelleService = ibisFirma2KostenstelleService;
        this.ibisFirmaService = ibisFirmaService;
        this.vertragsartService = vertragsartService;
        this.kostenstelleService = kostenstelleService;
        this.personalnummerService = personalnummerService;
        this.klasseService = klasseService;
        this.verwandtschaftService = verwandtschaftService;
        this.dienstortService = dienstortService;
        this.ibosReferenceService = ibosReferenceService;
        this.benutzerService = benutzerService;
        this.aiUiShortcutService = aiUiShortcutService;
        this.gateway2AiService = gateway2AiService;
        this.germanCollator = Collator.getInstance(Locale.GERMAN);
        this.abrechnungsgruppeService = abrechnungsgruppeService;
        this.dienstnehmergruppeService = dienstnehmergruppeService;
        this.jobticketService = jobticketService;
        this.teilnehmerNotizKategorieService = teilnehmerNotizKategorieService;
        this.seminarAustrittsgrundService = seminarAustrittsgrundService;
        this.sprachkenntnisNiveauService = sprachkenntnisNiveauService;
        this.teilnehmerNotizTypeService = teilnehmerNotizTypeService;
        this.seminarPruefungBezeichnungService = seminarPruefungBezeichnungService;
        this.seminarPruefungArtService = seminarPruefungArtService;
        this.seminarPruefungGegenstandService = seminarPruefungGegenstandService;
        this.seminarPruefungNiveauService = seminarPruefungNiveauService;
        this.seminarPruefungInstitutService = seminarPruefungInstitutService;
        this.seminarPruefungBegruendungService = seminarPruefungBegruendungService;
        this.seminarPruefungErgebnisTypeService = seminarPruefungErgebnisTypeService;
        this.teilnehmerAusbildungTypeService = teilnehmerAusbildungTypeService;
    }

    @Override
    @Cacheable
    public PayloadResponse getMitarbeiterPayload(String type) {
        log.info("masterdata type - {}", type);
        PayloadResponse response = new PayloadResponse();
        response.setSuccess(true);
        response.setData(getMitarbeiterMasterdata(type));
        return response;
    }

    @Override
    public PayloadResponse getKostenstelleFromPersonalnummer(String personalnummer) {

        PayloadResponse response = new PayloadResponse();
        response.setSuccess(true);
        Personalnummer personalnummerEntity = personalnummerService.findByPersonalnummer(personalnummer);
        List<String> kostenstellen = ibisFirma2KostenstelleService.findKostenstelleByIbisFirmaName(personalnummerEntity.getFirma().getName()).stream().map(Kostenstelle::getBezeichnung).sorted(germanCollator).toList();
        PayloadTypeList<String> kostenstellenPayload = new PayloadTypeList<>(PayloadTypes.KOSTENSTELLEN.getValue());
        kostenstellenPayload.setAttributes(kostenstellen);
        response.setData(Collections.singletonList(kostenstellenPayload));
        return response;
    }

    private List<PayloadType> getMitarbeiterMasterdata(String type) {
        PayloadTypeList<MitarbeiterMasterdata> masterStammdatenPayloadType = new PayloadTypeList<>(PayloadTypes.MASTERDATA.getValue());
        MitarbeiterMasterdata mitarbeiterMasterdata = new MitarbeiterMasterdata();

        mitarbeiterMasterdata.setTitelList(titelService.findAll().stream().map(Titel::getName).sorted(germanCollator).toList());
        mitarbeiterMasterdata.setAnredeList(anredeService.findAll().stream().map(Anrede::getName).sorted(germanCollator).toList());
        mitarbeiterMasterdata.setGeschlechtList(getGeschlechtData(geschlechtService.findAll()));
        mitarbeiterMasterdata.setFamilienstandList(getFamilienstandData(familienstandService.findAll()));
        mitarbeiterMasterdata.setArbeitsgenehmigungList(arbeitsgenehmigungService.findAll().stream().map(Arbeitsgenehmigung::getName).sorted(germanCollator).toList());
        mitarbeiterMasterdata.setStaatsbuergerschaftList(landService.findAll().stream().map(Land::getLandName).sorted(germanCollator).toList());
        mitarbeiterMasterdata.setLandList(landService.findAll().stream().map(Land::getLandName).filter(Objects::nonNull).sorted(germanCollator).toList());
        mitarbeiterMasterdata.setCompleteLandList(getCompleteLandList());
        List<String> firmenList = ibisFirmaService.findAllByStatus(Status.ACTIVE).stream().map(IbisFirma::getName).sorted(germanCollator).toList();
        mitarbeiterMasterdata.setFirmenList(firmenList);
        mitarbeiterMasterdata.setKostenstelleMap(getKostenstelleMapping(firmenList));
        mitarbeiterMasterdata.setKostenstelleList(kostenstelleService.findAllByStatus(Status.ACTIVE).stream().map(Kostenstelle::getBezeichnung).sorted(germanCollator).toList());
        mitarbeiterMasterdata.setDienstortList(dienstortService.findAll().stream().map(Dienstort::getName).sorted(germanCollator).toList());
        mitarbeiterMasterdata.setFuehrungskraftList(getBenutzerFromReferences(FUEHRUNGSKRAFT));
        mitarbeiterMasterdata.setStartcoachList(getBenutzerFromReferences(STARTCOACH));
        mitarbeiterMasterdata.setBeschaeftigungsausmassList(beschaeftigungsausmassService.findAll().stream().map((Beschaeftigungsausmass::getName)).sorted(germanCollator).toList());
        mitarbeiterMasterdata.setBeschaeftigungsstatusList(beschaeftigungsstatusService.findAll().stream().map((Beschaeftigungsstatus::getName)).sorted(germanCollator).toList());
        mitarbeiterMasterdata.setJobbeschreibungList(jobbeschreibungService.findAll().stream().map((Jobbeschreibung::getName)).sorted(germanCollator).toList());
        mitarbeiterMasterdata.setAbgeltungUeberstundenList(abgeltungUeberstundenService.findAll().stream().map(AbgeltungUeberstunden::getName).sorted(germanCollator).toList());
        mitarbeiterMasterdata.setArbeitszeitmodellList(arbeitszeitmodellService.findAll().stream().map(Arbeitszeitmodell::getName).sorted(germanCollator).toList());
        mitarbeiterMasterdata.setArtDerZulage(artDerZulageService.findAll().stream().map(ArtDerZulage::getName).sorted(germanCollator).toList());
        mitarbeiterMasterdata.setVertragsartList(vertragsartService.findAll().stream().map(Vertragsart::getName).sorted(germanCollator).toList());
        mitarbeiterMasterdata.setJobticketList(jobticketService.findAll().stream().map(Jobticket::getName).toList());
        mitarbeiterMasterdata.setMutterspracheList(mutterspracheService.findAll().stream().map(Muttersprache::getName).sorted(germanCollator).toList());
        List<String> vertragsaenderungStatusesList = Arrays.stream(VertragsaenderungStatus.values())
                .sorted(Comparator.comparingInt(VertragsaenderungStatus::ordinal))
                .map(Enum::name)
                .toList();
        mitarbeiterMasterdata.setVertragsaenderungStatusesList(vertragsaenderungStatusesList);
        mitarbeiterMasterdata.setAiUiShortcutsList(aiUiShortcutService.findAll().stream().map(AiUiShortcut::getShortcut).sorted(germanCollator).toList());
        masterStammdatenPayloadType.setAttributes(Collections.singletonList(mitarbeiterMasterdata));

        List<Kollektivvertrag> kvListRaw = kollektivvertragService.findAll();
        List<Verwendungsgruppe> verwendungsgruppeListRaw = verwendungsgruppeService.findAll();
        ResponseEntity<Map<String, String>> seminarVertretungTrainersResponse = gateway2AiService.getAllTrainersRequest();
        if(seminarVertretungTrainersResponse != null && seminarVertretungTrainersResponse.getStatusCode().equals(HttpStatus.OK) && seminarVertretungTrainersResponse.getBody() != null) {
            mitarbeiterMasterdata.setSeminarVertretungTrainersList(getTrainersForSeminarvertretung(seminarVertretungTrainersResponse.getBody()));
        }
        // Specific TN or MA Data
        if (isNullOrBlank(type) || type.equals("MITARBEITER")) {
            List<String> kvList = kvListRaw.stream().map((Kollektivvertrag::getName)).filter(name -> name.equals("BABE")).sorted(germanCollator).toList();
            List<String> verwendungsGruppeList = verwendungsgruppeListRaw.stream().filter(verwendungsgruppe -> verwendungsgruppe.getKollektivvertrag().getName().equals("BABE")).map((Verwendungsgruppe::getName)).sorted(germanCollator).toList();
            mitarbeiterMasterdata.setAbrechnungsgruppeList(getAbrechnungsgruppeDataMa());
            mitarbeiterMasterdata.setDienstnehmergruppeList(getDienstnehmergruppeDataMa());
            mitarbeiterMasterdata.setKollektivvertragList(kvList);
            mitarbeiterMasterdata.setVerwendungsgruppeList(verwendungsGruppeList);
            mitarbeiterMasterdata.setTaetigkeitList(taetigkeitService.findAll().stream().map((Taetigkeit::getName)).sorted(germanCollator).toList());
            mitarbeiterMasterdata.setKategorieList(kategorieService.findAll().stream().map((Kategorie::getName)).sorted(germanCollator).toList());
            mitarbeiterMasterdata.setVerwandtschaftList(verwandtschaftService.findAll().stream().map(Verwandtschaft::getName).sorted(germanCollator).toList());
        } else {
            List<String> kvList = kvListRaw.stream().map((Kollektivvertrag::getName)).filter(name -> name.equals("AMS-Lehrteilnehmer")).sorted(germanCollator).toList();
            List<String> verwendungsGruppeList = verwendungsgruppeListRaw.stream().filter(verwendungsgruppe -> verwendungsgruppe.getKollektivvertrag().getName().equals("AMS-Lehrteilnehmer")).map((Verwendungsgruppe::getName)).sorted(germanCollator).toList();
            mitarbeiterMasterdata.setAbrechnungsgruppeList(getAbrechnungsgruppeDataTn());
            mitarbeiterMasterdata.setDienstnehmergruppeList(getDienstnehmergruppeDataTn());
            mitarbeiterMasterdata.setKollektivvertragList(kvList);
            mitarbeiterMasterdata.setVerwendungsgruppeList(verwendungsGruppeList);
            mitarbeiterMasterdata.setTaetigkeitList(List.of("AMS-Lehrteilnehmer"));
            mitarbeiterMasterdata.setKategorieList(kategorieService.findAll().stream().map((Kategorie::getName)).filter(name -> name.equals("Lehrling")).sorted(germanCollator).toList());
            mitarbeiterMasterdata.setKlasseList(klasseService.findAll().stream().map(Klasse::getName).sorted(germanCollator).toList());
            mitarbeiterMasterdata.setTeilnehmerNotizenKategorieList(teilnehmerNotizKategorieService.findAll().stream().map(TeilnehmerNotizKategorie::getName).sorted(germanCollator).toList());
            mitarbeiterMasterdata.setSeminarAustrittsgrundList(seminarAustrittsgrundService.findAll().stream().map(SeminarAustrittsgrund::getName).sorted(germanCollator).toList());
            mitarbeiterMasterdata.setSprachkenntnisNiveauList(sprachkenntnisNiveauService.findAll().stream().map(SprachkenntnisNiveau::getName).sorted(germanCollator).toList());
            mitarbeiterMasterdata.setTeilnehmerNotizenTypeList(teilnehmerNotizTypeService.findAll().stream().map(TeilnehmerNotizType::getName).sorted(germanCollator).toList());
            mitarbeiterMasterdata.setSeminarPruefungBezeichnungList(seminarPruefungBezeichnungService.findAll().stream().map(SeminarPruefungBezeichnung::getName).sorted(germanCollator).toList());
            mitarbeiterMasterdata.setSeminarPruefungArtList(seminarPruefungArtService.findAll().stream().map(SeminarPruefungArt::getName).sorted(germanCollator).toList());
            mitarbeiterMasterdata.setSeminarPruefungGegenstandList(seminarPruefungGegenstandService.findAll().stream().map(SeminarPruefungGegenstand::getName).sorted(germanCollator).toList());
            mitarbeiterMasterdata.setSeminarPruefungNiveauList(seminarPruefungNiveauService.findAll().stream().map(SeminarPruefungNiveau::getName).sorted(germanCollator).toList());
            mitarbeiterMasterdata.setSeminarPruefungInstitutList(seminarPruefungInstitutService.findAll().stream().map(SeminarPruefungInstitut::getName).sorted(germanCollator).toList());
            mitarbeiterMasterdata.setSeminarPruefungBegruendungList(seminarPruefungBegruendungService.findAll().stream().map(SeminarPruefungBegruendung::getName).sorted(germanCollator).toList());
            mitarbeiterMasterdata.setSeminarPruefungErgebnisList(seminarPruefungErgebnisTypeService.findAll().stream().map(SeminarPruefungErgebnisType::getName).sorted(germanCollator).toList());
            mitarbeiterMasterdata.setTeilnehmerAusbildungTypeList(teilnehmerAusbildungTypeService.findAll().stream().map(TnAusbildungType::getName).sorted(germanCollator).toList());
        }
        return Collections.singletonList(masterStammdatenPayloadType);
    }

    private List<BasicPersonDto> getTrainersForSeminarvertretung(Map<String, String> trainers) {
        return trainers.entrySet().stream().map(entry -> mapFromMap(entry.getKey(), entry.getValue())).sorted(Comparator.comparing(BasicPersonDto::getName, germanCollator)).toList();
    }

    private BasicPersonDto mapFromMap(String id, String fullname) {
        BasicPersonDto basicPersonDto = new BasicPersonDto();
        basicPersonDto.setId(Long.parseLong(id));
        basicPersonDto.setName(fullname);
        return basicPersonDto;
    }

    private Map<String, List<String>> getKostenstelleMapping(List<String> firmenList) {
        Map<String, List<String>> result = new HashMap<>();
        firmenList.forEach(firma -> result.put(firma, ibisFirma2KostenstelleService.findKostenstelleByIbisFirmaName(firma).stream().map(Kostenstelle::getBezeichnung).sorted(germanCollator).toList()));
        return result;
    }

    private Map<String, String> getAbrechnungsgruppeDataMa() {
        List<Abrechnungsgruppe> abrechnungsgruppeList = abrechnungsgruppeService.findAll().stream().filter(abrechnungsgruppe -> !abrechnungsgruppe.getAbbreviation().toLowerCase().startsWith("t_")).toList();
        Map<String, String> map = new HashMap<>();
        abrechnungsgruppeList.forEach(abrechnungsgruppe -> map.put(abrechnungsgruppe.getAbbreviation(), abrechnungsgruppe.getBezeichnung()));
        return sortMapAfterValues(map);
    }

    private Map<String, String> getAbrechnungsgruppeDataTn() {
        List<Abrechnungsgruppe> abrechnungsgruppeList = abrechnungsgruppeService.findAll().stream().filter(abrechnungsgruppe -> abrechnungsgruppe.getAbbreviation().toLowerCase().startsWith("t_")).toList();
        Map<String, String> map = new HashMap<>();
        abrechnungsgruppeList.forEach(abrechnungsgruppe -> map.put(abrechnungsgruppe.getAbbreviation(), abrechnungsgruppe.getBezeichnung()));
        return sortMapAfterValues(map);
    }

    private Map<String, String> getDienstnehmergruppeDataMa() {
        List<Dienstnehmergruppe> dienstnehmergruppeList = dienstnehmergruppeService.findAll().stream().filter(dienstnehmergruppe -> dienstnehmergruppe.getAbbreviation().toLowerCase().startsWith("a_")).toList();
        Map<String, String> map = new HashMap<>();
        dienstnehmergruppeList.forEach(dienstnehmergruppe -> map.put(dienstnehmergruppe.getAbbreviation(), dienstnehmergruppe.getBezeichnung()));
        return sortMapAfterValues(map);
    }

    private Map<String, String> getDienstnehmergruppeDataTn() {
        List<Dienstnehmergruppe> dienstnehmergruppeList = dienstnehmergruppeService.findAll().stream().filter(dienstnehmergruppe -> dienstnehmergruppe.getAbbreviation().toLowerCase().startsWith("t_")).toList();
        Map<String, String> map = new HashMap<>();
        dienstnehmergruppeList.forEach(dienstnehmergruppe -> map.put(dienstnehmergruppe.getAbbreviation(), dienstnehmergruppe.getBezeichnung()));
        return sortMapAfterValues(map);
    }


    private List<LandDto> getCompleteLandList() {
        List<Land> laender = landService.findAll();
        List<LandDto> landDtos = new ArrayList<>();
        for (Land land : laender) {
            LandDto landDto = new LandDto();
            landDto.setLandName(land.getLandName());
            landDto.setLandCode(land.getLandCode());
            landDto.setEldaCode(landDto.getEldaCode());
            landDto.setTelefonVorwahl(land.getTelefonvorwahl());
            landDto.setEuEeaCh(land.getIsInEuEeaCh());
            landDtos.add(landDto);
        }
        landDtos.sort(Comparator.comparing(LandDto::getLandName));
        return landDtos;
    }

    private Map<String, String> getGeschlechtData(List<Geschlecht> geschlechts) {
        Map<String, String> map = new HashMap<>();
        geschlechts.forEach(familienstand -> map.put(familienstand.getAbbreviation(), familienstand.getName()));

        return sortMapAfterValues(map);
    }

    private Map<String, String> getFamilienstandData(List<Familienstand> familienstands) {
        Map<String, String> map = new HashMap<>();
        familienstands.forEach(familienstand -> map.put(familienstand.getAbbreviation(), familienstand.getName()));
        return sortMapAfterValues(map);
    }

    private List<BasicPersonDto> getBenutzerFromReferences(String reference) {
        return ibosReferenceService.findAllByData(reference).stream()
                .map(ref -> benutzerService.findById(ref.getIbosngId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(this::toBasicPersonDto)
                .sorted(Comparator.comparing(BasicPersonDto::getName, germanCollator))
                .toList();
    }

    private BasicPersonDto toBasicPersonDto(Benutzer benutzer) {
        BasicPersonDto dto = new BasicPersonDto();
        dto.setId(benutzer.getId().longValue());
        dto.setName(benutzer.getFirstName() + " " + benutzer.getLastName());
        return dto;
    }

    private Map<String, String> sortMapAfterValues(Map<String, String> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}
