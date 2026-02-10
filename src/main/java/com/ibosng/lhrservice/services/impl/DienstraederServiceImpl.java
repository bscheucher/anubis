package com.ibosng.lhrservice.services.impl;

import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.Arbeitszeiten;
import com.ibosng.dbservice.entities.mitarbeiter.ArbeitszeitenInfo;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.services.mitarbeiter.ArbeitszeitenInfoService;
import com.ibosng.dbservice.services.mitarbeiter.ArbeitszeitenService;
import com.ibosng.dbservice.services.mitarbeiter.VertragsdatenService;
import com.ibosng.lhrservice.client.LHRClient;
import com.ibosng.lhrservice.dtos.dienstraeder.DienstraederDataDto;
import com.ibosng.lhrservice.dtos.dienstraeder.DienstraederSingleTopLevelDto;
import com.ibosng.lhrservice.dtos.dienstraeder.DienstraederTimeframeDto;
import com.ibosng.lhrservice.dtos.dienstraeder.DienstraederTopLevelDto;
import com.ibosng.lhrservice.dtos.dienstraeder.settings.DienstraederSettingsDto;
import com.ibosng.lhrservice.dtos.dienstraeder.settings.DienstraederSettingsSollzeitDto;
import com.ibosng.lhrservice.dtos.dienstraeder.settings.DienstraederSettingsValidForDto;
import com.ibosng.lhrservice.exceptions.LHRException;
import com.ibosng.lhrservice.exceptions.LHRWebClientException;
import com.ibosng.lhrservice.services.DienstraederService;
import com.ibosng.lhrservice.services.HelperService;
import com.ibosng.lhrservice.services.LHREnvironmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static com.ibosng.dbservice.utils.Helpers.findFirstObject;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.lhrservice.utils.Helpers.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class DienstraederServiceImpl implements DienstraederService {

    private final LHRClient lhrClient;
    private final HelperService helperService;
    private final VertragsdatenService vertragsdatenService;
    private final ArbeitszeitenInfoService arbeitszeitenInfoService;
    private final ArbeitszeitenService arbeitszeitenService;
    private final LHREnvironmentService lhrEnvironmentService;

    @Override
    public void processDienstraeder(Personalnummer personalnummer, LocalDate eintrittsdatum) throws LHRWebClientException {
        String eintrittsdatumPostformat = getDateInPostRequestFormat(eintrittsdatum);
        DienstraederSingleTopLevelDto existingSingleDate = getDienstradSingleDate(personalnummer, eintrittsdatumPostformat);
        DienstraederTopLevelDto existingTopLevel = getDienstradTopLevel(personalnummer);

        if (isValidDienstradSingleDate(existingSingleDate, eintrittsdatum)) {
            if (existingSingleDate.getSettings().isEmpty()) {
                lhrClient.postDienstraederSingleDateTopLevel(mapDienstradSingleDateForLHR(personalnummer, true), eintrittsdatumPostformat, true);
            } else {
                if (!lhrEnvironmentService.isProduction()) {
                    lhrClient.deleteDienstraederSingleDate(helperService.createDienstnehmerRefDto(personalnummer), eintrittsdatumPostformat, true);
                    lhrClient.postDienstraederSingleDateTopLevel(mapDienstradSingleDateForLHR(personalnummer, true), eintrittsdatumPostformat, true);
                } else {
                    throw new LHRException(String.format("There already exists a Dienstraeder for a single day %s for personalnummer %s.", eintrittsdatumPostformat, personalnummer));
                }
            }
        } else if (isValidDienstradTopLevel(existingTopLevel)) {
            for (DienstraederDataDto dienstraederDataDto : existingTopLevel.getDienstraeder()) {
                String date = getDateInPostRequestFormat(dienstraederDataDto.getTimeframe().getValidFrom());
                DienstraederSingleTopLevelDto existingSettings = getDienstradSingleDate(personalnummer, date);
                if (!lhrEnvironmentService.isProduction()) {
                    if (isValidDienstradSingleDate(existingSettings, dienstraederDataDto.getTimeframe().getValidFrom())) {
                        lhrClient.deleteDienstraederSingleDate(helperService.createDienstnehmerRefDto(personalnummer), date, true);
                    }
                    lhrClient.deleteDienstraederSingleDate(helperService.createDienstnehmerRefDto(personalnummer), date, false);
                } else {
                    throw new LHRException(String.format("There already exists a Dienstraeder for a single day %s for personalnummer %s.", eintrittsdatumPostformat, personalnummer));
                }
            }
            lhrClient.postDienstraederTopLevel(mapDienstradTopLevelForLHR(personalnummer));
            lhrClient.postDienstraederSingleDateTopLevel(mapDienstradSingleDateForLHR(personalnummer, true), eintrittsdatumPostformat, true);
        } else {
            lhrClient.postDienstraederTopLevel(mapDienstradTopLevelForLHR(personalnummer));
            lhrClient.postDienstraederSingleDateTopLevel(mapDienstradSingleDateForLHR(personalnummer, true), eintrittsdatumPostformat, true);
        }
    }


    private DienstraederTopLevelDto getDienstradTopLevel(Personalnummer personalnummer) {
        ResponseEntity<DienstraederTopLevelDto> response = (ResponseEntity<DienstraederTopLevelDto>) getDienstrad(personalnummer, null);
        return getBodyIfStatusOk(response);
    }

    private DienstraederSingleTopLevelDto getDienstradSingleDate(Personalnummer personalnummer, String eintrittsDatum) {
        ResponseEntity<DienstraederSingleTopLevelDto> response = (ResponseEntity<DienstraederSingleTopLevelDto>) getDienstrad(personalnummer, eintrittsDatum);
        return getBodyIfStatusOk(response);
    }

    private boolean isValidDienstradSingleDate(DienstraederSingleTopLevelDto dto, LocalDate eintrittsDatum) {
        return Optional.ofNullable(dto)
                .map(DienstraederSingleTopLevelDto::getDienstrad)
                .map(obj -> obj.getTimeframe() != null && obj.getTimeframe().getValidFrom() != null && obj.getTimeframe().getValidFrom().equals(eintrittsDatum))
                .orElse(false);
    }

    private boolean isValidDienstradTopLevel(DienstraederTopLevelDto dto) {
        return Optional.ofNullable(dto)
                .map(DienstraederTopLevelDto::getDienstraeder)
                .map(obj -> !obj.isEmpty())
                .orElse(false);
    }

    public ResponseEntity<?> getDienstrad(Personalnummer personalnummer, String eintrittsDatum) {
        if (!isNullOrBlank(eintrittsDatum)) {
            return handleLHRExceptions(personalnummer.getPersonalnummer(), "Diensträder", () ->
                    lhrClient.getDienstraederSingleDateSettings(helperService.createDienstnehmerRefDto(personalnummer), eintrittsDatum)
            );
        }
        return handleLHRExceptions(personalnummer.getPersonalnummer(), "Diensträder", () ->
                lhrClient.getDienstraederTopLevel(helperService.createDienstnehmerRefDto(personalnummer))
        );
    }

    private DienstraederTopLevelDto mapDienstradTopLevelForLHR(Personalnummer personalnummer) {
        DienstraederTopLevelDto topLevelDto = new DienstraederTopLevelDto();
        Vertragsdaten vertragsdaten = vertragsdatenService.findByPersonalnummerIdAndStatus(personalnummer.getId(), List.of(MitarbeiterStatus.VALIDATED))
                .stream().findFirst().orElse(null);
        if (vertragsdaten == null) {
            throw new LHRException("Vertragsdaten are null in mapDienstradTopLevelForLHR, stopping process!!!", new RuntimeException());
        }
        topLevelDto.setDienstnehmer(helperService.createDienstnehmerRefDto(personalnummer));
        DienstraederDataDto dataDto = new DienstraederDataDto();
        dataDto.setWeeks(1);
        DienstraederTimeframeDto timeframeDto = new DienstraederTimeframeDto();
        timeframeDto.setValidFrom(vertragsdaten.getEintritt());
        dataDto.setTimeframe(timeframeDto);
        topLevelDto.setDienstraeder(List.of(dataDto));
        return topLevelDto;
    }

    private DienstraederSingleTopLevelDto mapDienstradSingleDateForLHR(Personalnummer personalnummer, boolean withSettings) {
        DienstraederSingleTopLevelDto singleTopLevelDto = new DienstraederSingleTopLevelDto();
        Vertragsdaten vertragsdaten = vertragsdatenService.findByPersonalnummerIdAndStatus(personalnummer.getId(), List.of(MitarbeiterStatus.VALIDATED))
                .stream().findFirst().orElse(null);
        if (vertragsdaten == null) {
            throw new LHRException("Vertragsdaten are null in mapDienstradSingleDateForLHR, stopping process!!!", new RuntimeException());
        }
        singleTopLevelDto.setDienstnehmer(helperService.createDienstnehmerRefDto(personalnummer));
        DienstraederDataDto dataDto = new DienstraederDataDto();
        dataDto.setWeeks(1);
        DienstraederTimeframeDto timeframeDto = new DienstraederTimeframeDto();
        timeframeDto.setValidFrom(vertragsdaten.getEintritt());
        dataDto.setTimeframe(timeframeDto);
        singleTopLevelDto.setDienstrad(dataDto);
        if (withSettings) {
            singleTopLevelDto.setSettings(mapDienstradSettingsForLHR(vertragsdaten.getId()));
        }
        return singleTopLevelDto;
    }

    private List<DienstraederSettingsDto> mapDienstradSettingsForLHR(Integer vertragsdatenId) {
        List<DienstraederSettingsDto> settingsDtoList = new ArrayList<>();
        ArbeitszeitenInfo arbeitszeitenInfo = arbeitszeitenInfoService.findByVertragsdatenId(vertragsdatenId);
        if (arbeitszeitenInfo != null) {
            List<Arbeitszeiten> arbeitszeitenList = arbeitszeitenService.findByArbAndArbeitszeitenInfoId(arbeitszeitenInfo.getId()).stream().filter(az -> (az.getKernzeit() == null || Boolean.FALSE.equals(az.getKernzeit()))).toList();
            Arbeitszeiten arbeitszeiten = findFirstObject(arbeitszeitenList, new HashSet<>(List.of(String.valueOf(arbeitszeitenInfo.getId()))), "Arbeitszeiten");
            if (arbeitszeiten != null) {
                settingsDtoList.add(createSettingsDto(1, 1, arbeitszeiten.getMontagNetto()));
                settingsDtoList.add(createSettingsDto(1, 2, arbeitszeiten.getDienstagNetto()));
                settingsDtoList.add(createSettingsDto(1, 3, arbeitszeiten.getMittwochNetto()));
                settingsDtoList.add(createSettingsDto(1, 4, arbeitszeiten.getDonnerstagNetto()));
                settingsDtoList.add(createSettingsDto(1, 5, arbeitszeiten.getFreitagNetto()));
                settingsDtoList.add(createSettingsDto(1, 6, arbeitszeiten.getSamstagNetto()));
                settingsDtoList.add(createSettingsDto(1, 7, arbeitszeiten.getSonntagNetto()));
            }
        }
        return settingsDtoList;
    }

    private static DienstraederSettingsDto createSettingsDto(int week, int day, Double netto) {
        double stunden = (netto != null) ? netto : 0.0;
        return getDienstraederSettingsDto(week, day, stunden);
    }

    @NotNull
    private static DienstraederSettingsDto getDienstraederSettingsDto(Integer week, Integer day, double stunden) {
        DienstraederSettingsDto settingsDto = new DienstraederSettingsDto();
        DienstraederSettingsValidForDto validForDto = new DienstraederSettingsValidForDto();
        validForDto.setWeek(week);
        validForDto.setDay(day);
        settingsDto.setValidFor(validForDto);
        DienstraederSettingsSollzeitDto settingsSollzeitDto = new DienstraederSettingsSollzeitDto();
        settingsSollzeitDto.setDuration(doubleToTime(stunden));
        settingsSollzeitDto.setIgnoreOnPublicHoliday(true);
        settingsDto.setSollzeit(settingsSollzeitDto);
        return settingsDto;
    }

    private static String doubleToTime(Double stunden) {
        int hours = stunden.intValue(); // get the whole hours
        int minutes = (int) ((stunden - hours) * 60); // calculate remaining minutes
        // Format as "hh:mm"
        return String.format("%02d:%02d", hours, minutes);
    }
}
