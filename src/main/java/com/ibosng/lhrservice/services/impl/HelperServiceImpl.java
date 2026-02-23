package com.ibosng.lhrservice.services.impl;

import com.ibosng.dbibosservice.services.AdresseIbosService;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.services.BenutzerService;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.lhrservice.dtos.DienstnehmerRefDto;
import com.ibosng.lhrservice.exceptions.LHRException;
import com.ibosng.lhrservice.services.HelperService;
import com.ibosng.lhrservice.services.LHR2ValidationService;
import com.ibosng.lhrservice.services.LHREnvironmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.lhrservice.utils.Parsers.parseStringToInteger;

@Service
@Slf4j
@RequiredArgsConstructor
public class HelperServiceImpl implements HelperService {

    private final LHREnvironmentService lhrEnvironmentService;
    private final PersonalnummerService personalnummerService;
    private final AdresseIbosService adresseIbosService;
    private final BenutzerService benutzerService;
    private final LHR2ValidationService lhr2ValidationService;

    @Override
    public DienstnehmerRefDto createDienstnehmerRefDto(String personalnummerString) {
        Personalnummer personalnummer = personalnummerService.findByPersonalnummer(personalnummerString);
        if (personalnummer == null) {
            throw new LHRException("Personalnummer is null, stopping process!!!", new RuntimeException());
        }
        DienstnehmerRefDto dienstnehmerRef = new DienstnehmerRefDto();
        dienstnehmerRef.setDnNr(parseStringToInteger(personalnummer.getPersonalnummer()));
        if (personalnummer.getFirma() != null) {
            dienstnehmerRef.setFaNr(lhrEnvironmentService.getFaNr(personalnummer.getFirma()));
            dienstnehmerRef.setFaKz(lhrEnvironmentService.getFaKz(personalnummer.getFirma()));
        } else {
            log.error("IbisFirma is null for personalnummer {}", personalnummer.getPersonalnummer());
        }

        return dienstnehmerRef;
    }

    @Override
    public DienstnehmerRefDto createDienstnehmerRefDto(Personalnummer personalnummer) {
        if (personalnummer == null) {
            throw new LHRException("Personalnummer is null, stopping process!!!", new RuntimeException());
        }
        DienstnehmerRefDto dienstnehmerRef = new DienstnehmerRefDto();
        dienstnehmerRef.setDnNr(parseStringToInteger(personalnummer.getPersonalnummer()));
        if (personalnummer.getFirma() != null) {
            dienstnehmerRef.setFaNr(lhrEnvironmentService.getFaNr(personalnummer.getFirma()));
            dienstnehmerRef.setFaKz(lhrEnvironmentService.getFaKz(personalnummer.getFirma()));
        } else {
            log.error("IbisFirma is null for personalnummer {}", personalnummer.getPersonalnummer());
        }

        return dienstnehmerRef;
    }

    @Override
    public Benutzer getFuehrungskraefte(Personalnummer personalnummer) {
        Benutzer benutzer = benutzerService.findByPersonalnummer(personalnummer);
        if (benutzer == null) {
            log.warn("No benutzer found for personalnummer : {}", personalnummer.getPersonalnummer());
            return null;
        }
        if (isNullOrBlank(benutzer.getUpn())) {
            log.warn("No upn found for benutzer : {}", benutzer.getFirstName() + " " + benutzer.getLastName());
            return null;
        }
        String fuhrungskraefteUpn = adresseIbosService.getFuehrungskraftUPNFromLogin(lhrEnvironmentService.getLogin(benutzer.getUpn()));
        Benutzer fuehrungskraft = null;
        if (!isNullOrBlank(fuhrungskraefteUpn)) {
            fuhrungskraefteUpn = fuhrungskraefteUpn.toLowerCase();
            fuehrungskraft = benutzerService.findByUpn(fuhrungskraefteUpn);

            if (fuehrungskraft == null) {
                // Sync the supervisor if not found
                lhr2ValidationService.validateSyncMitarbeiterWithUPN(fuhrungskraefteUpn);
                fuehrungskraft = benutzerService.findByUpn(fuhrungskraefteUpn); // Retry after sync
            }

        }
        return fuehrungskraft;
    }
}
