package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.entities.Land;
import com.ibosng.dbservice.entities.teilnehmer.*;
import com.ibosng.dbservice.repositories.TeilnehmerStagingRepository;
import com.ibosng.dbservice.services.TeilnehmerStagingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.dbservice.utils.Parsers.parseDate;

@Service
@Slf4j
public class TeilnehmerStagingServiceImpl implements TeilnehmerStagingService {

    private final TeilnehmerStagingRepository teilnehmerStagingRepository;

    @Autowired
    public TeilnehmerStagingServiceImpl(TeilnehmerStagingRepository teilnehmerStagingRepository) {
        this.teilnehmerStagingRepository = teilnehmerStagingRepository;
    }

    @Override
    public Optional<TeilnehmerStaging> findById(Integer id) {
        return teilnehmerStagingRepository.findById(id);
    }

    @Override
    public List<TeilnehmerStaging> findAll() {
        return teilnehmerStagingRepository.findAll();
    }

    @Override
    public TeilnehmerStaging save(TeilnehmerStaging teilnehmer) {
        return teilnehmerStagingRepository.save(teilnehmer);
    }

    @Override
    public List<TeilnehmerStaging> saveAll(List<TeilnehmerStaging> teilnehmers) {
        return teilnehmerStagingRepository.saveAll(teilnehmers);
    }

    @Override
    public void deleteById(Integer id) {
        this.teilnehmerStagingRepository.deleteById(id);
    }


    @Override
    public List<TeilnehmerStaging> findAllByIdentifier(String importFilename) {
        return teilnehmerStagingRepository.findAllByImportFilename(importFilename);
    }

    @Override
    public List<TeilnehmerStaging> findByImportFilenameAndStatusIn(String importFilename, List<TeilnehmerStatus> statuses) {
        return teilnehmerStagingRepository.findByImportFilenameAndStatusIn(importFilename, statuses);
    }

    @Override
    public void deleteAllByIdentifier(String identifier) {
        teilnehmerStagingRepository.deleteByIdentifier(identifier);
    }

    @Override
    public List<TeilnehmerStaging> findByImportFilenameAndTeilnehmerId(String filename, int teilnehmerId) {
        return teilnehmerStagingRepository.findByImportFilenameAndTeilnehmerId(filename, teilnehmerId);
    }

    @Override
    public List<TeilnehmerStaging> findAllByImportFilenameAndTeilnehmerIdAndStatus(String filename, int teilnehmerId, TeilnehmerStatus status) {
        return teilnehmerStagingRepository.findAllByImportFilenameAndTeilnehmerIdAndStatus(filename, teilnehmerId, status);
    }

    @Override
    public List<TeilnehmerStaging> findByTeilnehmerId(Integer teilnhemherId) {
        return teilnehmerStagingRepository.findByTeilnehmerId(teilnhemherId);
    }

    @Override
    public List<TeilnehmerStaging> findByVornameAndNachname(String vorname, String nachname) {
        return teilnehmerStagingRepository.findByVornameAndNachname(vorname, nachname);
    }

    @Override
    public List<TeilnehmerStaging> findAllByTeilnehmerIdAndStatus(Integer teilnehmerId, TeilnehmerStatus status) {
        return teilnehmerStagingRepository.findAllByTeilnehmerIdAndStatus(teilnehmerId, status);
    }

    @Override
    public List<TeilnehmerDto> mapTeilnehmer2TeilnehmerDto(Teilnehmer teilnehmer, boolean isFiltered, boolean isSeminarPresent, String seminarIdentifier, boolean isFromValidationService) {
        List<TeilnehmerDto> dtos = getDtosForAllSeminars(teilnehmer, isFiltered, isSeminarPresent, seminarIdentifier, isFromValidationService);
        dtos.forEach(dto -> mapTeilnehmerToDto(teilnehmer, dto, isSeminarPresent));
        return dtos;
    }

    public void mapTeilnehmerToDto(Teilnehmer teilnehmer, TeilnehmerDto dto, boolean isSeminarPresent) {
        Optional.ofNullable(teilnehmer.getId()).ifPresent(dto::setId);
        Optional.ofNullable(teilnehmer.getTitel()).ifPresent(dto::setTitel);
        Optional.ofNullable(teilnehmer.getTitel2()).ifPresent(dto::setTitel2);
        Optional.ofNullable(teilnehmer.isUeba()).ifPresent(dto::setUeba);
        Optional.ofNullable(teilnehmer.getNachname()).ifPresent(dto::setNachname);
        Optional.ofNullable(teilnehmer.getVorname()).ifPresent(dto::setVorname);
        Optional.ofNullable(teilnehmer.getGeschlecht()).ifPresent(g -> dto.setGeschlecht(g.getName()));
        Optional.ofNullable(teilnehmer.getSvNummer()).ifPresent(sv -> dto.setSvNummer(String.valueOf(sv)));
        Optional.ofNullable(teilnehmer.getGeburtsdatum()).ifPresent(gd -> dto.setGeburtsdatum(parseDate(gd.toString()).toString()));
        Optional.ofNullable(teilnehmer.getEmail()).ifPresent(dto::setEmail);
        Optional.ofNullable(teilnehmer.getAnrede()).ifPresent(anrede -> {
            dto.setAnrede(anrede.getName());
        });
        Optional.ofNullable(teilnehmer.getMuttersprache()).ifPresent(muttersprache -> {
            dto.setMuttersprache(muttersprache.getName());
        });

        Optional.ofNullable(teilnehmer.getPersonalnummer()).ifPresent(pnr -> {
            dto.setPersonalnummer(pnr.getPersonalnummer());
        });

        Optional.ofNullable(teilnehmer.getAdresse()).ifPresent(adresse -> {
            Optional.ofNullable(adresse.getPlz()).ifPresent(plz -> dto.setPlz(plz.getPlzString()));
            Optional.ofNullable(adresse.getOrt()).ifPresent(dto::setOrt);
            Optional.ofNullable(adresse.getStrasse()).ifPresent(dto::setStrasse);
        });

        Optional.ofNullable(teilnehmer.getNation())
                .filter(nations -> !nations.isEmpty())
                .ifPresent(nations ->
                        dto.setNation(nations.stream().map(Land::getLandName).collect(Collectors.joining(", "))));
        List<TeilnehmerDataStatus> errors = teilnehmer.getErrors();
        if (isSeminarPresent) {
            errors.removeIf(error -> "seminar".equals(error.getError()));
        }
        for (TeilnehmerDataStatus dataStatus : errors) {
            dto.getErrorsMap().put(dataStatus.getError(), dataStatus.getCause());
        }
        if(teilnehmer.getUrsprung() != null && teilnehmer.getUrsprung().getLand() != null && teilnehmer.getUrsprung().getLand().getLandName() != null) {
            dto.setUrsprungsland(teilnehmer.getUrsprung().getLand().getLandName());
        }
        if(teilnehmer.getUrsprung() != null && teilnehmer.getUrsprung().getLand() != null &&  !isNullOrBlank(teilnehmer.getUrsprung().getOrt())) {
            dto.setGeburtsort(teilnehmer.getUrsprung().getOrt());
        }
        dto.setErrors(new ArrayList<>(dto.getErrorsMap().keySet()));
    }

    @Override
    public List<TeilnehmerStaging> findAllByTeilnehmerIdAndStatusOrderByCreatedOnDesc(Integer teilnehmerId, TeilnehmerStatus status) {
        return teilnehmerStagingRepository.findAllByTeilnehmerIdAndStatusOrderByCreatedOnDesc(teilnehmerId, status);
    }

    @Override
    public List<TeilnehmerStaging> findAllByTeilnehmerIdAndStatusInOrderByCreatedOnDesc(Integer teilnehmerId, List<TeilnehmerStatus> statuses) {
        return teilnehmerStagingRepository.findAllByTeilnehmerIdAndStatusInOrderByCreatedOnDesc(teilnehmerId, statuses);
    }

    @Override
    public List<TeilnehmerStaging> findFirstByImportFilenameAndTeilnehmerIdOrderByCreatedOnDesc(String filename, int teilnehmerId) {
        return teilnehmerStagingRepository.findFirstByImportFilenameAndTeilnehmerIdOrderByCreatedOnDesc(filename, teilnehmerId);
    }

    @Override
    public List<TeilnehmerStaging> findByTeilnehmerIdAndSeminarIdentifierOrderByCreatedOnDesc(int teilnehmerId, String seminarIdentifier) {
        return teilnehmerStagingRepository.findByTeilnehmerIdAndSeminarIdentifierOrderByCreatedOnDesc(teilnehmerId, seminarIdentifier);
    }

    private List<TeilnehmerDto> getDtosForAllSeminars(Teilnehmer teilnehmer, boolean isFiltered, boolean isSeminarPresent, String seminarIdentifier, boolean isFromValidationService) {
        List<TeilnehmerDto> dtos = new ArrayList<>();
        List<TeilnehmerStaging> teilnehmerStagingList;
        if (isFromValidationService) {
            teilnehmerStagingList = findFirstByImportFilenameAndTeilnehmerIdOrderByCreatedOnDesc(teilnehmer.getImportFilename(), teilnehmer.getId());
        } else {
            teilnehmerStagingList = findAllByTeilnehmerIdAndStatusInOrderByCreatedOnDesc(teilnehmer.getId(), List.of(TeilnehmerStatus.VALID, TeilnehmerStatus.INVALID));
        }
        List<Teilnehmer2Seminar> validTeilnehmer2Seminar = teilnehmer.getTeilnehmerSeminars().stream().filter(t2s -> t2s.getStatus().equals(TeilnehmerStatus.VALID)).toList();
        List<Teilnehmer2Seminar> invalidTeilnehmer2Seminar = teilnehmer.getTeilnehmerSeminars().stream().filter(t2s -> t2s.getStatus().equals(TeilnehmerStatus.INVALID)).toList();
        TeilnehmerDataStatusProcessor teilnehmerDataStatusProcessor = new TeilnehmerDataStatusProcessor();

        if (isFiltered) {
            TeilnehmerStaging teilnehmerSt;
            if (isSeminarPresent) {
                for (Teilnehmer2Seminar t2s : validTeilnehmer2Seminar) {
                    if (isSeminarValid(t2s, teilnehmer)) {
                        teilnehmerSt = teilnehmerStagingList.stream().filter(teilnehmerStaging -> isTeilnehmer2SeminarSame(t2s, teilnehmerStaging, isFiltered, true, seminarIdentifier)).findFirst().orElse(null);
                        if (teilnehmerSt != null) {
                            setDtoValuesFromTeilnehmer2SeminarAndStaging(teilnehmer, teilnehmerDataStatusProcessor, dtos, t2s, teilnehmerSt);
                        }
                    }
                }
            } else {
                for (Teilnehmer2Seminar t2s : invalidTeilnehmer2Seminar) {
                    if (isSeminarValid(t2s, teilnehmer)) {
                        teilnehmerSt = teilnehmerStagingList.stream().filter(teilnehmerStaging -> isTeilnehmer2SeminarSame(t2s, teilnehmerStaging, isFiltered, false, seminarIdentifier)).findFirst().orElse(null);
                        if (teilnehmerSt != null) {
                            setDtoValuesFromTeilnehmer2SeminarAndStaging(teilnehmer, teilnehmerDataStatusProcessor, dtos, t2s, teilnehmerSt);
                        }
                    }
                }
            }
        } else {
            TeilnehmerStaging teilnehmerSt;
            for (Teilnehmer2Seminar t2s : teilnehmer.getTeilnehmerSeminars()) {
                if (isSeminarValid(t2s, teilnehmer)) {
                    teilnehmerStagingList = findByTeilnehmerId(teilnehmer.getId());
                    teilnehmerSt = teilnehmerStagingList.stream().filter(teilnehmerStaging -> isTeilnehmer2SeminarSame(t2s, teilnehmerStaging, isFiltered, false, seminarIdentifier)).findFirst().orElse(null);
                    if (teilnehmerSt != null) {
                        setDtoValuesFromTeilnehmer2SeminarAndStaging(teilnehmer, teilnehmerDataStatusProcessor, dtos, t2s, teilnehmerSt);
                    }
                }
            }
            if (dtos.isEmpty() && !teilnehmerStagingList.isEmpty()) {
                TeilnehmerStaging teilnehmerStaging = teilnehmerStagingList.stream()
                        .max(Comparator.comparing(TeilnehmerStaging::getCreatedOn))
                        .orElse(null);

                setDtoValuesFromTeilnehmer2SeminarAndStaging(teilnehmer, teilnehmerDataStatusProcessor, dtos, null, teilnehmerStaging);

            }
        }

        return dtos;
    }

    private boolean isSeminarValid(Teilnehmer2Seminar t2s, Teilnehmer teilnehmer) {
        return teilnehmer.getErrors().stream().noneMatch(error -> "seminar".equals(error.getError()));
    }

    private void setDtoValuesFromTeilnehmer2SeminarAndStaging(Teilnehmer teilnehmer, TeilnehmerDataStatusProcessor teilnehmerDataStatusProcessor, List<TeilnehmerDto> dtos, Teilnehmer2Seminar teilnehmer2Seminar, TeilnehmerStaging teilnehmerStaging) {
        TeilnehmerDto dto = new TeilnehmerDto();
        matchTeilnehmer2SeminarAndStaging(teilnehmer2Seminar, teilnehmerStaging, dto);
        teilnehmerDataStatusProcessor.setErrorFields(teilnehmer, teilnehmerStaging, dto);
        dtos.add(dto);
    }

    private void matchTeilnehmer2SeminarAndStaging(Teilnehmer2Seminar teilnehmer2Seminar, TeilnehmerStaging teilnehmerStaging, TeilnehmerDto dto) {
        if (teilnehmer2Seminar != null) {
            setDataFromTeilnehmer2Seminar(teilnehmer2Seminar, dto);
        } else {
            if (!isNullOrBlank(teilnehmerStaging.getSeminarType()) && !isNullOrBlank(teilnehmerStaging.getSeminarIdentifier())) {
                // Concatenate SeminarType and SeminarIdentifier if both are not blank
                dto.setSeminarBezeichnung(teilnehmerStaging.getSeminarType() + " " + teilnehmerStaging.getSeminarIdentifier());
            } else if (!isNullOrBlank(teilnehmerStaging.getSeminarType())) {
                // Set SeminarType if it's not blank and SeminarIdentifier is blank or vice versa
                dto.setSeminarBezeichnung(teilnehmerStaging.getSeminarType());
            } else if (!isNullOrBlank(teilnehmerStaging.getSeminarIdentifier())) {
                dto.setSeminarBezeichnung(teilnehmerStaging.getSeminarIdentifier());
            }
        }
        if (!isNullOrBlank(teilnehmerStaging.getTelefon())) {
            dto.setTelefon(teilnehmerStaging.getTelefon());
        } else {
            dto.setTelefon(teilnehmerStaging.getTelefonNummer());
        }
        dto.setSource(teilnehmerStaging.getSource().getCode());
    }

    private boolean isTeilnehmer2SeminarSame(Teilnehmer2Seminar t2s, TeilnehmerStaging teilnehmerStaging, boolean isFiltered, boolean isSeminarPresent, String seminarIdentifier) {
        boolean isSeminarSame = t2s.getSeminar() != null && t2s.getSeminar().getIdentifier().equals(teilnehmerStaging.getSeminarIdentifier())
                && t2s.getSeminar().getBezeichnung().equals(teilnehmerStaging.getSeminarType());
        boolean isMassnahmenummerSame = !isNullOrBlank(t2s.getMassnahmennummer()) && !isNullOrBlank(teilnehmerStaging.getMassnahmennummer())
                && t2s.getMassnahmennummer().equals(teilnehmerStaging.getMassnahmennummer());
        if (!isFiltered) {
            return isSeminarSame || isMassnahmenummerSame;
        } else {
            if (isSeminarPresent) {
                return isSeminarSame && t2s.getSeminar().getBezeichnung().equals(seminarIdentifier);
            } else {
                return t2s.getSeminar() == null && isMassnahmenummerSame && t2s.getMassnahmennummer().equals(seminarIdentifier);
            }
        }
    }

    private void setDataFromTeilnehmer2Seminar(Teilnehmer2Seminar teilnehmer2Seminar, TeilnehmerDto dto) {
        Optional.ofNullable(teilnehmer2Seminar.getSeminar()).ifPresent(seminar -> {
            dto.setSeminarNumber(seminar.getSeminarNummer());
            dto.setSeminarBezeichnung(seminar.getBezeichnung());
        });

        Optional.ofNullable(teilnehmer2Seminar.getBetreuer()).ifPresent(betreuer -> {
            Optional.ofNullable(betreuer.getTitel()).ifPresent(dto::setBetreuerTitel);
            Optional.ofNullable(betreuer.getNachname()).ifPresent(dto::setBetreuerNachname);
            Optional.ofNullable(betreuer.getVorname()).ifPresent(dto::setBetreuerVorname);
        });

        Optional.ofNullable(teilnehmer2Seminar.getBuchungsstatus()).ifPresent(dto::setBuchungsstatus);
        Optional.ofNullable(teilnehmer2Seminar.getAnmerkung()).ifPresent(dto::setAnmerkung);
        Optional.ofNullable(teilnehmer2Seminar.getGeplant()).ifPresent(geplant -> dto.setGeplant(geplant.toString()));
        Optional.ofNullable(teilnehmer2Seminar.getEintritt()).ifPresent(eintritt -> dto.setEintritt(eintritt.toString()));
        Optional.ofNullable(teilnehmer2Seminar.getAustritt()).ifPresent(austritt -> dto.setAustritt(austritt.toString()));
        Optional.ofNullable(teilnehmer2Seminar.getMassnahmennummer()).ifPresent(dto::setMassnahmennummer);
        Optional.ofNullable(teilnehmer2Seminar.getVeranstaltungsnummer()).ifPresent(dto::setVeranstaltungsnummer);
        Optional.ofNullable(teilnehmer2Seminar.getZubuchung()).ifPresent(zubuchung -> dto.setZubuchung(zubuchung.toString()));

        Optional.ofNullable(teilnehmer2Seminar.getRgs()).ifPresent(rgs -> {
            Optional.ofNullable(rgs.getRgs()).ifPresent(rgsValue -> dto.setRgs(rgsValue.toString()));
            Optional.ofNullable(rgs.getBezeichnung()).ifPresent(dto::setRgsBezeichnung);
        });
    }
}
