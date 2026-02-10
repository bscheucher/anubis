package com.ibosng.validationservice.teilnehmer.validations.manual;

import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import com.ibosng.validationservice.validations.SVNValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class TeilnehmerDtoSVNValidation extends AbstractValidation<TeilnehmerDto, Teilnehmer> {

    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(TeilnehmerDto teilnehmerDto, Teilnehmer teilnehmer) {

        if (isNullOrBlank(teilnehmerDto.getSvNummer())) {
            teilnehmer.addError("svNummer", "Das Feld ist leer", globalUserHolder.getUsername());
            return false;
        }

        String svn = SVNValidation.validateSVN(teilnehmerDto.getSvNummer(),
                teilnehmer.getGeburtsdatum() != null ? teilnehmer.getGeburtsdatum() : null);
        if (svn != null) {
            teilnehmer.setSvNummer(svn);
            return true;
        }
        teilnehmer.addError("svNummer", "Ungültige SV-Nummer angegeben", globalUserHolder.getUsername());
        return false;
    }
}
