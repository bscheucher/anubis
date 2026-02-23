package com.ibosng.validationservice.services.impl;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbibosservice.services.SeminarIbosService;
import com.ibosng.dbibosservice.services.SeminarTeilnehmerIbosService;
import com.ibosng.dbmapperservice.services.TeilnehmerMapperService;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.dbservice.services.TeilnehmerStagingService;
import com.ibosng.dbservice.services.workflows.WWorkflowGroupService;
import com.ibosng.validationservice.services.ValidatorService;
import com.ibosng.workflowservice.services.ManageWFItemsService;
import com.ibosng.workflowservice.services.ManageWFsService;
import com.ibosng.workflowservice.services.WFStartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Disabled
public class ImportedTeilnehmerValidatorServiceImplTest extends BaseIntegrationTest {
    private TeilnehmerValidatorServiceImpl teilnehmerService;
    @Mock
    private com.ibosng.dbservice.services.TeilnehmerService teilnehmerServiceImpl;
    @Mock
    private TeilnehmerMapperService teilnehmerMapperService;
    @Mock
    private TeilnehmerStagingService teilnehmerStagingService;
    @Mock
    private SeminarTeilnehmerIbosService seminarTeilnehmerIbosService;
    @Mock
    private SeminarIbosService seminarIbosService;
    @Mock
    private WFStartService wfStartService;
    @Mock
    private WWorkflowGroupService wWorkflowGroupService;
    @Mock
    private ManageWFsService manageWFsService;
    @Mock
    private ManageWFItemsService manageWFItemsService;
    @Mock
    private ValidatorService validatorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        teilnehmerService = new TeilnehmerValidatorServiceImpl(
                teilnehmerServiceImpl,
                teilnehmerMapperService,
                teilnehmerStagingService,
                seminarTeilnehmerIbosService,
                seminarIbosService,
                wfStartService,
                wWorkflowGroupService,
                manageWFsService,
                manageWFItemsService,
                validatorService);
    }

    @Test
    void getTeilnehmer_ExistingBySVN() {
        TeilnehmerStaging staging = new TeilnehmerStaging();
        staging.setSvNummer("5212010190");
        Teilnehmer expectedTeilnehmer = new Teilnehmer();
        when(teilnehmerServiceImpl.getBySVN(anyString())).thenReturn(Arrays.asList(expectedTeilnehmer));

        Teilnehmer result = teilnehmerService.getTeilnehmer(staging);

        assertNotNull(result);
        verify(teilnehmerServiceImpl).getBySVN(anyString());
    }

    @Test
    void getTeilnehmer_NotExisting() {
        TeilnehmerStaging staging = new TeilnehmerStaging();
        when(teilnehmerServiceImpl.getBySVN(anyString())).thenReturn(null);
        when(teilnehmerServiceImpl.getByVorname(anyString())).thenReturn(null);
        when(teilnehmerServiceImpl.getByNachname(anyString())).thenReturn(null);

        Teilnehmer result = teilnehmerService.getTeilnehmer(staging);

        assertNotNull(result);
        assertTrue(result instanceof Teilnehmer);
    }
}
