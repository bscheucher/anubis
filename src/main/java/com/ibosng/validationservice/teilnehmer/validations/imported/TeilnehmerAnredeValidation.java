package com.ibosng.validationservice.teilnehmer.validations.imported;

import com.ibosng._config.GlobalUserHolder;
import com.ibosng.dbservice.entities.masterdata.Anrede;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.dbservice.services.masterdata.AnredeService;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class TeilnehmerAnredeValidation extends AbstractValidation<TeilnehmerStaging, Teilnehmer> {

    private final AnredeService anredeService;

    @Override
    public boolean executeValidation(TeilnehmerStaging teilnehmerStaging, Teilnehmer teilnehmer) {

        if (!isNullOrBlank(teilnehmerStaging.getAnrede())) {
            Anrede anrede = anredeService.findByName(teilnehmerStaging.getAnrede());
            if (anrede != null) {
                teilnehmer.setAnrede(anrede);
                return true;
            }
            teilnehmer.addError("anrede", "Ungültige Anrede angegeben", GlobalUserHolder.IBOSNG_BACKEND);
            return false;
        }
        teilnehmer.setAnrede(null);
        return true;
    }

    @Override
    public boolean shouldValidationRun() {
        List<TeilnehmerSource> allowedSources = List.of(
                TeilnehmerSource.EAMS,
                TeilnehmerSource.VHS_EAMS,
                TeilnehmerSource.OEIF,
                TeilnehmerSource.EAMS_STANDALONE,
                TeilnehmerSource.MANUAL,
                TeilnehmerSource.SYNC_SERVICE,
                TeilnehmerSource.TN_ONBOARDING);

        return getSources().stream().anyMatch(allowedSources::contains);
    }
}
