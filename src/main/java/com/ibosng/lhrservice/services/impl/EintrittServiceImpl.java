package com.ibosng.lhrservice.services.impl;

import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.services.mitarbeiter.VertragsdatenService;
import com.ibosng.lhrservice.client.LHRClient;
import com.ibosng.lhrservice.dtos.DnEintrittDto;
import com.ibosng.lhrservice.dtos.DnEintritteDto;
import com.ibosng.lhrservice.dtos.variabledaten.EintrittDto;
import com.ibosng.lhrservice.dtos.variabledaten.ZeitangabeDto;
import com.ibosng.lhrservice.exceptions.LHRException;
import com.ibosng.lhrservice.exceptions.LHRWebClientException;
import com.ibosng.lhrservice.services.EintrittService;
import com.ibosng.lhrservice.services.HelperService;
import com.ibosng.lhrservice.services.LHREnvironmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.lhrservice.utils.Constants.*;
import static com.ibosng.lhrservice.utils.Helpers.*;
import static com.ibosng.lhrservice.utils.Parsers.parseStringToInteger;

@Service
@Slf4j
@RequiredArgsConstructor
public class EintrittServiceImpl implements EintrittService {
    private final LHRClient lhrClient;
    private final HelperService helperService;
    private final LHREnvironmentService lhrEnvironmentService;
    private final VertragsdatenService vertragsdatenService;

    @Override
    public void processEintritt(Personalnummer personalnummer, String eintrittsdatum) throws LHRWebClientException {
        DnEintritteDto existingEintritte = getEintritte(personalnummer);
        if (existingEintritte != null) {
            List<EintrittDto> eintrittsToBeRemoved = existingEintritte.getEintritte().stream().filter(eintritt -> eintritt.getGrund().equals(EINTRITT_GRUND_ANFANG) || eintritt.getGrund().equals(URLAUB_GRUND) || eintritt.getGrund().equals("ZABLF")).toList();
            for (EintrittDto eintrittToBeRemoved : eintrittsToBeRemoved) {
                if (eintrittToBeRemoved != null) {
                    EintrittDto existingAustritt = existingEintritte.getEintritte().stream().filter(eintritt -> eintritt.getGrund().equals(AUSTRITT_GRUND_EINVN)).findFirst().orElse(null);
                    if (existingAustritt == null) {
                        log.error("There is an {} but not an AUSTRITT for personalnummer {}. Data for Eintritt wont be send!", eintrittToBeRemoved.getGrund(), personalnummer);
                        if (!lhrEnvironmentService.isProduction()) {
                            lhrClient.deleteEintritt(
                                    lhrEnvironmentService.getFaKz(personalnummer.getFirma()),
                                    lhrEnvironmentService.getFaNr(personalnummer.getFirma()),
                                    parseStringToInteger(personalnummer.getPersonalnummer()),
                                    eintrittToBeRemoved.getGrund(),
                                    eintrittToBeRemoved.getZeitangabe().getVon(),
                                    null,
                                    null);
                        } else {
                            throw new LHRException(String.format("There is an %s but not an AUSTRITT for personalnummer %s. Data for Eintritt wont be send!", eintrittToBeRemoved.getGrund(), personalnummer));
                        }
                    }
                }
            }

        }
        DnEintrittDto existingEintritt = getEintrittSingleDate(personalnummer, eintrittsdatum, EINTRITT_GRUND_ANFANG);
        if (existingEintritt != null && existingEintritt.getEintritt() != null) {
            lhrClient.putEintritt(lhrEnvironmentService.getFaKz(personalnummer.getFirma()), lhrEnvironmentService.getFaNr(personalnummer.getFirma()), parseStringToInteger(personalnummer.getPersonalnummer()), EINTRITT_GRUND_ANFANG, eintrittsdatum, null, null,
                    mapDnEintrittForLHR(personalnummer, EINTRITT_GRUND_ANFANG, EINTRITT_ART_ANFANG));
        } else {
            lhrClient.postEintritt(lhrEnvironmentService.getFaKz(personalnummer.getFirma()), lhrEnvironmentService.getFaNr(personalnummer.getFirma()), parseStringToInteger(personalnummer.getPersonalnummer()), null, null,
                    mapDnEintritteForLHR(personalnummer, EINTRITT_GRUND_ANFANG, EINTRITT_ART_ANFANG));
        }
    }

    private DnEintrittDto getEintrittSingleDate(Personalnummer personalnummer, String eintrittsDatum, String grund) {
        ResponseEntity<DnEintrittDto> response = (ResponseEntity<DnEintrittDto>) getEintritt(personalnummer, eintrittsDatum, grund);
        return getBodyIfStatusOk(response);
    }

    private DnEintritteDto getEintritte(Personalnummer personalnummer) {
        ResponseEntity<DnEintritteDto> response = (ResponseEntity<DnEintritteDto>) getEintritt(personalnummer, null, null);
        return getBodyIfStatusOk(response);
    }

    public ResponseEntity<?> getEintritt(Personalnummer personalnummer, String eintrittsDatum, String grund) {
        if (!isNullOrBlank(eintrittsDatum)) {
            return handleLHRExceptions(personalnummer.getPersonalnummer(), "Single Eintritt", () ->
                    lhrClient.getEintritt(lhrEnvironmentService.getFaKz(personalnummer.getFirma()), lhrEnvironmentService.getFaNr(personalnummer.getFirma()), parseStringToInteger(personalnummer.getPersonalnummer()), grund, eintrittsDatum)
            );
        }
        return handleLHRExceptions(personalnummer.getPersonalnummer(), "Eintritt", () ->
                lhrClient.getEintritte(lhrEnvironmentService.getFaKz(personalnummer.getFirma()), lhrEnvironmentService.getFaNr(personalnummer.getFirma()), parseStringToInteger(personalnummer.getPersonalnummer()), null, null)
        );
    }

    private DnEintrittDto mapDnEintrittForLHR(Personalnummer personalnummer, String grund, String art) {
        Vertragsdaten vertragsdaten = vertragsdatenService.findByPersonalnummerIdAndStatus(personalnummer.getId(), List.of(MitarbeiterStatus.VALIDATED))
                .stream().findFirst().orElse(null);
        if (vertragsdaten == null) {
            throw new LHRException("Vertragsdaten are null in mapDnEintrittForLHR, stopping process!!!", new RuntimeException());
        }
        DnEintrittDto dnEintritt = new DnEintrittDto();
        dnEintritt.setDienstnehmer(helperService.createDienstnehmerRefDto(personalnummer));
        EintrittDto eintritt = new EintrittDto();
        eintritt.setArt(art);
        eintritt.setGrund(grund);
        if (grund.equals(AUSTRITT_GRUND_EINVN)) {
            eintritt.setBeschreibung(AUSTRITT_BESCHREIBUNG_EINVN);
        }
        ZeitangabeDto zeitangabe = new ZeitangabeDto();
        if (vertragsdaten.getEintritt() != null) {
            zeitangabe.setVon(getDateInPostRequestFormat(vertragsdaten.getEintritt()));
        }
        eintritt.setZeitangabe(zeitangabe);

        dnEintritt.setEintritt(eintritt);
        return dnEintritt;
    }

    private DnEintritteDto mapDnEintritteForLHR(Personalnummer personalnummer, String grund, String art) {
        Vertragsdaten vertragsdaten = vertragsdatenService.findByPersonalnummerIdAndStatus(personalnummer.getId(), List.of(MitarbeiterStatus.VALIDATED))
                .stream().findFirst().orElse(null);
        if (vertragsdaten == null) {
            throw new LHRException("Vertragsdaten are null in mapDnEintritteForLHR, stopping process!!!", new RuntimeException());
        }
        DnEintritteDto dnEintritte = new DnEintritteDto();
        dnEintritte.setDienstnehmer(helperService.createDienstnehmerRefDto(personalnummer));
        List<EintrittDto> eintritts = new ArrayList<>();

        EintrittDto eintritt = new EintrittDto();
        eintritt.setArt(art);
        eintritt.setGrund(grund);
        ZeitangabeDto zeitangabe = new ZeitangabeDto();
        if (vertragsdaten.getEintritt() != null) {
            zeitangabe.setVon(getDateInPostRequestFormat(vertragsdaten.getEintritt()));
        }
        if (grund.equals(AUSTRITT_GRUND_EINVN)) {
            eintritt.setBeschreibung(AUSTRITT_BESCHREIBUNG_EINVN);
        }
        eintritt.setZeitangabe(zeitangabe);
        eintritts.add(eintritt);
        if (vertragsdaten.getIsBefristet() != null && vertragsdaten.getIsBefristet() && vertragsdaten.getBefristungBis() != null) {
            EintrittDto austritt = new EintrittDto();
            austritt.setGrund("ZABLF");
            ZeitangabeDto zeitangabeAustritt = new ZeitangabeDto();
            zeitangabeAustritt.setVon(getDateInPostRequestFormat(vertragsdaten.getBefristungBis()));
            austritt.setZeitangabe(zeitangabeAustritt);
            eintritts.add(austritt);
        }


        dnEintritte.setEintritte(eintritts);
        return dnEintritte;
    }

}
