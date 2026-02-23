package com.ibosng.lhrservice.services.impl;

import com.ibosng.dbservice.entities.Plz;
import com.ibosng.dbservice.entities.masterdata.Kommunalsteuergemeinde;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.masterdata.Taetigkeit;
import com.ibosng.dbservice.entities.mitarbeiter.GehaltInfo;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterType;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.services.masterdata.KommunalsteuergemeindeService;
import com.ibosng.dbservice.services.mitarbeiter.GehaltInfoService;
import com.ibosng.dbservice.services.mitarbeiter.VertragsdatenService;
import com.ibosng.lhrservice.client.LHRClient;
import com.ibosng.lhrservice.dtos.GemeindeDto;
import com.ibosng.lhrservice.dtos.GruppeNameKzDto;
import com.ibosng.lhrservice.dtos.GruppeNameNrDto;
import com.ibosng.lhrservice.dtos.variabledaten.*;
import com.ibosng.lhrservice.exceptions.LHRException;
import com.ibosng.lhrservice.exceptions.LHRWebClientException;
import com.ibosng.lhrservice.services.HelperService;
import com.ibosng.lhrservice.services.VariableDatenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.lhrservice.utils.Helpers.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class VariableDatenServiceImpl implements VariableDatenService {
    private final LHRClient lhrClient;
    private final HelperService helperService;
    private final VertragsdatenService vertragsdatenService;
    private final GehaltInfoService gehaltInfoService;
    private final KommunalsteuergemeindeService kommunalsteuergemeindeService;

    @Override
    public void processVariableDaten(Personalnummer personalnummer, String eintrittsDatum) throws LHRWebClientException {
        TopLevelSingleDateDto existingVariableDatenSingleDate = getVariableDatenSingleDate(personalnummer, eintrittsDatum);

        if (isValidVariableDatenSingleDate(existingVariableDatenSingleDate)) {
            lhrClient.putVariableDatenToLHR(mapVariableDatenSingleDateForLHR(personalnummer, existingVariableDatenSingleDate), eintrittsDatum);
        } else {
            lhrClient.sendVariableDatenToLHR(mapVariableDatenForLHR(personalnummer), true);
        }
    }

    private TopLevelSingleDateDto getVariableDatenSingleDate(Personalnummer personalnummer, String eintrittsDatum) {
        ResponseEntity<TopLevelSingleDateDto> response = (ResponseEntity<TopLevelSingleDateDto>) getVariableDaten(personalnummer, eintrittsDatum);
        return getBodyIfStatusOk(response);
    }

    private boolean isValidVariableDatenSingleDate(TopLevelSingleDateDto dto) {
        return Optional.ofNullable(dto)
                .map(TopLevelSingleDateDto::getVariableDaten)
                .map(variableDaten -> variableDaten.getData() != null)
                .orElse(false);
    }

    private boolean isValidVariableDaten(TopLevelDto dto) {
        return Optional.ofNullable(dto)
                .map(TopLevelDto::getVariableDaten)
                .filter(list -> !list.isEmpty())
                .map(list -> list.get(0).getData() != null)
                .orElse(false);
    }

    public ResponseEntity<?> getVariableDaten(Personalnummer personalnummer, String eintrittsDatum) {
        return handleLHRExceptions(personalnummer.getPersonalnummer(), "variable daten", () ->
                lhrClient.getVariableDatenFromLHR(helperService.createDienstnehmerRefDto(personalnummer), eintrittsDatum)
        );
    }

    private TopLevelDto mapVariableDatenForLHR(Personalnummer personalnummer) {
        Vertragsdaten vertragsdaten = vertragsdatenService.findByPersonalnummerIdAndStatus(personalnummer.getId(), List.of(MitarbeiterStatus.VALIDATED))
                .stream().findFirst().orElse(null);
        if (vertragsdaten == null) {
            throw new LHRException("Vertragsdaten are null in mapVariableDatenForLHR, stopping process!!!", new RuntimeException());
        }
        TopLevelDto topLevelDTO = new TopLevelDto();
        topLevelDTO.setDienstnehmer(helperService.createDienstnehmerRefDto(personalnummer));
        VariableDatenDto variableDaten = new VariableDatenDto();
        if (vertragsdaten.getEintritt() != null) {
            variableDaten.setValidFrom(vertragsdaten.getEintritt());
        }
        topLevelDTO.setVariableDaten(List.of(setDataForVariableDaten(variableDaten, vertragsdaten)));
        return topLevelDTO;
    }

    private TopLevelSingleDateDto mapVariableDatenSingleDateForLHR(Personalnummer personalnummer, TopLevelSingleDateDto existingVariableDatenSingleDate) throws LHRException {
        Vertragsdaten vertragsdaten = vertragsdatenService.findByPersonalnummerIdAndStatus(personalnummer.getId(), List.of(MitarbeiterStatus.VALIDATED))
                .stream().findFirst().orElse(null);
        if (vertragsdaten == null) {
            throw new LHRException("Vertragsdaten are null in mapVariableDatenSingleDateForLHR, stopping process!!!", new RuntimeException());
        }
        TopLevelSingleDateDto topLevelDTO = new TopLevelSingleDateDto();
        topLevelDTO.setDienstnehmer(helperService.createDienstnehmerRefDto(personalnummer));
        if (existingVariableDatenSingleDate != null) {
            topLevelDTO.setVariableDaten(setDataForVariableDaten(existingVariableDatenSingleDate.getVariableDaten(), vertragsdaten));
        } else {
            topLevelDTO.setVariableDaten(setDataForVariableDaten(null, vertragsdaten));
        }
        return topLevelDTO;
    }

/*    private void mapVariableDatenToIbosNG(TopLevelDTO topLevelDTO) {
        //TODO: LHR is the master: if the Dienstnehmergruppe from LHR is different from the one from ibosNG, the data in ibosNG will be overwritten
        if (topLevelDTO != null && topLevelDTO.getVariableDaten() != null && !topLevelDTO.getVariableDaten().isEmpty() && topLevelDTO.getVariableDaten().get(0).getData() != null) {
            GruppenData gruppenData = topLevelDTO.getVariableDaten().get(0).getData();
            Vertragsdaten vertragsdaten = vertragsdatenService.findByPersonalnummerString(String.format("%010d", topLevelDTO.getDienstnehmer().getDnNr()));
            if (gruppenData.getDnGruppe() != null && !isNullOrBlank(gruppenData.getDnGruppe().getKz()) && !isNullOrBlank(gruppenData.getDnGruppe().getName())) {
                com.ibosng.dbservice.entities.masterdata.Dienstnehmergruppe dienstnehmergruppe = dienstnehmergruppeService.findAllByAbbreviationAndBezeichnung(gruppenData.getDnGruppe().getKz(), gruppenData.getDnGruppe().getName()).stream().findFirst().orElse(null);

                if (dienstnehmergruppe != null && !dienstnehmergruppe.equals(vertragsdaten.getDienstnehmergruppe())) {
                    vertragsdaten.setDienstnehmergruppe(dienstnehmergruppe);
                    vertragsdatenService.save(vertragsdaten);
                }
            }
            if (gruppenData.getBatchGruppe() != null && !isNullOrBlank(gruppenData.getBatchGruppe().getKz()) && !isNullOrBlank(gruppenData.getBatchGruppe().getName())) {
                com.ibosng.dbservice.entities.masterdata.Abrechnungsgruppe Abrechnungsgruppe = abrechnungsgruppeService.findAllByAbbreviationAndBezeichnung(gruppenData.getBatchGruppe().getKz(), gruppenData.getBatchGruppe().getName()).stream().findFirst().orElse(null);
                if (Abrechnungsgruppe != null && !Abrechnungsgruppe.equals(vertragsdaten.getAbrechnungsgruppe())) {
                    vertragsdaten.setAbrechnungsgruppe(Abrechnungsgruppe);
                    vertragsdatenService.save(vertragsdaten);
                }
            }
        }
    }*/

    private VariableDatenDto setDataForVariableDaten(VariableDatenDto variableDaten, Vertragsdaten vertragsdaten) {
        if (variableDaten == null) {
            variableDaten = new VariableDatenDto();
        }
        if (variableDaten.getData() == null) {
            variableDaten.setData(new GruppenDataDto());
        }
        GruppenDataDto gruppenData = variableDaten.getData();
        if (vertragsdaten.getEintritt() != null) {
            variableDaten.setValidFrom(vertragsdaten.getEintritt());
            gruppenData.setValidFrom(vertragsdaten.getEintritt());
        }
        if (vertragsdaten.getDienstnehmergruppe() != null) {
            DienstnehmergruppeDto dienstnehmergruppe;
            if (gruppenData.getDnGruppe() == null) {
                dienstnehmergruppe = new DienstnehmergruppeDto();
            } else {
                dienstnehmergruppe = gruppenData.getDnGruppe();
            }
            dienstnehmergruppe.setKz(vertragsdaten.getDienstnehmergruppe().getAbbreviation());
            dienstnehmergruppe.setName(vertragsdaten.getDienstnehmergruppe().getBezeichnung());
            gruppenData.setDnGruppe(dienstnehmergruppe);
        }
        if (vertragsdaten.getAbrechnungsgruppe() != null) {
            AbrechnungsgruppeDto abrechnungsgruppe;
            if (gruppenData.getBatchGruppe() == null) {
                abrechnungsgruppe = new AbrechnungsgruppeDto();
            } else {
                abrechnungsgruppe = gruppenData.getBatchGruppe();
            }
            abrechnungsgruppe.setKz(vertragsdaten.getAbrechnungsgruppe().getAbbreviation());
            abrechnungsgruppe.setName(vertragsdaten.getAbrechnungsgruppe().getBezeichnung());
            gruppenData.setBatchGruppe(abrechnungsgruppe);
        }

        SozialversicherungDto sozialversicherung;
        if (gruppenData.getSozialVersicherung() == null) {
            sozialversicherung = new SozialversicherungDto();
        } else {
            sozialversicherung = gruppenData.getSozialVersicherung();
        }
        if (vertragsdaten.getDienstort() != null) {
            if (vertragsdaten.getDienstort().getLhrNr() != null) {
                sozialversicherung.setDienstortAbteilungNr(vertragsdaten.getDienstort().getLhrNr());
                if (!isNullOrBlank(vertragsdaten.getDienstort().getLhrKz())) {
                    sozialversicherung.setDienstortAbteilungName(vertragsdaten.getDienstort().getLhrKz());
                }
                gruppenData.setSozialVersicherung(sozialversicherung);
            }
        }
        PflichtigkeitenDto pflichtigkeiten;
        if (gruppenData.getPflichtigkeiten() == null) {
            pflichtigkeiten = new PflichtigkeitenDto();
        } else {
            pflichtigkeiten = gruppenData.getPflichtigkeiten();
        }
        GemeindeDto kommunalgemeinde;
        if (pflichtigkeiten.getKommunalSteuerGemeinde() == null) {
            kommunalgemeinde = new GemeindeDto();
        } else {
            kommunalgemeinde = pflichtigkeiten.getKommunalSteuerGemeinde();
        }

        if (vertragsdaten.getDienstort() != null && vertragsdaten.getDienstort().getAdresse() != null && vertragsdaten.getDienstort().getAdresse().getPlz() != null && (vertragsdaten.getDienstort().getAdresse().getPlz() instanceof Plz plz)) {
            Kommunalsteuergemeinde kommunalsteuergemeinde = kommunalsteuergemeindeService.findByDienstortPlz(plz.getPlz());
            if (kommunalsteuergemeinde != null) {
                kommunalgemeinde.setName(kommunalsteuergemeinde.getLhrName());
                kommunalgemeinde.setPostleitzahl(String.valueOf(kommunalsteuergemeinde.getLhrPlz()));
                kommunalgemeinde.setStaat(vertragsdaten.getDienstort().getAdresse().getLand().getLhrKz());
                pflichtigkeiten.setKommunalSteuerGemeinde(kommunalgemeinde);
                gruppenData.setPflichtigkeiten(pflichtigkeiten);
            }
        }


        GehaltInfo gehaltInfo = gehaltInfoService.findByVertragsdatenId(vertragsdaten.getId());

        if (gehaltInfo != null) {
            KollektivvertragDto kollektivvertrag = new KollektivvertragDto();
            KollektivvertragEinstufungDto kollektivvertragEinstufung = new KollektivvertragEinstufungDto();
            if (gehaltInfo.getVerwendungsgruppe() != null) {
                if (gehaltInfo.getVerwendungsgruppe().getKollektivvertrag() != null) {
                    com.ibosng.dbservice.entities.masterdata.Kollektivvertrag koll = gehaltInfo.getVerwendungsgruppe().getKollektivvertrag();
                    if (!isNullOrBlank(koll.getLhrKz())) {
                        kollektivvertrag.setKollektivvertragName(koll.getLhrKz());
                    }
                    if (koll.getLhrNr() != null) {
                        kollektivvertrag.setKollektivvertragNr(koll.getLhrNr());
                    }
                }

                if (!isNullOrBlank(gehaltInfo.getVerwendungsgruppe().getLhrKlasse())) {
                    kollektivvertragEinstufung.setKlasse(gehaltInfo.getVerwendungsgruppe().getLhrKlasse());
                }
                if (!isNullOrBlank(gehaltInfo.getVerwendungsgruppe().getLhrGruppe())) {
                    kollektivvertragEinstufung.setGruppe(gehaltInfo.getVerwendungsgruppe().getLhrGruppe());
                }
            }

            if (gehaltInfo.getStufe() != null) {
                String stufe = extractDigitFromStringEnd(gehaltInfo.getStufe().getName());
                if (!isNullOrBlank(stufe)) {
                    kollektivvertragEinstufung.setStufe(stufe);
                }
            }
            if (MitarbeiterType.MITARBEITER.equals(vertragsdaten.getPersonalnummer().getMitarbeiterType()) && gehaltInfo.getNaechsteStufeDatum() != null) {
                kollektivvertrag.setNaechsteVorrueckung(gehaltInfo.getNaechsteStufeDatum());
            } else if (gehaltInfo.getNaechsteVorrueckung() != null) {
                kollektivvertrag.setNaechsteVorrueckung(gehaltInfo.getNaechsteVorrueckung());
            }
            kollektivvertrag.setEinstufung(kollektivvertragEinstufung);
            gruppenData.setKollektivvertrag(kollektivvertrag);
        }
        gruppenData.setLohnsteuer(setLohnsteuerForGruppenData(gruppenData.getLohnsteuer()));
        gruppenData.setSonstiges(getSonstigesForVariableDaten(gruppenData, vertragsdaten));
        return variableDaten;
    }

    private SonstigesDto getSonstigesForVariableDaten(GruppenDataDto gruppenData, Vertragsdaten vertragsdaten) {
        SonstigesDto sonstiges;
        if (gruppenData.getSonstiges() != null) {
            sonstiges = gruppenData.getSonstiges();
        } else {
            sonstiges = new SonstigesDto();
        }
        if (MitarbeiterType.TEILNEHMER.equals(vertragsdaten.getPersonalnummer().getMitarbeiterType())) {
            if (vertragsdaten.getTaetigkeit() != null) {
                Taetigkeit taetigkeit = vertragsdaten.getTaetigkeit();
                GruppeNameNrDto beruf = new GruppeNameNrDto();
                if (taetigkeit.getLhrNr() != null) {
                    beruf.setNr(taetigkeit.getLhrNr());
                }
                if (!isNullOrBlank(taetigkeit.getLhrKz())) {
                    beruf.setName(taetigkeit.getLhrKz());
                }
                sonstiges.setBeruf(beruf);
            }
        }

        SonstigesAutomatikDto sonstigesAutomatik = new SonstigesAutomatikDto();
        sonstigesAutomatik.setBetriebsvorsorge("J");
        sonstigesAutomatik.setPendlerpauschale("J");
        sonstiges.setAutomatik(sonstigesAutomatik);
        return sonstiges;
    }

    private LohnsteuerDto setLohnsteuerForGruppenData(LohnsteuerDto lohnsteuer) {
        if (lohnsteuer == null) {
            lohnsteuer = new LohnsteuerDto();
        }
        GruppeNameKzDto gruppe;
        if (lohnsteuer.getGruppe() == null) {
            gruppe = new GruppeNameKzDto();
        } else {
            gruppe = lohnsteuer.getGruppe();
        }

        gruppe.setKz("O");
        lohnsteuer.setGruppe(gruppe);
        GruppeNameKzDto jahresausgleich;
        if (lohnsteuer.getJahresausgleich() == null) {
            jahresausgleich = new GruppeNameKzDto();
        } else {
            jahresausgleich = lohnsteuer.getJahresausgleich();
        }
        jahresausgleich.setKz("N");
        lohnsteuer.setJahresausgleich(jahresausgleich);
        return lohnsteuer;
    }
}
