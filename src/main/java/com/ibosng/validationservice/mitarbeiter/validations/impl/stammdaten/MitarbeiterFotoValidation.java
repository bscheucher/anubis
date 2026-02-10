package com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.entities.mitarbeiter.BlobStatus;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Slf4j
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MitarbeiterFotoValidation extends AbstractValidation<StammdatenDto, Stammdaten> {

    @Override
    public boolean executeValidation(StammdatenDto stammdatenDto, Stammdaten stammdaten) {
        if (!isNullOrBlank(stammdatenDto.getFoto()) && BlobStatus.fromValue(stammdatenDto.getFoto()).equals(BlobStatus.NONE)) {
            //stammdatenDto.getErrors().add("foto");
            return false;
        }
        return true;
    }

    @Override
    public boolean shouldValidationRun() {
        return getSources().isEmpty();
    }
}
