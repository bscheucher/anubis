package com.ibosng.validationservice.services.impl;

import com.ibosng.BaseIntegrationTest;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.dbservice.entities.masterdata.KVStufe;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.masterdata.Vertragsart;
import com.ibosng.dbservice.entities.mitarbeiter.BlobStatus;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.entities.mitarbeiter.Vordienstzeiten;
import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.dbservice.repositories.workflows.WWorkflowItemRepository;
import com.ibosng.dbservice.services.impl.workflows.WWorkflowItemServiceImpl;
import com.ibosng.dbservice.services.masterdata.GehaltService;
import com.ibosng.dbservice.services.masterdata.KVStufeService;
import com.ibosng.dbservice.services.masterdata.VertragsartService;
import com.ibosng.dbservice.services.mitarbeiter.*;
import com.ibosng.dbservice.services.workflows.WWorkflowGroupService;
import com.ibosng.dbservice.services.workflows.WWorkflowService;
import com.ibosng.validationservice.services.VertragsdatenValidatorService;
import com.ibosng.workflowservice.enums.SWorkflows;
import com.ibosng.workflowservice.services.ManageWFItemsService;
import com.ibosng.workflowservice.services.ManageWFsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {
        ManageWFItemsService.class,
        WWorkflowItemServiceImpl.class,
        WWorkflowItemRepository.class,
        VertragsartService.class,
        VertragsdatenService.class,
        VordienstzeitenService.class,
        KVStufeService.class,
        ManageWFsService.class,
        GehaltInfoService.class,
        GehaltService.class,
        ArbeitszeitenInfoService.class,
        WWorkflowService.class,
        WWorkflowGroupService.class,
        VertragsdatenValidatorService.class
})
public class KVEinstufungServiceImplTest extends BaseIntegrationTest {
    private static final String PERSONALNUMMER = "0010000045";
    @Mock
    private ManageWFItemsService manageWFItemsService;
    @Mock
    private VertragsartService vertragsartService;
    @Mock
    private VertragsdatenService vertragsdatenService;
    @Mock
    private VordienstzeitenService vordienstzeitenService;
    @Mock
    private KVStufeService kvStufeService;
    @Mock
    private GehaltInfoService gehaltInfoService;
    @Mock
    private GehaltService gehaltService;
    @Mock
    private ManageWFsService manageWFsService;
    @Mock
    private WWorkflowItemServiceImpl workflowItemService;
    @MockBean
    private WWorkflowItemRepository wWorkflowItemRepository;
    @Mock
    private ArbeitszeitenInfoService arbeitszeitenInfoService;
    @Mock
    private VertragsdatenValidatorService vertragsdatenValidatorService;
    @Mock
    private GehaltInfoZulageService gehaltInfoZulageService;
    @Mock
    private GlobalUserHolder globalUserHolder;
    @InjectMocks
    private KVEinstufungServiceImpl kvEinstufungServiceMock;

    Vertragsart fixVertragsart = new Vertragsart();
    Vertragsart freiVertragsart1 = new Vertragsart();
    Vertragsart freiVertragsart2 = new Vertragsart();
    Vertragsart freiVertragsart3 = new Vertragsart();
    Vertragsdaten vertragsdaten = new Vertragsdaten();

    List<Vordienstzeiten> vordienstzeitens = new ArrayList<>();

    @BeforeEach
    public void setup() {
        kvEinstufungServiceMock.setVordienstzeitens(new ArrayList<>()); // Ensure the list is reset

        Personalnummer personalnummer = new Personalnummer();
        personalnummer.setPersonalnummer(PERSONALNUMMER);
        vertragsdaten.setId(1);
        vertragsdaten.setPersonalnummer(personalnummer);

        fixVertragsart.setName("Fix");
        freiVertragsart1.setName("Freier Dienstnehmer");
        freiVertragsart2.setName("Werkvertrag");
        freiVertragsart3.setName("Selbständig");

        when(vertragsartService.findAll()).thenReturn(List.of(fixVertragsart, freiVertragsart1, freiVertragsart2, freiVertragsart3));
        when(manageWFsService.getWorkflowFromDataAndWFType(anyString(), eq(SWorkflows.ERFASSEN_STAMMDATEN_VERTRAGSDATEN))).thenReturn(new WWorkflow());
        when(vertragsdatenService.findByPersonalnummerString(PERSONALNUMMER)).thenReturn(List.of(vertragsdaten));

        kvEinstufungServiceMock = new KVEinstufungServiceImpl(38.0,
                manageWFItemsService,
                vordienstzeitenService,
                vertragsdatenService,
                kvStufeService,
                vertragsartService,
                gehaltInfoService,
                gehaltService,
                arbeitszeitenInfoService,
                gehaltInfoZulageService,
                manageWFsService,
                globalUserHolder);

    }

    @Test
    public void overlappingFixTimes() {
        vordienstzeitens = List.of(
                new Vordienstzeiten(vertragsdaten,
                        fixVertragsart,
                        "Firma A",
                        LocalDate.of(2020, 1, 1),
                        LocalDate.of(2020, 12, 31),
                        20.0,
                        true,
                        BlobStatus.NOT_VERIFIED,
                        MitarbeiterStatus.ACTIVE,
                        "test"),
                new Vordienstzeiten(vertragsdaten,
                        fixVertragsart,
                        "Firma B",
                        LocalDate.of(2020, 1, 1),
                        LocalDate.of(2021, 12, 31),
                        20.0,
                        true,
                        BlobStatus.NOT_VERIFIED,
                        MitarbeiterStatus.ACTIVE,
                        "test")
        );
        KVStufe kvStufe = new KVStufe();
        kvStufe.setName("Stufe 2");
        when(vordienstzeitenService.findAllByVertragsdatenId(anyInt())).thenReturn(vordienstzeitens);
        when(kvStufeService.findByTotalMonths(anyInt())).thenReturn(kvStufe);

        int monate = kvEinstufungServiceMock.calculateTotalMonths(vordienstzeitens);
        assertEquals(24, monate);
    }

    @Test
    public void overlappingFreeTimesSameContractType() {
        vordienstzeitens = List.of(
                new Vordienstzeiten(vertragsdaten,
                        freiVertragsart1,
                        "Firma A",
                        LocalDate.of(2020, 1, 1),
                        LocalDate.of(2020, 12, 31),
                        30.0,
                        true,
                        BlobStatus.NOT_VERIFIED,
                        MitarbeiterStatus.ACTIVE,
                        "test"),
                new Vordienstzeiten(vertragsdaten,
                        freiVertragsart1,
                        "Firma B",
                        LocalDate.of(2020, 1, 1),
                        LocalDate.of(2021, 12, 31),
                        30.0,
                        true,
                        BlobStatus.NOT_VERIFIED,
                        MitarbeiterStatus.ACTIVE,
                        "test")
        );
        KVStufe kvStufe = new KVStufe();
        kvStufe.setName("Stufe 2");
        when(vordienstzeitenService.findAllByVertragsdatenId(anyInt())).thenReturn(vordienstzeitens);
        when(kvStufeService.findByTotalMonths(anyInt())).thenReturn(kvStufe);

        int monate = kvEinstufungServiceMock.calculateTotalMonths(vordienstzeitens);
        assertEquals(22, monate);
    }

    @Test
    public void overlappingFreeTimesDifferentContractType() {
        vordienstzeitens = List.of(
                new Vordienstzeiten(vertragsdaten,
                        freiVertragsart1,
                        "Firma A",
                        LocalDate.of(2020, 1, 1),
                        LocalDate.of(2020, 12, 31),
                        30.0,
                        true,
                        BlobStatus.NOT_VERIFIED,
                        MitarbeiterStatus.ACTIVE,
                        "test"),
                new Vordienstzeiten(vertragsdaten,
                        freiVertragsart2,
                        "Firma B",
                        LocalDate.of(2020, 1, 1),
                        LocalDate.of(2021, 12, 31),
                        30.0,
                        true,
                        BlobStatus.NOT_VERIFIED,
                        MitarbeiterStatus.ACTIVE,
                        "test")
        );
        KVStufe kvStufe = new KVStufe();
        kvStufe.setName("Stufe 2");
        when(vordienstzeitenService.findAllByVertragsdatenId(anyInt())).thenReturn(vordienstzeitens);
        when(kvStufeService.findByTotalMonths(anyInt())).thenReturn(kvStufe);


        int monate = kvEinstufungServiceMock.calculateTotalMonths(vordienstzeitens);
        assertEquals(22, monate);
    }

    @Test
    public void mixFixANdFree() {
        vordienstzeitens = List.of(
                new Vordienstzeiten(vertragsdaten,
                        fixVertragsart,
                        "Firma A",
                        LocalDate.of(2020, 1, 1),
                        LocalDate.of(2020, 12, 31),
                        20.0,
                        true,
                        BlobStatus.NOT_VERIFIED,
                        MitarbeiterStatus.ACTIVE,
                        "test"),
                new Vordienstzeiten(vertragsdaten,
                        freiVertragsart1,
                        "Firma B",
                        LocalDate.of(2020, 1, 1),
                        LocalDate.of(2021, 12, 31),
                        19.0,
                        true,
                        BlobStatus.NOT_VERIFIED,
                        MitarbeiterStatus.ACTIVE,
                        "test")
        );
        KVStufe kvStufe = new KVStufe();
        kvStufe.setName("Stufe 2");
        when(vordienstzeitenService.findAllByVertragsdatenId(anyInt())).thenReturn(vordienstzeitens);
        when(kvStufeService.findByTotalMonths(anyInt())).thenReturn(kvStufe);

        int monate = kvEinstufungServiceMock.calculateTotalMonths(vordienstzeitens);
        assertEquals(18, monate);
    }
}
