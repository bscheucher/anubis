package com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.entities.Land;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.entities.telefon.Telefon;
import com.ibosng.dbservice.entities.telefon.TelefonStatus;
import com.ibosng.dbservice.services.LandService;
import com.ibosng.dbservice.services.TelefonService;
import com.ibosng.validationservice.Validation;
import com.ibosng._config.GlobalUserHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.validationservice.utils.Constants.AUSTRIA_LAND_CODE;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Slf4j
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class MitarbeiterMobilnummerValidation implements Validation<StammdatenDto, Stammdaten> {

    private final LandService landService;
    private final TelefonService telefonService;
    private final GlobalUserHolder globalUserHolder;


    @Override
    public boolean executeValidation(StammdatenDto stammdatenDto, Stammdaten stammdaten) {
        if (!isNullOrBlank(stammdatenDto.getMobilnummer())) {
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            try {
                Phonenumber.PhoneNumber parsedNumber = phoneUtil.parse(stammdatenDto.getMobilnummer(), AUSTRIA_LAND_CODE);
                Telefon telefon = getMobilnummer(stammdaten, parsedNumber);
                stammdaten.setMobilnummer(telefon);
                return true;
            } catch (NumberParseException e) {
                log.error("NumberParseException was thrown: " + e.getMessage());
                stammdaten.addError("mobilnummer", "Ungültige mobilnummer", globalUserHolder.getUsername());
                return false;
            }
        }
        stammdaten.addError("mobilnummer", "Das Feld ist leer", globalUserHolder.getUsername());
        return false;
    }

    private Telefon getMobilnummer(Stammdaten stammdaten, Phonenumber.PhoneNumber parsedNumber) {
        Telefon telefon = new Telefon();
        Land land = landService.getLandFromCountryCode("+" + parsedNumber.getCountryCode());
        telefon.setLand(land);
        telefon.setTelefonnummer(parsedNumber.getNationalNumber());
        telefon.setStatus(TelefonStatus.ACTIVE);
        telefon.setCreatedBy(globalUserHolder.getUsername());
        if (stammdaten.getMobilnummer() != null && stammdaten.getMobilnummer().equals(telefon)) {
            return stammdaten.getMobilnummer();
        } else if (stammdaten.getMobilnummer() != null) {
            stammdaten.getMobilnummer().setStatus(TelefonStatus.INACTIVE);
            telefonService.save(stammdaten.getMobilnummer());
        }
        return telefonService.save(telefon);
    }
}
