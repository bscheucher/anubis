package com.ibosng.validationservice.teilnehmer.validations.imported;

import com.ibosng.dbservice.entities.masterdata.Muttersprache;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.dbservice.services.masterdata.MutterspracheService;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class TeilnehmerMutterspracheValidation extends AbstractValidation<TeilnehmerStaging, Teilnehmer> {

    private final MutterspracheService mutterspracheService;

    @Override
    public boolean executeValidation(TeilnehmerStaging teilnehmerStaging, Teilnehmer teilnehmer) {
        if (!isNullOrBlank(teilnehmerStaging.getMuttersprache())) {
            Muttersprache muttersprache = mutterspracheService.findByName(teilnehmerStaging.getMuttersprache());
            if (muttersprache != null) {
                teilnehmer.setMuttersprache(muttersprache);
                return true;
            }
            teilnehmer.addError("muttersprache", "Ungültige Muttersprache angegeben", GlobalUserHolder.IBOSNG_BACKEND);
            return false;
        }
        // If a value already exists keep it and return it to the FE
        if (teilnehmer.getMuttersprache() != null) {
            teilnehmerStaging.setMuttersprache(teilnehmer.getMuttersprache().getName());
        }
        return true;
    }
}
