package com.ibosng.dbservice.dtos.mitarbeiter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VertragsdatenBasicDto {

    private String eintritt;
    private Boolean isBefristet;
    private String befristungBis;
    private String stundenaenderung;
    private String verwendungsbereichsaenderung;
    private String stufenwechsel;
    private String geschaeftsbereichsaenderung;
    private Boolean kvErhoehung;
    private String dienstort;
    private String fuehrungskraft;
    private String startcoach;

    private String kategorie;
    private String taetigkeit;
    private String jobBezeichnung;
    private String notizAllgemein;

    private String beschaeftigungsausmass;
    private String beschaeftigungsstatus;
    private String wochenstunden;

    private String arbeitszeitmodell;
    private String arbeitszeitmodellVon;
    private String arbeitszeitmodellBis;

    private String auswahlBegruendungFuerDurchrechner;

    private String aMontagVon;
    private String aMontagBis;
    private String aMontagNetto;
    private String aDienstagVon;
    private String aDienstagBis;
    private String aDienstagNetto;
    private String aMittwochVon;
    private String aMittwochBis;
    private String aMittwochNetto;
    private String aDonnerstagVon;
    private String aDonnerstagBis;
    private String aDonnerstagNetto;
    private String aFreitagVon;
    private String aFreitagBis;
    private String aFreitagNetto;
    private String aSamstagVon;
    private String aSamstagBis;
    private String aSamstagNetto;
    private String aSonntagVon;
    private String aSonntagBis;
    private String aSonntagNetto;

    private String kMontagVon;
    private String kMontagBis;
    private String kDienstagVon;
    private String kDienstagBis;
    private String kMittwochVon;
    private String kMittwochBis;
    private String kDonnerstagVon;
    private String kDonnerstagBis;
    private String kFreitagVon;
    private String kFreitagBis;
    private String kSamstagVon;
    private String kSamstagBis;
    private String kSonntagVon;
    private String kSonntagBis;

    private String spezielleMittagspausenregelung;
    private Boolean urlaubVorabVereinbart;
    private String notizArbeitszeit;

    private String kollektivvertrag;
    private String verwendungsgruppe;
    private String stufe;
    private Boolean facheinschlaegigeTaetigkeitenGeprueft;
    private Integer angerechneteIbisMonate;
    private Integer angerechneteFacheinschlaegigeTaetigkeitenMonate;
    private Double kvGehaltBerechnet;
    private Double gehaltVereinbart;
    private Double ueberzahlung;


    private Double gesamtBrutto;
    private String vereinbarungUEberstunden;
    private Double uestPauschale;
    private String deckungspruefung;
    private Boolean jobticket;
    private String jobticketTitle;
    private String notizGehalt;

    private Boolean mobileWorking;
    private Boolean weitereAdressezuHauptwohnsitz;
    private String notizZusatzvereinbarung;
    //    private List<ZulageDto> zulagen;
    private Boolean fixZulage;
    private String zulageInEuroFix;
    private Boolean funktionsZulage;
    private String zulageInEuroFunktion;
    private Boolean leitungsZulage;
    private String zulageInEuroLeitung;
    private LocalDate naechsteVorrueckung;
}
