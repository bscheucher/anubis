package com.ibosng.gatewayservice.services.impl;

import com.ibosng.dbservice.dtos.VereinbarungDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.entities.mitarbeiter.vereinbarung.Vereinbarung;
import com.ibosng.dbservice.services.VereinbarungService;
import com.ibosng.dbservice.services.mitarbeiter.VertragsdatenService;
import com.ibosng.gatewayservice.dtos.ReportRequestDto;
import com.ibosng.gatewayservice.services.BenutzerDetailsService;
import com.ibosng.gatewayservice.services.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;

import static com.ibosng.gatewayservice.utils.Constants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    private final BenutzerDetailsService benutzerDetailsService;
    private final VertragsdatenService vertragsdatenService;

    private final VereinbarungService vereinbarungService;

    @Override
    public boolean canCreateVereinbarung(Benutzer benutzer, VereinbarungDto vereinbarungDto) {
        if (!benutzerDetailsService.isUserEligible(benutzer, Arrays.asList(FN_VEREINBARUNGEN_ERSTELLEN))) {
            return false;
        }
        Vertragsdaten vertragsdaten = vertragsdatenService.findByPersonalnummerString(vereinbarungDto.getPersonalnummer()).stream().findFirst().orElse(null);
        if (vertragsdaten == null) {
            log.error("No Vertragsdaten found for Personalnummer: " + vereinbarungDto.getPersonalnummer());
            return false;
        }
        if (vertragsdaten.getFuehrungskraft().equals(benutzer)) {
            return true;
        }

        return false;
    }

    @Override
    public boolean canCreateVereinbarung(Benutzer benutzer, ReportRequestDto reportRequestDto) {
        if (!benutzerDetailsService.isUserEligible(benutzer, Arrays.asList(FN_VEREINBARUNGEN_ERSTELLEN))) {
            return false;
        }
        Vertragsdaten vertragsdaten = vertragsdatenService.findByPersonalnummerString(reportRequestDto.getPersonalnummer()).stream().findFirst().orElse(null);
        if (vertragsdaten == null) {
            log.error("No Vertragsdaten found for Personalnummer: " + reportRequestDto.getPersonalnummer());
            return false;
        }
        if (vertragsdaten.getFuehrungskraft().equals(benutzer)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean canReadVereinbarung(Benutzer benutzer, Integer vereinbarungId) {
        if (!benutzerDetailsService.isUserEligible(benutzer, Arrays.asList(FN_VEREINBARUNGEN_EINSEHEN))) {
            return false;
        }
        Vereinbarung vereinbarung = vereinbarungService.findById(vereinbarungId).orElse(null);
        if(vereinbarung == null){
            log.error("No Vereinbarung found for id: " + vereinbarungId);
            return false;
        }
        Vertragsdaten vertragsdaten = vertragsdatenService.findByPersonalnummerString(vereinbarung.getPersonalnummer().getPersonalnummer()).stream().findFirst().orElse(null);
        if (vertragsdaten == null) {
            log.error("No Vertragsdaten found for Personalnummer: " + vereinbarung.getPersonalnummer().getPersonalnummer());
            return false;
        }
        if (vertragsdaten.getFuehrungskraft().equals(benutzer)) {
            return true;
        }
        return false;
    }
}
