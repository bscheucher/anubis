package com.ibosng.validationservice.services.impl;

import com.ibosng.dbservice.dtos.mitarbeiter.UnterhaltsberechtigteDto;
import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.mitarbeiter.Unterhaltsberechtigte;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.services.mitarbeiter.UnterhaltsberechtigteService;
import com.ibosng.dbservice.services.mitarbeiter.VertragsdatenService;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.services.UnterhaltsberechtigteValidatorService;
import com.ibosng.validationservice.services.VertragsdatenValidatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.ibosng.dbservice.utils.Parsers.*;
import static com.ibosng.validationservice.utils.Parsers.isLong;

@Service
@Slf4j
@RequiredArgsConstructor
public class UnterhaltsberechtigteValidatorServiceImpl implements UnterhaltsberechtigteValidatorService {

    private final UnterhaltsberechtigteService unterhaltsberechtigteService;
    private final VertragsdatenService vertragsdatenService;
    private final GlobalUserHolder globalUserHolder;
    private final VertragsdatenValidatorService vertragsdatenValidatorService;

    @Override
    public Unterhaltsberechtigte getUnterhaltsberechtige(UnterhaltsberechtigteDto unterhaltsberechtigteDto) {
        //Editing an existing entry
        if (unterhaltsberechtigteDto.getId() != null) {
            Optional<Unterhaltsberechtigte> unterhaltsberechtigte = unterhaltsberechtigteService.findById(unterhaltsberechtigteDto.getId());
            if (unterhaltsberechtigte.isEmpty()) {
                return null;
            }
            unterhaltsberechtigte.get().setErrors(new ArrayList<>());
            unterhaltsberechtigte.get().setChangedOn(LocalDateTime.now());
            return unterhaltsberechtigte.get();
        }
        // Create a new entry
        return createNewUnterhaltsberechtigte(unterhaltsberechtigteDto);
    }

    private Unterhaltsberechtigte createNewUnterhaltsberechtigte(UnterhaltsberechtigteDto unterhaltsberechtigteDto) {
        VertragsdatenDto vertragsdatenDto = new VertragsdatenDto();
        vertragsdatenDto.setPersonalnummer(unterhaltsberechtigteDto.getPersonalnummer());
        Vertragsdaten vertragsdaten = vertragsdatenService.findByPersonalnummerStringAndStatus(unterhaltsberechtigteDto.getPersonalnummer(), List.of(MitarbeiterStatus.NEW, MitarbeiterStatus.VALIDATED, MitarbeiterStatus.NOT_VALIDATED))
                .stream().max(Comparator.comparingInt((Vertragsdaten v) -> v.getStatus().getCode())).orElseGet(() -> vertragsdatenValidatorService.getVertragsdaten(vertragsdatenDto, null));
        Unterhaltsberechtigte unterhaltsberechtigte = new Unterhaltsberechtigte();
        if (vertragsdaten != null) {
            unterhaltsberechtigte.setVertragsdaten(vertragsdaten);
        }
        if (!isNullOrBlank(unterhaltsberechtigteDto.getUVorname())) {
            unterhaltsberechtigte.setVorname(unterhaltsberechtigteDto.getUVorname());
        }
        if (!isNullOrBlank(unterhaltsberechtigteDto.getUNachname())) {
            unterhaltsberechtigte.setNachname(unterhaltsberechtigteDto.getUNachname());
        }
        if (!isNullOrBlank(unterhaltsberechtigteDto.getUGeburtsdatum()) && isValidDate(unterhaltsberechtigteDto.getUGeburtsdatum())) {
            unterhaltsberechtigte.setGeburtsdatum(parseDate(unterhaltsberechtigteDto.getUGeburtsdatum()));
        }

        if (!isNullOrBlank(unterhaltsberechtigteDto.getUSvnr()) && isLong(unterhaltsberechtigteDto.getUSvnr())) {
            unterhaltsberechtigte.setSvn(unterhaltsberechtigteDto.getUSvnr());
        }
        unterhaltsberechtigte.setCreatedBy(globalUserHolder.getUsername());
        unterhaltsberechtigte.setStatus(MitarbeiterStatus.NEW);
        unterhaltsberechtigte = unterhaltsberechtigteService.save(unterhaltsberechtigte);
        return unterhaltsberechtigte;
    }
}
