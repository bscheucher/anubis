package com.ibosng.validationservice.teilnehmer.validations;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TeilnehmerGeschlechtValidationTest {



/*    @Test
    void testExecuteValidation_validGeschlecht() {

        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        Teilnehmer teilnehmer = new Teilnehmer();
        teilnehmerStaging.setGeschlecht("weiblich");
        teilnehmerStaging.setSource(TeilnehmerSource.VHS);
        teilnehmer.setDataStatuses(new ArrayList<>());
        Validation<TeilnehmerStaging, Teilnehmer> validation = new TeilnehmerGeschlechtValidation(geschlechtValidation);

        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer);

        assertTrue(result);
        assertTrue(teilnehmer.getDataStatuses().isEmpty());
    }

    @Test
    void testExecuteValidation_invalidGeschlecht() {

        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        Teilnehmer teilnehmer = new Teilnehmer();
        teilnehmerStaging.setGeschlecht("X");
        teilnehmerStaging.setSource(TeilnehmerSource.VHS);
        teilnehmer.setDataStatuses(new ArrayList<>());
        Validation<TeilnehmerStaging, Teilnehmer> validation = new TeilnehmerGeschlechtValidation(geschlechtValidation);

        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer);

        assertFalse(result);
        assertEquals(1, teilnehmer.getDataStatuses().size());
        assertEquals(TeilnehmerDataStatus.INVALID_GESCHLECHT, teilnehmer.getDataStatuses().get(0));
    }*/

}
