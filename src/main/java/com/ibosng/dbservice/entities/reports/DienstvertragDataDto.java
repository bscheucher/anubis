package com.ibosng.dbservice.entities.reports;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class DienstvertragDataDto {

    private String pnr;
    private Integer id;
    private String firmenname;
    private String anrede;
    private String vorname;
    private String nachname;
    private String titel1;
    private Integer titel1_position;
    private String titel2;
    private Integer titel2_position;
    private String svnr;
    private Date geburtsdatum;
    private String strasse;
    private String ort;
    private Integer plz;
    private String land;
    private Date eintrittsdatum;
    private String telefonnummer;
    private String nationalitaet;
    private Boolean drittstaat;
    private String arbeitsgenehmigung;
    private Date gueltigbis;
    private Boolean befristung;
    private Date befristung_bis;
    private String dienstort;
    private Boolean andereeinsatz;
    private String einsatzgebiet;
    private String taetigkeit;
    private String job_bezeichnung;
    private String kategorie;
    private String kollektivvertrag;
    private String kv_stufe;
    private String verwendungsgruppe;
    private String vor_zeit_firma;
    private Boolean vor_zeit_anrechenbar;
    private Date vor_zeit_von;
    private Date vor_zeit_bis;
    private Double vor_zeit_wochenstunden;
    private String vor_zeit_vertragsart;
    private Integer vor_zeit_sum_mon;
    private BigDecimal kv_gehalt;
    private BigDecimal gesamt_brutto;
    private BigDecimal gehalt_vereinbart;
    private String art_der_zulage;
    private BigDecimal eur_zulage;
    private Boolean is_zulage;
    private BigDecimal ueberzahlung;
    private Boolean is_ueberzahlung;
    private Boolean is_all_in;
    private String bank;
    private String iban;
    private String bicm;
    private String az_model;
    private Integer az_model_id;
    private Time montag_von;
    private Time montag_bis;
    private Double montag_netto;
    private Time dienstag_von;
    private Time dienstag_bis;
    private Double dienstag_netto;
    private Time mittwoch_von;
    private Time mittwoch_bis;
    private Double mittwoch_netto;
    private Time donnerstag_von;
    private Time donnerstag_bis;
    private Double donnerstag_netto;
    private Time freitag_von;
    private Time freitag_bis;
    private Double freitag_netto;
    private Time samstag_von;
    private Time samstag_bis;
    private Double samstag_netto;
    private Date arbeitszeitmodell_von;
    private Date arbeitszeitmodell_bis;
    private String wochenstunden;
    private String dr_begruendung;
    private Boolean urlaub_vorab_vereinbart;
    private Boolean konkurrenz;
    private Boolean betriebskontakter;
    private Boolean erfinde;

}
