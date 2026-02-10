package com.ibosng.dbibosservice.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "PROJEKT")
public class ProjektIbos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PJnr")
    private Integer id;

    @Column(name = "PJmnr")
    private String pjMnr;

    @Column(name = "ASnr", nullable = false)
    private Integer asNr;

    @Column(name = "PJbezeichnung1" , columnDefinition = "char(80)")
    private String pjBezeichnung1;

    @Column(name = "PJbezeichnung2" , columnDefinition = "char(80)")
    private String pjBezeichnung2;

    @Column(name = "PJtyp", columnDefinition = "int(6) unsigned default 1")
    private Integer pjTyp = 1;

    @Column(name = "FIT2WORK_BUNDESLAND_id")
    private Integer fit2WorkBundeslandId;

    @Column(name = "PJdatumvon")
    private LocalDate pjDatumVon;

    @Column(name = "PJdatumbis")
    private LocalDate pjDatumBis;

    @Column(name = "PJmonAbschlussJahr")
    private Integer pjMonAbschlussJahr;

    @Column(name = "PJmonAbschlussMonat")
    private Integer pjMonAbschlussMonat;

    @Column(name = "PJeinheiten")
    private Integer pjEinheiten;

    @Column(name = "PJSOstandortid")
    private Integer pjSOstandortid;

    @Column(name = "PJkontaktibis")
    private Integer pjKontaktibis;

    @Column(name = "PJkontaktauftraggeber")
    private String pjKontaktauftraggeber;

    @Column(name = "PJkontaktauftraggeber_old")
    private Integer pjKontaktauftraggeberOld;

    @Column(name = "PJTasNr")
    private String pjTasNr;

    @Column(name = "PJkstgr")
    private Integer pjKstGr;

    @Column(name = "PJkstnr")
    private Integer pjKstNr;

    @Column(name = "PJkstsub")
    private Integer pjKstSub;

    @Column(name = "PJkostentraeger")
    private Integer pjKostentraeger;

    @Column(name = "TEILNAHMESTATUS_BEREICH_id")
    private Integer teilnahmestatusBereichId;

    @Column(name = "PJvermittlungsquote")
    private Integer pjVermittlungsquote;

    @Column(name = "PJprojektpreis")
    private BigDecimal pjProjektpreis;

    @Column(name = "PJprojektMNH")
    private BigDecimal pjProjektMNH;

    @Column(name = "PJteilnehmeranz")
    private Integer pjTeilnehmeranz;

    @Column(name = "PJstundensatz")
    private BigDecimal pjStundensatz;

    @Column(name = "PJkurszeiten")
    private String pjKurszeiten;

    @Column(name = "PJanmeldeschluss")
    private LocalDate pjAnmeldeschluss;

    @Column(name = "PJbemerk")
    private String pjBemerk;

    @Column(name = "PJbezugsaenderung", nullable = false, columnDefinition = "char default 'n' not null")
    private char pjBezugsaenderung = 'n';

    @Column(name = "PJteilnehmerdetail", nullable = false, columnDefinition = "char default 'n' not null")
    private char pjTeilnehmerdetail = 'n';

    @Column(name = "PJinhaltsbeschr")
    private String pjInhaltsbeschr;

    @Column(name = "PJpublishvon")
    private LocalDate pjPublishvon;

    @Column(name = "PJpublishbis")
    private LocalDate pjPublishbis;

    @Column(name = "PJpublishtxt")
    private String pjPublishtxt;

    @Column(name = "PJstatus")
    private Integer pjStatus;

    @Column(name = "PJif", columnDefinition = "enum('y', 'n') default 'n'")
    private String pjIf = "n";

    @Column(name = "PJik", columnDefinition = "enum('y', 'n') default 'n'")
    private String pjIk = "n";

    @Column(name = "PJkb", columnDefinition = "enum('y', 'n') default 'n'")
    private String pjKb = "n";

    @Column(name = "PJmassnahmennebenkosten")
    private BigDecimal pjMassnahmennebenkosten;

    @Column(name = "PJpreis_ergaenzung")
    private BigDecimal pjPreisErgaenzung;

    @Column(name = "PJsubunternehmer")
    private BigDecimal pjSubunternehmer;

    @Column(name = "PJvertrag", columnDefinition = "enum('y', 'n')")
    private String pjVertrag = "n";

    @Column(name = "PJbearbeiten" , columnDefinition = "tinyint")
    private Boolean pjBearbeiten;

    @Column(name = "PJvertragszuordnung", length = 100)
    private String pjVertragszuordnung;

    @Column(name = "PJendabrechnung", columnDefinition = "enum('y', 'n')")
    private String pjEndabrechnung = "n";

    @Column(name = "PJendabrechnung_datum")
    private LocalDate pjEndabrechnungDatum;

    @Column(name = "PJendabrechnung_bemerkung", length = 100)
    private String pjEndabrechnungBemerkung;

    @Column(name = "PJendabrechnung_betrag")
    private BigDecimal pjEndabrechnungBetrag;

    @Column(name = "PJendabrechnung_projektpreis")
    private BigDecimal pjEndabrechnungProjektpreis;

    @Column(name = "PJendabrechnung_mnk")
    private BigDecimal pjEndabrechnungMnk;

    @Column(name = "PJendabrechnung_betrag_bewilligt")
    private BigDecimal pjEndabrechnungBetragBewilligt;

    @Column(name = "PJentwurf_faellig_am")
    private LocalDate pjEntwurfFaelligAm;

    @Column(name = "PJabgabe_kunde")
    private LocalDate pjAbgabeKunde;

    @Column(name = "PJaltern_abgabefrist")
    private LocalDate pjAlternAbgabefrist;

    @Column(name = "PJec_storno_verr_schluessel")
    private BigDecimal pjEcStornoVerrSchluessel;

    @Column(name = "PJprojektabrechnung_pdf_gruppierung", columnDefinition = "enum('month', 'week') default 'month'")
    private String pjProjektabrechnungPdfGruppierung = "month";

    @Column(name = "PJloek", columnDefinition = "enum('n', 'y') default 'n'")
    private String pjLoek = "n";

    @Column(name = "PJaeda")
    private LocalDateTime pjAeda;

    @Column(name = "PJaeuser" , columnDefinition = "char(35)")
    private String pjAeuser;

    @Column(name = "PJerda")
    private LocalDateTime pjErda;

    @Column(name = "PJeruser" ,  columnDefinition = "char(35)")
    private String pjEruser;

}
