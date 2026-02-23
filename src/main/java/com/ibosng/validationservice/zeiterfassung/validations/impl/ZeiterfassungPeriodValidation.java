package com.ibosng.validationservice.zeiterfassung.validations.impl;


import com.ibosng.dbservice.dtos.zeiterfassung.ZeiterfassungTransferDto;
import com.ibosng.dbservice.entities.zeiterfassung.ZeiterfassungTransfer;
import com.ibosng.validationservice.Validation;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static com.ibosng.dbservice.utils.Parsers.parseDate;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ZeiterfassungPeriodValidation implements Validation<ZeiterfassungTransferDto, ZeiterfassungTransfer> {

    @Override
    public boolean executeValidation(ZeiterfassungTransferDto zeiterfassungDto, ZeiterfassungTransfer zeiterfassung) {
        LocalDate datumBis = parseDate(zeiterfassungDto.getDatumBis());
        LocalDate datumVon = parseDate(zeiterfassungDto.getDatumVon());
        if (datumVon == null) {
            zeiterfassungDto.getErrorsMap().put("datumVon", "Das Feld ist leer");
            return false;
        }
        if (datumBis == null) {
            zeiterfassungDto.getErrorsMap().put("datumBis", "Das Feld ist leer");
            return false;
        }
        if (datumVon.isAfter(datumBis)) {
            zeiterfassungDto.getErrorsMap().put("datumBis", "Das Datum Bis muss größer oder gleich dem Datum Von sein");
            zeiterfassungDto.getErrorsMap().put("datumVon", "Das Datum Von muss kleiner oder gleich dem Datum Bis sein");
            return false;
        }
        zeiterfassung.setDatumVon(datumVon);
        zeiterfassung.setDatumBis(datumBis);

        return true;
    }
}
