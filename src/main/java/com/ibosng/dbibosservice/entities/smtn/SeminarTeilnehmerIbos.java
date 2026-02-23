package com.ibosng.dbibosservice.entities.smtn;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "SM_TN")
@Data
public class SeminarTeilnehmerIbos {

    @EmbeddedId
    private SmTnId smTnId;

    @Column(name = "TAanmeldedatum")
    private LocalDate taanmeldedatum;

    @Column(name = "TAamsbetreuer" , columnDefinition = "char(40)")
    private String taamsbetreuer;

    @Column(name = "TArgsnr")
    private Integer targsnr;

    @Column(name = "TARgsKYnr")
    private Integer targsKYnr;

    @Column(name = "TAteilnahmevon")
    private LocalDate tateilnahmevon;

    @Column(name = "TAteilnahmebis")
    private LocalDate tateilnahmebis;

    @Column(name = "TAvermittlungsart")
    private Integer tavermittlungsart;

    @Column(name = "TAvermittlung")
    private Integer tavermittlung;

    @Column(name = "TAdatumbeginn")
    private LocalDate tadatumbeginn;

    @Column(name = "TAdatumende")
    private LocalDate tadataumende;

    @Column(name = "TAaa_bei" , columnDefinition = "char(80)")
    private String taaaBei;

    @Column(name = "TAaa_als" , columnDefinition = "char(80)")
    private String taaaAls;

    @Column(name = "TAqual_ort" , columnDefinition = "char(40)")
    private String taqualOrt;

    @Column(name = "TAqual_kursbez" , columnDefinition = "char(80)")
    private String taqualKursbez;

    @Column(name = "TAsonstiges")
    private Integer tasonstiges;

    @Column(name = "TApruefung" , columnDefinition = "char(80)")
    private String tapruefung;

    @Column(name = "TApruefungerg")
    @Enumerated(EnumType.STRING)
    private Pruefungsergebnis tapruefungerg;

    @Column(name = "TApruefungwdh")
    private LocalDate tapruefungwdh;

    @Column(name = "TAkarriereplan")
    @Enumerated(EnumType.STRING)
    private Karriereplan takarriereplan;

    @Column(name = "TAabschluss")
    private Integer taabschluss;

    @Column(name = "TAabschlussdatum")
    private LocalDate taabschlussdatum;

    @Column(name = "TAabschlussBeiCrmFirma")
    private Integer taabschlussBeiCrmFirma;

    @Column(name = "TAabschlussgrund")
    private String taabschlussgrund;

    @Column(name = "TAinternebemerk")
    private String tainternebemerk;

    @Column(name = "TAarbeitsplatzsucheseit" , columnDefinition = "char(7)")
    private String taarbeitsplatzsucheseit;

    @Column(name = "TAletztearbeitsstelle" , columnDefinition = "char(40)")
    private String taletztearbeitsstelle;

    @Column(name = "TAletztearbeitsstelleals" , columnDefinition = "char(40)")
    private String taletztearbeitsstelleals;

    @Column(name = "TAletztearbeitsstellevon" , columnDefinition = "char(7)")
    private String taletztearbeitsstellevon;

    @Column(name = "TAletztearbeitsstellebis" , columnDefinition = "char(7)")
    private String taletztearbeitsstellebis;

    @Column(name = "TAalternativeMNummer")
    private String taalternativeMNummer;

    @Column(name = "TAalternativeVNummer")
    private String taalternativeVNummer;

    @Column(name = "TAtn_zufriedenheit_ausgefuellt" , columnDefinition = "tinyint")
    private Boolean tatnZufriedenheitAusgefuellt;

    @Column(name = "TAmbbe_betreuung")
    private Integer tambbeBetreuung;

    @Column(name = "TAmbbe_leistungsbezug")
    private Integer tambbeLeistungsbezug;

    @Column(name = "TAmbbe_uebertritt")
    private LocalDate tambbeUebertritt;

    @Column(name = "TAjobsuche_aktiv" , columnDefinition = "tinyint")
    private Boolean tajobsucheAktiv;

    @Column(name = "TAteilnehmerkategorie_id")
    private Integer tateilnehmerkategorieId;

    @Column(name = "TAbBaktiv", columnDefinition = "char default 'N'")
    private Character tabbaktiv = 'N';

    @Column(name = "TAbBcreate")
    private Character tabbcreate;

    @Column(name = "TAteamsaktiv", columnDefinition = "char default 'N'")
    private Character tateamsaktiv = 'N';

    @Column(name = "TAteamscreate")
    private Character tateamscreate;

    @Column(name = "TAaeda")
    private LocalDate taaeda;

    @Column(name = "TAaeuser" , columnDefinition = "char(35)")
    private String taaeuser;

    @Column(name = "TAerda")
    private LocalDate taerda;

    @Column(name = "TAeruser" , columnDefinition = "char(35)")
    private String taeruser;


    public enum Pruefungsergebnis {
        b, t, n
    }

    public enum Karriereplan {
        n, y
    }

}
