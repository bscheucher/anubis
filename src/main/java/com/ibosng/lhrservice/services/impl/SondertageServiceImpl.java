package com.ibosng.lhrservice.services.impl;

import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.ArbeitszeitenInfo;
import com.ibosng.dbservice.services.mitarbeiter.ArbeitszeitenInfoService;
import com.ibosng.lhrservice.client.LHRClient;
import com.ibosng.lhrservice.dtos.sondertage.*;
import com.ibosng.lhrservice.exceptions.LHRWebClientException;
import com.ibosng.lhrservice.services.HelperService;
import com.ibosng.lhrservice.services.SondertageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.ibosng.lhrservice.utils.Helpers.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class SondertageServiceImpl implements SondertageService {
    private final static Integer MATCH_CODE_END_PROJEKT = 99;

    private final LHRClient lhrClient;
    private final HelperService helperService;
    private final ArbeitszeitenInfoService arbeitszeitenInfoService;

    @Override
    public void processSondertageForOnboarding(Personalnummer personalnummer, Integer vertragsdatedId) throws LHRWebClientException {
        ArbeitszeitenInfo arbeitszeitenInfo = arbeitszeitenInfoService.findByVertragsdatenId(vertragsdatedId);
        if (arbeitszeitenInfo != null && arbeitszeitenInfo.getArbeitszeitmodellBis() != null) {
            String datePostFormat = getDateInPostRequestFormat(arbeitszeitenInfo.getArbeitszeitmodellBis());
            SondertageMatchCodeSingleDateDto matchCodeSingleDateDto = getSondertageMatchCodeSingleDateResponse(personalnummer, MATCH_CODE_END_PROJEKT, datePostFormat);

            if (isValidmatchCodeSingleDate(matchCodeSingleDateDto)) {
                lhrClient.putSondertageMatchCodeSingleDate(mapSondertageMatchCodeSingleDate(personalnummer, MATCH_CODE_END_PROJEKT, arbeitszeitenInfo.getArbeitszeitmodellBis()), MATCH_CODE_END_PROJEKT, datePostFormat);
            } else {
                lhrClient.postSondertageMatchCodeSingleDate(mapSondertageTopLevel(personalnummer, MATCH_CODE_END_PROJEKT, arbeitszeitenInfo.getArbeitszeitmodellBis()));
            }
        }

    }

    private SondertageMatchCodeSingleDateDto getSondertageMatchCodeSingleDateResponse(Personalnummer personalnummer, Integer matchCode, String datum) {
        ResponseEntity<SondertageMatchCodeSingleDateDto> response = (ResponseEntity<SondertageMatchCodeSingleDateDto>) getSondertageMatchCodeSingleDate(personalnummer, matchCode, datum);
        return getBodyIfStatusOk(response);
    }

    public ResponseEntity<?> getSondertageMatchCodeSingleDate(Personalnummer personalnummer, Integer mathcCode, String date) {
        return handleLHRExceptions(personalnummer.getPersonalnummer(), "Sondertage", () ->
                lhrClient.getSondertageMatchCodeSingleDate(helperService.createDienstnehmerRefDto(personalnummer), mathcCode, date)
        );
    }

    private boolean isValidmatchCodeSingleDate(SondertageMatchCodeSingleDateDto dto) {
        return Optional.ofNullable(dto)
                .map(SondertageMatchCodeSingleDateDto::getSondertag)
                .map(daten -> daten.getDate() != null)
                .orElse(false);
    }

    private SondertageMatchCodeSingleDateDto mapSondertageMatchCodeSingleDate(Personalnummer personalnummer, Integer matchCode, LocalDate datum) {
        SondertageMatchCodeSingleDateDto matchCodeSingleDateDto = new SondertageMatchCodeSingleDateDto();
        matchCodeSingleDateDto.setDienstnehmer(helperService.createDienstnehmerRefDto(personalnummer));
        SondertagDto sondertagDto = new SondertagDto();
        sondertagDto.setMatchCode(matchCode);
        SondertagDatesDto sondertagDatesDto = new SondertagDatesDto();
        sondertagDatesDto.setDay(datum);
        sondertagDto.setDate(sondertagDatesDto);
        matchCodeSingleDateDto.setSondertag(sondertagDto);
        return matchCodeSingleDateDto;
    }

    private SondertageTopLevelDto mapSondertageTopLevel(Personalnummer personalnummer, Integer matchCode, LocalDate datum) {
        SondertageTopLevelDto sondertageTopLevelDto = new SondertageTopLevelDto();
        sondertageTopLevelDto.setDienstnehmer(helperService.createDienstnehmerRefDto(personalnummer));
        SondertageDto sondertageDto = new SondertageDto();
        sondertageDto.setMatchCode(matchCode);
        SondertagDatesDto sondertagDatesDto = new SondertagDatesDto();
        sondertagDatesDto.setDay(datum);
        sondertageDto.setDates(List.of(sondertagDatesDto));
        sondertageTopLevelDto.setSondertage(List.of(sondertageDto));
        return sondertageTopLevelDto;
    }
}
