package com.ibosng.validationservice.utils;

import com.ibosng.dbservice.entities.*;
import com.ibosng.dbservice.entities.masterdata.Titel;
import com.ibosng.dbservice.entities.mitarbeiter.Arbeitszeiten;
import com.ibosng.dbservice.entities.mitarbeiter.ArbeitszeitenInfo;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.telefon.Telefon;
import com.ibosng.dbservice.entities.telefon.TelefonStatus;
import com.ibosng.dbservice.services.InternationalPlzService;
import com.ibosng.dbservice.services.LandService;
import com.ibosng.validationservice.validations.OrtValidation;
import com.ibosng.validationservice.validations.PLZValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ibosng.dbservice.utils.Parsers.*;
import static com.ibosng.validationservice.utils.Constants.AUSTRIA_VORWAHL;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidationHelpers {
    private static final String[] TIME_PATTERNS = {"hh:mm", "HH:mm", "h:mm a", "hh:mm:ss a", "H:mm:ss"};

    private static final String[] DATE_PATTERNS = {"dd.MM.yyyy", "yyyy-MM-dd", "ddMMyy"};

    private final PLZValidation plzValidation;
    private final OrtValidation ortValidation;
    private final LandService landService;
    private final InternationalPlzService internationalPlzService;

    public static boolean isNullOrBalnkOrZero(String string) {
        return isNullOrBlank(string) || Double.parseDouble(string) == 0;
    }

    public static Titel findValidTitle(List<Titel> dbTitles, String title) {
        for (Titel dbTitle : dbTitles) {
            String dbTitleClean = cleanTitleString(dbTitle.getName());
            if (dbTitleClean.equalsIgnoreCase(title)) {
                return dbTitle;
            }
        }
        return null;
    }

    public static String findValidTitleString(List<String> dbTitles, String title) {
        for (String dbTitle : dbTitles) {
            String dbTitleClean = cleanTitleString(dbTitle);
            if (dbTitleClean.equalsIgnoreCase(title)) {
                return dbTitle;
            }
        }
        return null;
    }

    public static String cleanTitleString(String titel) {
        return titel.replace(".", "").replace("_", " ").trim();
    }

    public static boolean isDateInWorkingHours(String timeString) {
        if (isValidTime(timeString)) {
            LocalTime time = parseTime(timeString);
            if (time.isAfter(LocalTime.of(5, 59, 59)) && time.isBefore(LocalTime.of(22, 0, 1))) {
                return true;
            }
            log.info("Time {} is not in working hours.", timeString);
            return false;
        }
        log.info("String {} could not be parsed into LocalTime.", timeString);
        return false;
    }

    public boolean isTheSameAdresse(Adresse savedAdresse, String strasseInput, String plzInput, String ortInput) {
        Adresse adresse = new Adresse();
        adresse.setStrasse(strasseInput);
        if (!isNullOrBlank(plzInput)) {
            Plz plz = plzValidation.validatePlz(plzInput);
            if (plz != null) {
                adresse.setPlz(plz);
            } else {
                return false;
            }
        } else {
            return false;
        }

        if (!isNullOrBlank(ortInput)) {
            String ort = ortValidation.validateOrt(ortInput);
            if (ort != null) {
                adresse.setOrt(ort);
            } else {
                return false;
            }
        } else {
            return false;
        }
        return adresse.equals(savedAdresse);
    }

    public boolean isTheSameAdresse(Adresse savedAdresse, String strasseInput, String plzInput, String ortInput, String landInput) {
        Adresse adresse = new Adresse();
        if (!isNullOrBlank(strasseInput)) {
            adresse.setStrasse(strasseInput);
        } else {
            return false;
        }
        boolean isAustria;
        Land land;
        if (!isNullOrBlank(landInput)) {
            land = landService.findByLandName(landInput);
            if (land != null) {
                isAustria = land.equals(landService.findByTelefonvorwahl(AUSTRIA_VORWAHL).get(0));
                adresse.setLand(land);
            } else {
                return false;
            }
        } else {
            return false;
        }

        if (!isNullOrBlank(ortInput)) {
            adresse.setOrt(ortInput);
        } else {
            return false;
        }

        if (!isNullOrBlank(plzInput)) {
            BasePlz plz;
            if (isAustria) {
                plz = plzValidation.validatePlz(plzInput);
                if (plz != null) {
                    adresse.setPlz(plz);
                } else {
                    return false;
                }
            } else {
                plz = internationalPlzService.findPlzByPlzOrtLand(plzInput, ortInput, land.getId()).orElse(null);
                if (plz != null) {
                    adresse.setPlz(plz);
                } else {
                    return false;
                }
            }

        } else {
            return false;
        }


        return adresse.equals(savedAdresse);
    }

    public static boolean isDateInFuture(LocalDate date) {
        return date.isAfter(LocalDate.now());
    }

    public static Arbeitszeiten createNewArbeitzeiten(ArbeitszeitenInfo arbeitszeitenInfo, String changedBy) {
        Arbeitszeiten arbeitszeiten = new Arbeitszeiten();
        arbeitszeiten.setCreatedBy(changedBy);
        arbeitszeiten.setArbeitszeitenInfo(arbeitszeitenInfo);
        return arbeitszeiten;
    }

    public static Adresse createNewAdresse(String changedBy) {
        Adresse adresse = new Adresse();
        adresse.setStatus(Status.ACTIVE);
        adresse.setCreatedBy(changedBy);
        return adresse;
    }

    public static ZusatzInfo createNewZusatzInfo(String changedBy) {
        ZusatzInfo zusatzInfo = new ZusatzInfo();
        zusatzInfo.setStatus(MitarbeiterStatus.NEW);
        zusatzInfo.setCreatedBy(changedBy);
        zusatzInfo.setCreatedOn(getLocalDateNow());
        return zusatzInfo;
    }

    public static Telefon createNewTelefon(String changedBy) {
        Telefon telefon = new Telefon();
        telefon.setStatus(TelefonStatus.ACTIVE);
        telefon.setCreatedBy(changedBy);
        return telefon;
    }

    public static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static boolean isUnknownBirthday(String svn) {
        String regexPattern = "^(.{6})13(.*)$";
        Pattern pattern = Pattern.compile(regexPattern);

        // Create a Matcher to match the input string against the pattern
        Matcher matcher = pattern.matcher(svn);
        // Replace the last 3 and 4 characters with "01"
        return matcher.matches();
    }

    public static String getNotNullStringValue(String string) {
        return !isNullOrBlank(string) ? string : "";
    }

    public static List<String> findMatchingOrts(List<String> list, String keyword) {
        if (keyword == null) {
            keyword = "";
        }
        List<String> matches = new ArrayList<>();

        // Define a regex that matches the keyword as a whole word or as a prefix followed by a non-alphabetic character
        String regex = "\\b" + Pattern.quote(keyword) + "(\\b|[^a-zA-Z])";
        Pattern pattern = Pattern.compile(regex);

        // Iterate through the list and find matches
        for (String entry : list) {
            Matcher matcher = pattern.matcher(entry);
            if (matcher.find()) {
                matches.add(entry); // Add the matching entry to the result list
            }
        }

        return matches;
    }
}
