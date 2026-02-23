package com.ibosng.validationservice.teilnehmer.validations.imported;

import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import com.ibosng.validationservice.validations.VornameValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

/**
 * Required for OEIF, VHS, MDLC and eAMS
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class TeilnehmerVornameValidation extends AbstractValidation<TeilnehmerStaging, Teilnehmer> {

    @Override
    public boolean executeValidation(TeilnehmerStaging teilnehmerStaging, Teilnehmer teilnehmer) {
        if (!isNullOrBlank(teilnehmerStaging.getVorname())) {
            boolean result = VornameValidation.isVornameValid(teilnehmerStaging.getVorname());
            if (!result) {
                teilnehmer.addError("vorname", "Ungültiger Vorname angegeben", GlobalUserHolder.IBOSNG_BACKEND);
                return false;
            } else {
                teilnehmer.setVorname(teilnehmerStaging.getVorname());
                return true;
            }
        }
        teilnehmer.addError("vorname", "Ungültiger Vorname angegeben", GlobalUserHolder.IBOSNG_BACKEND);
        return false;
    }
}
