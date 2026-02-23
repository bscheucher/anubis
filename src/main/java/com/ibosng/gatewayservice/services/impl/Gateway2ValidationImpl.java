package com.ibosng.gatewayservice.services.impl;

import com.ibosng.dbmapperservice.services.TeilnehmerMapperService;
import com.ibosng.dbservice.dtos.SeminarDto;
import com.ibosng.dbservice.dtos.SeminarPruefungDto;
import com.ibosng.dbservice.dtos.SprachkenntnisDto;
import com.ibosng.dbservice.dtos.TeilnehmerNotizDto;
import com.ibosng.dbservice.dtos.TnAusbildungDto;
import com.ibosng.dbservice.dtos.TnBerufserfahrungDto;
import com.ibosng.dbservice.dtos.TnZertifikatDto;
import com.ibosng.dbservice.dtos.ZeitbuchungSyncRequestDto;
import com.ibosng.dbservice.dtos.ZeitbuchungenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.UnterhaltsberechtigteDto;
import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.VordienstzeitenDto;
import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.dtos.zeiterfassung.ZeiterfassungTransferDto;
import com.ibosng.dbservice.entities.workflows.WWorkflowStatus;
import com.ibosng.gatewayservice.services.Gateway2Validation;
import com.ibosng.validationservice.services.KVEinstufungService;
import com.ibosng.validationservice.services.MitarbeiterSyncService;
import com.ibosng.validationservice.services.ValidatorService;
import com.ibosng.validationservice.services.ZeitbuchungenValidatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.ibosng.gatewayservice.utils.Constants.GATEWAY_SERVICE;

@Slf4j
@Component
@RequiredArgsConstructor
public class Gateway2ValidationImpl implements Gateway2Validation {

    private final TeilnehmerMapperService teilnehmerMapperService;
    private final ValidatorService validatorService;
    private final KVEinstufungService kvEinstufungService;
    private final ZeitbuchungenValidatorService zeitbuchungenValidatorService;
    private final MitarbeiterSyncService mitarbeiterSyncService;

    @Override//todo
    public List<ZeitbuchungenDto> validateZeitbuchungen(ZeitbuchungSyncRequestDto zeitbuchungSyncRequestDto) {
        return zeitbuchungenValidatorService.getZeitbuchungen(zeitbuchungSyncRequestDto).getBody();
    }

    @Override
    public TeilnehmerDto validateSingleTeilnehmer(TeilnehmerDto invalidTeilnehmerDto, String changedBy) {
        return validatorService.validateSingleParticipant(
                teilnehmerMapperService.mapInvalidToTeilnehmerStaging(invalidTeilnehmerDto, changedBy, GATEWAY_SERVICE),
                changedBy
        ).getBody();
    }

    @Override
    public StammdatenDto validateMitarbeiterStammdaten(StammdatenDto stammdatenDto, Boolean isOnboarding, String changedBy) {
        // Make sure default behaviour from REST endpoint is preserved for direct method call
        if (isOnboarding == null) {
            isOnboarding = false;
        }
        return validatorService.validateMitarbeiterStammdaten(stammdatenDto, isOnboarding, changedBy).getBody();
    }

    @Override
    public VertragsdatenDto validateMitarbeiterVertragsdaten(VertragsdatenDto vertragsdatenDto, Boolean isOnboarding, String changedBy) {
        if (isOnboarding == null) {
            isOnboarding = true;
        }
        return validatorService.validateMitarbeiterVertragsdaten(vertragsdatenDto, isOnboarding, changedBy).getBody();
    }

    @Override
    public VordienstzeitenDto validateMitarbeiterVordienstzeiten(VordienstzeitenDto vordienstzeitenDto, String changedBy) {
        return validatorService.validateMitarbeiterVordienstzeiten(vordienstzeitenDto, changedBy).getBody();
    }

    @Override
    public UnterhaltsberechtigteDto validateMitarbeiterUnterhaltsberechtigte(UnterhaltsberechtigteDto unterhaltsberechtigteDto, String changedBy) {
        return validatorService.validateMitarbeiterUnterhaltsberechtigte(unterhaltsberechtigteDto, changedBy).getBody();
    }

    @Override
    public WWorkflowStatus calculateKVEinstufung(String personalNummer, String changedBy, Boolean isOnboarding) {
        // Make sure default behaviour from REST endpoint is preserved for direct method call
        if (isOnboarding == null) {
            isOnboarding = true;
        }
        return kvEinstufungService.calculateKVEinstufung(personalNummer, isOnboarding).getBody();
    }

    @Override
    public TeilnehmerDto validateTeilnehmerDto(TeilnehmerDto teilnehmerDto, String changedBy) {
        return validatorService.validateTeilnehmer(teilnehmerDto, changedBy).getBody();
    }

    @Override
    public SeminarDto validateSeminarByTeilnehmer(SeminarDto seminarDto, String changedBy, Integer teilnehmerId) {
        return validatorService.validateSeminarByTeilnehmerDto(seminarDto, changedBy, String.valueOf(teilnehmerId)).getBody();
    }

    @Override
    public TeilnehmerNotizDto validateTeilnehmerNotiz(TeilnehmerNotizDto teilnehmerNotizDto, String changedBy, Integer teilnehmerId) {
        return validatorService.validateTeilnehmerNotizDto(teilnehmerNotizDto, changedBy, String.valueOf(teilnehmerId)).getBody();
    }

    @Override
    public TnAusbildungDto validateTeilnehmerAusbildung(TnAusbildungDto tnAusbildungDto, String changedBy, Integer teilnehmerId) {
        return validatorService.validateTeilnehmerAusbildungDto(tnAusbildungDto, changedBy, String.valueOf(teilnehmerId)).getBody();
    }

    @Override
    public TnBerufserfahrungDto validateTeilnehmerBerufserfahrung(TnBerufserfahrungDto tnBerufserfahrungDto, String changedBy, Integer teilnehmerId) {
        return validatorService.validateTeilnehmerBerufserfahrungDto(tnBerufserfahrungDto, changedBy, String.valueOf(teilnehmerId)).getBody();
    }

    @Override
    public SeminarPruefungDto validateSeminarPruefung(SeminarPruefungDto seminarPruefungDto, String changedBy, Integer teilnehmerId, Integer seminarId) {
        return validatorService.validateSeminarPruefungDto(
                seminarPruefungDto,
                changedBy,
                String.valueOf(teilnehmerId),
                String.valueOf(seminarId)
        ).getBody();
    }

    @Override
    public SprachkenntnisDto validateTeilnehmerSprachkenntnis(SprachkenntnisDto sprachkenntnisDto, String changedBy, Integer teilnehmerId) {
        return validatorService.validateTeilnehmerSprachkenntnisDto(sprachkenntnisDto, String.valueOf(teilnehmerId), changedBy).getBody();
    }

    @Override
    public TnZertifikatDto validateTeilnehmerZertifikat(TnZertifikatDto tnZertifikatDto, String changedBy, Integer teilnehmerId) {
        return validatorService.validateTeilnehmerZertifikatDto(tnZertifikatDto, changedBy, String.valueOf(teilnehmerId)).getBody();
    }

    @Override
    public Mono<Boolean> validateTeilnehmersZeiterfassung(ZeiterfassungTransferDto zeiterfassungDto, String changedBy) {
        return validatorService.validateZeiterfassungAsync(zeiterfassungDto, changedBy)
                .map(response -> {
                    log.info("Validation request accepted with status: {}", response.getStatusCode());
                    return response.getStatusCode().is2xxSuccessful(); // Return true for success
                })
                .doOnError(error -> log.error("Failed to send validation request: {}", error.getMessage()))
                .onErrorReturn(false);
    }

    @Override
    public ResponseEntity<String> validateSyncMitarbeiterWithUPN(String upn) {
        return mitarbeiterSyncService.syncMitarbeiterFromIbisacamWithUPN(upn, null, null);
    }
}
