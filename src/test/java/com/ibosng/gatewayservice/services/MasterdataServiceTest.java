package com.ibosng.gatewayservice.services;


import com.ibosng.dbservice.entities.masterdata.IbisFirma;
import com.ibosng.dbservice.entities.masterdata.Kostenstelle;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.masterdata.Titel;
import com.ibosng.dbservice.entities.masterdata.Verwendungsgruppe;
import com.ibosng.dbservice.services.BenutzerService;
import com.ibosng.dbservice.services.DienstortService;
import com.ibosng.dbservice.services.IbosReferenceService;
import com.ibosng.dbservice.services.LandService;
import com.ibosng.dbservice.services.ai.AiUiShortcutService;
import com.ibosng.dbservice.services.masterdata.*;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.gatewayservice.dtos.response.PayloadTypeList;
import com.ibosng.gatewayservice.enums.PayloadTypes;
import com.ibosng.gatewayservice.services.impl.MasterdataServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class MasterdataServiceTest {

    @Mock
    private TitelService titelService;
    @Mock
    private AnredeService anredeService;
    @Mock
    private GeschlechtService geschlechtService;
    @Mock
    private FamilienstandService familienstandService;
    @Mock
    private ArbeitsgenehmigungService arbeitsgenehmigungService;
    @Mock
    private LandService landService;
    @Mock
    private BeschaeftigungsausmassService beschaeftigungsausmassService;
    @Mock
    private BeschaeftigungsstatusService beschaeftigungsstatusService;
    @Mock
    private JobbeschreibungService jobbeschreibungService;
    @Mock
    private KategorieService kategorieService;
    @Mock
    private KollektivvertragService kollektivvertragService;
    @Mock
    private TaetigkeitService taetigkeitService;
    @Mock
    private VerwendungsgruppeService verwendungsgruppeService;
    @Mock
    private AbgeltungUeberstundenService abgeltungUeberstundenService;
    @Mock
    private ArbeitszeitmodellService arbeitszeitmodellService;
    @Mock
    private ArtDerZulageService artDerZulageService;
    @Mock
    private IbisFirmaService ibisFirmaService;
    @Mock
    private DienstortService dienstortService;
    @Mock
    private VertragsartService vertragsartService;
    @Mock
    private KostenstelleService kostenstelleService;
    @Mock
    private IbisFirma2KostenstelleService ibisFirma2KostenstelleService;
    @Mock
    private AbrechnungsgruppeService abrechnungsgruppeService;
    @Mock
    private DienstnehmergruppeService dienstnehmergruppeService;
    @Mock
    private KommunalsteuergemeindeService kommunalsteuergemeindeService;
    @Mock
    private PersonalnummerService personalnummerService;
    @Mock
    private JobticketService jobticketService;
    @Mock
    private KlasseService klasseService;
    @Mock
    private VerwandtschaftService verwandtschaftService;
    @Mock
    private MutterspracheService mutterspracheService;
    @Mock
    private IbosReferenceService ibosReferenceService;
    @Mock
    private BenutzerService benutzerService;
    @Mock
    private AiUiShortcutService aiUiShortcutService;

    @Mock
    private TeilnehmerNotizKategorieService teilnehmerNotizKategorieService;

    @Mock
    private SeminarAustrittsgrundService seminarAustrittsgrundService;

    @Mock
    private SprachkenntnisNiveauService sprachkenntnisNiveauService;

    @Mock
    private TeilnehmerNotizTypeService teilnehmerNotizTypeService;

    @Mock
    private SeminarPruefungBezeichnungService seminarPruefungBezeichnungService;

    @Mock
    private SeminarPruefungArtService seminarPruefungArtService;

    @Mock
    private SeminarPruefungGegenstandService seminarPruefungGegenstandService;

    @Mock
    private SeminarPruefungNiveauService seminarPruefungNiveauService;

    @Mock
    private SeminarPruefungInstitutService seminarPruefungInstitutService;

    @Mock
    private SeminarPruefungBegruendungService seminarPruefungBegruendungService;

    @Mock
    private SeminarPruefungErgebnisTypeService seminarPruefungErgebnisTypeService;

    @Mock
    private TeilnehmerAusbildungTypeService teilnehmerAusbildungTypeService;

    @Mock
    private Gateway2AiService gateway2AiService;

    private MasterdataService masterdataService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        masterdataService = new MasterdataServiceImpl(titelService, anredeService, geschlechtService, familienstandService,
                arbeitsgenehmigungService, landService, beschaeftigungsausmassService,
                beschaeftigungsstatusService, jobbeschreibungService, kategorieService, kollektivvertragService,
                taetigkeitService, verwendungsgruppeService, abgeltungUeberstundenService, arbeitszeitmodellService,
                artDerZulageService, ibisFirmaService, vertragsartService, ibisFirma2KostenstelleService, kostenstelleService,
                abrechnungsgruppeService, dienstnehmergruppeService, kommunalsteuergemeindeService, personalnummerService, jobticketService,
                klasseService, verwandtschaftService, mutterspracheService, dienstortService, ibosReferenceService, benutzerService, aiUiShortcutService,
                teilnehmerNotizKategorieService, seminarAustrittsgrundService, sprachkenntnisNiveauService, teilnehmerNotizTypeService,
                seminarPruefungBezeichnungService, seminarPruefungArtService, seminarPruefungGegenstandService, seminarPruefungNiveauService,
                seminarPruefungInstitutService, seminarPruefungBegruendungService, seminarPruefungErgebnisTypeService, teilnehmerAusbildungTypeService, gateway2AiService);
    }

    @Test
    void testGetMitarbeiterPayload() {
        Titel titel1 = new Titel();
        titel1.setName("TITEL1");
        Titel titel2 = new Titel();
        titel2.setName("TITEL2");
        when(titelService.findAll()).thenReturn(List.of(titel1, titel2));

        var payloadResponse = masterdataService.getMitarbeiterPayload(null);

        assertEquals(true, payloadResponse.isSuccess());

    }

    @Test
    void testGetKostenstelleFromPersonalnummer() {
        String personalnummer = "12345";

        Personalnummer personalnummerEntity = new Personalnummer();
        IbisFirma firma = new IbisFirma();
        firma.setName("TestFirma");
        personalnummerEntity.setFirma(firma);

        Kostenstelle kostenstelle1 = new Kostenstelle();
        kostenstelle1.setBezeichnung("Kostenstelle1");

        Kostenstelle kostenstelle2 = new Kostenstelle();
        kostenstelle2.setBezeichnung("Kostenstelle2");

        when(personalnummerService.findByPersonalnummer(personalnummer)).thenReturn(personalnummerEntity);
        when(ibisFirma2KostenstelleService.findKostenstelleByIbisFirmaName("TestFirma"))
                .thenReturn(List.of(kostenstelle2, kostenstelle1));

        var response = masterdataService.getKostenstelleFromPersonalnummer(personalnummer);

        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
        assertEquals(1, response.getData().size());

        @SuppressWarnings("unchecked")
        PayloadTypeList<String> kostenstellenPayload = (PayloadTypeList<String>) response.getData().get(0);

        assertEquals(PayloadTypes.KOSTENSTELLEN.getValue(), kostenstellenPayload.getType());
        assertEquals(List.of("Kostenstelle1", "Kostenstelle2"), kostenstellenPayload.getAttributes());
    }

}