package com.ibosng.validationservice.teilnehmer.validations.manual;

import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.entities.masterdata.Anrede;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
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
public class TeilnehmerDtoAnredeValidation extends AbstractValidation<TeilnehmerDto, Teilnehmer> {

    private final AnredeService anredeService;
    private final GlobalUserHolder globalUserHolder;


    @Override
    public boolean executeValidation(TeilnehmerDto teilnehmerDto, Teilnehmer teilnehmer) {

        if (!isNullOrBlank(teilnehmerDto.getAnrede())) {
            Anrede anrede = anredeService.findByName(teilnehmerDto.getAnrede());
            if (anrede != null) {
                teilnehmer.setAnrede(anrede);
                return true;
            }
            teilnehmer.addError("anrede", "Ungültige Anrede angegeben", globalUserHolder.getUsername());
            return false;
        }
        teilnehmer.setAnrede(null);
        return true;
    }
}
