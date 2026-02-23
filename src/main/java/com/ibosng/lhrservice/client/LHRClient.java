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
import jakarta.annotation.Nullable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface LHRClient {
    ResponseEntity<DokumentRubrikenDto> getDokumenteRubrikiren(String faKz, Integer faNr,
                                                               @Nullable String location,
                                                               @Nullable Boolean count,
                                                               @Nullable String created) throws LHRWebClientException;

    ResponseEntity<DnDokumenteDto> getDokumenteInfo(String faKz, Integer faNr, Integer dnNr, Integer rubrikId,
                                                    @Nullable String created) throws LHRWebClientException;

    File downloadDokument(String faKz, Integer faNr, Integer dnNr, Integer rubrikId, Integer docId) throws LHRWebClientException;

    ResponseEntity<DnZeitdatenDto[]> getDienstnehmerZeitDatenAllDienstnehmer(DienstnehmerRefDto dienstnehmerRef) throws LHRWebClientException;

    ResponseEntity<DnZeitdatenDto[]> getDienstnehmerZeitdaten(DienstnehmerRefDto dienstnehmerRef, String from, String to, List<String> zeitspeicherRefs) throws LHRWebClientException;

    ResponseEntity<AnfrageSuccessDto> postAuszahlungsanfrage(DienstnehmerRefDto dienstnehmerRef, String day, Integer zspNr, Integer minutes) throws LHRWebClientException;

    ResponseEntity<AnfrageSuccessDto> getAuszahlungsanfrage(DienstnehmerRefDto dienstnehmerRef, Integer anfrageNr) throws LHRWebClientException;

    ResponseEntity<SondertageMatchCodeSingleDateDto> getSondertageMatchCodeSingleDate(DienstnehmerRefDto dienstnehmerRef, Integer matchCode, String validFrom) throws LHRWebClientException;

    ResponseEntity<String> deleteSondertageMatchCodeSingleDate(DienstnehmerRefDto dienstnehmerRef, Integer matchCode, String validFrom) throws LHRWebClientException;

    ResponseEntity<SondertageTopLevelDto> postSondertageMatchCodeSingleDate(SondertageTopLevelDto dto) throws LHRWebClientException;

    ResponseEntity<SondertageMatchCodeSingleDateDto> putSondertageMatchCodeSingleDate(SondertageMatchCodeSingleDateDto dto, Integer matchCode, String validFrom) throws LHRWebClientException;

    ResponseEntity<DienstraederTopLevelDto> getDienstraederTopLevel(DienstnehmerRefDto dienstnehmerRef) throws LHRWebClientException;

    ResponseEntity<DienstraederSingleTopLevelDto> getDienstraederSingleDateSettings(DienstnehmerRefDto dienstnehmerRef, String validFrom) throws LHRWebClientException;

    ResponseEntity<DienstraederTopLevelDto> postDienstraederTopLevel(DienstraederTopLevelDto dto) throws LHRWebClientException;

    ResponseEntity<UrlaubsdatenStandaloneDto> getUrlaube(String faKz, Integer faNr, Integer dnNr, String monthFrom, String monthTo) throws LHRWebClientException;

    ResponseEntity<UrlaubsdatenStandaloneDetailsDto> getUrlaubeDetails(String faKz, Integer faNr, Integer dnNr, String monthFrom) throws LHRWebClientException;

    ResponseEntity<?> deleteDienstraederSingleDate(DienstnehmerRefDto dienstnehmerRef, String validFrom, boolean withSettings) throws LHRWebClientException;

    ResponseEntity<DienstraederSingleTopLevelDto> postDienstraederSingleDateTopLevel(DienstraederSingleTopLevelDto dto, String validFrom, boolean withSettings) throws LHRWebClientException;

    ResponseEntity<MitversicherteTopLevelDto> getMitversicherteTopLevel(DienstnehmerRefDto dienstnehmerRef) throws LHRWebClientException;

    ResponseEntity<String> deleteMitversicherteStammdaten(DienstnehmerRefDto dienstnehmerRef, Integer mvNr, String validFrom) throws LHRWebClientException;

    ResponseEntity<MitversicherteTopLevelDto> postMitversicherteTopLevel(MitversicherteTopLevelDto dto) throws LHRWebClientException;

    ResponseEntity<MitversicherteSingleDto> putMitversicherteTopLevel(Integer mvNr, MitversicherteSingleDto dto, String validFrom) throws LHRWebClientException;

    ResponseEntity<AnfrageSuccessDto> getZeitmodellanfrage(String faKz, Integer faNr, Integer dnNr) throws LHRWebClientException;

    ResponseEntity<AnfrageSuccessDto> postZeitmodellanfrage(String faKz, Integer faNr, Integer dnNr, Integer zeitmodellNr, String fromDate) throws LHRWebClientException;

    ResponseEntity<DnEintritteDto> postEintritt(String faKz, Integer faNr, Integer dnNr, String modify, String ignore, DnEintritteDto dnEintritte) throws LHRWebClientException;

    ResponseEntity<DnZeitdatenPeriodensummenDto> getPeriodensummen(String faKz, Integer faNr, Integer dnNr, String zeitdatenMonth, String zeitspeicherrefs);

    ResponseEntity<ZeitdatenStatusDto> geZeitdatenStatus(String faKz, Integer faNr, Integer dnNr);

    ResponseEntity<AnfrageSuccessDto> postBuchunganfrage(String faKz, Integer faNr, Integer dnNr, String type, String date,
                                                         @Nullable String original,
                                                         @Nullable String zspNr,
                                                         @Nullable String terminal) throws LHRWebClientException;

    ResponseEntity<AnfrageSuccessDto> postStatusanfrage(String faKz, Integer faNr, Integer dnNr,
                                                        String day, StatusAnfrage status) throws LHRWebClientException;

    ResponseEntity<DnEintritteDto> getEintritte(String faKz, Integer faNr, Integer dnNr, String effectiveDate, String[] art) throws LHRWebClientException;

    ResponseEntity<DnEintrittDto> getEintritt(String faKz, Integer faNr, Integer dnNr, String grund, String day) throws LHRWebClientException;

    ResponseEntity<DnEintrittDto> putEintritt(String faKz, Integer faNr, Integer dnNr, String grund,
                                              String day, String modify, String ignore, DnEintrittDto dnEintritt) throws LHRWebClientException;

    ResponseEntity<DnEintrittDto> deleteEintritt(String faKz, Integer faNr, Integer dnNr, String grund,
                                                 String day, String modify, String ignore) throws LHRWebClientException;

    ResponseEntity<DnStammStandaloneDto> getDienstnehmerstammFromLHR(String faKz, Integer faNr, Integer dnNr) throws LHRWebClientException;

    ResponseEntity<?> getKostenstellenaufteilungFromLHR(String faKz, Integer faNr, Integer dnNr, String date) throws LHRWebClientException;

    ResponseEntity<String> deleteKostenstellenaufteilungFromLHR(String faKz, Integer faNr, Integer dnNr, String day) throws LHRWebClientException;

    ResponseEntity<?> getPersoenlicheSaetzeFromLHR(String faKz, Integer faNr, Integer dnNr, String date, Integer satzNr) throws LHRWebClientException;

    ResponseEntity<?> getVariableDatenFromLHR(DienstnehmerRefDto dienstnehmerRef, String eintrittsDatum) throws LHRWebClientException;

    ResponseEntity<List<DnStammStandaloneDto>> findAllDienstnehmersFromLHR(String faKz, Integer faNr, Integer minDnNr, Integer maxDnNr,
                                                                           String effectiveDate, String activeSince) throws LHRWebClientException;

    ResponseEntity<DnStammStandaloneDto> sendDienstnehmerstammToLHR(DnStammStandaloneDto dnStammStandalone) throws LHRWebClientException;

    ResponseEntity<?> sendKostenstellenaufteilungToLHR(
            KostenstellenaufteilungTopLevelDto dto,
            KostenstellenaufteilungSingleTopLevelDto singleDto,
            String date) throws LHRWebClientException;

    ResponseEntity<DnStammStandaloneDto> updateDienstnehmerstammOnLHR(DnStammStandaloneDto dnStammStandalone) throws LHRWebClientException;

    ResponseEntity<TopLevelDto> sendVariableDatenToLHR(TopLevelDto topLevelDTO, boolean vorgabewerte) throws LHRWebClientException;

    ResponseEntity<TopLevelSingleDateDto> putVariableDatenToLHR(TopLevelSingleDateDto topLevelDTO, String eintrittsDatum) throws LHRWebClientException;

    ResponseEntity<?> sendPersoenlicheSaetzeToLHR(
            PersoenlicheSaetzeTopLevelDto persoenlicheSaetzeTopLevelDto,
            PersoenlicheSaetzeSingleTopLevelDto persoenlicheSaetzeSingleTopLevelDto,
            Integer satzNummer,
            String date) throws LHRWebClientException;

    File downloadFile(String path, HttpMethod method, Object body, Integer uriParameter, Map<String, String> queryParams, Integer personalnummer, String extension) throws LHRWebClientException;

    <T> ResponseEntity<T> buildResponse(String path, HttpMethod method, Object body, Integer uriParameter, Map<String, String> queryParams, Class<T> responseType, Integer personalnummer) throws LHRWebClientException;
}
