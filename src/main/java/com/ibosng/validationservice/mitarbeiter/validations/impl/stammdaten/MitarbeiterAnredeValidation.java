package com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.entities.masterdata.Anrede;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.services.masterdata.AnredeService;
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
public class MitarbeiterAnredeValidation extends AbstractValidation<StammdatenDto, Stammdaten> {

    private final AnredeService anredeService;
    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(StammdatenDto stammdatenDto, Stammdaten stammdaten) {
        if (!isNullOrBlank(stammdatenDto.getAnrede())) {
            Anrede anrede = anredeService.findByName(stammdatenDto.getAnrede());
            if (anrede != null) {
                stammdaten.setAnrede(anrede);
                return true;
            }
        }
        stammdaten.addError("anrede", "Das Feld ist leer", globalUserHolder.getUsername());
        return false;
    }
}
