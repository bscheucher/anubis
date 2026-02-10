package com.ibosng.validationservice.teilnehmer.validations.imported;

import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import com.ibosng.validationservice.validations.SVNValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

/**
 * Required for VHS, eAMS, MDLC
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class TeilnehmerSVNValidation extends AbstractValidation<TeilnehmerStaging, Teilnehmer> {

    @Override
    public boolean executeValidation(TeilnehmerStaging teilnehmerStaging, Teilnehmer teilnehmer) {

        if (isNullOrBlank(teilnehmerStaging.getSvNummer())) {
            teilnehmer.addError("svNummer", "Das Feld ist leer", GlobalUserHolder.IBOSNG_BACKEND);
            return false;
        }

        String svn = SVNValidation.validateSVN(teilnehmerStaging.getSvNummer(), teilnehmer.getGeburtsdatum() != null ? teilnehmer.getGeburtsdatum() : null);
        if (svn != null) {
            teilnehmer.setSvNummer(svn);
            return true;
        }
        teilnehmer.addError("svNummer", "Ungültige SV-Nummer angegeben", GlobalUserHolder.IBOSNG_BACKEND);
        return false;
    }
}
