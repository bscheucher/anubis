package com.ibosng.validationservice.mitarbeiter.validations.impl.vertragsdaten;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.ibosng.dbibosservice.dtos.DienstortDto;
import com.ibosng.dbibosservice.services.mitarbeiter.ArbeitsvertragIbosService;
import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.entities.Dienstort;
import com.ibosng.dbservice.entities.Land;
import com.ibosng.dbservice.entities.Plz;
import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.entities.telefon.Telefon;
import com.ibosng.dbservice.entities.telefon.TelefonStatus;
import com.ibosng.dbservice.services.AdresseService;
import com.ibosng.dbservice.services.DienstortService;
import com.ibosng.dbservice.services.LandService;
import com.ibosng.dbservice.services.TelefonService;
import com.ibosng.validationservice.Validation;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.validations.EmailValidation;
import com.ibosng.validationservice.validations.OrtValidation;
import com.ibosng.validationservice.validations.PLZValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.ibosng.validationservice.utils.Constants.AUSTRIA_LAND_CODE;
import static com.ibosng.validationservice.utils.ValidationHelpers.createNewAdresse;
import static com.ibosng.validationservice.utils.ValidationHelpers.createNewTelefon;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Slf4j
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class MitarbeiterDienstortValidation implements Validation<VertragsdatenDto, Vertragsdaten> {

    private final DienstortService dienstortService;
    private final ArbeitsvertragIbosService arbeitsvertragIbosService;
    private final PLZValidation plzValidation;
    private final OrtValidation ortValidation;
    private final LandService landService;
    private final AdresseService adresseService;
    private final TelefonService telefonService;
    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(VertragsdatenDto vertragsdatenDto, Vertragsdaten vertragsdaten) {
        Dienstort dienstort = null;
        if (!isNullOrBlank(vertragsdatenDto.getDienstort())) {
            List<Dienstort> dienstorts = dienstortService.findAllByName(vertragsdatenDto.getDienstort());
            if (!dienstorts.isEmpty()) {
                dienstort = dienstorts.get(0);
                vertragsdaten.setDienstort(dienstort);
                return true;
            } else {
                vertragsdaten.addError("dienstort", String.format("Kein Dienstort mit dem Name %s konnte gefunden werden", vertragsdatenDto.getDienstort()), globalUserHolder.getUsername());
                return false;
            }
        }
        vertragsdaten.addError("dienstort", "Das Feld ist leer", globalUserHolder.getUsername());
        return false;
    }

    private Dienstort mapDienstortDtoToDienstort(Dienstort dienstort, List<DienstortDto> dienstortDtoList) {
        DienstortDto dienstortDto = dienstortDtoList.get(0);
        dienstort.setStatus(Status.ACTIVE);
        dienstort.setChangedBy(globalUserHolder.getUsername());
        dienstort.setAdresse(adresseService.save(createNewAdresse(globalUserHolder.getUsername())));

        if (EmailValidation.isEmailValid(dienstortDto.getEmail())) {
            dienstort.setEmail(dienstortDto.getEmail());
        }
        Plz plz = plzValidation.validatePlz(dienstortDto.getPlz());
        if (plz != null) {
            dienstort.getAdresse().setPlz(plz);
        }
        Land land = landService.findByLandCode(dienstortDto.getLand()).get(0);
        if (land != null) {
            dienstort.getAdresse().setLand(land);
        } else {
            land = landService.findByLandCode(AUSTRIA_LAND_CODE).get(0);
        }
        String ort = ortValidation.validateOrt(dienstortDto.getOrt());
        if (!isNullOrBlank(ort)) {
            dienstort.getAdresse().setOrt(ort);
        }
        if (!isNullOrBlank(dienstortDto.getStrasse())) {
            dienstort.getAdresse().setStrasse(dienstortDto.getStrasse());
        }
        validateAndSetTelefon(dienstort, dienstortDto, land);
        dienstort = dienstortService.save(dienstort);
        return dienstort;
    }

    private void validateAndSetTelefon(Dienstort dienstort, DienstortDto dienstortDto, Land land) {
        Telefon telefon = createNewTelefon(globalUserHolder.getUsername());
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            String cleanTelefon = dienstortDto.getTelefon().replaceFirst("^0+", "").replaceAll("\\s+", "");
            Phonenumber.PhoneNumber parsedNumber = phoneUtil.parse(cleanTelefon, land != null ? land.getLandCode() : AUSTRIA_LAND_CODE);
            if (land != null) {
                telefon.setLand(land);
            }
            telefon.setTelefonnummer(parsedNumber.getNationalNumber());
            telefon.setStatus(TelefonStatus.ACTIVE);
            telefon.setCreatedBy(globalUserHolder.getUsername());
            dienstort.setTelefon(telefonService.save(telefon));
        } catch (NumberParseException e) {
            log.error("Could not parse telefon from Dienstort was thrown: " + e.getMessage());
        }
    }
}
