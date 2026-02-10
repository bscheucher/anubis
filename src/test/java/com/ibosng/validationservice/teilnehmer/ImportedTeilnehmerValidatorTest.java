package com.ibosng.validationservice.teilnehmer;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbservice.entities.Adresse;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStatus;
import com.ibosng.dbservice.services.impl.TeilnehmerServiceImpl;
import com.ibosng.dbservice.services.impl.TeilnehmerStagingServiceImpl;
import com.ibosng.dbservice.services.impl.ValidationsServiceImpl;
import com.ibosng.validationservice.Validation;
import com.ibosng.validationservice.ValidationObjectPair;
import com.ibosng.validationservice.services.TeilnehmerValidatorService;
import com.ibosng.validationservice.teilnehmer.validations.imported.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

public class ImportedTeilnehmerValidatorTest extends BaseIntegrationTest {

    @Mock
    private TeilnehmerStagingServiceImpl teilnehmerStagingServiceImpl;
    @Mock
    private TeilnehmerServiceImpl teilnehmerServiceImpl;
    @Mock
    private ValidationsServiceImpl validationsService;
    @Mock
    private TeilnehmerValidatorService teilnehmerValidatorService;
    @Mock
    private TeilnehmerEmailValidation teilnehmerEmailValidation;
    @Mock
    private TeilnehmerTitelValidation teilnehmerTitelValidation;
    @Mock
    private TeilnehmerGeburtsdatumValidation teilnehmerGeburtsdatumValidation;
    @Mock
    private TeilnehmerGeburtsdatumAndSVNValidation teilnehmerGeburtsdatumAndSVNValidation;
    @Mock
    private VHSOptionalAdresseValidation vhsOptionalAdresseValidation;
    @Mock
    private SeminarValidation seminarValidation;
    @Mock
    private NationValidation nationValidation;
    @Mock
    private TeilnehmerAnredeValidation teilnehmerAnredeValidation;
    @Mock
    private TeilnehmerNotizValidation teilnehmerNotizValidation;
    @Mock
    private UrsprungValidation ursprungValidation;
    @Mock
    private VermittelbarAbValidation vermittelbarAbValidation;
    @Mock
    private ZielValidation zielValidation;
    @Mock
    private VHSOptionalGeschlechtValidation vhsOptionalGeschlechtValidation;
    @Mock
    private VHSOptionalNachnameValidation vhsOptionalNachnameValidation;
    @Mock
    private VHSOptionalSvnrValidation vhsOptionalSvnrValidation;
    @Mock
    private VHSOptionalTelefonValidation vhsOptionalTelefonValidation;
    @Mock
    private VHSOptionalVornameValidation vhsOptionalVornameValidation;
    @InjectMocks
    private ImportedTeilnehmerValidator importedTeilnehmerValidator;

    @Test
    void shouldAddAllValidationsOnPrepare() {
        importedTeilnehmerValidator.prepare();
        List<Validation<TeilnehmerStaging, Teilnehmer>> validations = importedTeilnehmerValidator.getValidations();
        boolean containsEmailValidation = validations.stream()
                .anyMatch(TeilnehmerEmailValidation.class::isInstance);
        assertTrue(containsEmailValidation, "List should contain an instance of EmailValidation");

        boolean containsTitelValidation = validations.stream()
                .anyMatch(TeilnehmerTitelValidation.class::isInstance);
        assertTrue(containsTitelValidation, "List should contain an instance of TitelValidation");

        boolean containsVornameValidation = validations.stream()
                .anyMatch(VHSOptionalVornameValidation.class::isInstance);
        assertTrue(containsVornameValidation, "List should contain an instance of VornameValidation");

        boolean containsNachnameValidation = validations.stream()
                .anyMatch(VHSOptionalNachnameValidation.class::isInstance);
        assertTrue(containsNachnameValidation, "List should contain an instance of NachnameValidation");

        boolean containsGeschlechtValidation = validations.stream()
                .anyMatch(VHSOptionalGeschlechtValidation.class::isInstance);
        assertTrue(containsGeschlechtValidation, "List should contain an instance of GeschlechtValidation");

        boolean containsSVNValidation = validations.stream()
                .anyMatch(VHSOptionalSvnrValidation.class::isInstance);
        assertTrue(containsSVNValidation, "List should contain an instance of SVNValidation");

        boolean containsAdresseValidation = validations.stream()
                .anyMatch(VHSOptionalAdresseValidation.class::isInstance);
        assertTrue(containsAdresseValidation, "List should contain an instance of AdresseValidation");

        boolean containsGeburtsdatumValidation = validations.stream()
                .anyMatch(TeilnehmerGeburtsdatumValidation.class::isInstance);
        assertTrue(containsGeburtsdatumValidation, "List should contain an instance of GeburtsdatumValidation");

        boolean containsTelefonValidation = validations.stream()
                .anyMatch(VHSOptionalTelefonValidation.class::isInstance);
        assertTrue(containsTelefonValidation, "List should contain an instance of TelefonValidation");

        boolean containsCourseValidation = validations.stream()
                .anyMatch(SeminarValidation.class::isInstance);
        assertTrue(containsCourseValidation, "List should contain an instance of CourseValidation");

    }


    @Test
    void shouldExecuteValidationsCorrectly() {
        TeilnehmerStaging mockStaging = new TeilnehmerStaging();
        Teilnehmer mockTeilnehmer = new Teilnehmer();
        mockTeilnehmer.setAdresse(new Adresse());
        mockStaging.setTitel("Titel");
        mockStaging.setSvNummer("1234567890");
        importedTeilnehmerValidator.setIdentifier("identifier");
        mockStaging.setSource(TeilnehmerSource.VHS);
        Mockito.when(teilnehmerStagingServiceImpl.findAllByIdentifier(anyString())).thenReturn(Collections.singletonList(mockStaging));
        importedTeilnehmerValidator.prepare();
        importedTeilnehmerValidator.setValidations(Collections.singletonList(new TeilnehmerTitelValidation()));

        Mockito.when(teilnehmerServiceImpl.getBySVN(anyString())).thenReturn(List.of(mockTeilnehmer));

        mockTeilnehmer.setId(123);
        Mockito.when(teilnehmerServiceImpl.save(any(Teilnehmer.class))).thenReturn(mockTeilnehmer);
        importedTeilnehmerValidator.executeValidations();
        Map<ValidationObjectPair<TeilnehmerStaging, Teilnehmer>, Boolean> validationResults = importedTeilnehmerValidator.getValidationResults();
        Optional<Map.Entry<ValidationObjectPair<TeilnehmerStaging, Teilnehmer>, Boolean>> actualValueOptional = validationResults.entrySet()
                .stream()
                .findFirst();
        actualValueOptional.ifPresent(validationObjectPairBooleanEntry -> assertFalse(validationObjectPairBooleanEntry.getValue()));

        importedTeilnehmerValidator.postProcess();
        Optional<Map.Entry<ValidationObjectPair<TeilnehmerStaging, Teilnehmer>, Boolean>> actualTeilnehmerOptional = validationResults.entrySet()
                .stream()
                .findFirst();
        actualTeilnehmerOptional.ifPresent(validationObjectPairBooleanEntry -> assertSame(TeilnehmerStatus.INVALID, validationObjectPairBooleanEntry.getKey().getSecond().getStatus()));
    }
}
