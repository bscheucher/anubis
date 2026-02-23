package com.ibosng.lhrservice.client;

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Conditional(LHRCondition.Reverse.class)
public class MockLHRClient implements LHRClient {

    private static final String MOCK_DELETED = "MOCK-DELETED";

    @Override
    public ResponseEntity<DokumentRubrikenDto> getDokumenteRubrikiren(String faKz, Integer faNr, String location, Boolean count, String created) throws LHRWebClientException {
        log.info("MOCK getDokumenteRubrikiren({}, {}, {}, {}, {})", faKz, faNr, location, count, created);
        return ResponseEntity.ok(new DokumentRubrikenDto());
    }

    @Override
    public ResponseEntity<DnDokumenteDto> getDokumenteInfo(String faKz, Integer faNr, Integer dnNr, Integer rubrikId, String created) throws LHRWebClientException {
        log.info("MOCK getDokumenteInfo({}, {}, {}, {}, {})", faKz, faNr, dnNr, rubrikId, created);
        return ResponseEntity.ok(new DnDokumenteDto());
    }

    @Override
    public File downloadDokument(String faKz, Integer faNr, Integer dnNr, Integer rubrikId, Integer docId) throws LHRWebClientException {
        log.info("MOCK downloadDokument({}, {}, {}, {}, {})", faKz, faNr, dnNr, rubrikId, docId);
        return new File(System.getProperty("java.io.tmpdir"),
                String.format("lhr-mock-%s-%s-%s.pdf", dnNr, rubrikId, docId));
    }

    @Override
    public ResponseEntity<DnZeitdatenDto[]> getDienstnehmerZeitDatenAllDienstnehmer(DienstnehmerRefDto dienstnehmerRef) throws LHRWebClientException {
        log.info("MOCK getDienstnehmerZeitDatenAllDienstnehmer({})", dienstnehmerRef);
        return ResponseEntity.ok(new DnZeitdatenDto[]{new DnZeitdatenDto()});
    }

    @Override
    public ResponseEntity<DnZeitdatenDto[]> getDienstnehmerZeitdaten(DienstnehmerRefDto dienstnehmerRef, String from, String to, List<String> zeitspeicherRefs) throws LHRWebClientException {
        log.info("MOCK getDienstnehmerZeitdaten({}, {}, {}, {})", dienstnehmerRef, from, to, zeitspeicherRefs);
        return ResponseEntity.ok(new DnZeitdatenDto[]{new DnZeitdatenDto()});
    }

    @Override
    public ResponseEntity<AnfrageSuccessDto> postAuszahlungsanfrage(DienstnehmerRefDto dienstnehmerRef, String day, Integer zspNr, Integer minutes) throws LHRWebClientException {
        log.info("MOCK postAuszahlungsanfrage({}, {}, {}, {})", dienstnehmerRef, day, zspNr, minutes);
        return ResponseEntity.ok(new AnfrageSuccessDto());
    }

    @Override
    public ResponseEntity<AnfrageSuccessDto> getAuszahlungsanfrage(DienstnehmerRefDto dienstnehmerRef, Integer anfrageNr) throws LHRWebClientException {
        log.info("MOCK getAuszahlungsanfrage({}, {})", dienstnehmerRef, anfrageNr);
        return ResponseEntity.ok(new AnfrageSuccessDto());
    }

    @Override
    public ResponseEntity<SondertageMatchCodeSingleDateDto> getSondertageMatchCodeSingleDate(DienstnehmerRefDto dienstnehmerRef, Integer matchCode, String validFrom) throws LHRWebClientException {
        log.info("MOCK getSondertageMatchCodeSingleDate({}, {}, {})", dienstnehmerRef, matchCode, validFrom);
        return ResponseEntity.ok(new SondertageMatchCodeSingleDateDto());
    }

    @Override
    public ResponseEntity<String> deleteSondertageMatchCodeSingleDate(DienstnehmerRefDto dienstnehmerRef, Integer matchCode, String validFrom) throws LHRWebClientException {
        log.info("MOCK deleteSondertageMatchCodeSingleDate({}, {}, {})", dienstnehmerRef, matchCode, validFrom);
        return ResponseEntity.ok(MOCK_DELETED);
    }

    @Override
    public ResponseEntity<SondertageTopLevelDto> postSondertageMatchCodeSingleDate(SondertageTopLevelDto dto) throws LHRWebClientException {
        log.info("MOCK postSondertageMatchCodeSingleDate({})", dto);
        return ResponseEntity.ok(dto != null ? dto : new SondertageTopLevelDto());
    }

    @Override
    public ResponseEntity<SondertageMatchCodeSingleDateDto> putSondertageMatchCodeSingleDate(SondertageMatchCodeSingleDateDto dto, Integer matchCode, String validFrom) throws LHRWebClientException {
        log.info("MOCK putSondertageMatchCodeSingleDate({}, {}, {})", dto, matchCode, validFrom);
        return ResponseEntity.ok(dto != null ? dto : new SondertageMatchCodeSingleDateDto());
    }

    @Override
    public ResponseEntity<DienstraederTopLevelDto> getDienstraederTopLevel(DienstnehmerRefDto dienstnehmerRef) throws LHRWebClientException {
        log.info("MOCK getDienstraederTopLevel({})", dienstnehmerRef);
        return ResponseEntity.ok(new DienstraederTopLevelDto());
    }

    @Override
    public ResponseEntity<DienstraederSingleTopLevelDto> getDienstraederSingleDateSettings(DienstnehmerRefDto dienstnehmerRef, String validFrom) throws LHRWebClientException {
        log.info("MOCK getDienstraederSingleDateSettings({}, {})", dienstnehmerRef, validFrom);
        return ResponseEntity.ok(new DienstraederSingleTopLevelDto());
    }

    @Override
    public ResponseEntity<DienstraederTopLevelDto> postDienstraederTopLevel(DienstraederTopLevelDto dto) throws LHRWebClientException {
        log.info("MOCK postDienstraederTopLevel({})", dto);
        return ResponseEntity.ok(dto != null ? dto : new DienstraederTopLevelDto());
    }

    @Override
    public ResponseEntity<UrlaubsdatenStandaloneDto> getUrlaube(String faKz, Integer faNr, Integer dnNr, String monthFrom, String monthTo) throws LHRWebClientException {
        log.info("MOCK getUrlaube({}, {}, {}, {}, {})", faKz, faNr, dnNr, monthFrom, monthTo);
        return ResponseEntity.ok(new UrlaubsdatenStandaloneDto());
    }

    @Override
    public ResponseEntity<UrlaubsdatenStandaloneDetailsDto> getUrlaubeDetails(String faKz, Integer faNr, Integer dnNr, String monthFrom) throws LHRWebClientException {
        log.info("MOCK getUrlaubeDetails({}, {}, {}, {})", faKz, faNr, dnNr, monthFrom);
        return ResponseEntity.ok(new UrlaubsdatenStandaloneDetailsDto());
    }

    @Override
    public ResponseEntity<?> deleteDienstraederSingleDate(DienstnehmerRefDto dienstnehmerRef, String validFrom, boolean withSettings) throws LHRWebClientException {
        log.info("MOCK deleteDienstraederSingleDate({}, {}, {})", dienstnehmerRef, validFrom, withSettings);
        return ResponseEntity.ok(MOCK_DELETED);
    }

    @Override
    public ResponseEntity<DienstraederSingleTopLevelDto> postDienstraederSingleDateTopLevel(DienstraederSingleTopLevelDto dto, String validFrom, boolean withSettings) throws LHRWebClientException {
        log.info("MOCK postDienstraederSingleDateTopLevel({}, {}, {})", dto, validFrom, withSettings);
        return ResponseEntity.ok(dto != null ? dto : new DienstraederSingleTopLevelDto());
    }

    @Override
    public ResponseEntity<MitversicherteTopLevelDto> getMitversicherteTopLevel(DienstnehmerRefDto dienstnehmerRef) throws LHRWebClientException {
        log.info("MOCK getMitversicherteTopLevel({})", dienstnehmerRef);
        return ResponseEntity.ok(new MitversicherteTopLevelDto());
    }

    @Override
    public ResponseEntity<String> deleteMitversicherteStammdaten(DienstnehmerRefDto dienstnehmerRef, Integer mvNr, String validFrom) throws LHRWebClientException {
        log.info("MOCK deleteMitversicherteStammdaten({}, {}, {})", dienstnehmerRef, mvNr, validFrom);
        return ResponseEntity.ok(MOCK_DELETED);
    }

    @Override
    public ResponseEntity<MitversicherteTopLevelDto> postMitversicherteTopLevel(MitversicherteTopLevelDto dto) throws LHRWebClientException {
        log.info("MOCK postMitversicherteTopLevel({})", dto);
        return ResponseEntity.ok(dto != null ? dto : new MitversicherteTopLevelDto());
    }

    @Override
    public ResponseEntity<MitversicherteSingleDto> putMitversicherteTopLevel(Integer mvNr, MitversicherteSingleDto dto, String validFrom) throws LHRWebClientException {
        log.info("MOCK putMitversicherteTopLevel({}, {}, {})", mvNr, dto, validFrom);
        return ResponseEntity.ok(dto != null ? dto : new MitversicherteSingleDto());
    }

    @Override
    public ResponseEntity<AnfrageSuccessDto> getZeitmodellanfrage(String faKz, Integer faNr, Integer dnNr) throws LHRWebClientException {
        log.info("MOCK getZeitmodellanfrage({}, {}, {})", faKz, faNr, dnNr);
        return ResponseEntity.ok(new AnfrageSuccessDto());
    }

    @Override
    public ResponseEntity<AnfrageSuccessDto> postZeitmodellanfrage(String faKz, Integer faNr, Integer dnNr, Integer zeitmodellNr, String fromDate) throws LHRWebClientException {
        log.info("MOCK postZeitmodellanfrage({}, {}, {}, {}, {})", faKz, faNr, dnNr, zeitmodellNr, fromDate);
        return ResponseEntity.ok(new AnfrageSuccessDto());
    }

    @Override
    public ResponseEntity<DnEintritteDto> postEintritt(String faKz, Integer faNr, Integer dnNr, String modify, String ignore, DnEintritteDto dnEintritte) throws LHRWebClientException {
        log.info("MOCK postEintritt({}, {}, {}, {}, {}, <dto>)", faKz, faNr, dnNr, modify, ignore);
        return ResponseEntity.status(HttpStatus.CREATED).body(dnEintritte != null ? dnEintritte : new DnEintritteDto());
    }

    @Override
    public ResponseEntity<DnZeitdatenPeriodensummenDto> getPeriodensummen(String faKz, Integer faNr, Integer dnNr, String zeitdatenMonth, String zeitspeicherrefs) {
        log.info("MOCK getPeriodensummen({}, {}, {}, {}, {})", faKz, faNr, dnNr, zeitdatenMonth, zeitspeicherrefs);
        return ResponseEntity.ok(new DnZeitdatenPeriodensummenDto());
    }

    @Override
    public ResponseEntity<ZeitdatenStatusDto> geZeitdatenStatus(String faKz, Integer faNr, Integer dnNr) {
        log.info("MOCK geZeitdatenStatus({}, {}, {})", faKz, faNr, dnNr);
        return ResponseEntity.ok(new ZeitdatenStatusDto());
    }

    @Override
    public ResponseEntity<AnfrageSuccessDto> postBuchunganfrage(String faKz, Integer faNr, Integer dnNr, String type, String date, String original, String zspNr, String terminal) throws LHRWebClientException {
        log.info("MOCK postBuchunganfrage({}, {}, {}, {}, {}, {}, {}, {})", faKz, faNr, dnNr, type, date, original, zspNr, terminal);
        return ResponseEntity.ok(new AnfrageSuccessDto());
    }

    @Override
    public ResponseEntity<AnfrageSuccessDto> postStatusanfrage(String faKz, Integer faNr, Integer dnNr, String day, StatusAnfrage status) throws LHRWebClientException {
        log.info("MOCK postStatusanfrage({}, {}, {}, {}, {})", faKz, faNr, dnNr, day, status);
        return ResponseEntity.ok(new AnfrageSuccessDto());
    }

    @Override
    public ResponseEntity<DnEintritteDto> getEintritte(String faKz, Integer faNr, Integer dnNr, String effectiveDate, String[] art) throws LHRWebClientException {
        log.info("MOCK getEintritte({}, {}, {}, {}, <art>)", faKz, faNr, dnNr, effectiveDate);
        return ResponseEntity.ok(new DnEintritteDto());
    }

    @Override
    public ResponseEntity<DnEintrittDto> getEintritt(String faKz, Integer faNr, Integer dnNr, String grund, String day) throws LHRWebClientException {
        log.info("MOCK getEintritt({}, {}, {}, {}, {})", faKz, faNr, dnNr, grund, day);
        return ResponseEntity.ok(new DnEintrittDto());
    }

    @Override
    public ResponseEntity<DnEintrittDto> putEintritt(String faKz, Integer faNr, Integer dnNr, String grund, String day, String modify, String ignore, DnEintrittDto dnEintritt) throws LHRWebClientException {
        log.info("MOCK putEintritt({}, {}, {}, {}, {}, {}, <dto>)", faKz, faNr, dnNr, grund, day, modify);
        return ResponseEntity.ok(dnEintritt != null ? dnEintritt : new DnEintrittDto());
    }

    @Override
    public ResponseEntity<DnEintrittDto> deleteEintritt(String faKz, Integer faNr, Integer dnNr, String grund, String day, String modify, String ignore) throws LHRWebClientException {
        log.info("MOCK deleteEintritt({}, {}, {}, {}, {}, {}, {})", faKz, faNr, dnNr, grund, day, modify, ignore);
        return ResponseEntity.ok(new DnEintrittDto());
    }

    @Override
    public ResponseEntity<DnStammStandaloneDto> getDienstnehmerstammFromLHR(String faKz, Integer faNr, Integer dnNr) throws LHRWebClientException {
        log.info("MOCK getDienstnehmerstammFromLHR({}, {}, {})", faKz, faNr, dnNr);
        return ResponseEntity.ok(new DnStammStandaloneDto());
    }

    @Override
    public ResponseEntity<?> getKostenstellenaufteilungFromLHR(String faKz, Integer faNr, Integer dnNr, String date) throws LHRWebClientException {
        log.info("MOCK getKostenstellenaufteilungFromLHR({}, {}, {}, {})", faKz, faNr, dnNr, date);
        return ResponseEntity.ok(new KostenstellenaufteilungTopLevelDto());
    }

    @Override
    public ResponseEntity<String> deleteKostenstellenaufteilungFromLHR(String faKz, Integer faNr, Integer dnNr, String day) throws LHRWebClientException {
        log.info("MOCK deleteKostenstellenaufteilungFromLHR({}, {}, {}, {})", faKz, faNr, dnNr, day);
        return ResponseEntity.ok(MOCK_DELETED);
    }

    @Override
    public ResponseEntity<?> getPersoenlicheSaetzeFromLHR(String faKz, Integer faNr, Integer dnNr, String date, Integer satzNr) throws LHRWebClientException {
        log.info("MOCK getPersoenlicheSaetzeFromLHR({}, {}, {}, {}, {})", faKz, faNr, dnNr, date, satzNr);
        return ResponseEntity.ok(new PersoenlicheSaetzeTopLevelDto());
    }

    @Override
    public ResponseEntity<?> getVariableDatenFromLHR(DienstnehmerRefDto dienstnehmerRef, String eintrittsDatum) throws LHRWebClientException {
        log.info("MOCK getVariableDatenFromLHR({}, {})", dienstnehmerRef, eintrittsDatum);
        return ResponseEntity.ok(new TopLevelDto());
    }

    @Override
    public ResponseEntity<List<DnStammStandaloneDto>> findAllDienstnehmersFromLHR(String faKz, Integer faNr, Integer minDnNr, Integer maxDnNr, String effectiveDate, String activeSince) throws LHRWebClientException {
        log.info("MOCK findAllDienstnehmersFromLHR({}, {}, {}, {}, {}, {})", faKz, faNr, minDnNr, maxDnNr, effectiveDate, activeSince);
        return ResponseEntity.ok(Collections.singletonList(new DnStammStandaloneDto()));
    }

    @Override
    public ResponseEntity<DnStammStandaloneDto> sendDienstnehmerstammToLHR(DnStammStandaloneDto dnStammStandalone) throws LHRWebClientException {
        log.info("MOCK sendDienstnehmerstammToLHR(<dto>)");
        return ResponseEntity.status(HttpStatus.CREATED).body(dnStammStandalone != null ? dnStammStandalone : new DnStammStandaloneDto());
    }

    @Override
    public ResponseEntity<?> sendKostenstellenaufteilungToLHR(KostenstellenaufteilungTopLevelDto dto, KostenstellenaufteilungSingleTopLevelDto singleDto, String date) throws LHRWebClientException {
        log.info("MOCK sendKostenstellenaufteilungToLHR({}, {}, {})", dto, singleDto, date);
        return ResponseEntity.status(HttpStatus.CREATED).body(singleDto != null ? singleDto : (dto != null ? dto : new KostenstellenaufteilungTopLevelDto()));
    }

    @Override
    public ResponseEntity<DnStammStandaloneDto> updateDienstnehmerstammOnLHR(DnStammStandaloneDto dnStammStandalone) throws LHRWebClientException {
        log.info("MOCK updateDienstnehmerstammOnLHR(<dto>)");
        return ResponseEntity.ok(dnStammStandalone != null ? dnStammStandalone : new DnStammStandaloneDto());
    }

    @Override
    public ResponseEntity<TopLevelDto> sendVariableDatenToLHR(TopLevelDto topLevelDTO, boolean vorgabewerte) throws LHRWebClientException {
        log.info("MOCK sendVariableDatenToLHR(<dto>, {})", vorgabewerte);
        return ResponseEntity.status(HttpStatus.CREATED).body(topLevelDTO != null ? topLevelDTO : new TopLevelDto());
    }

    @Override
    public ResponseEntity<TopLevelSingleDateDto> putVariableDatenToLHR(TopLevelSingleDateDto topLevelDTO, String eintrittsDatum) throws LHRWebClientException {
        log.info("MOCK putVariableDatenToLHR(<dto>, {})", eintrittsDatum);
        return ResponseEntity.ok(topLevelDTO != null ? topLevelDTO : new TopLevelSingleDateDto());
    }

    @Override
    public ResponseEntity<?> sendPersoenlicheSaetzeToLHR(PersoenlicheSaetzeTopLevelDto persoenlicheSaetzeTopLevelDto, PersoenlicheSaetzeSingleTopLevelDto persoenlicheSaetzeSingleTopLevelDto, Integer satzNummer, String date) throws LHRWebClientException {
        log.info("MOCK sendPersoenlicheSaetzeToLHR({}, {}, {}, {})", persoenlicheSaetzeTopLevelDto, persoenlicheSaetzeSingleTopLevelDto, satzNummer, date);
        Object body = persoenlicheSaetzeSingleTopLevelDto != null ? persoenlicheSaetzeSingleTopLevelDto : (persoenlicheSaetzeTopLevelDto != null ? persoenlicheSaetzeTopLevelDto : new PersoenlicheSaetzeTopLevelDto());
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @Override
    public File downloadFile(String path, HttpMethod method, Object body, Integer uriParameter, Map<String, String> queryParams, Integer personalnummer, String extension) throws LHRWebClientException {
        log.info("MOCK downloadFile({}, {}, <body>, {}, <params>, {}, {})", path, method, uriParameter, personalnummer, extension);
        String safeExt = (extension != null && !extension.isBlank()) ? extension : ".bin";
        String fileName = String.format("lhr-mock-%s%s", personalnummer != null ? String.valueOf(personalnummer) : "file", safeExt);
        return new File(System.getProperty("java.io.tmpdir"), fileName);
    }

    @Override
    public <T> ResponseEntity<T> buildResponse(String path, HttpMethod method, Object body, Integer uriParameter, Map<String, String> queryParams, Class<T> responseType, Integer personalnummer) throws LHRWebClientException {
        log.info("MOCK buildResponse({}, {}, <body>, {}, <params>, {}, {})", path, method, uriParameter, responseType != null ? responseType.getSimpleName() : "null", personalnummer);
        // Return 200 OK with null body as a generic stub — simple and clearly a mock.
        return ResponseEntity.ok().build();
    }
}