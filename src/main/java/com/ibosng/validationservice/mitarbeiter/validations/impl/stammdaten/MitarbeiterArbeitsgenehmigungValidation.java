package com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.entities.mitarbeiter.BlobStatus;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.dbservice.utils.Parsers.isValidDate;
import static com.ibosng.dbservice.utils.Parsers.parseDate;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class MitarbeiterArbeitsgenehmigungValidation extends AbstractValidation<StammdatenDto, Stammdaten> {

    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(StammdatenDto stammdatenDto, Stammdaten stammdaten) {
        if (!stammdaten.getErrors().stream().filter(datastatus -> datastatus.getError().equals("staatsbuergerschaft")).toList().isEmpty()) {
            return false;
        }
        if (Boolean.FALSE.equals(stammdaten.getStaatsbuergerschaft().getIsInEuEeaCh())) {
            boolean isArbeitsgenehmigungVorhanden = true;
            if (isNullOrBlank(stammdatenDto.getArbeitsgenehmigung())) {
                stammdaten.addError("arbeitsgenehmigung", "Das Feld ist leer", globalUserHolder.getUsername());
                isArbeitsgenehmigungVorhanden = false;
            } else {
                if (stammdaten.getZusatzInfo() != null) {
                    stammdaten.getZusatzInfo().setArbeitsgenehmigungStatus(BlobStatus.fromValue(stammdatenDto.getArbeitsgenehmigungDok()));
                    stammdaten.getZusatzInfo().setArbeitsgenehmigung(stammdatenDto.getArbeitsgenehmigung());
                }
            }
            boolean isArbeitsgenehmigungVorhandenDoc = true;
            if (isNullOrBlank(stammdatenDto.getArbeitsgenehmigungDok())) {
                stammdaten.addError("arbeitsgenehmigungDok", "File nicht hochgeladen", globalUserHolder.getUsername());
                isArbeitsgenehmigungVorhandenDoc = false;
            }

            boolean isGueltigBis = true;
            if (isValidDate(stammdatenDto.getGueltigBis()) && isValidDate(stammdatenDto.getGueltigBis()) && parseDate(stammdatenDto.getGueltigBis()).isBefore(LocalDate.now())) {
                stammdaten.addError("gueltigBis", "Das Feld ist kein valid Datum", globalUserHolder.getUsername());
                isGueltigBis = false;
            } else {
                if (stammdaten.getZusatzInfo() != null) {
                    stammdaten.getZusatzInfo().setGueltigBis(parseDate(stammdatenDto.getGueltigBis()));
                }
            }

            if (isArbeitsgenehmigungVorhanden && isArbeitsgenehmigungVorhandenDoc && isGueltigBis) {
                return true;
            }
        }
        return true;
    }

    @Override
    public boolean shouldValidationRun() {
        return getSources().isEmpty();
    }
}
