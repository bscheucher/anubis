package com.ibosng.dbservice.services.changelog.impl;

import com.google.common.base.CaseFormat;
import com.ibosng.dbservice.dtos.changelog.ChangeLogDto;
import com.ibosng.dbservice.dtos.changelog.FieldChangeDto;
import com.ibosng.dbservice.dtos.changelog.MitarbeiterChangeLogDto;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.*;
import com.ibosng.dbservice.services.changelog.EntityChangeLogService;
import com.ibosng.dbservice.services.changelog.MAChangeLogService;
import com.ibosng.dbservice.services.mitarbeiter.*;
import com.ibosng.dbservice.utils.FieldNameMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class MAChangeLogServiceImpl implements MAChangeLogService {
    private final static String STAMMDATEN_HISTORY = "stammdaten_history";
    private final static String STAMMDATEN_ID = "stammdaten_id";
    private final static String VERTRAGSDATEN_HISTORY = "vertragsdaten_history";
    private final static String VERTRAGSDATEN_ID = "vertragsdaten_id";
    private final static String GEHALT_INFO_HISTORY = "gehalt_info_history";
    private final static String GEHALT_INFO_ID = "gehalt_info_id";
    private final static String GEHALT_INFO_ZULAGE_HISTORY = "gehalt_info_zulage_history";
    private final static String GEHALT_INFO_ZULAGE_ID = "gehalt_info_zulage_id";
    private final static String ARBEITSZEITEN_INFO_HISTORY = "arbeitszeiten_info_history";
    private final static String ARBEITSZEITEN_INFO_ID = "arbeitszeiten_info_id";
    private final static String ARBEITSZEITEN_HISTORY = "arbeitszeiten_history";
    private final static String ARBEITSZEITEN_ID = "arbeitszeiten_id";

    private final static String VERTRAGSDATEN = "vertragsdaten";
    private final static String GEHALT_INFO = "gehalt_info";
    private final static String ARBEITSZEITEN_INFO = "arbeitszeiten_info";
    private final static String ARBEITSZEITEN = "arbeitszeiten";

    private final PersonalnummerService personalnummerService;
    private final EntityChangeLogService entityChangeLogService;
    private final StammdatenService stammdatenService;
    private final VertragsdatenService vertragsdatenService;
    private final GehaltInfoService gehaltInfoService;
    private final ArbeitszeitenService arbeitszeitenService;
    private final ArbeitszeitenInfoService arbeitszeitenInfoService;
    private final GehaltInfoZulageService gehaltInfoZulageService;

    @Override
    public MitarbeiterChangeLogDto getMAChangeLog(String personalnummerString) {
        MitarbeiterChangeLogDto mitarbeiterChangeLogDto = new MitarbeiterChangeLogDto();
        Personalnummer personalnummer = personalnummerService.findByPersonalnummer(personalnummerString);
        if (personalnummer == null) {
            log.error("Personalnummer not found for {}", personalnummerString);
            return null;
        }
        Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(personalnummerString);
        if (stammdaten == null) {
            log.error("Stammdaten not found for personalnummer {}", personalnummerString);
            return null;
        }
        List<ChangeLogDto> changeLogDtoStammdaten = entityChangeLogService.getHistoryChangeLogs(STAMMDATEN_HISTORY,
                STAMMDATEN_ID, stammdaten.getId(), personalnummer.getOnboardedOn());

        mitarbeiterChangeLogDto.setStammdatenAenderungen(changeLogDtoStammdaten);

        Vertragsdaten vertragsdaten = vertragsdatenService.findAllByPNAndStatusesNotInVertragsdatenaenderungen(personalnummerString, List.of(MitarbeiterStatus.VALIDATED, MitarbeiterStatus.NOT_VALIDATED, MitarbeiterStatus.ACTIVE)).stream().findFirst().orElse(null);
        if (vertragsdaten == null) {
            log.error("Vertragsdaten not found for personalnummer {}", personalnummerString);
            return null;
        }
        List<ChangeLogDto> changeLogDtoVertragsdaten = new ArrayList<>(entityChangeLogService.getHistoryChangeLogs(VERTRAGSDATEN_HISTORY,
                VERTRAGSDATEN_ID, vertragsdaten.getId(), personalnummer.getOnboardedOn()));

        GehaltInfo gehaltInfo = gehaltInfoService.findByVertragsdatenId(vertragsdaten.getId());
        if (gehaltInfo == null) {
            log.error("GehaltInfo not found for personalnummer {}", personalnummerString);
            return null;
        }
        List<ChangeLogDto> changeLogDtoGehaltInfo = entityChangeLogService.getHistoryChangeLogs(GEHALT_INFO_HISTORY,
                GEHALT_INFO_ID, gehaltInfo.getId(), personalnummer.getOnboardedOn());


        List<GehaltInfoZulage> gehaltInfoZulageList = gehaltInfoZulageService.findAllByGehaltInfoId(gehaltInfo.getId());
        if (gehaltInfoZulageList == null || gehaltInfoZulageList.isEmpty()) {
            log.error("GehaltInfoZulage not found for personalnummer {}", personalnummerString);
        } else {
            for (GehaltInfoZulage zulage : gehaltInfoZulageList) {
                List<ChangeLogDto> zulageChangeLogs = entityChangeLogService.getHistoryChangeLogs(GEHALT_INFO_ZULAGE_HISTORY,
                        GEHALT_INFO_ZULAGE_ID, zulage.getId(), personalnummer.getOnboardedOn()
                );
                if (zulageChangeLogs != null && !zulageChangeLogs.isEmpty()) {
                    String fieldName = zulage.getArtDerZulage().toLowerCase();

                    for (ChangeLogDto logDto : zulageChangeLogs) {
                        logDto.getFieldChanges().forEach(change ->
                                change.setFieldName(fieldName)
                        );
                    }
                    changeLogDtoVertragsdaten.addAll(zulageChangeLogs);
                }
            }
        }


        ArbeitszeitenInfo arbeitszeitenInfo = arbeitszeitenInfoService.findByVertragsdatenId(vertragsdaten.getId());
        if (arbeitszeitenInfo == null) {
            log.error("ArbeitszeitenInfo not found for personalnummer {}", personalnummerString);
            return null;
        }
        List<ChangeLogDto> changeLogDtoArbeitszeitenInfo = entityChangeLogService.getHistoryChangeLogs(ARBEITSZEITEN_INFO_HISTORY,
                ARBEITSZEITEN_INFO_ID, arbeitszeitenInfo.getId(), personalnummer.getOnboardedOn());

        Arbeitszeiten arbeitszeitenNormale = arbeitszeitenService.findByArbAndArbeitszeitenInfoId(arbeitszeitenInfo.getId()).stream().filter(arbeitszeiten -> arbeitszeiten.getKernzeit() == null || Boolean.FALSE.equals(arbeitszeiten.getKernzeit())).findFirst().orElse(null);
        if (arbeitszeitenNormale == null) {
            log.error("arbeitszeitenNormale not found for personalnummer {}", personalnummerString);
        } else {
            List<ChangeLogDto> changeLogDtoArbeitszeitenNormale = entityChangeLogService.getHistoryChangeLogs(ARBEITSZEITEN_HISTORY,
                    ARBEITSZEITEN_ID, arbeitszeitenNormale.getId(), personalnummer.getOnboardedOn());
            changeLogDtoVertragsdaten.addAll(changeLogDtoArbeitszeitenNormale);
        }


        Arbeitszeiten arbeitszeitenKernzeit = arbeitszeitenService.findByArbAndArbeitszeitenInfoId(arbeitszeitenInfo.getId()).stream().filter(arbeitszeiten -> arbeitszeiten.getKernzeit() != null && Boolean.TRUE.equals(arbeitszeiten.getKernzeit())).findFirst().orElse(null);
        if (arbeitszeitenKernzeit == null) {
            log.error("arbeitszeitenKernzeit not found for personalnummer {}", personalnummerString);
        } else {
            List<ChangeLogDto> changeLogDtoArbeitszeitenKernzeit = entityChangeLogService.getHistoryChangeLogs(ARBEITSZEITEN_HISTORY,
                    ARBEITSZEITEN_ID, arbeitszeitenKernzeit.getId(), personalnummer.getOnboardedOn());
            changeLogDtoVertragsdaten.addAll(changeLogDtoArbeitszeitenKernzeit);
        }

        changeLogDtoVertragsdaten.addAll(changeLogDtoGehaltInfo);
        changeLogDtoVertragsdaten.addAll(changeLogDtoArbeitszeitenInfo);
        mitarbeiterChangeLogDto.setVertragsaenderungen(changeLogDtoVertragsdaten);

        return mitarbeiterChangeLogDto;
    }

    @Override
    public List<FieldChangeDto> getVertragsaenderungenPredecessorAndSuccessor(Integer predecessor, Integer successor) {
        List<FieldChangeDto> vertragsdatenAenderungen = entityChangeLogService.getChangeLogs(VERTRAGSDATEN, successor, predecessor);
        GehaltInfo gehaltInfoPredecessor = gehaltInfoService.findByVertragsdatenId(predecessor);
        GehaltInfo gehaltInfoSuccessor = gehaltInfoService.findByVertragsdatenId(successor);
        List<FieldChangeDto> gehaltInfoAenderungen = entityChangeLogService.getChangeLogs(GEHALT_INFO, gehaltInfoSuccessor.getId(), gehaltInfoPredecessor.getId());
        List<FieldChangeDto> gehaltInfoZulageAenderungen = getGehaltInfoZulageAenderungen(gehaltInfoPredecessor.getId(), gehaltInfoSuccessor.getId());
        ArbeitszeitenInfo arbeitszeitenInfoPredecessor = arbeitszeitenInfoService.findByVertragsdatenId(predecessor);
        ArbeitszeitenInfo arbeitszeitenInfoSucessor = arbeitszeitenInfoService.findByVertragsdatenId(successor);
        List<FieldChangeDto> arbeitszeitenInfoAenderungen = entityChangeLogService.getChangeLogs(ARBEITSZEITEN_INFO, arbeitszeitenInfoSucessor.getId(), arbeitszeitenInfoPredecessor.getId());
        List<FieldChangeDto> arbeitszeitenAenderungen = new ArrayList<>(getArbeitszeitenAenderungen(arbeitszeitenInfoPredecessor.getId(), arbeitszeitenInfoSucessor.getId(), Boolean.TRUE));
        arbeitszeitenAenderungen.addAll(getArbeitszeitenAenderungen(arbeitszeitenInfoPredecessor.getId(), arbeitszeitenInfoSucessor.getId(), Boolean.FALSE));
        vertragsdatenAenderungen.forEach(this::mapChangeLogDto);
        gehaltInfoAenderungen.forEach(this::mapChangeLogDto);
        gehaltInfoZulageAenderungen.forEach(this::mapChangeLogDto);
        arbeitszeitenAenderungen.forEach(this::mapChangeLogDto);
        arbeitszeitenInfoAenderungen.forEach(this::mapChangeLogDto);
        Set<FieldChangeDto> all = new HashSet<>();
        all.addAll(vertragsdatenAenderungen);
        all.addAll(gehaltInfoAenderungen);
        all.addAll(gehaltInfoZulageAenderungen);
        all.addAll(arbeitszeitenAenderungen);
        all.addAll(arbeitszeitenInfoAenderungen);
        return all.stream().filter(change ->
                !"vertragsdatenId".equals(change.getFieldName()) &&
                        !"aarbeitszeitenInfoId".equals(change.getFieldName()) &&
                        !"achangedBy".equals(change.getFieldName())).toList();
    }


    private List<FieldChangeDto> getArbeitszeitenAenderungen(Integer arbeitszeitenIdPredecessor, Integer arbeitszeitenIdSuccessor, Boolean isKernzeit) {
        List<FieldChangeDto> result = new ArrayList<>();
        Arbeitszeiten arbeistzeitenListPredecessor;
        Arbeitszeiten arbeitszeitenListSuccessor;
        String prefix = isKernzeit ? "k" : "a";
        if(isKernzeit) {
            arbeistzeitenListPredecessor = arbeitszeitenService.findByArbAndArbeitszeitenInfoId(arbeitszeitenIdPredecessor).stream().filter(arbeitszeiten -> arbeitszeiten.getKernzeit() != null && isKernzeit.equals(arbeitszeiten.getKernzeit())).findFirst().orElse(null);
            arbeitszeitenListSuccessor = arbeitszeitenService.findByArbAndArbeitszeitenInfoId(arbeitszeitenIdSuccessor).stream().filter(arbeitszeiten -> arbeitszeiten.getKernzeit() != null && isKernzeit.equals(arbeitszeiten.getKernzeit())).findFirst().orElse(null);
        } else {
            arbeistzeitenListPredecessor = arbeitszeitenService.findByArbAndArbeitszeitenInfoId(arbeitszeitenIdPredecessor).stream().filter(arbeitszeiten -> arbeitszeiten.getKernzeit() == null || isKernzeit.equals(arbeitszeiten.getKernzeit())).findFirst().orElse(null);
            arbeitszeitenListSuccessor = arbeitszeitenService.findByArbAndArbeitszeitenInfoId(arbeitszeitenIdSuccessor).stream().filter(arbeitszeiten -> arbeitszeiten.getKernzeit() == null || isKernzeit.equals(arbeitszeiten.getKernzeit())).findFirst().orElse(null);
        }
        if (arbeitszeitenListSuccessor != null && arbeistzeitenListPredecessor != null) {
            List<FieldChangeDto> arbeitszeitenAenderungen = entityChangeLogService.getChangeLogs(ARBEITSZEITEN, arbeitszeitenListSuccessor.getId(), arbeistzeitenListPredecessor.getId());
            arbeitszeitenAenderungen.forEach(change -> change.setFieldName(prefix + change.getFieldName()));
            result.addAll(arbeitszeitenAenderungen);
        }
        return result;
    }


    private List<FieldChangeDto> getGehaltInfoZulageAenderungen(Integer gehaltInfoIdPredecessor, Integer gehaltInfoIdSuccessor) {
        List<FieldChangeDto> result = new ArrayList<>();
        List<GehaltInfoZulage> gehaltInfoZulageListPredecessor = gehaltInfoZulageService.findAllByGehaltInfoId(gehaltInfoIdPredecessor);
        List<GehaltInfoZulage> gehaltInfoZulageListSuccessor = gehaltInfoZulageService.findAllByGehaltInfoId(gehaltInfoIdSuccessor);
        for (GehaltInfoZulage zulagePredecessor : gehaltInfoZulageListPredecessor) {
            GehaltInfoZulage neuZulage = gehaltInfoZulageListSuccessor.stream().filter(zulageSucessor -> zulagePredecessor.getArtDerZulage().equals(zulageSucessor.getArtDerZulage())).findFirst().orElse(null);
            if (neuZulage != null) {
                FieldChangeDto fieldChangeDto = new FieldChangeDto();
                fieldChangeDto.setFieldName(zulagePredecessor.getArtDerZulage());
                fieldChangeDto.setOldValue(String.valueOf(zulagePredecessor.getZulageInEuro()));
                fieldChangeDto.setNewValue(String.valueOf(neuZulage.getZulageInEuro()));
                result.add(fieldChangeDto);
            }
        }
        return result;
    }

    private void mapChangeLogDto(FieldChangeDto fieldChangeDto) {
        String fieldName = FieldNameMapper.getDtoFieldName(fieldChangeDto.getFieldName());
        if (!FieldNameMapper.isMappingExists(fieldChangeDto.getFieldName())) {
            fieldChangeDto.setFieldName(
                    CaseFormat.LOWER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL)
                            .convert(fieldChangeDto.getFieldName()));
        } else {
            fieldChangeDto.setFieldName(fieldName);
        }
    }
}
