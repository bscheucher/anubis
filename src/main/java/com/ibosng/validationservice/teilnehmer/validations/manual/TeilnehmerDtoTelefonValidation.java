package com.ibosng.validationservice.teilnehmer.validations.manual;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.entities.Land;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.telefon.Telefon;
import com.ibosng.dbservice.entities.telefon.TelefonStatus;
import com.ibosng.dbservice.services.LandService;
import com.ibosng.dbservice.services.TelefonService;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ibosng.dbservice.utils.Helpers.findFirstObject;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.validationservice.utils.Constants.AUSTRIA_LAND_CODE;
import static com.ibosng.validationservice.utils.Constants.VALIDATION_SERVICE;
import static com.ibosng.validationservice.utils.ValidationHelpers.getNotNullStringValue;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
@RequiredArgsConstructor
public class TeilnehmerDtoTelefonValidation extends AbstractValidation<TeilnehmerDto, Teilnehmer> {
    private static final Pattern STARTS_WITH_PLUS_PATTERN = Pattern.compile("^\\+");
    private final LandService landService;
    private final TelefonService telefonService;

    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(TeilnehmerDto teilnehmerDto, Teilnehmer teilnehmer) {
        return validatePhone(teilnehmerDto.getTelefon(), teilnehmer);
    }

    private boolean validatePhone(String telefon, Teilnehmer teilnehmer) {
        if (!isNullOrBlank(telefon)) {

            // Split the string by a delimiter (e.g., comma) to handle multiple numbers
            String[] phoneSegments = telefon.split(",");

            for (String segment : phoneSegments) {
                segment = segment.trim(); // Trim whitespace
                String[] parts = segment.split("\\s*\\(", 2); // Split by "("
                String phoneNumber = parts[0].trim();
                String label = parts.length > 1 ? parts[1].replaceAll("\\)", "").trim() : ""; // Remove ")" and trim

                if (!processPhoneNumberVHS(phoneNumber, label, teilnehmer)) {
                    return false;
                }
            }
            return true;
        }
        teilnehmer.addError("telefon", "Ungültige Telefonnummer angegeben", globalUserHolder.getUsername());
        return false;
    }

    private boolean processPhoneNumberVHS(String telefonStaging, String label, Teilnehmer teilnehmer) {
        Matcher matcher = STARTS_WITH_PLUS_PATTERN.matcher(telefonStaging);
        Phonenumber.PhoneNumber parsedTelefon;
        if (matcher.find()) {
            parsedTelefon = parseNumber(telefonStaging, true, null);
        } else {
            parsedTelefon = parseNumber(telefonStaging, false, null);
        }
        return addTelefonToTeilnehmer(teilnehmer, parsedTelefon, label);
    }

    private boolean addTelefonToTeilnehmer(Teilnehmer teilnehmer, Phonenumber.PhoneNumber parsedTelefon, String label) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        if (parsedTelefon == null || !phoneUtil.isValidNumber(parsedTelefon)) {
            teilnehmer.addError("telefon", "Ungültige Telefonnummer angegeben", globalUserHolder.getUsername());
            return false;
        }
        Land land = getLand("+" + parsedTelefon.getCountryCode());
        if (land == null) {
            log.warn("Country code {} is not found for telefon for teilnehmer vorname {} and nachname {}, telefon will not be set", parsedTelefon.getCountryCode(), getNotNullStringValue(teilnehmer.getVorname()), getNotNullStringValue(teilnehmer.getNachname()));
            return false;
        }
        Telefon telefon = new Telefon();
        if (label != null && !label.isEmpty()) {
            telefon.setOwner(label);
        }
        telefon.setLand(land);
        telefon.setTelefonnummer(parsedTelefon.getNationalNumber());
        telefon.setStatus(TelefonStatus.ACTIVE);
        telefon.setCreatedBy(VALIDATION_SERVICE);
        if (!teilnehmer.getTelefons().contains(telefon)) {
            saveAndAddTelefon(teilnehmer, telefon);
        }
        return true;
    }

    private void saveAndAddTelefon(Teilnehmer teilnehmer, Telefon telefon) {
        List<Telefon> telefonsToUpdate = new ArrayList<>(teilnehmer.getTelefons());
        telefonsToUpdate.forEach(tel -> tel.setStatus(TelefonStatus.INACTIVE));

        telefonService.saveAll(telefonsToUpdate);

        telefonsToUpdate.add(telefon);
        telefon = telefonService.save(telefon);
        if (telefon != null) {
            teilnehmer.addTelefon(telefon);
        }
    }

    private Phonenumber.PhoneNumber parseNumber(String telefonStaging, boolean isCompleteNumber, String countryCodeString) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber parsedNumber;
        telefonStaging = telefonStaging.replaceAll("\\s+", "");
        try {
            if (isCompleteNumber) {
                if (!isNullOrBlank(countryCodeString)) {
                    Land land = getLand(countryCodeString);
                    String landCode = land != null ? land.getLandCode() : null;
                    parsedNumber = phoneUtil.parse(telefonStaging, landCode);
                } else {
                    parsedNumber = phoneUtil.parse(telefonStaging, null);
                }
            } else {
                parsedNumber = phoneUtil.parse(telefonStaging, AUSTRIA_LAND_CODE);
                if (phoneUtil.isValidNumber(parsedNumber)) {
                    return parsedNumber;
                } else {
                    for (String countryCode : phoneUtil.getSupportedRegions()) {
                        parsedNumber = phoneUtil.parse(telefonStaging, countryCode);
                        if (phoneUtil.isValidNumber(parsedNumber)) {
                            return parsedNumber;
                        }
                    }
                }
            }
        } catch (NumberParseException e) {
            log.warn("NumberParseException was thrown: " + e.getMessage());
            return null;
        }
        return parsedNumber;
    }

    private Land getLand(String countryCode) {
        List<Land> laender = landService.findByTelefonvorwahl(countryCode);
        return findFirstObject(laender, new HashSet<>(List.of(countryCode)), "Land");
    }
}