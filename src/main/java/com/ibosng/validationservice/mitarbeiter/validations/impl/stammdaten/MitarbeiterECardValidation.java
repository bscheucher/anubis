package com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.entities.mitarbeiter.BlobStatus;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterType;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Slf4j
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class MitarbeiterECardValidation extends AbstractValidation<StammdatenDto, Stammdaten> {

    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(StammdatenDto stammdatenDto, Stammdaten stammdaten) {
        if (stammdaten.getPersonalnummer().getMitarbeiterType().equals(MitarbeiterType.TEILNEHMER)) {
            if (!isNullOrBlank(stammdatenDto.getEcard())) {
                stammdaten.setEcardStatus(BlobStatus.fromValue(stammdatenDto.getEcard()));
            }
            return true;
        }
        if (isNullOrBlank(stammdatenDto.getEcard()) ||
                (!isNullOrBlank(stammdatenDto.getEcard()) && BlobStatus.fromValue(stammdatenDto.getEcard()).equals(BlobStatus.NONE))) {
            stammdaten.addError("ecard", "Das Feld ist leer", globalUserHolder.getUsername());
            return false;
        }
        stammdaten.setEcardStatus(BlobStatus.fromValue(stammdatenDto.getEcard()));
        return true;
    }
}
