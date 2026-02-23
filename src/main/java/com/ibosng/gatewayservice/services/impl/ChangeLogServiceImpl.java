package com.ibosng.gatewayservice.services.impl;

import com.ibosng.dbservice.dtos.changelog.ChangeLogDto;
import com.ibosng.dbservice.dtos.changelog.MitarbeiterChangeLogDto;
import com.ibosng.dbservice.dtos.changelog.VertragsaenderungChangeLogDto;
import com.ibosng.dbservice.dtos.mitarbeiter.vertragsaenderung.VertragsaenderungDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.services.BenutzerService;
import com.ibosng.dbservice.services.changelog.MAChangeLogService;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.dbservice.services.mitarbeiter.VertragsaenderungService;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.dtos.response.PayloadTypeList;
import com.ibosng.gatewayservice.enums.PayloadTypes;
import com.ibosng.gatewayservice.services.ChangeLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChangeLogServiceImpl implements ChangeLogService {

    private final MAChangeLogService maChangeLogService;
    private final PersonalnummerService personalnummerService;
    private final BenutzerService benutzerService;
    private final VertragsaenderungService vertragsaenderungService;

    @Override
    public PayloadResponse getMAChangeLog(String personalnummer) {
        Personalnummer personalnummerEntity = personalnummerService.findByPersonalnummer(personalnummer);
        PayloadResponse response = new PayloadResponse();
        if (personalnummerEntity == null) {
            log.warn("personalnummer " + personalnummer + " not found");
            response.setMessage("personalnummer " + personalnummer + " not found");
            response.setSuccess(false);
            return response;
        }

        MitarbeiterChangeLogDto dto = maChangeLogService.getMAChangeLog(personalnummer);
        PayloadTypeList<ChangeLogDto> changeLogPayload = new PayloadTypeList<>(PayloadTypes.MA_CHANGE_LOG.getValue());
        changeLogPayload.setAttributes(getChangeLogs(personalnummerEntity, dto));
        response.setSuccess(true);
        response.setData(List.of(changeLogPayload));
        return response;
    }

    @Override
    public PayloadResponse getVertragsaenderungenChangeLog(String personalnummer) {
        PayloadResponse response = new PayloadResponse();
        VertragsaenderungChangeLogDto dto = vertragsaenderungService.getVertragsaenderungenChangeLog(personalnummer);
        PayloadTypeList<VertragsaenderungDto> changeLogPayload = new PayloadTypeList<>(PayloadTypes.VERTRAGSAENDERUNGEN_CHANGE_LOG.getValue());
        changeLogPayload.setAttributes(dto.getVertragsaenderungen());
        response.setSuccess(true);
        response.setData(List.of(changeLogPayload));
        return response;
    }

    private List<ChangeLogDto> getChangeLogs(Personalnummer personalnummer, MitarbeiterChangeLogDto dto) {
        ChangeLogDto changeLogDto = new ChangeLogDto();
        Benutzer benutzer = benutzerService.findByEmail(personalnummer.getCreatedBy());
        if(benutzer != null) {
            changeLogDto.setChangedBy(benutzer.getFirstName() + " " + benutzer.getLastName());
            changeLogDto.setChangedAt(personalnummer.getCreatedOn());
        }
        changeLogDto.setType("create");
        List<ChangeLogDto> finalList = new ArrayList<>();
        finalList.add(changeLogDto);
        if (dto != null) {
            finalList.addAll(dto.getStammdatenAenderungen());
            finalList.addAll(dto.getVertragsaenderungen());
        }
        return finalList.stream()
                .sorted(Comparator.comparing(ChangeLogDto::getChangedAt))
                .toList();
    }
}
