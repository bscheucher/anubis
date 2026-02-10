package com.ibosng.lhrservice.services.impl;

import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.lhrservice.client.LHRClient;
import com.ibosng.lhrservice.dtos.DienstnehmerRefDto;
import com.ibosng.lhrservice.dtos.zeitdaten.AnfrageSuccessDto;
import com.ibosng.lhrservice.services.LHRAuszahlungsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import wiremock.com.github.jknack.handlebars.internal.lang3.math.NumberUtils;

import static com.ibosng.lhrservice.utils.Parsers.parseStringToInteger;

@Service
@Slf4j
@RequiredArgsConstructor
public class LHRAuszahlungsServiceImpl implements LHRAuszahlungsService {

    private final LHRClient lhrClient;
    private final com.ibosng.lhrservice.services.LHREnvironmentService LHREnvironmentService;
    private final PersonalnummerService personalnummerService;

    @Override
    public ResponseEntity<AnfrageSuccessDto> postAuszahlungsanfrage(String personalnummer, String day, String zspNr, String minutes) {
        final int dnNr = parseStringToInteger(personalnummer);

        Personalnummer foundPersonalnummer = personalnummerService.findByPersonalnummer(personalnummer);
        String firma;
        Integer firmaNr;
        if(foundPersonalnummer != null && foundPersonalnummer.getFirma() != null) {
            firma = LHREnvironmentService.getFaKz(foundPersonalnummer.getFirma());
            firmaNr = LHREnvironmentService.getFaNr(foundPersonalnummer.getFirma());
            DienstnehmerRefDto dnRef = DienstnehmerRefDto.builder()
                    .faKz(firma)
                    .faNr(firmaNr)
                    .dnNr(dnNr)
                    .build();
            ResponseEntity<AnfrageSuccessDto> response = lhrClient.postAuszahlungsanfrage(dnRef, day, NumberUtils.createInteger(zspNr), NumberUtils.createInteger(minutes));
            return response;
        } else {
            log.error("IbisFirma is null for personalnummer {}", foundPersonalnummer != null ? foundPersonalnummer.getPersonalnummer() : null);
            return null;
        }
    }

    @Override
    public ResponseEntity<AnfrageSuccessDto> getAuszahlungsanfrage(String personalnummer, Integer anfrageNr) {
        final int dnNr = parseStringToInteger(personalnummer);

        Personalnummer foundPersonalnummer = personalnummerService.findByPersonalnummer(personalnummer);
        String firma;
        Integer firmaNr;
        if(foundPersonalnummer != null && foundPersonalnummer.getFirma() != null) {
            firma = LHREnvironmentService.getFaKz(foundPersonalnummer.getFirma());
            firmaNr = LHREnvironmentService.getFaNr(foundPersonalnummer.getFirma());
            DienstnehmerRefDto dnRef = DienstnehmerRefDto.builder()
                    .faKz(firma)
                    .faNr(firmaNr)
                    .dnNr(dnNr)
                    .build();
            ResponseEntity<AnfrageSuccessDto> response = lhrClient.getAuszahlungsanfrage(dnRef, anfrageNr);
            return response;
        } else {
            log.error("IbisFirma is null for personalnummer {}", foundPersonalnummer != null ? foundPersonalnummer.getPersonalnummer() : null);
            return null;
        }
    }
}
