package com.ibosng.validationservice.teilnehmer.validations.manual;

import com.ibosng.dbibosservice.entities.SeminarIbos;
import com.ibosng.dbibosservice.services.SeminarIbosService;
import com.ibosng.dbmapperservice.services.SeminarProjektMapperService;
import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.seminar.Seminar;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer2Seminar;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStatus;
import com.ibosng.dbservice.services.BetreuerService;
import com.ibosng.dbservice.services.RgsService;
import com.ibosng.dbservice.services.SeminarService;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.ibosng.dbservice.utils.Helpers.findFirstObject;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.validationservice.utils.Constants.VALIDATION_SERVICE;

@Getter
@Setter
@Slf4j
@RequiredArgsConstructor
public class TeilnehmerDtoSeminarValidation extends AbstractValidation<TeilnehmerDto, Teilnehmer> {
    private final SeminarService seminarService;
    private final SeminarIbosService seminarIbosService;
    private final RgsService rgsService;
    private final BetreuerService betreuerService;
    private final SeminarProjektMapperService seminarProjektMapperService;
    private final GlobalUserHolder globalUserHolder;

    @Getter
    @Setter
    private boolean isValid = true;


    @Override
    public boolean executeValidation(TeilnehmerDto teilnehmerDto, Teilnehmer teilnehmer) {
        setValid(true);
        Seminar seminar = getSeminarForTeilnehmer(teilnehmerDto);
        if (seminar != null) {
            if (!teilnehmer.getSeminars().contains(seminar)) {
                Teilnehmer2Seminar teilnehmer2Seminar = findTeilnehmer2Seminar(teilnehmerDto, teilnehmer.getTeilnehmerSeminars().stream().filter(t2s -> t2s.getStatus().equals(TeilnehmerStatus.INVALID)).toList());
                if (teilnehmer2Seminar == null) {
                    teilnehmer2Seminar = teilnehmer.addSeminar(seminar);
                }
                teilnehmer2Seminar.setSeminar(seminar);
                setTeilnehmer2SeminarFields(teilnehmer2Seminar, teilnehmerDto);
                teilnehmer2Seminar.setStatus(TeilnehmerStatus.VALID);
            } else {
                Teilnehmer2Seminar alreadyExistingSeminar = teilnehmer.getTeilnehmerSeminars().stream().filter(sem -> sem.getSeminar().equals(seminar)).findFirst().orElse(null);
                if (alreadyExistingSeminar != null) {
                    setTeilnehmer2SeminarFields(alreadyExistingSeminar, teilnehmerDto);
                }
            }
            return isValid();
        } else {
            if (checkForMassnahmenummer(teilnehmerDto, teilnehmer)) {
                return isValid();
            }
        }
        teilnehmer.addError("seminar", "Ungültiges Seminar angegeben", globalUserHolder.getUsername());
        return isValid();
    }

    private boolean checkForMassnahmenummer(TeilnehmerDto teilnehmerDto, Teilnehmer teilnehmer) {
        if (!isNullOrBlank(teilnehmerDto.getMassnahmennummer())) {
            Teilnehmer2Seminar teilnehmer2Seminar = findTeilnehmer2Seminar(teilnehmerDto, teilnehmer.getTeilnehmerSeminars());
            if (teilnehmer2Seminar == null) {
                teilnehmer2Seminar = new Teilnehmer2Seminar();
                teilnehmer2Seminar.setTeilnehmer(teilnehmer);
                teilnehmer2Seminar.setStatus(TeilnehmerStatus.INVALID);
                teilnehmer.getTeilnehmerSeminars().add(teilnehmer2Seminar);
            }
            setTeilnehmer2SeminarFields(teilnehmer2Seminar, teilnehmerDto);
            if (teilnehmer2Seminar.getStatus().equals(TeilnehmerStatus.VALID)) {
                return true;
            }
        }
        return false;
    }

    private Teilnehmer2Seminar findTeilnehmer2Seminar(TeilnehmerDto teilnehmerDto, List<Teilnehmer2Seminar> teilnehmer2Seminars) {
        for (Teilnehmer2Seminar t2s : teilnehmer2Seminars) {
            boolean isMassnahmenummerSame = !isNullOrBlank(t2s.getMassnahmennummer()) && !isNullOrBlank(teilnehmerDto.getMassnahmennummer())
                    && t2s.getMassnahmennummer().equals(teilnehmerDto.getMassnahmennummer());
            if (isMassnahmenummerSame) {
                return t2s;
            }
        }
        return null;
    }

    private void setTeilnehmer2SeminarFields(Teilnehmer2Seminar teilnehmer2Seminar, TeilnehmerDto teilnehmerDto) {
        boolean result = true;
        if (teilnehmer2Seminar.getSeminar() != null) {
            teilnehmerDto.setSeminarBezeichnung(teilnehmer2Seminar.getSeminar().getBezeichnung());
            teilnehmerDto.setSeminarNumber(teilnehmer2Seminar.getSeminar().getSeminarNummer());
        }
        AbstractValidation<TeilnehmerDto, Teilnehmer2Seminar> einAustrittValidation = new TeilnehmerDtoEinAustrittsdatumValidator(globalUserHolder);
        einAustrittValidation.setSources(getSources());
        if (einAustrittValidation.shouldValidationRun()) {
            result = einAustrittValidation.executeValidation(teilnehmerDto, teilnehmer2Seminar);
        }

        AbstractValidation<TeilnehmerDto, Teilnehmer2Seminar> zubuchungValidation = new TeilnehmerDtoZubuchungValidation(globalUserHolder);
        zubuchungValidation.setSources(getSources());
        if (zubuchungValidation.shouldValidationRun()) {
            result = result && zubuchungValidation.executeValidation(teilnehmerDto, teilnehmer2Seminar);
        }

        AbstractValidation<TeilnehmerDto, Teilnehmer2Seminar> rgsValidation = new TeilnehmerDtoRgsValidation(rgsService, globalUserHolder);
        rgsValidation.setSources(getSources());
        if (rgsValidation.shouldValidationRun()) {
            result = result && rgsValidation.executeValidation(teilnehmerDto, teilnehmer2Seminar);
        }

        AbstractValidation<TeilnehmerDto, Teilnehmer2Seminar> geplantValidation = new TeilnehmerDtoGeplantValidation(globalUserHolder);
        geplantValidation.setSources(getSources());
        if (geplantValidation.shouldValidationRun()) {
            result = result && geplantValidation.executeValidation(teilnehmerDto, teilnehmer2Seminar);
        }

        AbstractValidation<TeilnehmerDto, Teilnehmer2Seminar> betreuerValidation = new TeilnehmerDtoBetreuerValidation(betreuerService, globalUserHolder);

        result = result && betreuerValidation.executeValidation(teilnehmerDto, teilnehmer2Seminar);
        setValid(isValid() && result);

        if (!isNullOrBlank(teilnehmerDto.getBuchungsstatus())) {
            teilnehmer2Seminar.setBuchungsstatus(teilnehmerDto.getBuchungsstatus());
        }
        if (!isNullOrBlank(teilnehmerDto.getAnmerkung())) {
            teilnehmer2Seminar.setAnmerkung(teilnehmerDto.getAnmerkung());
        }
        if (!isNullOrBlank(teilnehmerDto.getMassnahmennummer())) {
            teilnehmer2Seminar.setMassnahmennummer(teilnehmerDto.getMassnahmennummer());
        }
        if (!isNullOrBlank(teilnehmerDto.getVeranstaltungsnummer())) {
            teilnehmer2Seminar.setVeranstaltungsnummer(teilnehmerDto.getVeranstaltungsnummer());
        }
    }

    private Seminar getSeminarForTeilnehmer(TeilnehmerDto teilnehmerDto) {
        List<Seminar> seminars = new ArrayList<>();
        Seminar seminar;
        if (teilnehmerDto.getSeminarNumber() != null) { // Try to find by seminar number
            seminars = seminarService.findAllBySeminarNummer(teilnehmerDto.getSeminarNumber());
        } else {
            seminars = seminarService.findByBezeichnung(teilnehmerDto.getSeminarBezeichnung());
        }
        seminar = findFirstObject(seminars,
                Set.of(teilnehmerDto.getSeminarBezeichnung() != null ? teilnehmerDto.getSeminarBezeichnung() : ""),
                "seminar");


        //The seminar does not exist in the ibosNG DB, try and find it in the ibos DB
        if (seminar == null) {
            SeminarIbos seminarIbos = null;
            if (teilnehmerDto.getSeminarNumber() != null) {
                Optional<SeminarIbos> optionalSeminarIbos = seminarIbosService.findById(teilnehmerDto.getSeminarNumber());
                if (optionalSeminarIbos.isPresent()) {
                    seminarIbos = optionalSeminarIbos.get();
                }
            }
            if (seminarIbos != null) {
                seminar = seminarProjektMapperService.mapSeminarIbosToSeminar(seminarIbos);
                Set<Seminar> seminarSet = new HashSet<>(seminarService.findAll());
                if (!seminarSet.contains(seminar)) {
                    seminar.setStatus(Status.ACTIVE);
                    seminar.setCreatedBy(VALIDATION_SERVICE);
                    seminar = seminarService.save(seminar);
                } else {
                    for (Seminar s : seminarSet) {
                        if (s.equals(seminar)) {
                            seminar = s;
                            break;
                        }
                    }
                }
            }
        }
        return seminar;
    }
}