package com.ibosng.aiservice.services.impl;

import com.ibosng.aiservice.services.SeminarvertretungService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class SeminarvertretungServiceImpl implements SeminarvertretungService {    
/*    private final SeminarService seminarService;
    private final TrainerService trainerService;
    private final VoraussetzungService voraussetzungService;

    @Override
    public ResponseDto<SeminarDto> findAllSeminarsByTrainerVornameAndTrainerNachname(String vorname, String nachname, int page, int size) {
        return seminarService.findAllByTrainerVornameAndTrainerNachname(vorname, nachname, page, size);
    }

    @Override
    public Page<SeminarScheduleDto> findSeminarDetailsBySeminarBezeichnung(String bezeichnung, int page, int size) {
        return seminarService.findSeminarDetailsBySeminarBezeichnung(bezeichnung, page, size);
    }

    @Override
    public Page<Voraussetzung> findVoraussetzungenBySeminarBezeichnungen(List<String> seminarNames, int page, int size) {
        return voraussetzungService.findVoraussetzungenBySeminarBezeichnungen(seminarNames, page, size);
    }

    @Override
    public ResponseDto<TrainerDto> findTrainers(
        String from, 
        String to, 
        List<String> voraussetzungen, 
        String praesenzType, 
        int page,
        int size
    ) {
        LocalDate fromDate = isValidDate(from) ? parseDate(from) : LocalDate.now();
        LocalDate toDate = isValidDate(to) ? parseDate(to) : LocalDate.now().plusMonths(7);

        return trainerService.findTrainersScheduleByVoraussetzungenAndPraesenzType(
            fromDate, 
            toDate, 
            voraussetzungen, 
            praesenzType, 
            page, 
            size
        );
    }*/
}
