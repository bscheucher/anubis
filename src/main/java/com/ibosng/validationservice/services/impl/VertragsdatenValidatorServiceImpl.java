package com.ibosng.validationservice.services.impl;

import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.masterdata.Verwendungsgruppe;
import com.ibosng.dbservice.entities.mitarbeiter.ArbeitszeitenInfo;
import com.ibosng.dbservice.entities.mitarbeiter.GehaltInfo;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.services.impl.masterdata.JobticketServiceImpl;
import com.ibosng.dbservice.services.impl.masterdata.VerwendungsgruppeServiceImpl;
import com.ibosng.dbservice.services.mitarbeiter.ArbeitszeitenInfoService;
import com.ibosng.dbservice.services.mitarbeiter.GehaltInfoService;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.dbservice.services.mitarbeiter.VertragsdatenService;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.services.VertragsdatenValidatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Service
@Slf4j
@RequiredArgsConstructor
public class VertragsdatenValidatorServiceImpl implements VertragsdatenValidatorService {

    private final VertragsdatenService vertragsdatenService;
    private final ArbeitszeitenInfoService arbeitszeitenInfoService;
    private final GehaltInfoService gehaltInfoService;
    private final PersonalnummerService personalnummerService;
    private final VerwendungsgruppeServiceImpl verwendungsgruppeService;
    private final JobticketServiceImpl jobticketService;
    private final GlobalUserHolder globalUserHolder;

    @Override
    public Vertragsdaten getVertragsdaten(VertragsdatenDto vertragsdatenDto, Boolean isOnboarding) {
        if (isNullOrBlank(vertragsdatenDto.getPersonalnummer())) {
            log.error("No personalnummer");
            return null;
        }
        Vertragsdaten vertragsdaten;
        if (vertragsdatenDto.getId() != null) {
            vertragsdaten = vertragsdatenService.findById(vertragsdatenDto.getId())
                    .orElseGet(() -> createNewVertragsdaten(vertragsdatenDto.getPersonalnummer()));
        } else {
            vertragsdaten = createNewVertragsdaten(vertragsdatenDto.getPersonalnummer());
        }

        if (vertragsdaten == null) {
            log.error("No vertragsdaten created for dto: {}", vertragsdatenDto);
            return null;
        }

        if (!isNullOrBlank(vertragsdatenDto.getPersonalnummer())) {
            vertragsdaten.setErrors(new ArrayList<>());
        }
        setAndSaveGehaltInfo(vertragsdaten, vertragsdatenDto);
        vertragsdaten.setChangedOn(LocalDateTime.now());
        vertragsdaten.setChangedBy(globalUserHolder.getUsername());
        return vertragsdatenService.save(vertragsdaten);
    }

    public void setAndSaveGehaltInfo(Vertragsdaten vertragsdaten, VertragsdatenDto vertragsdatenDto) {
        GehaltInfo gehaltInfo = gehaltInfoService.findByVertragsdatenId(vertragsdaten.getId());
        if (gehaltInfo == null) {
            return;
        }
        if (vertragsdatenDto.getGehaltVereinbart() != null) {
            gehaltInfo.setGehaltVereinbart(BigDecimal.valueOf(vertragsdatenDto.getGehaltVereinbart()));
        } else {
            gehaltInfo.setGehaltVereinbart(null);
        }
        if (!isNullOrBlank(vertragsdatenDto.getVerwendungsgruppe())) {
            Verwendungsgruppe verwendungsgruppe = verwendungsgruppeService.findByName(vertragsdatenDto.getVerwendungsgruppe());
            if (verwendungsgruppe != null) {
                gehaltInfo.setVerwendungsgruppe(verwendungsgruppe);
            }
        }
        if (vertragsdatenDto.getFacheinschlaegigeTaetigkeitenGeprueft() != null) {
            gehaltInfo.setFacheinschlaegigeTaetigkeitenGeprueft(vertragsdatenDto.getFacheinschlaegigeTaetigkeitenGeprueft());
        }
        if (vertragsdatenDto.getAngerechneteIbisMonate() != null) {
            gehaltInfo.setAngerechneteIbisMonate(vertragsdatenDto.getAngerechneteIbisMonate());
        }
        if (vertragsdatenDto.getAngerechneteFacheinschlaegigeTaetigkeitenMonate() != null) {
            gehaltInfo.setAngerechneteFacheinschlaegigeTaetigkeitenMonate(vertragsdatenDto.getAngerechneteFacheinschlaegigeTaetigkeitenMonate());
        }
        if (vertragsdatenDto.getUeberzahlung() != null) {
            gehaltInfo.setUeberzahlung(BigDecimal.valueOf(vertragsdatenDto.getUeberzahlung()));
        }
        gehaltInfo.setVereinbarungUEberstunden(vertragsdatenDto.getVereinbarungUEberstunden());
        if (vertragsdatenDto.getUestPauschale() != null) {
            gehaltInfo.setUestPauschale(BigDecimal.valueOf(vertragsdatenDto.getUestPauschale()));
        }
        if (vertragsdatenDto.getJobticket() != null && !isNullOrBlank(vertragsdatenDto.getJobticketTitle())) {
            gehaltInfo.setJobticket(jobticketService.findByName(vertragsdatenDto.getJobticketTitle()).orElse(null));
        }

        gehaltInfo.setNotizGehalt(vertragsdatenDto.getNotizGehalt());
        gehaltInfo.setDeckungspruefung(vertragsdatenDto.getDeckungspruefung());

        gehaltInfo.setChangedBy(globalUserHolder.getUsername());
        gehaltInfo.setChangedOn(getLocalDateNow());
        gehaltInfoService.save(gehaltInfo);
        log.info("Gehaltinfo for vertragsdaten {} saved", vertragsdaten.getId());

    }

    protected Vertragsdaten createNewVertragsdaten(String personalnummerString) {
        Personalnummer personalnummer = personalnummerService.findByPersonalnummer(personalnummerString);
        if (personalnummer != null) {
            Vertragsdaten vertragsdaten = new Vertragsdaten();
            vertragsdaten.setPersonalnummer(personalnummer);
            vertragsdaten.setStatus(MitarbeiterStatus.NEW);
            vertragsdaten.setCreatedBy(globalUserHolder.getUsername());
            vertragsdaten = vertragsdatenService.save(vertragsdaten);

            ArbeitszeitenInfo arbeitszeitenInfo = new ArbeitszeitenInfo();
            arbeitszeitenInfo.setCreatedBy(globalUserHolder.getUsername());
            arbeitszeitenInfo.setStatus(MitarbeiterStatus.NEW);
            arbeitszeitenInfo.setVertragsdaten(vertragsdaten);
            arbeitszeitenInfoService.save(arbeitszeitenInfo);

            GehaltInfo gehaltInfo = new GehaltInfo();
            gehaltInfo.setCreatedBy(globalUserHolder.getUsername());
            gehaltInfo.setStatus(MitarbeiterStatus.NEW);
            gehaltInfo.setVertragsdaten(vertragsdaten);
            gehaltInfoService.save(gehaltInfo);

            return vertragsdaten;
        }
        return null;
    }
}
