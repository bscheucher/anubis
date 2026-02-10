package com.ibosng.dbibosservice.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "AUSSCHREIBUNG")
public class AusschreibungIbos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ASnr")
    private Integer asnr;

    @Column(name = "ASpnr")
    private String aspnr;

    @Column(name = "ASbundesland", nullable = false)
    private Integer asbundesland;

    @Column(name = "ASibis_gs")
    private Integer asibisGs;

    @Column(name = "ASbezeichnung1" , columnDefinition = "char(80)")
    private String asbezeichnung1;

    @Column(name = "ASbezeichnung2" , columnDefinition = "char(80)")
    private String asbezeichnung2;

    @Column(name = "ASmassnahmennr" , columnDefinition = "char(20)")
    private String asmassnahmennr;

    @Column(name = "ASverfahrensart")
    private Integer asverfahrensart;

    @Column(name = "ASauftraggeber")
    private Integer asauftraggeber;

    @Column(name = "AUFTRAGGEBER_id")
    private Integer auftraggeberId;

    @Column(name = "ASauftraggeber_old")
    private Integer asauftraggeberOld;

    @Column(name = "ASansprech1", nullable = false, columnDefinition = "int default 0")
    private Integer asansprech1 = 0;

    @Column(name = "ASansprech1_old", nullable = false, columnDefinition = "int default 0")
    private Integer asansprech1Old = 0;

    @Column(name = "ASansprechfunkt1")
    private String asansprechfunkt1;

    @Column(name = "ASansprech2")
    private Integer asansprech2;

    @Column(name = "ASansprech2_old")
    private Integer asansprech2Old;

    @Column(name = "ASansprechfunkt2")
    private String asansprechfunkt2;

    @Column(name = "ASansprechpartner")
    private String asansprechpartner;

    @Column(name = "ASansprtelefon" , columnDefinition = "char(40)")
    private String asansprtelefon;

    @Column(name = "ASansprtelefax" , columnDefinition = "char(40)")
    private String asansprtelefax;

    @Column(name = "ASanspremail" , columnDefinition = "char(60)")
    private String asanspremail;

    @Column(name = "ASart" , columnDefinition = "char(80)")
    private String asart;

    @Column(name = "ASmarktbereich")
    private Integer asmarktbereich;

    @Column(name = "ASzielgruppe")
    private Integer aszielgruppe;

    @Column(name = "ASinhalt")
    private Integer asinhalt;

    @Column(name = "ASeingangsdatum")
    private LocalDate aseingangsdatum;

    @Column(name = "ASabgabedatum")
    private LocalDate asabgabedatum;

    @Column(name = "ASabgabezeit")
    private LocalTime asabgabezeit;

    @Column(name = "ASplz" , columnDefinition = "char(6)")
    private String asplz;

    @Column(name = "ASort" , columnDefinition = "char(40)")
    private String asort;

    @Column(name = "ASSOstandortid")
    private Integer assostandortid;

    @Column(name = "ASDatumVon")
    private LocalDate asdatumVon;

    @Column(name = "ASDatumBis")
    private LocalDate asdatumBis;

    @Column(name = "ASzuschlag")
    private String aszuschlag;

    @Column(name = "ASauftragsbestand")
    private LocalDate asauftragsbestand;

    @Column(name = "ASbegruendung")
    private String asbegruendung;

    @Column(name = "ASbemerkung")
    private String asbemerkung;

    @Column(name = "ASinhaltsbeschr")
    private String asinhaltsbeschr;

    @Column(name = "ASibiscode" , columnDefinition = "char(15)")
    private String asibiscode;

    @Column(name = "ASdauer")
    private Integer asdauer;

    @Column(name = "ASteilnehmeranz")
    private Integer asteilnehmeranz;

    @Column(name = "ASpersonal_MNH")
    private Integer aspersonalMnh;

    @Column(name = "ASpreis_ibis")
    private BigDecimal aspreisIbis;

    @Column(name = "ASdb2prozent")
    private BigDecimal asdb2prozent;

    @Column(name = "ASdb2absolut")
    private BigDecimal asdb2absolut;

    @Column(name = "ASpreis_bestbieter")
    private BigDecimal aspreisBestbieter;

    @Column(name = "ASmail_entscheidung" , columnDefinition = "char(60)")
    private String asmailEntscheidung;

    @Column(name = "AStermin_entscheidung")
    private LocalDate asterminEntscheidung;

    @Column(name = "ASmail_konzept" , columnDefinition = "char(60)")
    private String asmailKonzept;

    @Column(name = "AStermin_konzept")
    private LocalDate asterminKonzept;

    @Column(name = "ASmail_nachweise" , columnDefinition = "char(60)")
    private String asmailNachweise;

    @Column(name = "AStermin_nachweise")
    private LocalDate asterminNachweise;

    @Column(name = "ASmail_kalkulation" , columnDefinition = "char(60)")
    private String asmailKalkulation;

    @Column(name = "AStermin_kalkulation")
    private LocalDate asterminKalkulation;

    @Column(name = "ASmail_TC" , columnDefinition = "char(60)")
    private String asmailTc;

    @Column(name = "AStermin_TC")
    private LocalDate asterminTc;

    @Column(name = "ASmail_ressourcen" , columnDefinition = "char(60)")
    private String asmailRessourcen;

    @Column(name = "AStermin_ressourcen")
    private LocalDate asterminRessourcen;

    @Column(name = "ASmail_abgabefertig" , columnDefinition = "char(60)")
    private String asmailAbgabefertig;

    @Column(name = "AStermin_abgabefertig")
    private LocalDate asterminAbgabefertig;

    @Column(name = "ASmail_endkontrolle" , columnDefinition = "char(60)")
    private String asmailEndkontrolle;

    @Column(name = "AStermin_endkontrolle")
    private LocalDate asterminEndkontrolle;

    @Column(name = "ASmail_bietergemeinschaft" , columnDefinition = "char(60)")
    private String asmailBietergemeinschaft;

    @Column(name = "AStermin_bietergemeinschaft")
    private LocalDate asterminBietergemeinschaft;

    @Column(name = "ASbew_qualifikaton")
    private String asbewQualifikation;

    @Column(name = "ASbew_berufserfahrung")
    private String asbewBerufserfahrung;

    @Column(name = "ASbew_methodik")
    private String asbewMethodik;

    @Column(name = "ASbew_didaktik")
    private String asbewDidaktik;

    @Column(name = "ASbew_orgform")
    private String asbewOrgform;

    @Column(name = "ASbew_gleichstellung")
    private String asbewGleichstellung;

    @Column(name = "ASbew_frauenanteil")
    private String asbewFrauenanteil;

    @Column(name = "ASbew_gender")
    private String asbewGender;

    @Column(name = "ASbew_raumausstattung")
    private String asbewRaumausstattung;

    @Column(name = "ASbew_technausstattung")
    private String asbewTechnausstattung;

    @Column(name = "ASbew_verkehrsanbindung")
    private String asbewVerkehrsanbindung;

    @Column(name = "ASbew_kosten")
    private String asbewKosten;

    @Column(name = "ASrefasnr")
    private Integer asrefasnr;

    @Column(name = "ASplanungsstufe")
    private Integer asplanungsstufe;

    @Column(name = "ASesf_finanziert")
    private String asesfFinanziert;

    @Column(name = "ASams_finanziert")
    private String asamsFinanziert;

    @Column(name = "ASlandco_finanziert")
    private String aslandcoFinanziert;

    @Column(name = "ASstatus")
    private Integer asstatus;

    @Column(name = "ASAbrechnungsmodus")
    private Integer asabrechnungsmodus;

    @Column(name = "IndividuellesAnonymisierungsdatum", nullable = false, columnDefinition = "int default 0")
    private Integer individuellesAnonymisierungsdatum = 0;

    @Column(name = "Anonymisierungsdatum")
    private LocalDate anonymisierungsdatum;

    @Column(name = "ASloek", nullable = false, columnDefinition = "enum('n','y') default 'n'")
    private String asloek = "n";

    @Column(name = "ASaeda")
    private LocalDateTime asaeda;

    @Column(name = "ASaeuser" , columnDefinition = "char(35)")
    private String asaeuser;

    @Column(name = "ASerda")
    private LocalDateTime aserda;

    @Column(name = "ASeruser" , columnDefinition = "char(35)")
    private String aseruser;

    @Column(name = "ASzuschlagam")
    private LocalDate aszuschlagam;

}
