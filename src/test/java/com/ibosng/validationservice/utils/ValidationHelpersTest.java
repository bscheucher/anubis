package com.ibosng.validationservice.utils;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbservice.entities.masterdata.Titel;
import com.ibosng.dbservice.services.InternationalPlzService;
import com.ibosng.dbservice.services.impl.LandServiceImpl;
import com.ibosng.validationservice.validations.OrtValidation;
import com.ibosng.validationservice.validations.PLZValidation;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

import static com.ibosng.dbservice.utils.Parsers.*;
import static org.junit.jupiter.api.Assertions.*;

public class ValidationHelpersTest extends BaseIntegrationTest {

    @Mock
    private LandServiceImpl landService;
    @Mock
    private PLZValidation plzValidation;
    @Mock
    private OrtValidation ortValidation;
    @Mock
    private InternationalPlzService internationalPlzService;

    public static java.util.List<Titel> getListOfTitles() {
        java.util.List<String> titles = Arrays.asList(
                "DDr.",
                "DI (FH)",
                "DI Mag.",
                "Dipl. Ing.",
                "Dipl.-Wirt",
                "Dipl.Soz.P",
                "Dkffr.",
                "Dkfm.",
                "Dr.",
                "Dr. Ing.",
                "Dr. Phil.",
                "DAS",
                "Ing.",
                "Ing. (FH)",
                "Ing. Mag.",
                "Kom.Rat",
                "Mag(FH)",
                "Mag.",
                "Mag. Dr.",
                "Mag. phil.",
                "Mag. a",
                "Mag. a (FH)",
                "Mag. a phil",
                "Mmag.",
                "MMag.a",
                "Univ. Doz.",
                "Univ.Ass.",
                "Univ.Prof.",
                "BA",
                "MA",
                "MBA",
                "MSc",
                "Bakk",
                "Bed", // Note: Ensure this is 'Bed' not 'BEd' as per your context.
                "BSC",
                "Dipl. Päd.",
                "DSA",
                "MAS",
                "MTD",
                "Di.Reg.Wis",
                "MMSc"
        );
        java.util.List<Titel> titelList = new ArrayList<>();
        for (String title : titles) {
            Titel titel = new Titel();
            titel.setName(title);
            titelList.add(titel);
        }
        return titelList;

    }

    @Test
    public void whenValidDate_thenReturnTrue() {
        String validDate = "12.03.2021";
        assertTrue(isValidDate(validDate));
    }

    @Test
    public void whenInvalidDate_thenReturnFalse() {
        String invalidDate = "33.02.2021";
        assertFalse(isValidDate(invalidDate));
    }

    @Test
    public void whenvalidFormat_thenReturnTrue() {
        String invalidFormat = "2021-03-12";
        assertTrue(isValidDate(invalidFormat));
    }

    @Test
    public void whenValidFormat_thenParseDate() {
        String validDate = "2021-03-12";
        LocalDate date = parseDate(validDate);
        assertNotNull(date);
    }

    @Test
    public void validTimeFormat() {
        String time = "13:45";
        assertTrue(isValidTime(time));
    }

    @Test
    public void invalidTimeFormat() {
        String time = "13-45";
        assertFalse(isValidTime(time));
    }

    @Test
    public void whenValidFormat_thenParseTime() {
        String timeString = "13:45";
        LocalTime time = parseTime(timeString);
        assertNotNull(time);
    }

    @Test
    public void testConstructor() {
        ValidationHelpers validationHelpers =
                new ValidationHelpers(plzValidation, ortValidation, landService, internationalPlzService);
        assertTrue(validationHelpers instanceof ValidationHelpers);
    }

    @Test
    public void isDateInFuture() {
        ValidationHelpers validationHelpers = new ValidationHelpers(plzValidation, ortValidation, landService, internationalPlzService);

        assertTrue(validationHelpers.isDateInFuture(LocalDate.now().plusDays(1)));

        assertFalse(validationHelpers.isDateInFuture(LocalDate.now().minusDays(1)));
        assertFalse(validationHelpers.isDateInFuture((LocalDate.now())));
    }
}