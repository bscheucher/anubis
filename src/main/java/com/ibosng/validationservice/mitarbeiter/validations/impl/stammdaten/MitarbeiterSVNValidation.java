package com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.validationservice.Validation;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.validations.SVNValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class MitarbeiterSVNValidation implements Validation<StammdatenDto, Stammdaten> {
    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(StammdatenDto stammdatenDto, Stammdaten stammdaten) {

        if (isNullOrBlank(stammdatenDto.getSvnr())) {
            stammdaten.addError("svnr", "Das Feld ist leer", globalUserHolder.getUsername());
            return false;
        }

        String validatedSvnr = SVNValidation.validateSVN(
                stammdatenDto.getSvnr(),
                stammdaten.getGeburtsdatum() != null ? stammdaten.getGeburtsdatum() : null
        );

        if (validatedSvnr != null) {
            stammdaten.setSvnr(validatedSvnr);
            return true;
        }
        stammdaten.addError("svnr", "Ungültige SVNR", globalUserHolder.getUsername());
        return false;
    }
}
