package com.ibosng.lhrservice.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibosng.lhrservice.dtos.*;
import com.ibosng.lhrservice.dtos.dienstraeder.DienstraederSingleTopLevelDto;
import com.ibosng.lhrservice.dtos.dienstraeder.DienstraederTopLevelDto;
import com.ibosng.lhrservice.dtos.dokumente.DnDokumenteDto;
import com.ibosng.lhrservice.dtos.dokumente.DokumentRubrikenDto;
import com.ibosng.lhrservice.dtos.kostenstellenaufteilung.KostenstellenaufteilungSingleTopLevelDto;
import com.ibosng.lhrservice.dtos.kostenstellenaufteilung.KostenstellenaufteilungTopLevelDto;
import com.ibosng.lhrservice.dtos.mitversicherte.MitversicherteSingleDto;
import com.ibosng.lhrservice.dtos.mitversicherte.MitversicherteTopLevelDto;
import com.ibosng.lhrservice.dtos.persoenlicheSaetze.PersoenlicheSaetzeSingleTopLevelDto;
import com.ibosng.lhrservice.dtos.persoenlicheSaetze.PersoenlicheSaetzeTopLevelDto;
import com.ibosng.lhrservice.dtos.sondertage.SondertageMatchCodeSingleDateDto;
import com.ibosng.lhrservice.dtos.sondertage.SondertageTopLevelDto;
import com.ibosng.lhrservice.dtos.variabledaten.TopLevelDto;
import com.ibosng.lhrservice.dtos.variabledaten.TopLevelSingleDateDto;
import com.ibosng.lhrservice.dtos.zeitdaten.AnfrageSuccessDto;
import com.ibosng.lhrservice.dtos.zeitdaten.DnZeitdatenDto;
import com.ibosng.lhrservice.dtos.zeitdaten.DnZeitdatenPeriodensummenDto;
import com.ibosng.lhrservice.dtos.zeitdaten.ZeitdatenStatusDto;
import com.ibosng.lhrservice.exceptions.LHRWebClientException;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.lhrservice.utils.Helpers.extractReasonFromErrorBody;

@Service
@Slf4j
@Conditional(LHRCondition.class)
public class LHRClientImpl implements LHRClient {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private static final String ZEIT = "/zeit";
    private static final String DIENSTNEHMERSTAMM = "/dienstnehmerstamm";
    private static final String BASE_PATH = "/lhr/firmengruppen/%s/firmen/%s/dienstnehmer";
    private static final String BASE_PATH_ZEIT = "/lhr/zeit/firmengruppen/%s/firmen/%s/dienstnehmer";
    private static final String BASE_PATH_ZEITDATEN = "/lhr/zeit/firmengruppen/%s/firmen/%s";
    private static final String BASE_PATH_DOCUMENT = "/lhr/dokumente/firmengruppen/%s/firmen/%s/dienstnehmer";
    private static final String BASE_PATH_DOCUMENT_DN_NR = "/lhr/dokumente/firmengruppen/%s/firmen/%s/dienstnehmer/%s";

    private static final String RUBRIKEN = "/rubriken";
    private static final String DOKUMENTE = "/dokumente";

    private static final String EINTRITTE_PATH = "/eintritte";
    private static final String VARIABLE_DATEN = "/variableDaten";
    private static final String KOSTENSTELLENAUFTEILUNG = "/kostenstellenaufteilung";
    private static final String PERSOENLICHE_SAETZE = "/persoenlicheSaetze";
    private static final String ZEITMODELLANFRAGE = "/zeitmodellanfrage";
    private static final String MITVERSICHERTE = "/mitversicherte";
    private static final String STAMMDATEN = "/stammdaten";
    private static final String DIENSTRAEDER = "/dienstraeder";
    private static final String SETTINGS = "/settings";
    private static final String SONDERTAGE = "/sondertage";
    private static final String BUCHUNGANFRAGE = "/buchungsanfrage";
    private static final String ZEITDATEN = "/zeitdaten";
    private static final String PERIODENSUMMEN = "/periodensummen";
    private static final String STATUSANFRAGE = "/statusanfrage";
    private static final String AUSZAHLUNGSANFRAGE = "/auszahlungsanfrage";
    private static final String URLAUBE = "/urlaube";
    private static final String DETAILS = "/details";
    private static final String STATUS = "/status";
    private static final String ZEITSPEICHERREFS = "9, 25,55,29,30,56";

    public LHRClientImpl(@Qualifier("lhrservicewebclient") WebClient webClient, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public ResponseEntity<DokumentRubrikenDto> getDokumenteRubrikiren(String faKz, Integer faNr,
                                                                      @Nullable String location,
                                                                      @Nullable Boolean count,
                                                                      @Nullable String created) throws LHRWebClientException {
        final String path = BASE_PATH_DOCUMENT.formatted(faKz, faNr) + RUBRIKEN;
        Map<String, String> queryParams = new HashMap<>();
        if (!isNullOrBlank(location)) {
            queryParams.put("location", location);
        }
        if (count != null) {
            queryParams.put("count", String.valueOf(count));
        }
        if (!isNullOrBlank(created)) {
            queryParams.put("created", created);
        }
        return buildResponse(path, HttpMethod.GET, null, null, queryParams, DokumentRubrikenDto.class, null);
    }

    @Override
    public ResponseEntity<DnDokumenteDto> getDokumenteInfo(String faKz, Integer faNr, Integer dnNr, Integer rubrikId,
                                                           @Nullable String created) throws LHRWebClientException {
        final String path = BASE_PATH_DOCUMENT_DN_NR.formatted(faKz, faNr, dnNr) + RUBRIKEN + "/" + rubrikId + DOKUMENTE;
        Map<String, String> queryParams = new HashMap<>();
        if (!isNullOrBlank(created)) {
            queryParams.put("created", created);
        }
        return buildResponse(path, HttpMethod.GET, null, null, queryParams, DnDokumenteDto.class, null);
    }

    @Override
    public File downloadDokument(String faKz, Integer faNr, Integer dnNr, Integer rubrikId, Integer docId) throws LHRWebClientException {
        final String path = BASE_PATH_DOCUMENT_DN_NR.formatted(faKz, faNr, dnNr) + RUBRIKEN + "/" + rubrikId + DOKUMENTE + "/" + docId;
        return downloadFile(path, HttpMethod.GET, null, null, null, dnNr, ".pdf");
    }

    @Override
    public ResponseEntity<DnZeitdatenDto[]> getDienstnehmerZeitDatenAllDienstnehmer(DienstnehmerRefDto dienstnehmerRef) throws LHRWebClientException {
        String path = BASE_PATH_ZEITDATEN.formatted(dienstnehmerRef.getFaKz(), dienstnehmerRef.getFaNr()) + ZEITDATEN;
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("date", String.valueOf(LocalDate.now()));
        queryParams.put("zeitspeicherrefs", ZEITSPEICHERREFS);
        return buildResponse(path, HttpMethod.GET, null, dienstnehmerRef.getDnNr(), queryParams, DnZeitdatenDto[].class, null);
    }

    @Override
    public ResponseEntity<DnZeitdatenDto[]> getDienstnehmerZeitdaten(DienstnehmerRefDto dienstnehmerRef, String from, String to, List<String> zeitspeicherRefs) throws LHRWebClientException {
        String path = getBasePathZeit(dienstnehmerRef.getFaKz(), dienstnehmerRef.getFaNr()) + "/{dnNr}" + ZEITDATEN;

        Map<String, String> queryParams = new HashMap<>();
        if (!isNullOrBlank(to)) {
            queryParams.put("to", to);
        }
        if (!isNullOrBlank(from)) {
            queryParams.put("from", from);
        }
        if (zeitspeicherRefs != null && !zeitspeicherRefs.isEmpty()) {
            queryParams.put("zeitspeicherrefs", String.join(",", zeitspeicherRefs));
        }
        return buildResponse(path, HttpMethod.GET, null, dienstnehmerRef.getDnNr(), queryParams, DnZeitdatenDto[].class, dienstnehmerRef.getDnNr());

    }

    @Override
    public ResponseEntity<AnfrageSuccessDto> postAuszahlungsanfrage(DienstnehmerRefDto dienstnehmerRef, String day, Integer zspNr, Integer minutes) throws LHRWebClientException {
        String path = getBasePathZeit(dienstnehmerRef.getFaKz(), dienstnehmerRef.getFaNr()) + "/{dnNr}" + AUSZAHLUNGSANFRAGE;

        Map<String, String> queryParams = new HashMap<>();
        if (!isNullOrBlank(day)) {
            queryParams.put("day", day);
        }
        if (zspNr != null) {
            queryParams.put("zspNr", String.valueOf(zspNr));
        }
        if (minutes != null) {
            queryParams.put("minutes", String.valueOf(minutes));
        }
        return buildResponse(path, HttpMethod.POST, null, dienstnehmerRef.getDnNr(), queryParams, AnfrageSuccessDto.class, dienstnehmerRef.getDnNr());
    }

    @Override
    public ResponseEntity<AnfrageSuccessDto> getAuszahlungsanfrage(DienstnehmerRefDto dienstnehmerRef, Integer anfrageNr) throws LHRWebClientException {
        String path = getBasePathZeit(dienstnehmerRef.getFaKz(), dienstnehmerRef.getFaNr()) + "/{dnNr}" + AUSZAHLUNGSANFRAGE;

        Map<String, String> queryParams = new HashMap<>();
        if (anfrageNr != null) {
            queryParams.put("anfrageNr", String.valueOf(anfrageNr));
        }
        return buildResponse(path, HttpMethod.GET, null, dienstnehmerRef.getDnNr(), queryParams, AnfrageSuccessDto.class, dienstnehmerRef.getDnNr());
    }

    @Override
    public ResponseEntity<SondertageMatchCodeSingleDateDto> getSondertageMatchCodeSingleDate(DienstnehmerRefDto dienstnehmerRef, Integer matchCode, String validFrom) throws LHRWebClientException {
        try {
            String path = getBasePathZeit(dienstnehmerRef.getFaKz(), dienstnehmerRef.getFaNr()) + "/{dnNr}" + SONDERTAGE + "/" + matchCode + "/" + validFrom;
            return buildResponse(path, HttpMethod.GET, null, dienstnehmerRef.getDnNr(), null, SondertageMatchCodeSingleDateDto.class, dienstnehmerRef.getDnNr());
        } catch (WebClientResponseException ex) {
            throw new LHRWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<String> deleteSondertageMatchCodeSingleDate(DienstnehmerRefDto dienstnehmerRef, Integer matchCode, String validFrom) throws LHRWebClientException {
        try {
            String path = getBasePathZeit(dienstnehmerRef.getFaKz(), dienstnehmerRef.getFaNr()) + "/{dnNr}" + SONDERTAGE + "/" + matchCode + "/" + validFrom;
            return buildResponse(path, HttpMethod.DELETE, null, dienstnehmerRef.getDnNr(), null, String.class, dienstnehmerRef.getDnNr());
        } catch (WebClientResponseException ex) {
            throw new LHRWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<SondertageTopLevelDto> postSondertageMatchCodeSingleDate(SondertageTopLevelDto dto) throws LHRWebClientException {
        try {

            String path = getBasePathZeit(dto.getDienstnehmer().getFaKz(), dto.getDienstnehmer().getFaNr()) + "/{dnNr}" + SONDERTAGE;
            return buildResponse(path, HttpMethod.POST, dto, dto.getDienstnehmer().getDnNr(), null, SondertageTopLevelDto.class, dto.getDienstnehmer().getDnNr());
        } catch (WebClientResponseException ex) {
            throw new LHRWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<SondertageMatchCodeSingleDateDto> putSondertageMatchCodeSingleDate(SondertageMatchCodeSingleDateDto dto, Integer matchCode, String validFrom) throws LHRWebClientException {
        try {

            String path = getBasePathZeit(dto.getDienstnehmer().getFaKz(), dto.getDienstnehmer().getFaNr()) + "/{dnNr}" + SONDERTAGE + "/" + matchCode + "/" + validFrom;
            return buildResponse(path, HttpMethod.PUT, dto, dto.getDienstnehmer().getDnNr(), null, SondertageMatchCodeSingleDateDto.class, dto.getDienstnehmer().getDnNr());
        } catch (WebClientResponseException ex) {
            throw new LHRWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<DienstraederTopLevelDto> getDienstraederTopLevel(DienstnehmerRefDto dienstnehmerRef) throws LHRWebClientException {
        try {
            String path = getBasePathZeit(dienstnehmerRef.getFaKz(), dienstnehmerRef.getFaNr()) + "/{dnNr}" + DIENSTRAEDER;
            return buildResponse(path, HttpMethod.GET, null, dienstnehmerRef.getDnNr(), null, DienstraederTopLevelDto.class, dienstnehmerRef.getDnNr());
        } catch (WebClientResponseException ex) {
            throw new LHRWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<DienstraederSingleTopLevelDto> getDienstraederSingleDateSettings(DienstnehmerRefDto dienstnehmerRef, String validFrom) throws LHRWebClientException {
        try {
            String path = getBasePathZeit(dienstnehmerRef.getFaKz(), dienstnehmerRef.getFaNr()) + "/{dnNr}" + DIENSTRAEDER + "/" + validFrom + SETTINGS;
            return buildResponse(path, HttpMethod.GET, null, dienstnehmerRef.getDnNr(), null, DienstraederSingleTopLevelDto.class, dienstnehmerRef.getDnNr());
        } catch (WebClientResponseException ex) {
            throw new LHRWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<DienstraederTopLevelDto> postDienstraederTopLevel(DienstraederTopLevelDto dto) throws LHRWebClientException {
        try {

            String path = getBasePathZeit(dto.getDienstnehmer().getFaKz(), dto.getDienstnehmer().getFaNr()) + "/{dnNr}" + DIENSTRAEDER;
            return buildResponse(path, HttpMethod.POST, dto, dto.getDienstnehmer().getDnNr(), null, DienstraederTopLevelDto.class, dto.getDienstnehmer().getDnNr());
        } catch (WebClientResponseException ex) {
            throw new LHRWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<UrlaubsdatenStandaloneDto> getUrlaube(String faKz, Integer faNr, Integer dnNr, String monthFrom, String monthTo) throws LHRWebClientException {
        try {
            Map<String, String> queryParams = new HashMap<>();
            if (!isNullOrBlank(monthFrom)) {
                queryParams.put("monthFrom", monthFrom);
            }
            if (!isNullOrBlank(monthTo)) {
                queryParams.put("monthTo", monthTo);
            }
            String path = getBasePath(faKz, faNr) + "/{dnNr}" + URLAUBE;
            return buildResponse(path, HttpMethod.GET, null, dnNr, queryParams, UrlaubsdatenStandaloneDto.class, dnNr);
        } catch (WebClientResponseException ex) {
            throw new LHRWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<UrlaubsdatenStandaloneDetailsDto> getUrlaubeDetails(String faKz, Integer faNr, Integer dnNr, String monthFrom) throws LHRWebClientException {
        try {
            String path = getBasePath(faKz, faNr) + "/{dnNr}" + URLAUBE + "/" + monthFrom + DETAILS;
            return buildResponse(path, HttpMethod.GET, null, dnNr, null, UrlaubsdatenStandaloneDetailsDto.class, dnNr);
        } catch (WebClientResponseException ex) {
            throw new LHRWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<?> deleteDienstraederSingleDate(DienstnehmerRefDto dienstnehmerRef, String validFrom, boolean withSettings) throws LHRWebClientException {
        try {
            String path = getBasePathZeit(dienstnehmerRef.getFaKz(), dienstnehmerRef.getFaNr()) + "/{dnNr}" + DIENSTRAEDER + "/" + validFrom;
            if (withSettings) {
                path = path + SETTINGS;
            }
            return buildResponse(path, HttpMethod.DELETE, null, dienstnehmerRef.getDnNr(), null, String.class, dienstnehmerRef.getDnNr());
        } catch (WebClientResponseException ex) {
            throw new LHRWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<DienstraederSingleTopLevelDto> postDienstraederSingleDateTopLevel(DienstraederSingleTopLevelDto dto, String validFrom, boolean withSettings) throws LHRWebClientException {
        try {
            String path = getBasePathZeit(dto.getDienstnehmer().getFaKz(), dto.getDienstnehmer().getFaNr()) + "/{dnNr}" + DIENSTRAEDER + "/" + validFrom;
            if (withSettings) {
                path = path + SETTINGS;
            }
            return buildResponse(path, HttpMethod.POST, dto, dto.getDienstnehmer().getDnNr(), null, DienstraederSingleTopLevelDto.class, dto.getDienstnehmer().getDnNr());
        } catch (WebClientResponseException ex) {
            throw new LHRWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<MitversicherteTopLevelDto> getMitversicherteTopLevel(DienstnehmerRefDto dienstnehmerRef) throws LHRWebClientException {
        try {
            String path = getBasePath(dienstnehmerRef.getFaKz(), dienstnehmerRef.getFaNr()) + "/{dnNr}" + MITVERSICHERTE;
            return buildResponse(path, HttpMethod.GET, null, dienstnehmerRef.getDnNr(), null, MitversicherteTopLevelDto.class, dienstnehmerRef.getDnNr());
        } catch (WebClientResponseException ex) {
            throw new LHRWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<String> deleteMitversicherteStammdaten(DienstnehmerRefDto dienstnehmerRef, Integer mvNr, String validFrom) throws LHRWebClientException {
        try {
            String path = getBasePath(dienstnehmerRef.getFaKz(), dienstnehmerRef.getFaNr()) + "/{dnNr}" + MITVERSICHERTE + "/" + mvNr + STAMMDATEN + "/" + validFrom;
            return buildResponse(path, HttpMethod.DELETE, null, dienstnehmerRef.getDnNr(), null, String.class, dienstnehmerRef.getDnNr());
        } catch (WebClientResponseException ex) {
            throw new LHRWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<MitversicherteTopLevelDto> postMitversicherteTopLevel(MitversicherteTopLevelDto dto) throws LHRWebClientException {
        try {

            String path = getBasePath(dto.getDienstnehmer().getFaKz(), dto.getDienstnehmer().getFaNr()) + "/{dnNr}" + MITVERSICHERTE + STAMMDATEN;
            return buildResponse(path, HttpMethod.POST, dto, dto.getDienstnehmer().getDnNr(), null, MitversicherteTopLevelDto.class, dto.getDienstnehmer().getDnNr());
        } catch (WebClientResponseException ex) {
            throw new LHRWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<MitversicherteSingleDto> putMitversicherteTopLevel(Integer mvNr, MitversicherteSingleDto dto, String validFrom) throws LHRWebClientException {
        try {

            String path = getBasePath(dto.getDienstnehmer().getFaKz(), dto.getDienstnehmer().getFaNr()) + "/{dnNr}" + MITVERSICHERTE + "/" + mvNr + STAMMDATEN + "/" + dto.getMitversicherte().getStammdaten().getValidFrom();
            return buildResponse(path, HttpMethod.POST, dto, dto.getDienstnehmer().getDnNr(), null, MitversicherteSingleDto.class, dto.getDienstnehmer().getDnNr());
        } catch (WebClientResponseException ex) {
            throw new LHRWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<AnfrageSuccessDto> getZeitmodellanfrage(String faKz, Integer faNr, Integer dnNr) throws LHRWebClientException {
        try {
            String path = getBasePath(faKz, faNr) + "/{dnNr}" + ZEITMODELLANFRAGE;
            return buildResponse(path, HttpMethod.GET, null, dnNr, null, AnfrageSuccessDto.class, dnNr);
        } catch (WebClientResponseException ex) {
            throw new LHRWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<AnfrageSuccessDto> postZeitmodellanfrage(String faKz, Integer faNr, Integer dnNr, Integer zeitmodellNr, String fromDate) throws LHRWebClientException {
        try {
            Map<String, String> queryParams = new HashMap<>();
            if (zeitmodellNr != null) {
                queryParams.put("zeitmodellNr", zeitmodellNr.toString());
            }
            if (!isNullOrBlank(fromDate)) {
                queryParams.put("fromDate", fromDate);
            }
            String path = getBasePath(faKz, faNr) + "/{dnNr}" + EINTRITTE_PATH;
            return buildResponse(path, HttpMethod.POST, null, dnNr, queryParams, AnfrageSuccessDto.class, dnNr);
        } catch (WebClientResponseException ex) {
            throw new LHRWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<DnEintritteDto> postEintritt(String faKz, Integer faNr, Integer dnNr, String modify, String ignore, DnEintritteDto dnEintritte) throws LHRWebClientException {
        try {
            Map<String, String> queryParams = new HashMap<>();
            if (!isNullOrBlank(modify)) {
                queryParams.put("modify", modify);
            }
            if (!isNullOrBlank(ignore)) {
                queryParams.put("ignore", ignore);
            }
            String path = getBasePath(faKz, faNr) + "/{dnNr}" + EINTRITTE_PATH;
            return buildResponse(path, HttpMethod.POST, dnEintritte, dnNr, queryParams, DnEintritteDto.class, dnNr);
        } catch (WebClientResponseException ex) {
            throw new LHRWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<DnZeitdatenPeriodensummenDto> getPeriodensummen(String faKz, Integer faNr, Integer dnNr, String zeitdatenMonth, String zeitspeicherrefs) {
        try {
            Map<String, String> queryParams = new HashMap<>();
            if (!isNullOrBlank(zeitspeicherrefs)) {
                queryParams.put("zeitspeicherrefs", zeitspeicherrefs);
            }
            String path = getBasePathZeit(faKz, faNr) + "/{dnNr}" + ZEITDATEN + "/" + zeitdatenMonth + PERIODENSUMMEN;
            return buildResponse(path, HttpMethod.GET, null, dnNr, queryParams, DnZeitdatenPeriodensummenDto.class, dnNr);
        } catch (WebClientResponseException ex) {
            throw new LHRWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<ZeitdatenStatusDto> geZeitdatenStatus(String faKz, Integer faNr, Integer dnNr) {
        try {
            String path = getBasePathZeit(faKz, faNr) + "/{dnNr}" + ZEITDATEN + STATUS;
            return buildResponse(path, HttpMethod.GET, null, dnNr, null, ZeitdatenStatusDto.class, dnNr);
        } catch (WebClientResponseException ex) {
            throw new LHRWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<AnfrageSuccessDto> postBuchunganfrage(String faKz, Integer faNr, Integer dnNr, String type, String date,
                                                                @Nullable String original,
                                                                @Nullable String zspNr,
                                                                @Nullable String terminal) throws LHRWebClientException {
        try {
            Map<String, String> queryParams = new HashMap<>();
            if (!isNullOrBlank(date)) {
                queryParams.put("time", date);
            }
            if (!isNullOrBlank(type)) {
                queryParams.put("type", type);
            }
            if (!isNullOrBlank(original)) {
                queryParams.put("original", original);
            }
            if (!isNullOrBlank(zspNr)) {
                queryParams.put("zspNr", zspNr);
            }
            if (!isNullOrBlank(terminal)) {
                queryParams.put("terminal", terminal);
            }
            String path = getBasePathZeit(faKz, faNr) + "/{dnNr}" + BUCHUNGANFRAGE;
            return buildResponse(path, HttpMethod.POST, null, dnNr, queryParams, AnfrageSuccessDto.class, dnNr);
        } catch (WebClientResponseException ex) {
            throw new LHRWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<AnfrageSuccessDto> postStatusanfrage(String faKz, Integer faNr, Integer dnNr,
                                                               String day, StatusAnfrage status) throws LHRWebClientException {
        try {
            Map<String, String> queryParams = new HashMap<>();
            if (!isNullOrBlank(day)) {
                queryParams.put("day", day);
            }
            if (Objects.nonNull(status)) {
                queryParams.put("status", status.name());
            }
            String path = getBasePathZeit(faKz, faNr) + "/{dnNr}" + STATUSANFRAGE;
            return buildResponse(path, HttpMethod.POST, null, dnNr, queryParams, AnfrageSuccessDto.class, dnNr);
        } catch (WebClientResponseException ex) {
            throw new LHRWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<DnEintritteDto> getEintritte(String faKz, Integer faNr, Integer dnNr, String effectiveDate, String[] art) throws LHRWebClientException {
        try {
            Map<String, String> queryParams = new HashMap<>();
            if (!isNullOrBlank(effectiveDate)) {
                queryParams.put("effectiveDate", effectiveDate);
            }
            if (art != null && art.length > 0) {
                for (String artValue : art) {
                    queryParams.put("art", artValue);
                }
            }
            String path = getBasePath(faKz, faNr) + "/{dnNr}" + EINTRITTE_PATH;
            return buildResponse(path, HttpMethod.GET, null, dnNr, queryParams, DnEintritteDto.class, dnNr);
        } catch (WebClientResponseException ex) {
            throw new LHRWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<DnEintrittDto> getEintritt(String faKz, Integer faNr, Integer dnNr, String grund, String day) throws LHRWebClientException {
        try {
            String path = getBasePath(faKz, faNr) + "/{dnNr}" + EINTRITTE_PATH + "/" + grund + "/" + day;
            return buildResponse(path, HttpMethod.GET, null, dnNr, null, DnEintrittDto.class, dnNr);
        } catch (WebClientResponseException ex) {
            throw new LHRWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<DnEintrittDto> putEintritt(String faKz, Integer faNr, Integer dnNr, String grund,
                                                     String day, String modify, String ignore, DnEintrittDto dnEintritt) throws LHRWebClientException {
        try {
            Map<String, String> queryParams = new HashMap<>();
            if (!isNullOrBlank(modify)) {
                queryParams.put("modify", modify);
            }
            if (!isNullOrBlank(ignore)) {
                queryParams.put("ignore", ignore);
            }
            String path = getBasePath(faKz, faNr) + "/{dnNr}" + EINTRITTE_PATH + "/" + grund + "/" + day;
            ;
            return buildResponse(path, HttpMethod.PUT, dnEintritt, dnNr, queryParams, DnEintrittDto.class, dnNr);
        } catch (WebClientResponseException ex) {
            throw new LHRWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<DnEintrittDto> deleteEintritt(String faKz, Integer faNr, Integer dnNr, String grund,
                                                        String day, String modify, String ignore) throws LHRWebClientException {
        try {
            Map<String, String> queryParams = new HashMap<>();
            if (!isNullOrBlank(modify)) {
                queryParams.put("modify", modify);
            }
            if (!isNullOrBlank(ignore)) {
                queryParams.put("ignore", ignore);
            }
            String path = getBasePath(faKz, faNr) + "/{dnNr}" + EINTRITTE_PATH + "/" + grund + "/" + day;
            ;
            return buildResponse(path, HttpMethod.DELETE, null, dnNr, queryParams, DnEintrittDto.class, dnNr);
        } catch (WebClientResponseException ex) {
            throw new LHRWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<DnStammStandaloneDto> getDienstnehmerstammFromLHR(String faKz, Integer faNr, Integer dnNr) throws LHRWebClientException {
        try {
            String path = getBasePath(faKz, faNr) + "/{dnNr}" + DIENSTNEHMERSTAMM;
            return buildResponse(path, HttpMethod.GET, null, dnNr, null, DnStammStandaloneDto.class, dnNr);
        } catch (WebClientResponseException ex) {
            throw new LHRWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<?> getKostenstellenaufteilungFromLHR(String faKz, Integer faNr, Integer dnNr, String date) throws LHRWebClientException {
        try {
            String path;
            if (!isNullOrBlank(date)) {
                path = getBasePath(faKz, faNr) + "/{dnNr}" + KOSTENSTELLENAUFTEILUNG + "/" + date;
                return buildResponse(path, HttpMethod.GET, null, dnNr, null, KostenstellenaufteilungSingleTopLevelDto.class, dnNr);
            } else {
                path = getBasePath(faKz, faNr) + "/{dnNr}" + KOSTENSTELLENAUFTEILUNG;
                return buildResponse(path, HttpMethod.GET, null, dnNr, null, KostenstellenaufteilungTopLevelDto.class, dnNr);
            }
        } catch (WebClientResponseException ex) {
            throw new LHRWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<String> deleteKostenstellenaufteilungFromLHR(String faKz, Integer faNr, Integer dnNr, String day) throws LHRWebClientException {
        try {
            String path = getBasePath(faKz, faNr) + "/{dnNr}" + KOSTENSTELLENAUFTEILUNG + "/" + day;
            return buildResponse(path, HttpMethod.DELETE, null, dnNr, null, String.class, dnNr);
        } catch (WebClientResponseException ex) {
            throw new LHRWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<?> getPersoenlicheSaetzeFromLHR(String faKz, Integer faNr, Integer dnNr, String date, Integer satzNr) throws LHRWebClientException {
        try {
            String path;
            if (!isNullOrBlank(date)) {
                path = getBasePath(faKz, faNr) + "/{dnNr}" + PERSOENLICHE_SAETZE + "/" + satzNr + "/" + date;
                return buildResponse(path, HttpMethod.GET, null, dnNr, null, PersoenlicheSaetzeSingleTopLevelDto.class, dnNr);
            } else {
                path = getBasePath(faKz, faNr) + "/{dnNr}" + PERSOENLICHE_SAETZE + "/" + satzNr;
                return buildResponse(path, HttpMethod.GET, null, dnNr, null, PersoenlicheSaetzeTopLevelDto.class, dnNr);
            }
        } catch (WebClientResponseException ex) {
            throw new LHRWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<?> getVariableDatenFromLHR(DienstnehmerRefDto dienstnehmerRef, String eintrittsDatum) throws LHRWebClientException {
        try {
            String path;
            if (!isNullOrBlank(eintrittsDatum)) {
                path = getBasePath(dienstnehmerRef.getFaKz(), dienstnehmerRef.getFaNr()) + "/{dnNr}" + VARIABLE_DATEN + "/" + eintrittsDatum;
                return buildResponse(path, HttpMethod.GET, null, dienstnehmerRef.getDnNr(), null, TopLevelSingleDateDto.class, dienstnehmerRef.getDnNr());
            } else {
                path = getBasePath(dienstnehmerRef.getFaKz(), dienstnehmerRef.getFaNr()) + "/{dnNr}" + VARIABLE_DATEN;
                return buildResponse(path, HttpMethod.GET, null, dienstnehmerRef.getDnNr(), null, TopLevelDto.class, dienstnehmerRef.getDnNr());
            }
        } catch (WebClientResponseException ex) {
            throw new LHRWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<List<DnStammStandaloneDto>> findAllDienstnehmersFromLHR(String faKz, Integer faNr, Integer minDnNr, Integer maxDnNr,
                                                                                  String effectiveDate, String activeSince) throws LHRWebClientException {
        try {
            return webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path(getBasePath(faKz, faNr))
                            .queryParamIfPresent("minDnNr", Optional.ofNullable(minDnNr))
                            .queryParamIfPresent("maxDnNr", Optional.ofNullable(maxDnNr))
                            .queryParamIfPresent("effectiveDate", Optional.ofNullable(effectiveDate))
                            .queryParamIfPresent("activeSince", Optional.ofNullable(activeSince))
                            .build())
                    .retrieve()
                    .toEntityList(DnStammStandaloneDto.class)
                    .block();
        } catch (WebClientResponseException ex) {
            throw new LHRWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<DnStammStandaloneDto> sendDienstnehmerstammToLHR(DnStammStandaloneDto dnStammStandalone) throws LHRWebClientException {
        try {
            String path = getBasePath(dnStammStandalone.getDienstnehmer().getFaKz(), dnStammStandalone.getDienstnehmer().getFaNr()) + DIENSTNEHMERSTAMM;
            return buildResponse(path, HttpMethod.POST, dnStammStandalone, null, null, DnStammStandaloneDto.class, dnStammStandalone.getDienstnehmer().getDnNr());
        } catch (WebClientResponseException ex) {
            throw new LHRWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<?> sendKostenstellenaufteilungToLHR(
            KostenstellenaufteilungTopLevelDto dto,
            KostenstellenaufteilungSingleTopLevelDto singleDto,
            String date) throws LHRWebClientException {
        try {
            if (isNullOrBlank(date)) {
                String path = getBasePath(dto.getDienstnehmer().getFaKz(), dto.getDienstnehmer().getFaNr()) + "/" + dto.getDienstnehmer().getDnNr() + KOSTENSTELLENAUFTEILUNG;
                return buildResponse(path, HttpMethod.POST, dto, null, null, KostenstellenaufteilungTopLevelDto.class, dto.getDienstnehmer().getDnNr());
            } else {
                String path = getBasePath(singleDto.getDienstnehmer().getFaKz(), singleDto.getDienstnehmer().getFaNr()) + "/" + singleDto.getDienstnehmer().getDnNr() + KOSTENSTELLENAUFTEILUNG + "/" + date;
                return buildResponse(path, HttpMethod.PUT, singleDto, null, null, KostenstellenaufteilungSingleTopLevelDto.class, singleDto.getDienstnehmer().getDnNr());
            }
        } catch (WebClientResponseException ex) {
            throw new LHRWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<DnStammStandaloneDto> updateDienstnehmerstammOnLHR(DnStammStandaloneDto dnStammStandalone) throws LHRWebClientException {
        try {
            String path = getBasePath(dnStammStandalone.getDienstnehmer().getFaKz(), dnStammStandalone.getDienstnehmer().getFaNr()) + "/{dnNr}" + DIENSTNEHMERSTAMM;
            return buildResponse(path, HttpMethod.PUT, dnStammStandalone, dnStammStandalone.getDienstnehmer().getDnNr(), null, DnStammStandaloneDto.class, dnStammStandalone.getDienstnehmer().getDnNr());
        } catch (WebClientResponseException ex) {
            throw new LHRWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<TopLevelDto> sendVariableDatenToLHR(TopLevelDto topLevelDTO, boolean vorgabewerte) throws LHRWebClientException {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("vorgabewerte", Boolean.toString(vorgabewerte));
        try {
            String path = getBasePath(topLevelDTO.getDienstnehmer().getFaKz(), topLevelDTO.getDienstnehmer().getFaNr()) + "/{dnNr}" + VARIABLE_DATEN;
            return buildResponse(path, HttpMethod.POST, topLevelDTO, topLevelDTO.getDienstnehmer().getDnNr(), queryParams, TopLevelDto.class, topLevelDTO.getDienstnehmer().getDnNr());
        } catch (WebClientResponseException ex) {
            throw new LHRWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<TopLevelSingleDateDto> putVariableDatenToLHR(TopLevelSingleDateDto topLevelDTO, String eintrittsDatum) throws LHRWebClientException {
        try {
            String path = getBasePath(topLevelDTO.getDienstnehmer().getFaKz(), topLevelDTO.getDienstnehmer().getFaNr()) + "/{dnNr}" + VARIABLE_DATEN + "/" + eintrittsDatum;
            return buildResponse(path, HttpMethod.PUT, topLevelDTO, topLevelDTO.getDienstnehmer().getDnNr(), null, TopLevelSingleDateDto.class, topLevelDTO.getDienstnehmer().getDnNr());
        } catch (WebClientResponseException ex) {
            throw new LHRWebClientException(ex);
        }
    }

    @Override
    public ResponseEntity<?> sendPersoenlicheSaetzeToLHR(
            PersoenlicheSaetzeTopLevelDto persoenlicheSaetzeTopLevelDto,
            PersoenlicheSaetzeSingleTopLevelDto persoenlicheSaetzeSingleTopLevelDto,
            Integer satzNummer,
            String date) throws LHRWebClientException {

        try {
            if (isNullOrBlank(date)) {
                String path = getBasePath(persoenlicheSaetzeTopLevelDto.getDienstnehmer().getFaKz(), persoenlicheSaetzeTopLevelDto.getDienstnehmer().getFaNr()) + "/" + persoenlicheSaetzeTopLevelDto.getDienstnehmer().getDnNr() + PERSOENLICHE_SAETZE;
                return buildResponse(path, HttpMethod.POST, persoenlicheSaetzeTopLevelDto, null, null, PersoenlicheSaetzeTopLevelDto.class, persoenlicheSaetzeTopLevelDto.getDienstnehmer().getDnNr());
            } else {
                String path = getBasePath(persoenlicheSaetzeSingleTopLevelDto.getDienstnehmer().getFaKz(), persoenlicheSaetzeSingleTopLevelDto.getDienstnehmer().getFaNr()) + "/" + persoenlicheSaetzeSingleTopLevelDto.getDienstnehmer().getDnNr() + PERSOENLICHE_SAETZE + "/" + satzNummer + "/" + date;
                return buildResponse(path, HttpMethod.PUT, persoenlicheSaetzeSingleTopLevelDto, null, null, PersoenlicheSaetzeSingleTopLevelDto.class, persoenlicheSaetzeSingleTopLevelDto.getDienstnehmer().getDnNr());
            }
        } catch (WebClientResponseException ex) {
            throw new LHRWebClientException(ex);
        }
    }

    @Override
    public File downloadFile(String path, HttpMethod method, Object body, Integer uriParameter, Map<String, String> queryParams, Integer personalnummer, String extension) throws LHRWebClientException {
        if (body != null) {
            saveBodyAsJsonToTempFile(body, personalnummer, false);
        }

        final File tempFile = new File(System.getProperty("java.io.tmpdir"), "file_" + personalnummer +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("_yyyy-MM-dd_HH-mm-ss")) + extension);
        Flux<DataBuffer> dataBuffer = webClient
                .method(method)
                .uri(uriBuilder -> {
                    if (queryParams != null) {
                        queryParams.forEach(uriBuilder::queryParam);
                    }

                    if (uriParameter != null) {
                        return uriBuilder
                                .path(path)
                                .build(uriParameter);
                    } else {
                        return uriBuilder
                                .path(path)
                                .build();
                    }
                })
                .retrieve()
                .bodyToFlux(DataBuffer.class)
                .doOnError(error -> {
                    log.error("Error while downloading file from LHR: {}", error.getMessage());
                    throw new LHRWebClientException(error.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
                });

        DataBufferUtils.write(dataBuffer, tempFile.toPath(), StandardOpenOption.CREATE).share().block();
        return tempFile;
    }

    @Override
    public <T> ResponseEntity<T> buildResponse(String path, HttpMethod method, Object body, Integer uriParameter, Map<String, String> queryParams, Class<T> responseType, Integer personalnummer) throws LHRWebClientException {
        WebClient.RequestBodySpec requestBodySpec = webClient
                .method(method)
                .uri(uriBuilder -> {
                    if (queryParams != null) {
                        queryParams.forEach(uriBuilder::queryParam);
                    }

                    if (uriParameter != null) {
                        return uriBuilder
                                .path(path)
                                .build(uriParameter);
                    } else {
                        return uriBuilder
                                .path(path)
                                .build();
                    }
                });
        if (body != null) {
            saveBodyAsJsonToTempFile(body, personalnummer, false);
        }

        // Conditionally adding body if not null
        WebClient.ResponseSpec responseSpec = body != null ?
                requestBodySpec.body(BodyInserters.fromValue(body)).retrieve() :
                requestBodySpec.retrieve();

        //return responseSpec.toEntity(responseType).block();
        // Handle both success and error cases
        return responseSpec
                .onStatus(HttpStatusCode::isError, clientResponse -> {
                    log.warn("Non-2xx response, status code: {}", clientResponse.statusCode());

                    return clientResponse.bodyToMono(String.class)
                            .defaultIfEmpty("No error body")
                            .flatMap(errorBody -> {
                                if (errorBody != null && !errorBody.equals("No error body")) {
                                    saveBodyAsJsonToTempFile(errorBody, personalnummer, true);
                                    String reason = extractReasonFromErrorBody(errorBody);
                                    log.warn("Reason for rejecting the request: {}", reason);

                                    // Throw a custom exception with the reason
                                    return Mono.error(new LHRWebClientException(reason, HttpStatus.UNPROCESSABLE_ENTITY));
                                }

                                if (clientResponse.statusCode().equals(HttpStatus.NOT_FOUND)) {
                                    // Log specific message for 404
                                    log.warn("404 Not Found - Resource could not be found for request");
                                    // Return Mono.empty() to avoid throwing an exception for 404
                                    return Mono.empty();
                                } else {
                                    // Handle other errors by throwing a custom exception
                                    return clientResponse.createException()
                                            .flatMap(webClientResponseException -> Mono.error(new LHRWebClientException(webClientResponseException)));
                                }
                            });
                })
                .toEntity(responseType)
                .blockOptional()
                // If blockOptional() returns empty (e.g., for 404), return ResponseEntity with 404 status
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    private void saveBodyAsJsonToTempFile(Object body, Integer identifier, boolean isError) {
        try {
            String jsonBody = objectMapper.writeValueAsString(body);
            File tempFile;
            if (!isError) {
                tempFile = new File(System.getProperty("java.io.tmpdir"), "requestBody_" + identifier + ".json");
            } else {
                tempFile = new File(System.getProperty("java.io.tmpdir"), "errorResponse_" + identifier + ".json");
            }
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                FileCopyUtils.copy(jsonBody.getBytes(StandardCharsets.UTF_8), fos);
            }
            log.info("Temporary file created: {} , isError: {}", tempFile.getAbsolutePath(), isError);
        } catch (Exception e) {
            log.error("Exception caught while saving the json file: {}", e.getMessage());
        }
    }

    private String getBasePath(String faKz, Integer faNr) {
        return String.format(BASE_PATH, faKz, faNr);
    }

    private String getBasePathZeit(String faKz, Integer faNr) {
        return String.format(BASE_PATH_ZEIT, faKz, faNr);
    }
}
