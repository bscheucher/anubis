package com.ibosng.dbibosservice.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "ADRESSE")
public class AdresseIbos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ADadnr")
    private Integer adadnr;

    // tn = Teilnehmer
    // tr = Trainer
    @Column(name = "ADtyp")
    private String adtyp;

    @Column(name = "ADfadnr", columnDefinition = "int default 0")
    private Integer adfadnr = 0;

    @Column(name = "ADibis_gs")
    private Integer adibisGs;

    @Column(name = "ADibis_gseinsatz")
    private String adibisGseinsatz;

    @Column(name = "ADadrtype", nullable = false, columnDefinition = "enum ('p', 'f') default 'p'")
    private String adadrtype = "p";

    @Column(name = "ADanrede")
    private Integer adanrede;

    @Column(name = "ADgeschlecht")
    private String adgeschlecht;

    @Column(name = "ADtitel_old")
    private Integer adtitelOld;

    @Column(name = "ADtitel")
    private Integer adtitel;

    @Column(name = "ADtitelv")
    private Integer adtitelv;

    // Nachname
    @Column(name = "ADznf1")
    private String adznf1;

    // Vorname
    @Column(name = "ADvnf2")
    private String advnf2;

    @Column(name = "ADsube")
    private String adsube;

    @Column(name = "ADstrasse")
    private String adstrasse;

    @Column(name = "ADstrobjekt")
    private String adstrobjekt;

    @Column(name = "ADfsb")
    private Integer adfsb;

    @Column(name = "ADfsb_kopie_uebergeben")
    private Boolean adfsbKopieUebergeben;

    @Column(name = "ADlkz")
    private String adlkz;

    @Column(name = "ADplz")
    private String adplz;

    @Column(name = "ADort")
    private String adort;

    @Column(name = "ADplz2")
    private String adplz2;

    @Column(name = "ADlkz2")
    private String adlkz2;

    @Column(name = "ADort2")
    private String adort2;

    @Column(name = "ADstrasse2")
    private String adstrasse2;

    @Column(name = "ADbundesland")
    private Integer adbundesland;

    @Column(name = "ADzukz")
    private String adzukz = "p";

    @Column(name = "ADtelp")
    private String adtelp;

    @Column(name = "ADfaxp")
    private String adfaxp;

    @Column(name = "ADtelf")
    private String adtelf;

    @Column(name = "ADfaxf")
    private String adfaxf;

    @Column(name = "ADsipnr")
    private String adsipnr;

    @Column(name = "ADmobil1")
    private String admobil1;

    @Column(name = "ADmobil1Besitzer")
    private String admobil1Besitzer;

    @Column(name = "ADmobil2")
    private String admobil2;

    @Column(name = "ADmobil2Besitzer")
    private String admobil2Besitzer;

    @Column(name = "ADemail1")
    private String ademail1;

    @Column(name = "ADemail2")
    private String ademail2;

    @Column(name = "ADinternet")
    private String adinternet;

    @Column(name = "ADgebdatum")
    private LocalDate adgebdatum;

    @Column(name = "ADgebdatumf")
    private Integer adgebdatumf = 0;

    @Column(name = "ADgebort")
    private String adgebort;

    @Column(name = "ADgebland")
    private String adgebland;

    @Column(name = "ADstaatsb")
    private Integer adstaatsb;

    @Column(name = "ADstaatenlos")
    private String adstaatenlos;

    @Column(name = "ADmuttersprache")
    private Integer admuttersprache;

    @Column(name = "ADmuttersprache_keine_Angabe")
    private Boolean admutterspracheKeineAngabe;

    @Column(name = "ADfamstand")
    private Integer adfamstand;

    @Column(name = "ADerstkontaktAm")
    private LocalDate aderstkontaktAm;

    @Column(name = "ADbewerbungAm")
    private LocalDate adbewerbungAm;

    @Column(name = "ADersteintritt")
    private LocalDate adersteintritt;

    @Column(name = "ADpersnr")
    private String adpersnr;

    @Column(name = "ADsvnr")
    private String adsvnr;

    @Column(name = "ADsvnr_unbekannt")
    private Integer adsvnrUnbekannt = 0;

    @Column(name = "ADversicherung")
    private String adversicherung;

    @Column(name = "ADmitversichertbei")
    private String admitversichertbei;

    @Column(name = "ADErzBe")
    private String aderzBe;

    @Column(name = "ADErzBeTel")
    private String aderzBeTel;

    @Column(name = "ADErzBeMail")
    private String aderzBeMail;

    @Column(name = "ADVorM")
    private String advorM;

    @Column(name = "ADVorMTel")
    private String advorMTel;

    @Column(name = "ADVorMMail")
    private String advorMMail;

    @Column(name = "ADarbeitsgenehmigung")
    private String adarbeitsgenehmigung;

    @Column(name = "ADarbeitsgenbis")
    private LocalDate adarbeitsgenbis;

    @Column(name = "ADfuehrerschein")
    private String adfuehrerschein;

    @Column(name = "ADpruefungen")
    private String adpruefungen;

    @Column(name = "ADberuf")
    private String adberuf;

    @Column(name = "ADfunktion")
    private Integer adfunktion;

    @Column(name = "ADkreditornr")
    private String adkreditornr;

    @Column(name = "ADbank")
    private String adbank;

    @Column(name = "ADbankblz")
    private String adbankblz;

    @Column(name = "ADbankkonto")
    private String adbankkonto;

    @Column(name = "ADbankiban")
    private String adbankiban;

    @Column(name = "ADbankbic")
    private String adbankbic;

    @Column(name = "ADfbnr")
    private String adfbnr;

    @Column(name = "ADfbnrgericht")
    private String adfbnrgericht;

    @Column(name = "ADsteuernr")
    private String adsteuernr;

    @Column(name = "ADfinanzamt")
    private String adfinanzamt;

    @Column(name = "ADuid")
    private String aduid;

    @Column(name = "ADklientid")
    private Integer adklientid;

    @Column(name = "ADklientid_old", columnDefinition = "tinyint")
    private Integer adklientidOld;

    @Column(name = "ADbemerkung")
    private String adbemerkung;

    @Column(name = "ADnoml")
    private String adnoml = "n";

    @Column(name = "ADfoto")
    private String adfoto;

    @Column(name = "ADbildLink", columnDefinition = "text")
    private String adbildLink;

    @Column(name = "ADkategorie_old")
    private Integer adkategorieOld;

    @Column(name = "ADkategorie")
    private Integer adkategorie;

    @Column(name = "ADgewerbeschein")
    private Integer adgewerbeschein;

    @Column(name = "ADowner")
    private String adowner;

    @Column(name = "ADberecht")
    private String adberecht;

    @Column(name = "ADquelle")
    private String adquelle;

    @Column(name = "ADprda")
    private LocalDate adprda;

    @Column(name = "ADpruser")
    private String adpruser;

    @Column(name = "ADstatus", columnDefinition = "char")
    private Character adstatus = 'a';

    @Column(name = "ADuserid", unique = true)
    private String aduserid;

    @Column(name = "ADpwd")
    private String adpwd;

    @Column(name = "ADtcstatus")
    private String adtcstatus;

    @Column(name = "ADsettinglistlg")
    private Integer adsettinglistlg;

    @Column(name = "ADlastlogin")
    private LocalDateTime adlastlogin;

    @Column(name = "ADmbbe_uebertritt")
    private LocalDate admbbeUebertritt;

    @Column(name = "ADbBcreate", columnDefinition = "char")
    private Character adbBcreate;

    @Column(name = "ADloek",  columnDefinition = "enum ('n', 'y')")
    private String adloek= "n";

    @Column(name = "ADaeda")
    private LocalDateTime adaeda;

    @Column(name = "ADaeuser", columnDefinition = "char(35)")
    private String adaeuser;

    @Column(name = "ADerda")
    private LocalDateTime aderda;

    @Column(name = "ADeruser", columnDefinition = "char(35)")
    private String aderuser;

    public void setAdaeuser(String adaeuser) {
        if(adaeuser.contains("@")) {
            this.adaeuser = adaeuser.split("@")[0];
        } else {
            this.adaeuser = adaeuser;
        }
    }

    public void setAderuser(String aderuser) {
        if(aderuser.contains("@")) {
            this.aderuser = aderuser.split("@")[0];
        } else {
            this.aderuser = aderuser;
        }
    }
}
