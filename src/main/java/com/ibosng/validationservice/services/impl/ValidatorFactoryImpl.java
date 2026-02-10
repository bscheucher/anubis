package com.ibosng.validationservice.services.impl;

import com.ibosng.dbservice.dtos.ZeitbuchungenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.UnterhaltsberechtigteDto;
import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.VordienstzeitenDto;
import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.dtos.zeiterfassung.ZeiterfassungTransferDto;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.validationservice.AbstractValidator;
import com.ibosng.validationservice.SingleEntityAbstractValidator;
import com.ibosng.validationservice.config.ValidationsConfig;
import com.ibosng.validationservice.services.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidatorFactoryImpl implements ValidatorFactory {

    private final ValidationsConfig validationsConfig;

    @Override
    public <T, V> AbstractValidator<T, V> createValidator(String identifier, Class<T> typeT, Class<V> typeV) {
        if (TeilnehmerStaging.class.equals(typeT)) {
            AbstractValidator<T, V> teilnehmerValidator = (AbstractValidator<T, V>) validationsConfig.getImportedTeilnehmerValidator();
            teilnehmerValidator.setIdentifier(identifier);
            return teilnehmerValidator;
        }
        throw new IllegalArgumentException("Unsupported type: " + typeT);
    }


    @Override
    public <T, V> SingleEntityAbstractValidator<T, V> createSingleEntityValidator(Class<T> typeT, Class<V> typeV) {

        if (UnterhaltsberechtigteDto.class.equals(typeT)) {
            return (SingleEntityAbstractValidator<T, V>) validationsConfig.getMitarbeiterUnterhaltsBerechtigteValidator();
        }
        if (VordienstzeitenDto.class.equals(typeT)) {
            return (SingleEntityAbstractValidator<T, V>) validationsConfig.getMitarbeiterVordienstzeitenValidator();
        }
        if (StammdatenDto.class.equals(typeT)) {
            return (SingleEntityAbstractValidator<T, V>) validationsConfig.getMitarbeiterStammdatenValidator();
        }

        if (VertragsdatenDto.class.equals(typeT)) {
            return (SingleEntityAbstractValidator<T, V>) validationsConfig.getMitarbeiterVertragsdatenValidator();
        }
        if (ZeiterfassungTransferDto.class.equals(typeT)) {
            return (SingleEntityAbstractValidator<T, V>) validationsConfig.getZeiterfassungValidator();
        }
        if (ZeitbuchungenDto.class.equals(typeT)) {
            return (SingleEntityAbstractValidator<T, V>) validationsConfig.getZeitbuchungValidator();
        }

        if (TeilnehmerDto.class.equals(typeT)) {
            return (SingleEntityAbstractValidator<T, V>) validationsConfig.getTeilnehmerValidator();
        }

        throw new IllegalArgumentException("Unsupported type: " + typeT);
    }

}

