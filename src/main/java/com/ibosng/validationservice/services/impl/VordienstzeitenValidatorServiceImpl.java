package com.ibosng.validationservice.services.impl;

import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.VordienstzeitenDto;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.entities.mitarbeiter.Vordienstzeiten;
import com.ibosng.dbservice.services.mitarbeiter.VertragsdatenService;
import com.ibosng.dbservice.services.mitarbeiter.VordienstzeitenService;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.services.VertragsdatenValidatorService;
import com.ibosng.validationservice.services.VordienstzeitenValidatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class VordienstzeitenValidatorServiceImpl implements VordienstzeitenValidatorService {

    private final VordienstzeitenService vordienstzeitenService;
    private final VertragsdatenService vertragsdatenService;
    private final GlobalUserHolder globalUserHolder;
    private final VertragsdatenValidatorService vertragsdatenValidatorService;

    @Override
    public Vordienstzeiten getVordienstzeiten(VordienstzeitenDto vordienstzeitenDto) {
        if (vordienstzeitenDto.getId() != null) {
            Optional<Vordienstzeiten> vordienstzeiten = vordienstzeitenService.findById(vordienstzeitenDto.getId());
            if (vordienstzeiten.isPresent()) {
                vordienstzeiten.get().setErrors(new ArrayList<>());
                vordienstzeiten.get().setChangedOn(LocalDateTime.now());
                return vordienstzeiten.get();
            }
        }
        return createNewVordienstzeiten(vordienstzeitenDto);
    }

    private Vordienstzeiten createNewVordienstzeiten(VordienstzeitenDto vordienstzeitenDto) {
        VertragsdatenDto vertragsdatenDto = new VertragsdatenDto();
        vertragsdatenDto.setPersonalnummer(vordienstzeitenDto.getPersonalnummer());
        Vertragsdaten vertragsdaten = vertragsdatenService.findByPersonalnummerStringAndStatus(vordienstzeitenDto.getPersonalnummer(), List.of(MitarbeiterStatus.NEW, MitarbeiterStatus.VALIDATED, MitarbeiterStatus.NOT_VALIDATED))
                .stream().max(Comparator.comparingInt((Vertragsdaten v) -> v.getStatus().getCode())).orElseGet(() -> vertragsdatenValidatorService.getVertragsdaten(vertragsdatenDto, null));
        Vordienstzeiten vordienstzeiten = new Vordienstzeiten();
        vordienstzeiten.setVertragsdaten(vertragsdaten);
        vordienstzeiten.setCreatedBy(globalUserHolder.getUsername());
        vordienstzeiten.setStatus(MitarbeiterStatus.NEW);
        return vordienstzeitenService.save(vordienstzeiten);
    }
}