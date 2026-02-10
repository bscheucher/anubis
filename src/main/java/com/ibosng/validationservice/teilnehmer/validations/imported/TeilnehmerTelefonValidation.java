package com.ibosng.validationservice.teilnehmer.validations.imported;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.ibosng.dbservice.entities.Land;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
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

/**
 * Required for VHS, eAMS, MDLC, OEIF
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TeilnehmerTelefonValidation extends AbstractValidation<TeilnehmerStaging, Teilnehmer> {
    private static final Pattern STARTS_WITH_PLUS_PATTERN = Pattern.compile("^\\+");

    private final LandService landService;
    private final TelefonService telefonService;

    @Override
    public boolean executeValidation(TeilnehmerStaging teilnehmerStaging, Teilnehmer teilnehmer) {
        String phoneNumber = "";
        if (!isNullOrBlank(teilnehmerStaging.getVorwahl()) && (!isNullOrBlank(teilnehmerStaging.getTelefonNummer()) || isTelefonSingle(teilnehmerStaging.getTelefon()))) {
            if (!isNullOrBlank(teilnehmerStaging.getTelefonNummer())) {
                phoneNumber = teilnehmerStaging.getVorwahl() + teilnehmerStaging.getTelefonNummer();
            } else if (isTelefonSingle(teilnehmerStaging.getTelefon())) {
                phoneNumber = teilnehmerStaging.getVorwahl() + teilnehmerStaging.getTelefon();
            }
            Phonenumber.PhoneNumber parseNumber;
            if (isNullOrBlank(teilnehmerStaging.getLandesvorwahl())) {
                parseNumber = parseNumber(phoneNumber, false, null);
            } else {
                if (teilnehmerStaging.getLandesvorwahl().equals("0")) {
                    parseNumber = parseNumber(phoneNumber, false, null);
                } else {
                    String landesvorwahl = teilnehmerStaging.getLandesvorwahl(); // Get the original code
                    if (!landesvorwahl.startsWith("+")) {
                        landesvorwahl = "+" + landesvorwahl.replaceFirst("^0{1,2}", "");
                    }
                    parseNumber = parseNumber(phoneNumber, true, landesvorwahl);
                }
            }
            return addTelefonToTeilnehmer(teilnehmer, parseNumber, null);
        } else if (!isNullOrBlank(teilnehmerStaging.getTelefon())) {
            String telefonStaging = teilnehmerStaging.getTelefon();

            // Split the string by a delimiter (e.g., comma) to handle multiple numbers
            String[] phoneSegments = telefonStaging.split(",");


            for (String segment : phoneSegments) {
                segment = segment.trim(); // Trim whitespace
                String[] parts = segment.split("\\s*\\(", 2); // Split by "("
                phoneNumber = parts[0].trim();
                String label = parts.length > 1 ? parts[1].replaceAll("\\)", "").trim() : ""; // Remove ")" and trim

                if (!processPhoneNumberVHS(phoneNumber, label, teilnehmer)) {
                    return false;
                }
            }
            return true;
        }
        teilnehmer.addError("telefon", "Ungültige Telefonnummer angegeben", GlobalUserHolder.IBOSNG_BACKEND);
        return false;
    }

    private boolean isTelefonSingle(String telefon) {
        if (!isNullOrBlank(telefon)) {
            String[] phoneSegments = telefon.split(",");
            return phoneSegments.length == 1;
        }
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
            teilnehmer.addError("telefon", "Ungültige Telefonnummer angegeben", GlobalUserHolder.IBOSNG_BACKEND);
            return false;
        }
        Land land = getLand("+" + parsedTelefon.getCountryCode());
        if (land == null) {
            log.warn(String.format("Country code %s is not found for telefon for teilnehmer vorname %s and nachname %s, telefon will not be set", parsedTelefon.getCountryCode(), getNotNullStringValue(teilnehmer.getVorname()), getNotNullStringValue(teilnehmer.getNachname())));
            teilnehmer.addError("telefon", "Ungültige Telefonnummer angegeben", GlobalUserHolder.IBOSNG_BACKEND);
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

    @Override
    public boolean shouldValidationRun() {
        List<TeilnehmerSource> allowedSources = List.of(
                TeilnehmerSource.VHS,
                TeilnehmerSource.EAMS,
                TeilnehmerSource.VHS_EAMS,
                TeilnehmerSource.OEIF,
                TeilnehmerSource.EAMS_STANDALONE,
                TeilnehmerSource.MANUAL,
                TeilnehmerSource.SYNC_SERVICE,
                TeilnehmerSource.TN_ONBOARDING);

        return getSources().stream().anyMatch(allowedSources::contains);
    }
}