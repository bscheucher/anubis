package com.ibosng.aiservice.services.impl;

import com.ibosng.aiservice.services.AIValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AIValidationServiceImpl implements AIValidationService {
/*    private final SeminarService seminarService;
    private final TrainerService trainerService;*/

    @Override
    public List<String> validateResponseForSeminarAndTrainers(String response) {
        List<String> errorList = new ArrayList<>();
        /*
        RecommendationResponseDto recommendationResponseDto = parseResponseToDto(response);

        if (recommendationResponseDto == null) {
            errorList.add(String.format("Could not parse response %s to the asked json!", response));
            return errorList;
        }

        for (RecommendationDto recommendationDto : recommendationResponseDto.getRecommendations()) {
            List<Seminar> seminars = seminarService.findAllByBezeichnung(recommendationDto.getSeminar());
            if (seminars.isEmpty()) {
                errorList.add(String.format("The given seminar %s does not exist!", recommendationDto.getSeminar()));
            }
            for (RankedTrainerDto trainerDto : recommendationDto.getRankedTrainers()) {
                String[] parts = trainerDto.getName().split(" ");

                String firstName = parts[0];
                String lastName = parts.length > 1 ? String.join(" ", Arrays.copyOfRange(parts, 1, parts.length)) : "";
                Trainer trainer = trainerService.findByVornameAndNachname(firstName, lastName);
                if (trainer == null) {
                    errorList.add(String.format("The given trainer %s does not exist!", trainerDto.getName()));
                    continue;
                }
                LocalDate date = parseDate(recommendationDto.getDate());
                List<TrainerScheduleDto> trainerScheduleDtos = trainerService.isTrainerAvailableForDate(date, firstName, lastName);
                if (trainerScheduleDtos.isEmpty()) {
                    errorList.add(String.format("The given trainer %s is not available at %s!", trainerDto.getName(), recommendationDto.getDate()));
                    continue;
                }
                if (!seminars.isEmpty()) {
                    PresenceSchedule seminarSchedule = seminars.get(0).getPresenceSchedules().stream().filter(sch -> sch.getDay().equals(date.getDayOfWeek())).findFirst().orElse(null);
                    AvailabilityScheduleType timeOfDay = AvailabilityScheduleType.from(seminars.get(0).getStartTime(), seminars.get(0).getEndTime());
                    if(seminarSchedule != null){
                        long count = trainerScheduleDtos.stream().filter(sch -> sch.getTag().equals(seminarSchedule.getDay()) && sch.getPraesenzOnline().equals(seminarSchedule.getType()) && sch.getTagSchicht().equals(timeOfDay)).count();
                        if (count == 0) {
                            errorList.add(String.format("For seminar %s and trainer %s there is no availability match at day %s with präsenz type %s", recommendationDto.getSeminar(), trainerDto.getName(), date, seminarSchedule.getType()));
                        }
                    }
                }
            }
        }*/

        return errorList;
    }
}
