package com.ibosng.usercreationservice.service;

import com.ibosng.dbibosservice.services.BenutzerIbosService;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.dbservice.entities.workflows.WWorkflowGroup;
import com.ibosng.dbservice.entities.workflows.WWorkflowStatus;
import com.ibosng.dbservice.services.mitarbeiter.StammdatenService;
import com.ibosng.dbservice.services.mitarbeiter.VertragsdatenService;
import com.ibosng.dbservice.services.workflows.WWorkflowGroupService;
import com.ibosng.microsoftgraphservice.config.properties.SharePointProperties;
import com.ibosng.microsoftgraphservice.exception.MSGraphServiceException;
import com.ibosng.microsoftgraphservice.services.SharePointService;
import com.ibosng.usercreationservice.dto.UserAnlageDto;
import com.ibosng.usercreationservice.exception.TechnicalException;
import com.ibosng.usercreationservice.service.impl.UserCreationAnlageIbisAcamServiceImpl;
import com.ibosng.usercreationservice.service.impl.UserCreationMitarbeiterMapperServiceImpl;
import com.ibosng.workflowservice.enums.SWorkflowItems;
import com.ibosng.workflowservice.enums.SWorkflows;
import com.ibosng.workflowservice.services.ManageWFItemsService;
import com.ibosng.workflowservice.services.ManageWFsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.List;

import static com.ibosng.usercreationservice.util.Constants.USER_CREATION_SERVICE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Disabled
@ExtendWith(MockitoExtension.class)
class UserCreationAnlageIbisAcamServiceImplTest {

    @Mock
    private SharePointProperties sharePointProperties;
    @Mock
    private VertragsdatenService vertragsdatenService;
    @Mock
    private StammdatenService stammdatenService;
    @Mock
    private SharePointService sharePointService;
    @Mock
    private ManageWFsService manageWFsService;
    @Mock
    private WWorkflowGroupService wWorkflowGroupService;
    @Mock
    private ManageWFItemsService manageWFItemsService;
    @Mock
    private BenutzerIbosService benutzerIbosService;

    private UserCreationMitarbeiterMapperService userCreationMitarbeiterMapperService;

    @InjectMocks
    private UserCreationAnlageIbisAcamServiceImpl userCreationAnlageIbisAcamServiceImpl;

    private Stammdaten stammdaten;
    private Vertragsdaten vertragsdaten;
    private WWorkflowGroup workflowGroup;
    private WWorkflow workflow;
    private File tempFile;
    private UserAnlageDto userAnlageDto;

    @BeforeEach
    void setUp() {
        stammdaten = new Stammdaten();
        vertragsdaten = new Vertragsdaten();
        workflowGroup = new WWorkflowGroup();
        userAnlageDto = new UserAnlageDto();
        workflow = new WWorkflow();
        tempFile = mock(File.class);
    }

    @Test
    void testCreateMitarbeiterFileSuccess() throws MSGraphServiceException {
        String personnelnummer = "12345";
        when(manageWFsService.getWorkflowFromDataAndWFType(personnelnummer, any(SWorkflows.class))).thenReturn(workflow);
        when(manageWFsService.setWFStatus(any(), any(), any(), anyString())).thenReturn(workflow);
        when(stammdatenService.findByPersonalnummerString(personnelnummer)).thenReturn(stammdaten);
        when(vertragsdatenService.findByPersonalnummerString(personnelnummer)).thenReturn(List.of(vertragsdaten));
        when(sharePointService.createJsonFileFromDto(any(), anyString())).thenReturn(tempFile);
        when(tempFile.getPath()).thenReturn("tempPath");
        when(benutzerIbosService.getEmailForFuehrungskraftForMitarbeiter(anyLong())).thenReturn("Manager");
        when(benutzerIbosService.getEmailForStartcoachForMitarbeiter(anyLong())).thenReturn("Coach");

        try (MockedStatic<UserCreationMitarbeiterMapperServiceImpl> mockedStatic = mockStatic(UserCreationMitarbeiterMapperServiceImpl.class)) {
            mockedStatic.when(() -> userCreationMitarbeiterMapperService.toDto(stammdaten, vertragsdaten))
                    .thenReturn(userAnlageDto);

            boolean result = userCreationAnlageIbisAcamServiceImpl.createMitarbeiterFile(personnelnummer);

            assertThat(result).isTrue();
            verify(manageWFItemsService).setWFItemStatus(workflow, SWorkflowItems.NEUEN_MA_ANLEGEN, WWorkflowStatus.COMPLETED, USER_CREATION_SERVICE);
            verify(manageWFItemsService).setWFItemStatus(workflow, SWorkflowItems.USER_ANLAGE_BEAUFTRAGEN, WWorkflowStatus.COMPLETED, USER_CREATION_SERVICE);
            verify(manageWFItemsService).setWFItemStatus(workflow, SWorkflowItems.AD_UND_IBOS_USER_ANLEGEN, WWorkflowStatus.IN_PROGRESS, USER_CREATION_SERVICE);
        }
    }

    @Test
    void testCreateMitarbeiterFileWorkflowGroupNull() {
        String personnelnummer = "12345";
        when(manageWFsService.getWorkflowFromDataAndWFType(personnelnummer, any(SWorkflows.class))).thenReturn(null);

        boolean result = userCreationAnlageIbisAcamServiceImpl.createMitarbeiterFile(personnelnummer);

        assertThat(result).isFalse();
        verify(manageWFItemsService, never()).setWFItemStatus(any(), any(), any(), anyString());
    }

    @Test
    void testCreateMitarbeiterFileUploadException() throws MSGraphServiceException {
        String personnelnummer = "12345";
        when(sharePointProperties.getNeueBenutzer()).thenReturn("neueBenutzer");
        when(manageWFsService.getWorkflowFromDataAndWFType(personnelnummer, any(SWorkflows.class))).thenReturn(workflow);
        when(manageWFsService.setWFStatus(any(), any(), any(), anyString())).thenReturn(workflow);
        when(stammdatenService.findByPersonalnummerString(personnelnummer)).thenReturn(stammdaten);
        when(vertragsdatenService.findByPersonalnummerString(personnelnummer)).thenReturn(List.of(vertragsdaten));
        when(sharePointService.createJsonFileFromDto(any(), anyString())).thenReturn(tempFile);
        when(tempFile.getPath()).thenReturn("tempPath");
        doThrow(new TechnicalException("Upload error")).when(sharePointService).uploadFile(anyString(), anyString(), anyString());
        when(benutzerIbosService.getEmailForFuehrungskraftForMitarbeiter(anyLong())).thenReturn("Manager");
        when(benutzerIbosService.getEmailForStartcoachForMitarbeiter(anyLong())).thenReturn("Coach");

        try (MockedStatic<UserCreationMitarbeiterMapperServiceImpl> mockedStatic = mockStatic(UserCreationMitarbeiterMapperServiceImpl.class)) {
            mockedStatic.when(() -> userCreationMitarbeiterMapperService.toDto(stammdaten, vertragsdaten))
                    .thenReturn(userAnlageDto);

            boolean result = userCreationAnlageIbisAcamServiceImpl.createMitarbeiterFile(personnelnummer);

            assertThat(result).isFalse();
            verify(manageWFItemsService).setWFItemStatus(workflow, SWorkflowItems.USER_ANLAGE_BEAUFTRAGEN, WWorkflowStatus.ERROR, USER_CREATION_SERVICE);
        }
    }
}