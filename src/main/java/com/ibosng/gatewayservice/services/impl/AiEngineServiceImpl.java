package com.ibosng.gatewayservice.services.impl;

import com.ibosng.gatewayservice.dtos.ai.*;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.dtos.response.PayloadTypeList;
import com.ibosng.gatewayservice.enums.PayloadTypes;
import com.ibosng.gatewayservice.services.AIEngineService;
import com.ibosng.gatewayservice.services.Gateway2AiService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.gatewayservice.utils.Helpers.checkResultIfNull;

@Service
@Slf4j
@AllArgsConstructor
public class AiEngineServiceImpl implements AIEngineService {

    private final Gateway2AiService gateway2AiService;

    @Override
    public ResponseEntity<PayloadResponse> getSeminarVertretungRequest(String trainerId, String von, String bis) {
        PayloadResponse payloadResponse = new PayloadResponse();
        ResponseEntity<FinalRecommendationResponseDto> response = gateway2AiService.getSeminarVertretungRequest(trainerId, von, bis);
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            payloadResponse.setSuccess(true);
            PayloadTypeList<VertretungsplanungDataDto> data = createPayloadForSeminarVertretungData(response.getBody());
            payloadResponse.setData(List.of(data));
        } else {
            payloadResponse.setSuccess(false);
        }
        return checkResultIfNull(payloadResponse);
    }

    @Override
    public ResponseEntity<String> sendSingleStringRequest(String request) {
        return gateway2AiService.sendGenericRequest(request);
/*        PayloadResponse payloadResponse = new PayloadResponse();
        ResponseEntity<String> response = gateway2AiService.sendGenericRequest(request);
        if(response.getStatusCode().equals(HttpStatus.OK)) {
            payloadResponse.setSuccess(true);
            PayloadTypeList<String> aiPayloadType = createPayloadForAI(response.getBody());
            payloadResponse.setData(List.of(aiPayloadType));
        } else {
            payloadResponse.setSuccess(false);
        }
        return payloadResponse;*/
    }

    @Override
    public ResponseEntity<Void> setAnweisung(String assistantName, String anweisung, String user) {
        return gateway2AiService.setAnweisungForAssistant(assistantName, anweisung, user);
/*        PayloadResponse payloadResponse = new PayloadResponse();
        ResponseEntity<Void> response = gateway2AiService.setAnweisungForAssistant(assistantName, anweisung, user);
        if(response.getStatusCode().equals(HttpStatus.OK)) {
            payloadResponse.setSuccess(true);
        } else {
            payloadResponse.setSuccess(false);
        }
        return payloadResponse;*/
    }

    @Override
    public ResponseEntity<String> getAnweisung(String assistantName) {
        return gateway2AiService.getAnweisungForAssistant(assistantName);
/*        PayloadResponse payloadResponse = new PayloadResponse();
        ResponseEntity<String> response = gateway2AiService.getAnweisungForAssistant(assistantName);
        if(response.getStatusCode().equals(HttpStatus.OK)) {
            payloadResponse.setSuccess(true);
            PayloadTypeList<String> aiPayloadType = createPayloadForAI(response.getBody());
            payloadResponse.setData(List.of(aiPayloadType));
        } else {
            payloadResponse.setSuccess(false);
        }
        return payloadResponse;*/
    }

    private PayloadTypeList<VertretungsplanungDataDto> createPayloadForSeminarVertretungData(FinalRecommendationResponseDto response) {
        PayloadTypeList<VertretungsplanungDataDto> aiPayloadType = new PayloadTypeList<>(PayloadTypes.VERTRETUNGSPLAN.getValue());
        VertretungsplanungDataDto metaDataDto = mapSeminarvertretungData(response);
        aiPayloadType.setAttributes(Collections.singletonList(metaDataDto));
        return aiPayloadType;
    }

    private VertretungsplanungDataDto mapSeminarvertretungData(FinalRecommendationResponseDto response) {
        VertretungsplanungDataDto dataDto = new VertretungsplanungDataDto();
        dataDto.setVertretungsplanungMetaData(mapMetaData(response));
        dataDto.setVertretungsplanungTable(mapMainDataForSeminarVertretung(response));
        return dataDto;
    }

    private List<AISeminarDto> mapMainDataForSeminarVertretung(FinalRecommendationResponseDto response) {
        Set<AISeminarDto> data = new HashSet<>();
        Set<WeekDataDto> weekData = new HashSet<>();
        AISeminarDto aiseminarDto;
        for (RecommendationDto recommendationDto : response.getRecommendations()) {
            aiseminarDto = data.stream().filter(sem -> sem.getSeminarId().equals(recommendationDto.getSeminarId())).findFirst().orElse(null);
            OptimalCombinationDto optimalCombinationDto = response.getOptimalCombination().stream().filter(opt -> opt.getDate().equals(recommendationDto.getDate())).findFirst().orElse(null);
            if (aiseminarDto == null) {
                aiseminarDto = new AISeminarDto();
                if (recommendationDto.getSeminarId() != null) {
                    aiseminarDto.setSeminarId(recommendationDto.getSeminarId());
                }
                if (!isNullOrBlank(recommendationDto.getSeminar())) {
                    aiseminarDto.setSeminarTitle(recommendationDto.getSeminar());
                }
                data.add(aiseminarDto);
            }
            WeekDataDto weekDataDto = mapWeekDataDto(recommendationDto, optimalCombinationDto);
            weekData.add(weekDataDto);
            aiseminarDto.setWeekData(weekData.stream().sorted(Comparator.comparing(WeekDataDto::getDate)).toList());
        }
        return data.stream().toList();
    }

    private WeekDataDto mapWeekDataDto(RecommendationDto recommendationDto, OptimalCombinationDto optimalCombinationDto) {
        WeekDataDto weekDataDto = new WeekDataDto();
        if (!isNullOrBlank(recommendationDto.getDate())) {
            weekDataDto.setDate(recommendationDto.getDate());
        }
        if (optimalCombinationDto != null && optimalCombinationDto.getRecommendedTrainerId() != null) {
            weekDataDto.setSelection(optimalCombinationDto.getRecommendedTrainerId());
        }
        Set<TrainerDto> trainerDtos = new HashSet<>();
        for (RankedTrainerDto rankedTrainerDto : recommendationDto.getRankedTrainers()) {
            TrainerDto trainerDto = new TrainerDto();
            if (rankedTrainerDto.getId() != null) {
                trainerDto.setId(rankedTrainerDto.getId());
            }
            if (!isNullOrBlank(rankedTrainerDto.getName())) {
                trainerDto.setName(rankedTrainerDto.getName());
            }
            if (!isNullOrBlank(rankedTrainerDto.getScore())) {
                trainerDto.setPunkte(rankedTrainerDto.getScore());
            }
            if (!isNullOrBlank(rankedTrainerDto.getIsVorherigeVertretung()) && rankedTrainerDto.getIsVorherigeVertretung().equals("Ja")) {
                trainerDto.setHasExperience(true);
            } else {
                trainerDto.setHasExperience(false);
            }
            if (!isNullOrBlank(rankedTrainerDto.getEmail())) {
                trainerDto.setEmail(rankedTrainerDto.getEmail());
            }
            if (!isNullOrBlank(rankedTrainerDto.getTelefon())) {
                trainerDto.setPhone(rankedTrainerDto.getTelefon());
            }
            if (!isNullOrBlank(rankedTrainerDto.getVerfuegbarkeitOnline()) && rankedTrainerDto.getVerfuegbarkeitOnline().equals("Ja")) {
                trainerDto.getAvailability().add("online");
            }
            if (!isNullOrBlank(rankedTrainerDto.getVerfuegbarkeitPraesenz()) && rankedTrainerDto.getVerfuegbarkeitPraesenz().equals("Ja")) {
                trainerDto.getAvailability().add("onSite");
            }
            trainerDtos.add(trainerDto);
        }
        List<TrainerDto> sortedList = trainerDtos.stream()
                .sorted(Comparator.comparing(t -> !t.getId().equals(weekDataDto.getSelection())))
                .toList();
        weekDataDto.setList(sortedList);
        return weekDataDto;
    }

    private VertretungsplanungMetaDataDto mapMetaData(FinalRecommendationResponseDto response) {
        VertretungsplanungMetaDataDto metaDataDto = new VertretungsplanungMetaDataDto();
        if (!isNullOrBlank(response.getReplacement().getTrainerVorname())) {
            metaDataDto.setVorname(response.getReplacement().getTrainerVorname());
        }
        if (!isNullOrBlank(response.getReplacement().getTrainerNachname())) {
            metaDataDto.setNachname(response.getReplacement().getTrainerNachname());
        }
        if (response.getReplacement().getVon() != null) {
            metaDataDto.setFromDate(response.getReplacement().getVon());
        }
        if (response.getReplacement().getBis() != null) {
            metaDataDto.setToDate(response.getReplacement().getBis());
        }
        if (response.getReplacement().getId() != null) {
            metaDataDto.setMitarbeiterId(response.getReplacement().getId());
        }
        return metaDataDto;
    }
}
