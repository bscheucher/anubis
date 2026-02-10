package com.ibosng.validationservice.config;

import com.ibosng.validationservice.mitarbeiter.MitarbeiterStammdatenValidator;
import com.ibosng.validationservice.mitarbeiter.MitarbeiterUnterhaltsberechtigteValidator;
import com.ibosng.validationservice.mitarbeiter.MitarbeiterVertragsdatenValidator;
import com.ibosng.validationservice.mitarbeiter.MitarbeiterVordienstzeitenValidator;
import com.ibosng.validationservice.teilnehmer.ImportedTeilnehmerValidator;
import com.ibosng.validationservice.teilnehmer.TeilnehmerValidator;
import com.ibosng.validationservice.zeitbuchung.ZeitbuchungValidator;
import com.ibosng.validationservice.zeiterfassung.ZeiterfassungValidator;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

@Component
public class ValidationsConfig {

    @Lookup
    public ZeitbuchungValidator getZeitbuchungValidator() {
        return null;
    }

    @Lookup
    public ImportedTeilnehmerValidator getImportedTeilnehmerValidator() {
        return null;
    }

    @Lookup
    public TeilnehmerValidator getTeilnehmerValidator() {
        return null;
    }

    @Lookup
    public MitarbeiterUnterhaltsberechtigteValidator getMitarbeiterUnterhaltsBerechtigteValidator() {
        return null;
    }

    @Lookup
    public MitarbeiterVordienstzeitenValidator getMitarbeiterVordienstzeitenValidator() {
        return null;
    }

    @Lookup
    public MitarbeiterStammdatenValidator getMitarbeiterStammdatenValidator() {
        return null;
    }

    @Lookup
    public MitarbeiterVertragsdatenValidator getMitarbeiterVertragsdatenValidator() {
        return null;
    }

    @Lookup
    public ZeiterfassungValidator getZeiterfassungValidator() {
        return null;
    }
}
