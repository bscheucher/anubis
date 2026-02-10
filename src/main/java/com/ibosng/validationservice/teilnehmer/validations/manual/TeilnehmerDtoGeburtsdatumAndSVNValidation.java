package com.ibosng.validationservice.teilnehmer.validations.manual;

import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import com.ibosng.validationservice.validations.GeburtsdatumAndSVNValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class TeilnehmerDtoGeburtsdatumAndSVNValidation extends AbstractValidation<TeilnehmerDto, Teilnehmer> {

    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(TeilnehmerDto teilnehmerDto, Teilnehmer teilnehmer) {
        if (teilnehmer.getErrors().stream()
                .noneMatch(error -> "svNummer".equals(error.getError())) && teilnehmer.getErrors().stream()
                .noneMatch(error -> "geburtsdatum".equals(error.getError()))) {
            boolean geburtsDatumAndSVNMatch = GeburtsdatumAndSVNValidation.validateGeburtsdatumAndSVN(teilnehmerDto.getSvNummer(), teilnehmer.getGeburtsdatum() != null ? teilnehmer.getGeburtsdatum() : null);
            if (!geburtsDatumAndSVNMatch) {
                teilnehmer.addError("svNummer", "Ungültige SV-Nummer angegeben", globalUserHolder.getUsername());
                teilnehmer.addError("geburtsdatum", "Ungültiges Geburtsdatum angegeben", globalUserHolder.getUsername());
                return false;
            }
        }
        return true;
    }
}
