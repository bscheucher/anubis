package com.ibosng.dbservice.entities.teilnehmer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "teilnehmer_staging")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeilnehmerStaging {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "titel")
    private String titel;

    @Column(name = "titel2")
    private String titel2;

    @Column(name = "vorname")
    private String vorname;

    @Column(name = "nachname")
    private String nachname;

    @Column(name = "geschlecht")
    private String geschlecht;

    @Column(name = "sv_nummer")
    private String svNummer;

    @Column(name = "geburtsdatum")
    private String geburtsdatum;

    @Column(name = "buchungsstatus")
    private String buchungsstatus;

    @Column(name = "anmerkung")
    private String anmerkung;

    @Column(name = "zubuchung")
    private String zubuchung;

    @Column(name = "geplant")
    private String geplant;

    @Column(name = "eintritt")
    private String eintritt;

    @Column(name = "austritt")
    private String austritt;

    @Column(name = "rgs")
    private String rgs;

    @Column(name = "massnahmennummer")
    private String massnahmennummer;

    @Column(name = "veranstaltungsnummer")
    private String veranstaltungsnummer;

    @Column(name = "email")
    private String email;

    @Column(name = "telefon")
    private String telefon;

    @Column(name = "import_filename")
    private String importFilename;

    @Column(name = "info")
    private String info;

    @Column(name = "seminar_identifier")
    private String seminarIdentifier;

    @Column(name = "seminar_start_date")
    private String seminarStartDate;

    @Column(name = "seminar_end_date")
    private String seminarEndDate;

    @Column(name = "seminar_start_time")
    private String seminarStartTime;

    @Column(name = "seminar_type")
    private String seminarType;

    @Column(name = "trainer")
    private String trainer;

    @Column(name = "betreuer_titel")
    private String betreuerTitel;

    @Column(name = "betreuer_vorname")
    private String betreuerVorname;

    @Column(name = "betreuer_nachname")
    private String betreuerNachname;

    @Column(name = "plz")
    private String plz;

    @Column(name = "ort")
    private String ort;

    @Column(name = "strasse")
    private String strasse;

    @Column(name = "nation")
    private String nation;

    @Column(name = "landesvorwahl")
    private String landesvorwahl;

    @Column(name = "vorwahl")
    private String vorwahl;

    @Column(name = "telefon_nummer")
    private String telefonNummer;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private TeilnehmerStatus status;

    @Column(name = "teilnahmebeginn")
    private String startOfParticipation;

    @Column(name = "anmeldestatus")
    private String registrationStatus;

    @Column(name = "ams")
    private String ams;

    @Column(name = "abmeldegrund")
    private String reasonOfDeregistration;

    @Column(name = "anwesenheit_in_ue")
    private String presenceInUe;

    @Column(name = "entschuldigte_abwesenheit_in_ue")
    private String excusedAbscenceFromUe;

    @Column(name = "unentschuldigte_abwesenheit_in_ue")
    private String unexcusedAbscenceFromUe;

    @Column(name = "summe_erfasste_an_und_abwesenheit")
    private String totalAbscence;

    @Column(name = "anwesenheit_erfasst_bis")
    private String attendanceRecordedUntil;

    @Column(name = "absolute_anwesenheit")
    private String absolutePresencePercentage;

    @Column(name = "relative_anwesenheit")
    private String relativePresencePercentage;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "source")
    private TeilnehmerSource source;

    @Column(name = "teilnehmer_id")
    private int teilnehmerId;

    @Column(name = "ursprungsland")
    private String ursprungsland;

    @Column(name = "gerburtsort")
    private String gerburtsort;

    @Column(name = "erlaeuterung_ziel")
    private String erlaeuterungZiel;

    @Column(name = "vermittelbar_ab")
    private String vermittelbarAb;

    @Column(name = "notiz")
    private String notiz;

    @Column(name = "created_on")
    @JsonIgnore
    private LocalDateTime createdOn = getLocalDateNow();

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "changed_by")
    private String changedBy;

    @Column(name = "anrede")
    private String anrede;

    @Column(name = "teilnahme_von")
    private String teilnahmeVon;

    @Column(name = "teilnahme_bis")
    private String teilnahmeBis;

    @Column(name = "muttersprache")
    private String muttersprache;
}
