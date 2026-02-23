package com.ibosng.gatewayservice.dtos.masterdata;

import com.ibosng.dbibosservice.dtos.BasicPersonDto;
import com.ibosng.gatewayservice.dtos.LandDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MitarbeiterMasterdata implements Serializable {

    private List<String> anredeList;
    private List<String> titelList;
    private Map<String, String> familienstandList;
    private Map<String, String> geschlechtList;
    private List<String> kostenstelleList;
    private List<String> dienstortList;
    private List<BasicPersonDto> fuehrungskraftList;
    private List<BasicPersonDto> startcoachList;
    private List<String> firmenList;
    private List<String> arbeitsgenehmigungList;
    private List<String> staatsbuergerschaftList;
    private List<String> landList;
    private List<LandDto> completeLandList;
    private List<String> beschaeftigungsausmassList;
    private List<String> beschaeftigungsstatusList;
    private List<String> jobbeschreibungList;
    private List<String> kategorieList;
    private List<String> kollektivvertragList;
    private List<String> taetigkeitList;
    private List<String> verwendungsgruppeList;
    private List<String> abgeltungUeberstundenList;
    private List<String> arbeitszeitmodellList;
    private List<String> artDerZulage;
    private List<String> vertragsartList;
    private List<String> auswahlBegruendungFuerDurchrechnerList = List.of("DRM – Flex.AZ", "DRM – Gestaffelte Eintritte 1", "DRM – Auslastungsunterschiede", "DRM – Gestaffelte Eintritte 2");
    private List<String> jobticketList;
    private List<String> klasseList;
    private List<String> verwandtschaftList;
    private List<String> mutterspracheList;
    private Map<String, String> abrechnungsgruppeList;
    private Map<String, String> dienstnehmergruppeList;
    private Map<String, String> kommunalsteuergemeindeList;
    private List<String> vertragsaenderungStatusesList;
    private List<String> teilnehmerNotizenKategorieList;
    private List<String> seminarAustrittsgrundList;
    private List<String> sprachkenntnisNiveauList;
    private List<String> aiUiShortcutsList;
    private Map<String, List<String>> kostenstelleMap;
    private List<String> teilnehmerNotizenTypeList;
    private List<String> seminarPruefungBezeichnungList;
    private List<String> seminarPruefungArtList;
    private List<String> seminarPruefungGegenstandList;
    private List<String> seminarPruefungNiveauList;
    private List<String> seminarPruefungInstitutList;
    private List<String> seminarPruefungBegruendungList;
    private List<String> seminarPruefungErgebnisList;
    private List<String> teilnehmerAusbildungTypeList;
    private List<BasicPersonDto> seminarVertretungTrainersList;
}
