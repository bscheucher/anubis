package com.ibosng.lhrservice.services.impl;

import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.mitarbeiter.Unterhaltsberechtigte;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.dbservice.services.mitarbeiter.UnterhaltsberechtigteService;
import com.ibosng.dbservice.services.mitarbeiter.VertragsdatenService;
import com.ibosng.lhrservice.client.LHRClient;
import com.ibosng.lhrservice.dtos.GruppeNameKzDto;
import com.ibosng.lhrservice.dtos.mitversicherte.MitversicherteDataDto;
import com.ibosng.lhrservice.dtos.mitversicherte.MitversicherteStammdatenDto;
import com.ibosng.lhrservice.dtos.mitversicherte.MitversicherteTopLevelDto;
import com.ibosng.lhrservice.exceptions.LHRException;
import com.ibosng.lhrservice.exceptions.LHRWebClientException;
import com.ibosng.lhrservice.services.HelperService;
import com.ibosng.lhrservice.services.LHREnvironmentService;
import com.ibosng.lhrservice.services.MitversicherteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.lhrservice.utils.Helpers.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class MitversicherteServiceImpl implements MitversicherteService {
    private final LHRClient lhrClient;
    private final HelperService helperService;
    private final LHREnvironmentService lhrEnvironmentService;
    private final VertragsdatenService vertragsdatenService;
    private final PersonalnummerService personalnummerService;
    private final UnterhaltsberechtigteService unterhaltsberechtigteService;


    @Override
    public void processMitversicherte(Personalnummer personalnummer, Integer vertragsdatenId) throws LHRWebClientException {
        MitversicherteTopLevelDto mitversicherteTopLevelDto = getMitversicherteTopLevel(personalnummer);
        MitversicherteTopLevelDto newMitversicherteTopLevelDto = mapUnterhaltsberechtigte(personalnummer, vertragsdatenId);
        if (newMitversicherteTopLevelDto == null || newMitversicherteTopLevelDto.getMitversicherte() == null || newMitversicherteTopLevelDto.getMitversicherte().isEmpty()) {
            log.info("Unterhaltsberechtigte are empty, not sending mitversicherte.");
            return;
        }
        if (mitversicherteTopLevelDto != null && !mitversicherteTopLevelDto.getMitversicherte().isEmpty()) {
            if (doesMitversicherteAlreadyExists(mitversicherteTopLevelDto.getMitversicherte(), newMitversicherteTopLevelDto)) {
                log.error("There already exists a Mitversicherten for personalnummer {}.", personalnummer);
                if (!lhrEnvironmentService.isProduction()) {
                    for (MitversicherteDataDto mitversicherteDataDto : mitversicherteTopLevelDto.getMitversicherte()) {
                        Integer mitversicherteNr = mitversicherteDataDto.getNr();
                        for (MitversicherteStammdatenDto stammdatenDto : mitversicherteDataDto.getStammdaten()) {
                            lhrClient.deleteMitversicherteStammdaten(helperService.createDienstnehmerRefDto(personalnummer), mitversicherteNr, stammdatenDto.getValidFrom());
                        }
                    }
                    if (newMitversicherteTopLevelDto != null) {
                        lhrClient.postMitversicherteTopLevel(newMitversicherteTopLevelDto);
                    }
                } else {
                    throw new LHRException(String.format("There already exists a Mitversicherten for personalnummer %s.", personalnummer));
                }
            }
        } else {
            if (newMitversicherteTopLevelDto != null) {
                lhrClient.postMitversicherteTopLevel(newMitversicherteTopLevelDto);
            }
        }
    }

    private boolean doesMitversicherteAlreadyExists(List<MitversicherteDataDto> alreadyMitversicherte, MitversicherteTopLevelDto newMitversicherteTopLevelDto) {
        return alreadyMitversicherte.stream()
                .flatMap(alreadyDto -> alreadyDto.getStammdaten().stream())
                .anyMatch(existingStammdaten ->
                        newMitversicherteTopLevelDto.getMitversicherte().stream()
                                .flatMap(newDto -> newDto.getStammdaten().stream())
                                .anyMatch(existingStammdaten::equals)
                );
    }


    private MitversicherteTopLevelDto getMitversicherteTopLevel(Personalnummer personalnummer) {
        ResponseEntity<MitversicherteTopLevelDto> response = (ResponseEntity<MitversicherteTopLevelDto>) getMitversicherte(personalnummer);
        return getBodyIfStatusOk(response);
    }

    public ResponseEntity<?> getMitversicherte(Personalnummer personalnummer) {
        return handleLHRExceptions(personalnummer.getPersonalnummer(), "mitversicherte", () ->
                lhrClient.getMitversicherteTopLevel(helperService.createDienstnehmerRefDto(personalnummer))
        );
    }

    private boolean isValidMitversicherteTopLevel(MitversicherteTopLevelDto dto) {
        return Optional.ofNullable(dto)
                .map(MitversicherteTopLevelDto::getMitversicherte)
                .map(daten -> !daten.isEmpty())
                .orElse(false);
    }

    private MitversicherteTopLevelDto mapUnterhaltsberechtigte(Personalnummer personalnummer, Integer vertragsdatenId) {
        MitversicherteTopLevelDto topLevelDto = new MitversicherteTopLevelDto();
        List<Unterhaltsberechtigte> unterhaltsberechtigten = unterhaltsberechtigteService.findAllByVertragsdatenId(vertragsdatenId);
        if (unterhaltsberechtigten.isEmpty()) {
            log.info("Unterhaltsberechtigte are empty, not sending mitversicherte.");
            return null;
        }
        Vertragsdaten vertragsdaten = vertragsdatenService.findByPersonalnummerIdAndStatus(personalnummer.getId(), List.of(MitarbeiterStatus.VALIDATED))
                .stream().findFirst().orElse(null);
        if (vertragsdaten == null) {
            log.warn("Vertragsdaten are null in mapUnterhaltsberechtigte, stopping process for Unterhaltsberechtigte!!!");
            return null;
        }
        topLevelDto.setDienstnehmer(helperService.createDienstnehmerRefDto(personalnummer));
        List<MitversicherteDataDto> mitversicherteDataDtoList = new ArrayList<>();
        int mitversicherteNr = 1;
        for (Unterhaltsberechtigte unterhaltsberechtigte : unterhaltsberechtigten) {
            MitversicherteDataDto mitversicherteDataDto = mapMitversicherteDataDto(mitversicherteNr, unterhaltsberechtigte, vertragsdaten.getEintritt());
            if (mitversicherteDataDto != null) {
                mitversicherteDataDtoList.add(mitversicherteDataDto);
                mitversicherteNr++;
            }
        }

        topLevelDto.setMitversicherte(mitversicherteDataDtoList);
        return topLevelDto;
    }

    private MitversicherteDataDto mapMitversicherteDataDto(int mitversicherteNr, Unterhaltsberechtigte unterhaltsberechtigte, LocalDate eintrittsdatum) {
        MitversicherteDataDto dto = null;
        MitversicherteStammdatenDto mitversicherteStammdatenDto = mapUnterhaltsberechtigteToDto(unterhaltsberechtigte, eintrittsdatum);
        if (mitversicherteStammdatenDto != null) {
            dto = new MitversicherteDataDto();
            dto.setNr(mitversicherteNr);
            dto.setStammdaten(List.of(mitversicherteStammdatenDto));
        }
        return dto;
    }

    private MitversicherteStammdatenDto mapUnterhaltsberechtigteToDto(Unterhaltsberechtigte unterhaltsberechtigte, LocalDate eintrittsdatum) {
        MitversicherteStammdatenDto stammdatenDto = new MitversicherteStammdatenDto();
        if (!isNullOrBlank(unterhaltsberechtigte.getVorname())) {
            stammdatenDto.setVorname(unterhaltsberechtigte.getVorname());
        }
        if (!isNullOrBlank(unterhaltsberechtigte.getNachname())) {
            stammdatenDto.setName(unterhaltsberechtigte.getNachname());
        }
        if (unterhaltsberechtigte.getGeburtsdatum() != null) {
            stammdatenDto.setGeburtsdatum(getDateInPostRequestFormat(unterhaltsberechtigte.getGeburtsdatum()));
        }
        if (unterhaltsberechtigte.getSvn() != null) {
            stammdatenDto.setSvNummer(unterhaltsberechtigte.getSvn().toString());
        }

        if (unterhaltsberechtigte.getVerwandtschaft() != null) {
            GruppeNameKzDto verwandt = new GruppeNameKzDto();
            if (!isNullOrBlank(unterhaltsberechtigte.getVerwandtschaft().getLhrKz())) {
                verwandt.setKz(unterhaltsberechtigte.getVerwandtschaft().getLhrKz());
            }
            if (!isNullOrBlank(unterhaltsberechtigte.getVerwandtschaft().getName())) {
                verwandt.setName(unterhaltsberechtigte.getVerwandtschaft().getName());
            }
            stammdatenDto.setVerwandt(verwandt);
        } else {
            log.error("Verwandtschaft is null, mitversicherte won't be mapped for unterhaltsberechtigte {}", unterhaltsberechtigte.getId());
            return null;
        }

        stammdatenDto.setValidFrom(getDateInPostRequestFormat(eintrittsdatum));
        return stammdatenDto;
    }
}
