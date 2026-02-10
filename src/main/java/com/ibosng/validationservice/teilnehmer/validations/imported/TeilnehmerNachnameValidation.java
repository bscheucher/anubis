package com.ibosng.validationservice.teilnehmer.validations.imported;

import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import com.ibosng.validationservice.validations.NachnameValidation;
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
public class TeilnehmerNachnameValidation extends AbstractValidation<TeilnehmerStaging, Teilnehmer> {

    @Override
    public boolean executeValidation(TeilnehmerStaging teilnehmerStaging, Teilnehmer teilnehmer) {
        if (!isNullOrBlank(teilnehmerStaging.getNachname())) {
            boolean result = NachnameValidation.isNachnameValid(teilnehmerStaging.getNachname());
            if (!result) {
                teilnehmer.addError("nachname", "Ungültiger Nachname angegeben", GlobalUserHolder.IBOSNG_BACKEND);
                return false;
            } else {
                teilnehmer.setNachname(teilnehmerStaging.getNachname());
                return true;
            }
        }
        teilnehmer.addError("nachname", "Ungültiger Nachname angegeben", GlobalUserHolder.IBOSNG_BACKEND);
        return false;
    }
}
